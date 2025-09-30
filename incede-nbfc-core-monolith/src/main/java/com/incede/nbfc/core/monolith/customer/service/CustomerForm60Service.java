package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerForm60;
import com.incede.nbfc.core.monolith.customer.dto.CustomerForm60RequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerForm60ResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerForm60Mapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerForm60Repository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.DocumentMaster;
import com.incede.nbfc.core.monolith.masterdata.repository.DocumentMasterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerForm60Service {
    private final CustomerForm60Repository form60Repository;
    private final CustomerForm60Mapper form60Mapper;
    private final CustomerRepository customerRepository;
    private final DocumentMasterRepository documentRepository;
    private final CustomerForm60Repository customerForm60Repository;

    private static final BigDecimal MAX_TRANSACTION_AMOUNT = new BigDecimal("500000");

    @Transactional
    public CustomerForm60ResponseDto saveForm60(CustomerForm60RequestDto request, UUID customerIdentity) {

        validateForm60Request(request);

        try {

            Customer customer = customerRepository.findByIdentity(customerIdentity)
                    .orElseThrow(() -> new BusinessException(CommonConstants.ENTITY_CUSTOMER, ErrorCodes.NOT_FOUND));

            DocumentMaster pidDoc = request.getPidDocumentId() != null
                    ? documentRepository.findById(Math.toIntExact(request.getPidDocumentId()))
                    .orElseThrow(() -> new BusinessException(CommonConstants.PID_DOCUMENT_NOT_FOUND, ErrorCodes.NOT_FOUND))
                    : null;

            DocumentMaster addDoc = request.getAddDocumentId() != null
                    ? documentRepository.findById(Math.toIntExact(request.getAddDocumentId()))
                    .orElseThrow(() -> new BusinessException(CommonConstants.ADDRESS_DOC_NOT_FOUND, ErrorCodes.NOT_FOUND))
                    : null;

            if (request.getCreatedBy() == null) {
                throw new BusinessException(CommonConstants.CREATED_BY_REQUIRED, ErrorCodes.VALIDATION_FAILED);
            }

            CustomerForm60 entity = form60Mapper.toEntity(request, customer, pidDoc, addDoc);
            entity.setIdentity(UUID.randomUUID());
            entity.setCreatedBy(request.getCreatedBy());

            CustomerForm60 savedForm60 = form60Repository.save(entity);
            return form60Mapper.toResponseDto(savedForm60);

        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while saving Form60. DTO: {}", request, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION,
                    ErrorCodes.CONSTRAINT_VIOLATION, e);
        } catch (IllegalArgumentException e) {
            log.warn("Validation failed for Form60 DTO: {} - {}", request, e.getMessage());
            throw new BusinessException(e.getMessage(), ErrorCodes.VALIDATION_FAILED, e);
        }
    }

    @Transactional
    public CustomerForm60ResponseDto updateForm60(UUID customerIdentity, Integer form60Id, CustomerForm60RequestDto request) {
        try {

            Customer customer = customerRepository.findByIdentity(customerIdentity)
                    .orElseThrow(() -> new BusinessException(
                            CommonConstants.ENTITY_CUSTOMER,
                            ErrorCodes.NOT_FOUND));

            CustomerForm60 existingForm60 = customerForm60Repository
                    .findByForm60IdAndCustomerId(form60Id, customer.getCustomerId())
                    .orElseThrow(() -> new BusinessException(

                            CommonConstants.FORM_60_NOT_FOUND_FOR_THE_CUSTOMER,
                            ErrorCodes.NOT_FOUND));

            DocumentMaster pidDoc = request.getPidDocumentId() != null
                    ? documentRepository.findById(Math.toIntExact(request.getPidDocumentId()))
                    .orElseThrow(() -> new BusinessException(CommonConstants.PID_DOCUMENT_NOT_FOUND , ErrorCodes.NOT_FOUND))
                    : null;

            DocumentMaster addDoc = request.getAddDocumentId() != null
                    ? documentRepository.findById(Math.toIntExact(request.getAddDocumentId()))
                    .orElseThrow(() -> new BusinessException(CommonConstants.ADDRESS_DOC_NOT_FOUND, ErrorCodes.NOT_FOUND))
                    : null;

            validateForm60Request(request);

            form60Mapper.updateEntityFromDto(existingForm60, request, pidDoc, addDoc);
            existingForm60.setUpdatedAt(LocalDateTime.now());

            CustomerForm60 updatedForm60 = customerForm60Repository.save(existingForm60);

            return form60Mapper.toResponseDto(updatedForm60);

        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while updating Form 60. Request: {}", request, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION,
                    ErrorCodes.CONSTRAINT_VIOLATION, e);
        } catch (IllegalArgumentException e) {
            log.warn("Validation failed for Form 60 Request: {} - {}", request, e.getMessage());
            throw new BusinessException(e.getMessage(), ErrorCodes.VALIDATION_FAILED, e);
        }
    }

    private void validateForm60Request(CustomerForm60RequestDto request) {
        if (request.getTransactionAmount() == null) {
            throw new BusinessException(CommonConstants.TRANSACTION_AMOUNT_NOT_NULL, ErrorCodes.VALIDATION_FAILED);
        }
        if (request.getTransactionDate() == null) {
            throw new BusinessException(CommonConstants.TRANSACTION_DATE_NOT_NULL, ErrorCodes.VALIDATION_FAILED);
        }

        if (request.getTransactionAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(CommonConstants.TRANSACTION_AMOUNT_MUST_BE_NONZERO, ErrorCodes.VALIDATION_FAILED);
        }

        if (request.getTransactionAmount().compareTo(MAX_TRANSACTION_AMOUNT) > 0) {
            throw new BusinessException(CommonConstants.MAXIMUM_TRANSACTION_AMOUNT_CONSTRAINT,
                    ErrorCodes.VALIDATION_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public CustomerForm60ResponseDto getForm60ById(UUID customerIdentity, Integer form60Id) {
        Customer customer = customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.ENTITY_CUSTOMER,
                        ErrorCodes.NOT_FOUND));

        CustomerForm60 form60 = customerForm60Repository
                .findByForm60IdAndCustomerId(form60Id, customer.getCustomerId())
                .orElseThrow(() -> new BusinessException(
                     CommonConstants.FORM_60_NOT_FOUND_FOR_THE_CUSTOMER,
                        ErrorCodes.NOT_FOUND));

        return form60Mapper.toResponseDto(form60);
    }


}
