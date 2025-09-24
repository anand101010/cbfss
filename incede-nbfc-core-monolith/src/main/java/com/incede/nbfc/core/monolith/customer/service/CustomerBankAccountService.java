package com.incede.nbfc.core.monolith.customer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerBankAccount;
import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerBankAccountMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerBankAccountRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.repository.AccountStatusesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.AccountTypeMasterRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerBankAccountService {

    private final CustomerRepository customerRepository;
    private final CustomerBankAccountRepository customerBankAccountRepository;
    private final CustomerBankAccountMapper customerBankAccountMapper;
    private final AccountTypeMasterRepository accountTypeMasterRepository;
    private final AccountStatusesRepository accountStatusesRepository;
    private final ObjectMapper objectMapper;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Transactional
    public CustomerBankAccountResponseDto createBankAccount(UUID identity, String requestJson, MultipartFile bankProof) {
        try {
            CustomerBankAccountRequestDto requestDto = objectMapper.readValue(requestJson, CustomerBankAccountRequestDto.class);
            validate(requestDto);

            Customer customer = customerRepository.findByIdentity(identity)
                    .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, identity.toString()));

            checkAccountConflicts(requestDto, customer);

            CustomerBankAccount bankAccount = customerBankAccountMapper.toEntity(requestDto);
            bankAccount.setAccountType(fetchAccountType(requestDto.getAccountType()));
            bankAccount.setAccountStatus(fetchAccountStatus(requestDto.getAccountStatus()));
            bankAccount.setBankProofDocumentRefId(uploadBankProof(bankProof));
            bankAccount.setCustomer(customer);

            CustomerBankAccount saved = customerBankAccountRepository.save(bankAccount);
            CustomerBankAccountResponseDto.BankAccount accountDetail = customerBankAccountMapper.toAccountDetail(saved);

            return customerBankAccountMapper.toResponse(
                    customer,
                    CommonConstants.CUSTOMER_STATUS_IN_PROGRESS,
                    List.of(accountDetail)
            );
        } catch (ResourceNotFoundException | BusinessException e) {
            log.error("Error creating bank account: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error creating bank account", e);
            throw new BusinessException("Failed to create bank account", ErrorCodes.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Transactional
    public CustomerBankAccountResponseDto updateBankAccount(UUID identity, UUID bankAccountId, CustomerBankAccountRequestDto requestDto) {
        try {
            validate(requestDto);

            Customer customer = customerRepository.findByIdentity(identity)
                    .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, identity.toString()));

            CustomerBankAccount bankAccount = customerBankAccountRepository.findByIdentity(bankAccountId)
                    .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_BANK_ACCOUNT, bankAccountId.toString()));

            if (!bankAccount.getCustomer().getIdentity().equals(customer.getIdentity())) {
                throw new BusinessException(CommonConstants.CUSTOMER_BANK_ACCOUNT_MISMATCH);
            }

            checkAccountConflictsOnUpdate(requestDto, customer, bankAccountId);

            customerBankAccountMapper.updateEntityFromDto(bankAccount, requestDto);
            bankAccount.setAccountType(fetchAccountType(requestDto.getAccountType()));
            bankAccount.setAccountStatus(fetchAccountStatus(requestDto.getAccountStatus()));

            CustomerBankAccount updated = customerBankAccountRepository.save(bankAccount);

            List<CustomerBankAccountResponseDto.BankAccount> accounts =
                    customerBankAccountRepository.findByCustomerAndIsActiveTrue(customer)
                            .stream()
                            .map(customerBankAccountMapper::toAccountDetail)
                            .collect(Collectors.toList());

            return customerBankAccountMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_UPDATED, accounts);

        } catch (ResourceNotFoundException | BusinessException e) {
            log.error("Error updating bank account: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error updating bank account", e);
            throw new BusinessException("Failed to update bank account", ErrorCodes.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Transactional(readOnly = true)
    public CustomerBankAccountResponseDto getActiveBankAccounts(UUID identity) {
        Customer customer = customerRepository.findByIdentity(identity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, identity.toString()));

        List<CustomerBankAccountResponseDto.BankAccount> accounts =
                customerBankAccountRepository.findByCustomerAndIsActiveTrue(customer)
                        .stream()
                        .map(customerBankAccountMapper::toAccountDetail)
                        .collect(Collectors.toList());

        return customerBankAccountMapper.toResponse(customer, CommonConstants.CUSTOMER_STATUS_ACTIVE, accounts);
    }

    private void validate(CustomerBankAccountRequestDto requestDto) {
        Set<ConstraintViolation<CustomerBankAccountRequestDto>> violations = validator.validate(requestDto);
        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .reduce((m1, m2) -> m1 + ", " + m2)
                    .orElse("Validation failed");
            throw new BusinessException(errorMsg, ErrorCodes.VALIDATION_FAILED);
        }
    }

    private void checkAccountConflicts(CustomerBankAccountRequestDto requestDto, Customer customer) {
        if (customerBankAccountRepository.existsByAccountNumberAndCustomer(requestDto.getAccountNumber(), customer)) {
            throw new BusinessException(CommonConstants.CUSTOMER_BANK_ACCOUNT_CONFLICT);
        }
        if (customerBankAccountRepository.existsByUpiIdAndIsDelFalse(requestDto.getUpiId())) {
            throw new BusinessException(CommonConstants.CUSTOMER_BANK_UPI_CONFLICT);
        }
    }

    private void checkAccountConflictsOnUpdate(CustomerBankAccountRequestDto requestDto, Customer customer, UUID bankAccountId) {
        if (customerBankAccountRepository.existsByAccountNumberAndCustomerAndIdentityNot(requestDto.getAccountNumber(), customer, bankAccountId)) {
            throw new BusinessException(CommonConstants.CUSTOMER_BANK_ACCOUNT_CONFLICT);
        }
        if (customerBankAccountRepository.existsByUpiIdAndIsDelFalseAndIdentityNot(requestDto.getUpiId(), bankAccountId)) {
            throw new BusinessException(CommonConstants.CUSTOMER_BANK_UPI_CONFLICT);
        }
    }

    private com.incede.nbfc.core.monolith.masterdata.domain.entity.AccountTypeMaster fetchAccountType(UUID identity) {
        return accountTypeMasterRepository.findByIdentity(identity)
                .orElseThrow(() -> new BusinessException("Invalid AccountType", ErrorCodes.VALIDATION_FAILED));
    }

    private com.incede.nbfc.core.monolith.masterdata.domain.entity.AccountStatuses fetchAccountStatus(UUID identity) {
        return accountStatusesRepository.findByIdentity(identity)
                .orElseThrow(() -> new BusinessException("Invalid AccountStatus", ErrorCodes.VALIDATION_FAILED));
    }

    private Integer uploadBankProof(MultipartFile bankProof) {
        return Math.abs(UUID.randomUUID().hashCode());
    }
}
