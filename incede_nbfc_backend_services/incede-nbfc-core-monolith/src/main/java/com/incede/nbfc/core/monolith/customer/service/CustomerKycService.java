package com.incede.nbfc.core.monolith.customer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerKyc;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerKycUpload;
import com.incede.nbfc.core.monolith.customer.dto.CustomerKycRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerKycResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.KycDocumentResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.KycUploadResponseDto;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerKycService {

    private final CustomerRepository customerRepository;
    private final CustomerKycRepository customerKycRepository;
    private final CustomerKycMapper customerKycMapper;
    private final ObjectMapper objectMapper;
    private final BranchesRepository branchesRepository;
    private final TenantRepository tenantRepository;
    private final CustomerKycUploadRepository customerKycUploadRepository;
    private final KycTypesRepository kycTypesRepository;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Transactional
    public CustomerKycResponseDto createInitialCustomer(String requestJson, MultipartFile file) {
        try {
            if (requestJson == null || requestJson.trim().isEmpty()) {
                throw new BusinessException(CommonConstants.INVALID_REQUEST, ErrorCodes.VALIDATION_FAILED);
            }

            CustomerKycRequestDto request = objectMapper.readValue(requestJson, CustomerKycRequestDto.class);
            validateRequest(request);

            KycTypes kycType = kycTypesRepository.findByIdentity(request.getIdType())
                    .orElseThrow(() -> new BusinessException(CommonConstants.DOCUMENT_TYPE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));

            Optional<CustomerKyc> existingKyc = customerKycRepository.findByIdTypeAndIdNumber(kycType, request.getIdNumber());
            existingKyc.ifPresent(kyc -> throwConflict(kyc.getCustomer(), kycType, request.getIdNumber(), file));

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
            customerKyc.setIdType(kycType);
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

            KycTypes kycType = kycTypesRepository.findByIdentity(request.getIdType())
                    .orElseThrow(() -> new BusinessException(CommonConstants.DOCUMENT_TYPE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));

            Optional<CustomerKyc> existingKyc = customerKycRepository.findByIdTypeAndIdNumber(kycType, request.getIdNumber());
            existingKyc.ifPresent(kyc -> throwConflict(kyc.getCustomer(), kycType, request.getIdNumber(), file));

            if (customerKycRepository.existsByIdTypeAndCustomer(kycType, existingCustomer)) {
                throw new BusinessException(CommonConstants.DOCUMENT_ALREADY_EXISTS, ErrorCodes.CONFLICT);
            }

            CustomerKyc customerKyc = customerKycMapper.toKycEntity(request, existingCustomer);
            customerKyc.setIdType(kycType);

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

    private void throwConflict(Customer conflictCustomer, KycTypes kycType, String idNumber, MultipartFile file) {
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

    @Transactional(readOnly = true)
    public CustomerKycResponseDto getKycDocuments(UUID customerIdentity) {
        try {
            Customer customer = customerRepository.findByIdentity(customerIdentity)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerIdentity));

            List<CustomerKyc> kycs = customerKycRepository.findByCustomer(customer); // Updated method name

            return buildKycResponseDto(customer, kycs);

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching KYC documents for customer: {}", customerIdentity, e);
            throw new BusinessException("Failed to fetch KYC documents", ErrorCodes.INTERNAL_SERVER_ERROR, e);
        }
    }

    private CustomerKycResponseDto buildKycResponseDto(Customer customer, List<CustomerKyc> kycs) {
        List<KycDocumentResponseDto> kycDocuments = kycs.stream()
                .map(this::mapToKycDocumentDto)
                .collect(Collectors.toList());

        return CustomerKycResponseDto.builder()
                .identity(customer.getIdentity())
                .customerCode(customer.getCustomerCode())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dob(customer.getDob() != null ? customer.getDob().toString() : null)
                .gender(customer.getGender() != null ? customer.getGender().getIdentity() : null)
                .customerStatus(customer.getCustomerStatus() != null ? customer.getCustomerStatus().getIdentity() : null)
                .onboardingStatus(customer.getOnboardingStatus())
                .branchId(customer.getBranchId() != null ? customer.getBranchId().getIdentity() : null)
                .kycDocuments(kycDocuments)
                .build();
    }

    private KycDocumentResponseDto mapToKycDocumentDto(CustomerKyc kyc) {
        return KycDocumentResponseDto.builder()
                .identity(kyc.getIdentity())
                .idType(kyc.getIdType() != null ? kyc.getIdType().getIdentity() : null) // Added null check
                .idNumber(kyc.getIdNumber())
                .placeOfIssue(kyc.getPlaceOfIssue())
                .issuingAuthority(kyc.getIssuingAuthority())
                .validFrom(kyc.getValidFrom() != null ? kyc.getValidFrom().toString() : null)
                .validTo(kyc.getValidTo() != null ? kyc.getValidTo().toString() : null)
                .isVerified(kyc.getIsVerified())
                .isActive(kyc.getIsActive())
                .build();
    }

    private List<KycUploadResponseDto> mapToUploadDtos(List<CustomerKycUpload> uploads) {
        if (uploads == null || uploads.isEmpty()) {
            return Collections.emptyList();
        }

        return uploads.stream()
                .map(this::mapToUploadDto)
                .collect(Collectors.toList());
    }

    private KycUploadResponseDto mapToUploadDto(CustomerKycUpload upload) {
        return KycUploadResponseDto.builder()
                .identity(upload.getIdentity())
                .documentReference(upload.getDocumentReference() != null ? upload.getDocumentReference().toString() : null)
                .fileName(upload.getFileName())
                .fileType(upload.getFileType())
                .uploadStatus(upload.getUploadStatus())
                .version(upload.getVersion())
                .build();
    }
}