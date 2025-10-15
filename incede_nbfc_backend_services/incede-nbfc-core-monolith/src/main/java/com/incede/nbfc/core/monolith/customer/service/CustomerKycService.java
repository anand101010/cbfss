package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerKyc;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerKycUpload;
import com.incede.nbfc.core.monolith.customer.dto.CustomerKycRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerKycResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerKycMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerKycRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerKycUploadRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.*;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Branches;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.KycTypes;
import com.incede.nbfc.core.monolith.masterdata.repository.BranchesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.KycTypesRepository;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import com.incede.nbfc.core.monolith.tenant.repository.TenantRepository;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerKycService {

    private final CustomerRepository customerRepository;
    private final CustomerKycRepository customerKycRepository;
    private final CustomerKycMapper customerKycMapper;
    private final BranchesRepository branchesRepository;
    private final TenantRepository tenantRepository;
    private final CustomerKycUploadRepository customerKycUploadRepository;
    private final KycTypesRepository kycTypesRepository;
    private final Validator validator;

    @Transactional
    public CustomerKycResponseDto createInitialCustomer(CustomerKycRequestDto request) {
        validateRequest(request);

        try {
            if (request == null) {
                throw new BusinessException(CommonConstants.INVALID_REQUEST, ErrorCodes.VALIDATION_FAILED);
            }
            validateRequest(request);
            KycTypes kycType = getKycType(request.getIdType());
            validateNoExistingKyc(kycType, request.getIdNumber(), null);

            Customer customer = createCustomerEntity(request);
            Customer savedCustomer = customerRepository.save(customer);

            CustomerKyc customerKyc = createKycEntity(request, savedCustomer, kycType);
            CustomerKyc savedKyc = customerKycRepository.save(customerKyc);

            saveKycDocument(savedKyc, request);

            return buildCustomerKycResponse(savedCustomer);

        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while saving KYC: {}", request, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION, ErrorCodes.CONSTRAINT_VIOLATION, e);
        }
    }

    @Transactional
    public CustomerKycResponseDto addKycDocument(CustomerKycRequestDto request, UUID customerIdentity) {
        validateRequest(request);

        try {
            Customer existingCustomer = getCustomer(customerIdentity);
            KycTypes kycType = getKycType(request.getIdType());

            validateNoExistingKyc(kycType, request.getIdNumber(), existingCustomer);
            validateKycNotExistsForCustomer(kycType, existingCustomer);

            CustomerKyc customerKyc = createKycEntity(request, existingCustomer, kycType);
            CustomerKyc savedKyc = customerKycRepository.save(customerKyc);

            saveKycDocument(savedKyc, request);

            return buildCustomerKycResponse(existingCustomer);

        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while adding KYC document: {}", request, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION, ErrorCodes.CONSTRAINT_VIOLATION, e);
        }
    }

    @Transactional(readOnly = true)
    public CustomerKycResponseDto getKycDocuments(UUID customerIdentity) {
        Customer customer = getCustomer(customerIdentity);
        return buildCustomerKycResponse(customer);
    }

    // Private helper methods
    private void validateRequest(CustomerKycRequestDto request) {
        Objects.requireNonNull(request, CommonConstants.INVALID_REQUEST);

        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .reduce((m1, m2) -> m1 + ", " + m2)
                    .orElse(CommonConstants.INVALID_REQUEST);
            throw new BusinessException(errorMsg, ErrorCodes.VALIDATION_FAILED);
        }
    }

    private KycTypes getKycType(UUID kycTypeId) {
        return kycTypesRepository.findByIdentity(kycTypeId)
                .orElseThrow(() -> new BusinessException(CommonConstants.DOCUMENT_TYPE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));
    }

    private Customer getCustomer(UUID customerIdentity) {
        return customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.CUSTOMER_NOT_FOUND));
    }

    private void validateNoExistingKyc(KycTypes kycType, String idNumber, Customer currentCustomer) {
        Optional<CustomerKyc> existingKyc = customerKycRepository.findByIdTypeAndIdNumber(kycType, idNumber);

        existingKyc.ifPresent(kyc -> {
            // Skip conflict if it's the same customer (for updates)
            if (currentCustomer != null && currentCustomer.equals(kyc.getCustomer())) {
                return;
            }
            throwConflict(kyc.getCustomer(), kycType, idNumber);
        });
    }

    private void validateKycNotExistsForCustomer(KycTypes kycType, Customer customer) {
        if (customerKycRepository.existsByIdTypeAndCustomer(kycType, customer)) {
            throw new BusinessException(CommonConstants.DOCUMENT_ALREADY_EXISTS, ErrorCodes.CONFLICT);
        }
    }

    private Customer createCustomerEntity(CustomerKycRequestDto request) {
        String customerCode = customerKycMapper.generateCustomerCode(
                Optional.ofNullable(request.getBranchCode()).orElse("UNKNOWN"),
                CommonConstants.CUSTOMER_TYPE
        );
        UUID identity = UUID.randomUUID();

        Customer customer = customerKycMapper.toCustomerEntity(request, customerCode, identity);

        Branches branch = branchesRepository.findByIdentity(request.getBranchId())
                .orElseThrow(() -> new BusinessException(CommonConstants.INVALID_BRANCH, ErrorCodes.RESOURCE_NOT_FOUND));
        customer.setBranchId(branch);

        Tenant tenant = tenantRepository.findByIdentity(request.getTenantId())
                .orElseThrow(() -> new BusinessException(CommonConstants.TENANT_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));
        customer.setTenant(tenant);

        return customer;
    }

    private CustomerKyc createKycEntity(CustomerKycRequestDto request, Customer customer, KycTypes kycType) {
        CustomerKyc customerKyc = customerKycMapper.toKycEntity(request, customer);
        customerKyc.setIdType(kycType);
        customerKyc.setDocumentRefId(request.getDocumentRefId());
        return customerKyc;
    }

    private CustomerKycUpload saveKycDocument(CustomerKyc customerKyc, CustomerKycRequestDto request) {
        try {
            CustomerKycUpload upload = customerKycMapper.toKycUploadEntity(customerKyc, request);
            upload.setDocumentReference(request.getDocumentRefId());

            CustomerKycUpload savedUpload = customerKycUploadRepository.save(upload);
            log.info("Saved KYC document: {} with reference ID {}", request.getFileName(), request.getDocumentRefId());

            return savedUpload;
        } catch (Exception e) {
            log.error("Error saving KYC document for customer: {}", customerKyc.getCustomer().getIdentity(), e);
            throw new BusinessException(CommonConstants.FILE_UPLOAD_FAILED, ErrorCodes.INTERNAL_SERVER_ERROR, e);
        }
    }

    private CustomerKycResponseDto buildCustomerKycResponse(Customer customer) {
        List<CustomerKyc> kycList = customerKycRepository.findByCustomer(customer)
                .orElseThrow(() -> new BusinessException(CommonConstants.KYC_NOT_FOUND_FOR_CUSTOMER, ErrorCodes.RESOURCE_NOT_FOUND));

        List<CustomerKycUpload> uploadList = customerKycUploadRepository.findByCustomer(customer)
                .orElseThrow(() -> new BusinessException(CommonConstants.KYC_UPLOAD_NOT_FOUND_FOR_CUSTOMER, ErrorCodes.RESOURCE_NOT_FOUND));

        return customerKycMapper.toResponseDto(customer, kycList, uploadList);
    }

    private void throwConflict(Customer conflictCustomer, KycTypes kycType, String idNumber) {
        if (conflictCustomer == null) {
            throw new BusinessException(CommonConstants.CUSTOMER_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND);
        }

        List<CustomerKyc> kycList = customerKycRepository.findByCustomer(conflictCustomer)
                .orElseThrow(() -> new BusinessException(CommonConstants.KYC_NOT_FOUND_FOR_CUSTOMER, ErrorCodes.RESOURCE_NOT_FOUND));

        List<CustomerKycUpload> customerKycUploadList = customerKycUploadRepository.findByCustomer(conflictCustomer)
                .orElseThrow(() -> new BusinessException(CommonConstants.KYC_UPLOAD_NOT_FOUND_FOR_CUSTOMER, ErrorCodes.RESOURCE_NOT_FOUND));

        customerKycMapper.toResponseDto(conflictCustomer, kycList, customerKycUploadList);

        Map<String, Object> existingDetails = new HashMap<>();
        existingDetails.put("customerCode", conflictCustomer.getCustomerCode());
        existingDetails.put("firstName", conflictCustomer.getFirstName());
        existingDetails.put("lastName", conflictCustomer.getLastName());
        existingDetails.put("dob", conflictCustomer.getDob() != null ? conflictCustomer.getDob().toString() : null);

        throw new BusinessConflictException(
                String.format("Customer already exists with this ID (%s: %s).", kycType.getDisplayName(), maskIdNumber(idNumber)),
                CommonConstants.DUPLICATE_IDENTIFIER,
                Optional.ofNullable(conflictCustomer.getIdentity()).map(UUID::toString).orElse("UNKNOWN_ID"),
                existingDetails,
                List.of("use_existing", "merge_draft")
        );
    }

    private String maskIdNumber(String idNumber) {
        if (idNumber == null || idNumber.length() < 4) return idNumber;
        return "XXXX XXXX " + idNumber.substring(idNumber.length() - 4);
    }
}