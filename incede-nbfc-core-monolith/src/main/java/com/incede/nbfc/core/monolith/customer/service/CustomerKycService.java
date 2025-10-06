
package com.incede.nbfc.core.monolith.customer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.incede.nbfc.core.monolith.masterdata.domain.entity.DocumentType;
import com.incede.nbfc.core.monolith.masterdata.repository.BranchesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.DocumentTypeRepository;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import com.incede.nbfc.core.monolith.tenant.repository.TenantRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerKycService {

    private final CustomerRepository customerRepository;
    private final CustomerKycRepository customerKycRepository;
    private final CustomerKycMapper customerKycMapper;
    private final ObjectMapper objectMapper;
    private final DocumentTypeRepository documentTypeRepository;
    private final BranchesRepository branchesRepository;
    private final TenantRepository tenantRepository;
    private final CustomerKycUploadRepository customerKycUploadRepository;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Transactional
    public CustomerKycResponseDto createInitialCustomer(String requestJson, MultipartFile file) {
        try {
            if (requestJson == null || requestJson.trim().isEmpty()) {
                throw new BusinessException(CommonConstants.INVALID_REQUEST, ErrorCodes.VALIDATION_FAILED);
            }

            CustomerKycRequestDto request = objectMapper.readValue(requestJson, CustomerKycRequestDto.class);
            validateRequest(request);

            DocumentType documentType = documentTypeRepository.findByIdentity(request.getIdType())
                    .orElseThrow(() -> new BusinessException(CommonConstants.DOCUMENT_TYPE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));

            Optional<CustomerKyc> existingKyc = customerKycRepository.findByIdTypeAndIdNumber(documentType, request.getIdNumber());
            existingKyc.ifPresent(kyc -> throwConflict(kyc.getCustomer(), documentType, request.getIdNumber(), file));

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

            Customer savedCustomer = customerRepository.save(customer);

            CustomerKyc customerKyc = customerKycMapper.toKycEntity(request, savedCustomer);
            customerKyc.setIdType(documentType);


            CustomerKyc savedKyc = customerKycRepository.save(customerKyc);


            CustomerKycUpload upload = saveKycDocument(savedKyc, file);

            savedKyc.setDocumentRefId(upload.getDocumentReference());
            customerKycRepository.save(savedKyc);

            List<CustomerKyc> kycList = customerKycRepository.findByCustomer(savedCustomer);
            return customerKycMapper.toResponseDto(savedCustomer, kycList, file);

        } catch (JsonProcessingException e) {
            log.warn("Invalid JSON for KYC request: {}", requestJson, e);
            throw new BusinessException(CommonConstants.INVALID_JSON, ErrorCodes.VALIDATION_FAILED, e);
        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while saving KYC: {}", requestJson, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION, ErrorCodes.CONSTRAINT_VIOLATION, e);
        }
    }

    @Transactional
    public CustomerKycResponseDto addKycDocument(String requestJson, MultipartFile file, UUID identity) {
        try {
            if (requestJson == null || requestJson.trim().isEmpty()) {
                throw new BusinessException(CommonConstants.INVALID_REQUEST, ErrorCodes.VALIDATION_FAILED);
            }

            CustomerKycRequestDto request = objectMapper.readValue(requestJson, CustomerKycRequestDto.class);
            validateRequest(request);

            Customer existingCustomer = customerRepository.findByIdentity(identity)
                    .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.CUSTOMER_NOT_FOUND));

            DocumentType documentType = documentTypeRepository.findByIdentity(request.getIdType())
                    .orElseThrow(() -> new BusinessException(CommonConstants.DOCUMENT_TYPE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));

            Optional<CustomerKyc> existingKyc = customerKycRepository.findByIdTypeAndIdNumber(documentType, request.getIdNumber());
            existingKyc.ifPresent(kyc -> throwConflict(kyc.getCustomer(), documentType, request.getIdNumber(), file));

            if (customerKycRepository.existsByIdTypeAndCustomer(documentType, existingCustomer)) {
                throw new BusinessException(CommonConstants.DOCUMENT_ALREADY_EXISTS, ErrorCodes.CONFLICT);
            }

            CustomerKyc customerKyc = customerKycMapper.toKycEntity(request, existingCustomer);
            customerKyc.setIdType(documentType);

            CustomerKyc savedKyc = customerKycRepository.save(customerKyc);


            CustomerKycUpload upload = saveKycDocument(savedKyc, file);

            savedKyc.setDocumentRefId(upload.getDocumentReference());
            customerKycRepository.save(savedKyc);

            List<CustomerKyc> kycList = customerKycRepository.findByCustomer(existingCustomer);
            return customerKycMapper.toResponseDto(existingCustomer, kycList, file);

        } catch (JsonProcessingException e) {
            log.warn("Invalid JSON for KYC request: {}", requestJson, e);
            throw new BusinessException(CommonConstants.INVALID_JSON, ErrorCodes.VALIDATION_FAILED, e);
        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while saving KYC: {}", requestJson, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION, ErrorCodes.CONSTRAINT_VIOLATION, e);
        }
    }

    private CustomerKycUpload saveKycDocument(CustomerKyc customerKyc, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(CommonConstants.FILE_REQUIRED, ErrorCodes.VALIDATION_FAILED);
        }
        try {
            int fileRefId = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);

            CustomerKycUpload upload = customerKycMapper.toKycUploadEntity(customerKyc, file);
            upload.setDocumentReference(fileRefId);
            customerKycUploadRepository.save(upload);

            log.info("Saving KYC document: {} with reference ID {}", file.getOriginalFilename(), fileRefId);
            return upload;
        } catch (Exception e) {
            log.error("Error saving KYC document", e);
            throw new BusinessException(CommonConstants.FILE_UPLOAD_FAILED, ErrorCodes.INTERNAL_SERVER_ERROR, e);
        }
    }

    private void validateRequest(CustomerKycRequestDto request) {
        Objects.requireNonNull(request);
        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .reduce((m1, m2) -> m1 + ", " + m2)
                    .orElse(CommonConstants.INVALID_REQUEST);
            throw new BusinessException(errorMsg, ErrorCodes.VALIDATION_FAILED);
        }
    }

    private void throwConflict(Customer conflictCustomer, DocumentType documentType, String idNumber, MultipartFile file) {
        if (conflictCustomer == null) {
            throw new BusinessException(CommonConstants.CUSTOMER_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND);
        }

        List<CustomerKyc> kycList = customerKycRepository.findByCustomer(conflictCustomer);
        customerKycMapper.toResponseDto(conflictCustomer, kycList, file);

        Map<String, Object> existingDetails = new HashMap<>();
        existingDetails.put("customerCode", conflictCustomer.getCustomerCode());
        existingDetails.put("firstName", conflictCustomer.getFirstName());
        existingDetails.put("lastName", conflictCustomer.getLastName());
        existingDetails.put("dob", conflictCustomer.getDob() != null ? conflictCustomer.getDob().toString() : null);

        throw new BusinessConflictException(
                String.format("Customer already exists with this ID (%s: %s).", documentType.getDisplayName(), maskIdNumber(idNumber)),
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