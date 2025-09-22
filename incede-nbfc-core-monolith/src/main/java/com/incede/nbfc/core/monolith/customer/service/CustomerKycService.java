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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerKycService {

    private final CustomerRepository customerRepository;
    private final CustomerKycRepository customerKycRepository;
    private final CustomerKycMapper customerKycMapper;
    private final ObjectMapper objectMapper;

    @Transactional
    public CustomerKycResponseDto createInitialCustomer(String requestJson, MultipartFile file) {
        try {
            CustomerKycRequestDto request = objectMapper.readValue(requestJson, CustomerKycRequestDto.class);
            String customerCode = customerKycMapper.generateCustomerCode(
                    request.getBranchCode(),
                    request.getCustomerType()
            );
            Customer customer = customerKycMapper.toCustomerEntity(request, customerCode);
            Customer savedCustomer = customerRepository.save(customer);
            CustomerKyc customerKyc = customerKycMapper.toKycEntity(request, savedCustomer);
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
        } catch (IllegalArgumentException e) {
            log.warn("Validation failed for kyc DTO: {} - {}", requestJson, e.getMessage());
            throw new BusinessException(e.getMessage(), ErrorCodes.VALIDATION_FAILED, e);
        }
    }

    public Integer uploadDocument(MultipartFile file) {
        return Math.abs(UUID.randomUUID().hashCode());
    }
}
