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
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Branches;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.DocumentType;
import com.incede.nbfc.core.monolith.masterdata.repository.BranchesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.DocumentTypeRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * Creates an initial customer along with their KYC details.
     *
     * @param requestJson JSON string containing customer and KYC request data
     * @param file        Multipart file representing the KYC document to upload
     * @return {@link CustomerKycResponseDto} containing saved customer and KYC information
     * @throws BusinessException if validation fails, document type not found, JSON parsing fails,
     *                           or constraint violations occur
     */
    @Transactional
    public CustomerKycResponseDto createInitialCustomer(String requestJson, MultipartFile file) {
        try {
            CustomerKycRequestDto request = objectMapper.readValue(requestJson, CustomerKycRequestDto.class);

            var violations = validator.validate(request);
            if (!violations.isEmpty()) {
                String errorMsg = violations.stream()
                        .map(v -> v.getPropertyPath() + " " + v.getMessage())
                        .reduce((m1, m2) -> m1 + ", " + m2)
                        .orElse("Invalid request");
                throw new BusinessException(errorMsg, ErrorCodes.VALIDATION_FAILED);
            }

            String customerCode = customerKycMapper.generateCustomerCode(
                    request.getBranchCode(),
                    request.getCustomerType()
            );
            UUID identity = UUID.randomUUID();

            Customer customer = customerKycMapper.toCustomerEntity(request, customerCode, identity);
            Branches branch = branchesRepository.findByIdentity(request.getBranchId())
                    .orElseThrow(() -> new BusinessException("Invalid branch ID"));

            customer.setBranchId(branch);
            Customer savedCustomer = customerRepository.save(customer);

            DocumentType documentType = documentTypeRepository.findByIdentity(request.getIdType())
                    .orElseThrow(() -> new BusinessException("Document type not found", ErrorCodes.RESOURCE_NOT_FOUND));

            CustomerKyc customerKyc = customerKycMapper.toKycEntity(request, savedCustomer);
            customerKyc.setIdType(documentType);


            Integer documentRefId = uploadDocument(file);
            customerKyc.setDocumentRefId(documentRefId);

            CustomerKyc savedCustomerKyc = customerKycRepository.save(customerKyc);
            return customerKycMapper.toResponseDto(savedCustomer, savedCustomerKyc);

        } catch (JsonProcessingException e) {
            log.warn("Invalid JSON for kyc request: {} - {}", requestJson, e.getMessage());
            throw new BusinessException(CommonConstants.INVALID_JSON, ErrorCodes.VALIDATION_FAILED, e);
        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while saving kyc for customer. JSON: {}", requestJson, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION,
                    ErrorCodes.CONSTRAINT_VIOLATION, e);
        } catch (IOException e) {
            log.error("File processing failed for customer. File: {}", file.getOriginalFilename(), e);
            throw new BusinessException("Error processing file upload", ErrorCodes.INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Adds a new KYC document for an existing customer.
     *
     * @param requestJson JSON string containing KYC request data
     * @param file        Multipart file representing the KYC document
     * @param identity    UUID identity of the customer
     * @return {@link CustomerKycResponseDto} containing saved KYC information
     * @throws BusinessException if validation fails, document type not found, duplicate document exists,
     *                           or constraint violations occur
     */
    @Transactional
    public CustomerKycResponseDto addKycDocument(String requestJson, MultipartFile file, UUID identity) {
        try {
            CustomerKycRequestDto request = objectMapper.readValue(requestJson, CustomerKycRequestDto.class);

            var violations = validator.validate(request);
            if (!violations.isEmpty()) {
                String errorMsg = violations.stream()
                        .map(v -> v.getPropertyPath() + " " + v.getMessage())
                        .reduce((m1, m2) -> m1 + ", " + m2)
                        .orElse("Invalid request");
                throw new BusinessException(errorMsg, ErrorCodes.VALIDATION_FAILED);
            }

            Customer existingCustomer = customerRepository.findByIdentity(identity)
                    .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.NOT_FOUND_MESSAGE));

            DocumentType documentType = documentTypeRepository.findByIdentity(request.getIdType())
                    .orElseThrow(() -> new BusinessException("Document type not found", ErrorCodes.RESOURCE_NOT_FOUND));

            if (customerKycRepository.existsByIdTypeAndCustomer(documentType, existingCustomer)) {
                throw new BusinessException(CommonConstants.DOCUMENT_ALREADY_EXISTS);
            }

            CustomerKyc customerKyc = customerKycMapper.toKycEntity(request, existingCustomer);
            customerKyc.setIdType(documentType);

            Integer documentRefId = uploadDocument(file);
            customerKyc.setDocumentRefId(documentRefId);

            CustomerKyc savedCustomerKyc = customerKycRepository.save(customerKyc);
            return customerKycMapper.toResponseDto(existingCustomer, savedCustomerKyc);

        } catch (JsonProcessingException e) {
            log.warn("Invalid JSON for kyc request: {} - {}", requestJson, e.getMessage());
            throw new BusinessException(CommonConstants.INVALID_JSON, ErrorCodes.VALIDATION_FAILED, e);
        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while saving kyc for customer. JSON: {}", requestJson, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION,
                    ErrorCodes.CONSTRAINT_VIOLATION, e);
        } catch (IOException e) {
            log.error("File processing failed for customer. File: {}", file.getOriginalFilename(), e);
            throw new BusinessException("Error processing file upload", ErrorCodes.INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     * Uploads the provided KYC document file and returns a reference ID.
     * <p>
     * Currently implemented as a stub that generates a random integer.
     *
     * @param file KYC document file
     * @return unique integer reference ID for the uploaded file
     */
    public Integer uploadDocument(MultipartFile file) {
        return Math.abs(UUID.randomUUID().hashCode());
    }
}
