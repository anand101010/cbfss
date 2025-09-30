package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.dto.BasicInformationRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.BasicInformationResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerDto;
import com.incede.nbfc.core.monolith.customer.mapper.BasicInformationMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import com.incede.nbfc.core.monolith.tenant.repository.TenantRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Basic Information Service", description = "Service for handling basic information of customers")
public class BasicInformationService {

    private final CustomerRepository customerRepository;
    private final BasicInformationMapper customerMapper;
    private final GendersRepository gendersRepository;
    private final MaritalStatusRepository maritalStatusRepository;
    private final NationalityRepository nationalityRepository;
    private final TaxCategoryRepository taxCategoryRepository;
    private final OccupationRepository occupationRepository;
    private final LanguagesRepository languagesRepository;
    private final BranchesRepository branchesRepository;
    private final CustomerStatusRepository customerStatusRepository;
    private final ResidentialStatusesRepository residentialStatusesRepository;
    private final SalutationTypesRepository salutationRepository;
    private final TenantRepository tenantRepository;


    /**
     * Save basic information of a new customer.
     *
     * @param dto BasicInformationRequestDto containing customer details.
     * @return BasicInformationResponseDto with saved customer details.
     */
    @Operation(summary = "Save Basic Information", description = "Creates a new customer with basic information.")
    @Transactional
    public BasicInformationResponseDto saveBasicInformation(
            @Parameter(description = "Basic Information Request DTO", required = true)
            BasicInformationRequestDto dto) {

        Tenant tenant = tenantRepository.findByIdentity(dto.getTenantId())
                .orElseThrow(() -> new BusinessException(CommonConstants.TENANT_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));


        if (customerRepository.existsByTenantAndAadharVaultId(tenant, dto.getAadharVault())) {
            throw new BusinessException(CommonConstants.CUSTOMER_CONFLICT_MESSAGE, ErrorCodes.CONFLICT);
        }

        try {
            Customer customer = customerMapper.toEntity(dto);

            populateReferences(customer, dto);

            customer.setIdentity(UUID.randomUUID());
            customer.setCustomerCode(generateCustomerCode(tenant.getTenantId()));
            customer.setOnboardingStatus(CommonConstants.IN_PROGRESS);

            return customerMapper.toResponseDto(customerRepository.save(customer));
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION, ErrorCodes.CONSTRAINT_VIOLATION, e);
        }
    }

    /**
     * Update basic information of an existing customer.
     *
     * @param identity UUID of the customer to update.
     * @param dto      BasicInformationRequestDto containing updated details.
     * @return BasicInformationResponseDto with updated customer details.
     */
    @Operation(summary = "Update Basic Information", description = "Updates an existing customer's basic information.")
    @Transactional
    public BasicInformationResponseDto updateBasicInformation(
            @Parameter(description = "UUID of customer", required = true)
            UUID identity,
            @Parameter(description = "Basic Information Request DTO", required = true)
            BasicInformationRequestDto dto) {

        Customer existingCustomer = customerRepository.findByIdentity(identity)
                .orElseThrow(() -> new BusinessException(CommonConstants.CUSTOMER_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));
        Tenant tenant = tenantRepository.findByIdentity(dto.getTenantId())
                .orElseThrow(() -> new BusinessException(CommonConstants.TENANT_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));
        try {
            customerMapper.updateEntityFromDto(existingCustomer, dto);
            existingCustomer.setUpdatedAt(LocalDateTime.now());

            Optional<Customer> duplicate = customerRepository.findByTenantAndAadharVaultId(tenant, dto.getAadharVault());
            if (duplicate.isPresent() && !duplicate.get().getIdentity().equals(identity)) {
                throw new BusinessException(CommonConstants.CUSTOMER_CONFLICT_MESSAGE, ErrorCodes.CONFLICT);
            }

            populateReferences(existingCustomer, dto);

            return customerMapper.toResponseDto(customerRepository.save(existingCustomer));
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION, ErrorCodes.CONSTRAINT_VIOLATION, e);
        }
    }

    /**
     * Retrieve basic information for a customer by UUID.
     *
     * @param customerUuid UUID of the customer.
     * @return BasicInformationResponseDto containing customer details.
     */
    @Operation(summary = "Get Basic Information", description = "Fetches basic information of a customer by UUID.")
    @Transactional(readOnly = true)
    public BasicInformationResponseDto getBasicInformationByUuid(
            @Parameter(description = "UUID of customer", required = true)
            UUID customerUuid) {

        Customer customer = customerRepository.findByIdentity(customerUuid)
                .orElseThrow(() -> new BusinessException(CommonConstants.CUSTOMER_NOT_FOUND, ErrorCodes.NOT_FOUND));
        return customerMapper.toResponseDto(customer);
    }

    /**
     * Generates a unique customer code for a tenant.
     *
     * @param tenantId Tenant ID
     * @return Generated customer code string.
     */
    @Operation(summary = "Generate Customer Code", description = "Generates a unique customer code for a given tenant.")
    public String generateCustomerCode(
            @Parameter(description = "Tenant ID", required = true)
            Integer tenantId) {

        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }
        return String.format("%d-%s", tenantId, UUID.randomUUID().toString().substring(0, 6).toUpperCase());
    }

    /**
     * Populates referenced entities for the customer from DTO values.
     * @param customer Customer entity.
     * @param dto      BasicInformationRequestDto.
     */
    private void populateReferences(Customer customer, BasicInformationRequestDto dto) {
        Objects.requireNonNull(dto.getGender(), "Gender is required");
        customer.setGender(gendersRepository.findByIdentity(dto.getGender())
                .orElseThrow(() -> new BusinessException(CommonConstants.INVALID_GENDER, ErrorCodes.VALIDATION_FAILED)));

        Objects.requireNonNull(dto.getMaritalStatus(), "Marital status is required");
        customer.setMaritalStatus(maritalStatusRepository.findByIdentity(dto.getMaritalStatus())
                .orElseThrow(() -> new BusinessException(CommonConstants.INVALID_MARITAL_STATUS, ErrorCodes.VALIDATION_FAILED)));

        Objects.requireNonNull(dto.getTaxCategory(), "Tax category is required");
        customer.setTaxCategory(taxCategoryRepository.findByIdentity(dto.getTaxCategory())
                .orElseThrow(() -> new BusinessException(CommonConstants.INVALID_TAX_CATEGORY, ErrorCodes.VALIDATION_FAILED)));

        Objects.requireNonNull(dto.getOccupation(), "Occupation is required");
        customer.setOccupation(occupationRepository.findByIdentity(dto.getOccupation())
                .orElseThrow(() -> new BusinessException(CommonConstants.INVALID_OCCUPATION, ErrorCodes.VALIDATION_FAILED)));

        Objects.requireNonNull(dto.getBranchId(), "Branch is required");
        customer.setBranchId(branchesRepository.findByIdentity(dto.getBranchId())
                .orElseThrow(() -> new BusinessException(CommonConstants.INVALID_BRANCH, ErrorCodes.VALIDATION_FAILED)));

        Objects.requireNonNull(dto.getSalutation(), "Salutation is required");
        customer.setSalutation(salutationRepository.findByIdentity(dto.getSalutation())
                .orElseThrow(() -> new BusinessException(CommonConstants.INVALID_SALUTATION, ErrorCodes.VALIDATION_FAILED)));

        Objects.requireNonNull(dto.getCustomerStatus(), "Customer status is required");
        customer.setCustomerStatus(customerStatusRepository.findByIdentity(dto.getCustomerStatus())
                .orElseThrow(() -> new BusinessException(CommonConstants.INVALID_CUSTOMER_STATUS, ErrorCodes.VALIDATION_FAILED)));

        if (dto.getGuardianCustomerId() != null && Boolean.TRUE.equals(dto.getIsMinor())) {
            Customer guardian = customerRepository.findByIdentity(dto.getGuardianCustomerId())
                    .orElseThrow(() -> new BusinessException(CommonConstants.GUARDIAN_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));
            customer.setGuardianCustomer(guardian);
        } else {
            customer.setGuardianCustomer(null);
        }
    }

    public CustomerDto getCustomerWithCustomerId(String customerCode) {

        Customer customer= customerRepository.findByCustomerCode(customerCode)
                .orElseThrow(() -> new BusinessException(CommonConstants.CUSTOMER_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));


        CustomerDto  customerDto =new CustomerDto();
        customerDto.setFirstname(customer.getFirstName());
        customerDto.setIdentity(customer.getIdentity());
        customerDto.setLastname(customer.getLastName());
        return  customerDto;
    }

    public CustomerDto getCustomerWithCustomerIdentity(UUID customerIdentity) {

        Customer customer= customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new BusinessException(CommonConstants.CUSTOMER_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));


        CustomerDto  customerDto =new CustomerDto();
        customerDto.setFirstname(customer.getFirstName());
        customerDto.setIdentity(customer.getIdentity());
        customerDto.setLastname(customer.getLastName());
        return  customerDto;
    }
}
