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

import java.time.LocalDateTime;
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
        boolean exists = customerRepository.existsByTenantIdAndAadharVaultId(
                dto.getTenantId(), dto.getAadharVault());

        if (exists) {
            log.warn("Duplicate customer detected for Aadhaar: {}", dto.getAadharVault());
            throw new BusinessException(CommonConstants.CONFLICT_MESSAGE, ErrorCodes.CONFLICT);
        }



        try {
            Customer customer = customerMapper.toEntity(dto);
            customer.setIdentity(UUID.randomUUID());
            customer.setCustomerCode(generateCustomerCode(dto.getTenantId()));
            customer.setOnboardingStatus(CommonConstants.IN_PROGRESS);

            Customer savedCustomer = customerRepository.save(customer);
            log.info("Customer created successfully with Aadhaar: {} and identity: {}",
                    dto.getAadharVault(), savedCustomer.getIdentity());

            return customerMapper.toResponseDto(savedCustomer);

        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while saving customer. Aadhaar: {}, DTO: {}", dto.getAadharVault(), dto, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION,
                    ErrorCodes.CONSTRAINT_VIOLATION, e);
        } catch (IllegalArgumentException e) {
            log.warn("Validation failed for DTO with Aadhaar {}: {}", dto.getAadharVault(), e.getMessage());
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
            existingCustomer.setUpdatedAt(LocalDateTime.now());

            Optional<Customer> duplicate = customerRepository
                    .findByTenantIdAndAadharVaultId(dto.getTenantId(), dto.getAadharVault());

            if (duplicate.isPresent() && !duplicate.get().getIdentity().equals(identity)) {
                log.warn("Duplicate Aadhaar vault detected for update: {}", dto.getAadharVault());
                throw new BusinessException(CommonConstants.CONFLICT_MESSAGE, ErrorCodes.CONFLICT);
            }

            Customer updatedCustomer = customerRepository.save(existingCustomer);
            log.info("Customer {} updated successfully with Aadhaar vault {}", updatedCustomer.getIdentity(), dto.getAadharVault());

            return customerMapper.toResponseDto(updatedCustomer);

        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while updating customer. Aadhaar: {}, DTO: {}", dto.getAadharVault(), dto, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION,
                    ErrorCodes.CONSTRAINT_VIOLATION, e);
        } catch (IllegalArgumentException e) {
            log.warn("Validation failed for DTO with Aadhaar {}: {}", dto.getAadharVault(), e.getMessage());
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
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }
        return String.format("%d-%s", tenantId, UUID.randomUUID().toString().substring(0, 6).toUpperCase());
    }

}
