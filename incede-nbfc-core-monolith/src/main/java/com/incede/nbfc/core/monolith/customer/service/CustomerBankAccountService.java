package com.incede.nbfc.core.monolith.customer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerBankAccount;
import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoRequestDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerBankAccountMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerBankAccountRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CustomerBankAccountService {


    private final CustomerRepository customerRepository;
    private final CustomerBankAccountRepository customerBankAccountRepository;
    private final CustomerBankAccountMapper customerBankAccountMapper;
    private final ObjectMapper objectMapper;

    /**
     *  create a bank account for a customer
     * @param identity
     * @param requestJson
     * @param bankProof
     * @return
     */
    @Transactional
    public CustomerBankAccountResponseDto createBankAccount(UUID identity, String requestJson, MultipartFile bankProof) {
        try {
            CustomerBankAccountRequestDto requestDto = objectMapper.readValue(requestJson, CustomerBankAccountRequestDto.class);

            Customer customer = customerRepository.findByIdentity(identity)
                    .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, identity.toString()));

            if (customerBankAccountRepository.existsByAccountNumberAndCustomer(requestDto.getAccountNumber(), customer)) {
                throw new BusinessException(CommonConstants.CUSTOMER_BANK_ACCOUNT_CONFLICT);
            }

            CustomerBankAccount bankAccount = customerBankAccountMapper.toEntity(requestDto);
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
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bank account", e);
        }
    }

    /**
     * update bank account
     * @param identity
     * @param bankAccountId
     * @param requestDto
     * @return
     */
    @Transactional
    public CustomerBankAccountResponseDto updateBankAccount(UUID identity, Integer bankAccountId, CustomerBankAccountRequestDto requestDto) {
        try {
            Customer customer = customerRepository.findByIdentity(identity)
                    .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, identity.toString()));

            CustomerBankAccount bankAccount = customerBankAccountRepository.findById(bankAccountId)
                    .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_BANK_ACCOUNT, bankAccountId.toString()));

            if (!bankAccount.getCustomer().getIdentity().equals(customer.getIdentity())) {
                throw new BusinessException(CommonConstants.CUSTOMER_BANK_ACCOUNT_MISMATCH);
            }

            customerBankAccountMapper.updateEntityFromDto(bankAccount, requestDto);
            CustomerBankAccount updated = customerBankAccountRepository.save(bankAccount);

            List<CustomerBankAccountResponseDto.BankAccount> accounts =
                    customerBankAccountRepository.findByCustomerAndIsActiveTrue(customer)
                            .stream()
                            .map(customerBankAccountMapper::toAccountDetail)
                            .collect(Collectors.toList());

            return customerBankAccountMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_UPDATED, accounts);

        } catch (ResourceNotFoundException | BusinessException e) {
            throw e;
        }
    }


    /**
     * GET all active bank accounts
     * @param identity
     * @return
     */
    @Transactional
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

    public Integer uploadBankProof(MultipartFile bankProof) {
        return Math.abs(UUID.randomUUID().hashCode());
    }


}

