package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.dto.BasicInformationRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.BasicInformationResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.BasicInformationMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing basic customer information.
 * Handles create, update, and fetch operations for customers.
 * Exception handling is organized consistently for duplicates, validation, and unexpected errors.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BasicInformationService {

    private final CustomerRepository customerRepository;
    private final BasicInformationMapper customerMapper;

    /**
     * Save basic information for a new customer.
     *
     * @param dto The basic information request DTO
     * @return The created customer response DTO
     * @throws BusinessException on duplicate, validation, or constraint errors
     */
    @Transactional
    public BasicInformationResponseDto saveBasicInformation(BasicInformationRequestDto dto) {
        boolean exists = customerRepository.existsByTenantIdAndFirstNameAndLastName(
                dto.getTenantId(), dto.getFirstName(), dto.getLastName());

        if (exists) {
            log.warn("Duplicate customer detected: {} {}", dto.getFirstName(), dto.getLastName());
            throw new BusinessException(CommonConstants.CONFLICT_MESSAGE, ErrorCodes.CONFLICT);
        }
        try {
        Customer customer = customerMapper.toEntity(dto);
        customer.setIdentity(UUID.randomUUID());
        customer.setCustomerCode(generateCustomerCode(dto.getTenantId()));
        customer.setOnboardingStatus(CommonConstants.Draft);

        if (customer.getDisplayName() == null || customer.getDisplayName().isBlank()) {
            customer.setDisplayName(dto.getFirstName() + " " + dto.getLastName());
        }
        customer.setCreatedBy(dto.getCreatedBy());


            Customer savedCustomer = customerRepository.save(customer);
            return customerMapper.toResponseDto(savedCustomer);

        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while saving customer. DTO: {}", dto, e);
            throw new BusinessException("Constraint violation while saving customer",
                    ErrorCodes.CONSTRAINT_VIOLATION, e);
        } catch (IllegalArgumentException e) {
            log.warn("Validation failed for DTO: {} - {}", dto, e.getMessage());
            throw new BusinessException(e.getMessage(), ErrorCodes.VALIDATION_FAILED, e);
        }
    }

    /**
     * Update basic information for an existing customer.
     *
     * @param identity UUID of the customer to update
     * @param dto      Updated basic information
     * @return Updated customer response DTO
     * @throws BusinessException if customer not found, duplicate exists, or update fails
     */
    @Transactional
    public BasicInformationResponseDto updateBasicInformation(UUID identity, BasicInformationRequestDto dto) {
        Customer existingCustomer = customerRepository.findByIdentity(identity)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.NOT_FOUND_MESSAGE,
                        ErrorCodes.RESOURCE_NOT_FOUND));

        try {
            customerMapper.updateEntityFromDto(existingCustomer, dto);
            existingCustomer.setUpdatedBy(dto.getCreatedBy());
            existingCustomer.setUpdatedAt(java.time.LocalDateTime.now());

            if (existingCustomer.getDisplayName() == null || existingCustomer.getDisplayName().isBlank()) {
                existingCustomer.setDisplayName(dto.getFirstName() + " " + dto.getLastName());
            }

            Optional<Customer> duplicate = customerRepository
                    .findByTenantIdAndFirstNameAndLastName(dto.getTenantId(), dto.getFirstName(), dto.getLastName());
            if (duplicate.isPresent() && !duplicate.get().getIdentity().equals(identity)) {
                log.warn("Duplicate customer name detected for update: {} {}", dto.getFirstName(), dto.getLastName());
                throw new BusinessException(CommonConstants.CONFLICT_MESSAGE, ErrorCodes.CONFLICT);
            }

            Customer updatedCustomer = customerRepository.save(existingCustomer);
            return customerMapper.toResponseDto(updatedCustomer);

        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while updating customer. DTO: {}", dto, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION,
                    ErrorCodes.CONSTRAINT_VIOLATION, e);
        } catch (IllegalArgumentException e) {
            log.warn("Validation failed for DTO: {} - {}", dto, e.getMessage());
            throw new BusinessException(e.getMessage(), ErrorCodes.VALIDATION_FAILED, e);
        }
    }


    /**
     * Fetch customer information by UUID.
     *
     * @param customerUuid UUID of the customer
     * @return Customer response DTO
     * @throws BusinessException if customer not found
     */
    @Transactional(readOnly = true)
    public BasicInformationResponseDto getBasicInformationByUuid(UUID customerUuid) {
        Customer customer = customerRepository.findByIdentity(customerUuid)
                .orElseThrow(() -> new BusinessException(CommonConstants.NOT_FOUND_MESSAGE,
                        ErrorCodes.NOT_FOUND));

        return customerMapper.toResponseDto(customer);
    }

    /**
     * Generate a unique customer code based on tenant ID and a random suffix.
     *
     * @param tenantId Tenant ID
     * @return Generated customer code
     */
    public String generateCustomerCode(Integer tenantId) {
        return String.format("%d-%s", tenantId, UUID.randomUUID().toString().substring(0, 6).toUpperCase());
    }
}
