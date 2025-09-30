package com.incede.nbfc.core.monolith.customer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerKyc;
import com.incede.nbfc.core.monolith.customer.dto.CustomerKycRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerKycResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerKycMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerKycRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessConflictException;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
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

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Transactional
    public CustomerKycResponseDto createInitialCustomer(String requestJson, MultipartFile file) {
        try {
            CustomerKycRequestDto request = objectMapper.readValue(requestJson, CustomerKycRequestDto.class);

            var violations = validator.validate(request);
            if (!violations.isEmpty()) {
                String errorMsg = violations.stream()
                        .map(v -> v.getPropertyPath() + " " + v.getMessage())
                        .reduce((m1, m2) -> m1 + ", " + m2)
                        .orElse(CommonConstants.INVALID_REQUEST);
                throw new BusinessException(errorMsg, ErrorCodes.VALIDATION_FAILED);
            }

            DocumentType documentType = documentTypeRepository.findByIdentity(request.getIdType())
                    .orElseThrow(() -> new BusinessException(CommonConstants.DOCUMENT_TYPE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));

            // ✅ Check if same document type + number already exists
            Optional<CustomerKyc> existingKyc = customerKycRepository.findByIdTypeAndIdNumber(documentType, request.getIdNumber());
            if (existingKyc.isPresent()) {
                Customer existingCustomer = existingKyc.get().getCustomer();
                List<CustomerKyc> kycList = customerKycRepository.findByCustomer(existingCustomer);
                CustomerKycResponseDto response = customerKycMapper.toResponseDto(existingCustomer, kycList);

                throw new BusinessConflictException(CommonConstants.DOCUMENT_ALREADY_EXISTS, ErrorCodes.CONFLICT, response);
            }

            // Generate new customer
            String customerCode = customerKycMapper.generateCustomerCode(
                    request.getBranchCode(), CommonConstants.CUSTOMER_TYPE
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

            Integer documentRefId = uploadDocument(file);
            customerKyc.setDocumentRefId(documentRefId);

            customerKycRepository.save(customerKyc);

            List<CustomerKyc> kycList = customerKycRepository.findByCustomer(savedCustomer);
            return customerKycMapper.toResponseDto(savedCustomer, kycList);

        } catch (JsonProcessingException e) {
            log.warn("Invalid JSON for kyc request: {} - {}", requestJson, e.getMessage());
            throw new BusinessException(CommonConstants.INVALID_JSON, ErrorCodes.VALIDATION_FAILED, e);
        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while saving kyc for customer. JSON: {}", requestJson, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION, ErrorCodes.CONSTRAINT_VIOLATION, e);
        } catch (IOException e) {
            log.error("File processing failed for customer. File: {}", file.getOriginalFilename(), e);
            throw new BusinessException(CommonConstants.FILE_PROCESSING_FAILED, ErrorCodes.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Transactional
    public CustomerKycResponseDto addKycDocument(String requestJson, MultipartFile file, UUID identity) {
        try {
            CustomerKycRequestDto request = objectMapper.readValue(requestJson, CustomerKycRequestDto.class);

            var violations = validator.validate(request);
            if (!violations.isEmpty()) {
                String errorMsg = violations.stream()
                        .map(v -> v.getPropertyPath() + " " + v.getMessage())
                        .reduce((m1, m2) -> m1 + ", " + m2)
                        .orElse(CommonConstants.INVALID_REQUEST);
                throw new BusinessException(errorMsg, ErrorCodes.VALIDATION_FAILED);
            }

            Customer existingCustomer = customerRepository.findByIdentity(identity)
                    .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.CUSTOMER_NOT_FOUND));

            DocumentType documentType = documentTypeRepository.findByIdentity(request.getIdType())
                    .orElseThrow(() -> new BusinessException(CommonConstants.DOCUMENT_TYPE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));

            // ✅ Check if this document type + number already exists in the system
            Optional<CustomerKyc> existingKyc = customerKycRepository.findByIdTypeAndIdNumber(documentType, request.getIdNumber());
            if (existingKyc.isPresent()) {
                Customer conflictCustomer = existingKyc.get().getCustomer();
                List<CustomerKyc> kycList = customerKycRepository.findByCustomer(conflictCustomer);
                CustomerKycResponseDto response = customerKycMapper.toResponseDto(conflictCustomer, kycList);

                throw new BusinessConflictException(CommonConstants.DOCUMENT_ALREADY_EXISTS, ErrorCodes.CONFLICT, response);
            }

            // ✅ Check if same document type already exists for this customer
            if (customerKycRepository.existsByIdTypeAndCustomer(documentType, existingCustomer)) {
                throw new BusinessException(CommonConstants.DOCUMENT_ALREADY_EXISTS, ErrorCodes.CONFLICT);
            }

            CustomerKyc customerKyc = customerKycMapper.toKycEntity(request, existingCustomer);
            customerKyc.setIdType(documentType);

            Integer documentRefId = uploadDocument(file);
            customerKyc.setDocumentRefId(documentRefId);

            customerKycRepository.save(customerKyc);

            List<CustomerKyc> kycList = customerKycRepository.findByCustomer(existingCustomer);
            return customerKycMapper.toResponseDto(existingCustomer, kycList);

        } catch (JsonProcessingException e) {
            log.warn("Invalid JSON for kyc request: {} - {}", requestJson, e.getMessage());
            throw new BusinessException(CommonConstants.INVALID_JSON, ErrorCodes.VALIDATION_FAILED, e);
        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while saving kyc for customer. JSON: {}", requestJson, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION, ErrorCodes.CONSTRAINT_VIOLATION, e);
        } catch (IOException e) {
            log.error("File processing failed for customer. File: {}", file.getOriginalFilename(), e);
            throw new BusinessException(CommonConstants.FILE_PROCESSING_FAILED, ErrorCodes.INTERNAL_SERVER_ERROR, e);
        }
    }

    public Integer uploadDocument(MultipartFile file) {
        return Math.abs(UUID.randomUUID().hashCode());
    }
}
