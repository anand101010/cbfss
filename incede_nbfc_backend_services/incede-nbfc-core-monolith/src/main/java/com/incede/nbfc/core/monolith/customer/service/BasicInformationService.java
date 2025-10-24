package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.*;
import com.incede.nbfc.core.monolith.customer.dto.*;
import com.incede.nbfc.core.monolith.customer.mapper.BasicInformationMapper;
import com.incede.nbfc.core.monolith.customer.repository.*;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.ContactTypes;
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
import java.util.List;
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
    private final TaxCategoryRepository taxCategoryRepository;
    private final OccupationRepository occupationRepository;
    private final BranchesRepository branchesRepository;
    private final CustomerStatusRepository customerStatusRepository;
    private final SalutationTypesRepository salutationRepository;
    private final TenantRepository tenantRepository;
    private final CustomerContactRepository contactRepository;
    private final ContactTypesRepository contactTypesRepository;

    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerPhotoRepository customerPhotoRepository;
    private final NomineeRepository nomineeRepository;
    private final CustomerBankAccountRepository bankAccountRepository;
    private final CustomerContactRepository customerContactRepository;
    private final CustomerAdditionalInfoService customerAdditionalInfoService;

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

        if(customerRepository.existsByTenantAndAadharVaultId(tenant,dto.getAadharVault())){
            throw new BusinessException(CommonConstants.CUSTOMER_CONFLICT_MESSAGE, ErrorCodes.CONFLICT);
        }
        if (customerRepository.existsByTenantAndMobileNumber(tenant, dto.getMobileNumber())) {
            throw new BusinessException(CommonConstants.MOBILE_NUMBER_CONFLICT_MESSAGE, ErrorCodes.CONFLICT);
        }

        try {
            Customer customer = customerMapper.toEntity(dto);

            populateReferences(customer, dto);

            customer.setIdentity(UUID.randomUUID());
            customer.setCustomerCode(generateCustomerCode(tenant.getTenantId()));
            customer.setOnboardingStatus(CommonConstants.IN_PROGRESS);
            customer.setTenant(tenant);

            Customer savedCustomer = customerRepository.save(customer);

            savePrimaryContact(savedCustomer, dto.getMobileNumber(),dto.getIsVerified());

            return customerMapper.toResponseDto(savedCustomer);
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

            Customer updatedCustomer = customerRepository.save(existingCustomer);

            saveOrUpdatePrimaryContact(updatedCustomer, dto.getMobileNumber(),dto.getIsVerified());

            return customerMapper.toResponseDto(updatedCustomer);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION, ErrorCodes.CONSTRAINT_VIOLATION, e);
        }
    }

    /**
     * Save mobile number as primary contact for customer
     *
     * @param customer    Customer entity
     * @param mobileNumber Mobile number to save as primary contact
     */
    private void savePrimaryContact(Customer customer, String mobileNumber,Boolean isVerified) {
        ContactTypes mobileContactType = contactTypesRepository.findActiveContact("MOBILE")
                .orElseThrow(() -> new BusinessException(CommonConstants.MOBILE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));

        CustomerContact contact = customerMapper.toCustomerContact(customer, mobileNumber, mobileContactType,isVerified);

        contactRepository.save(contact);
        log.info("Primary contact saved for customer: {}", customer.getIdentity());
    }

    /**
     * Save or update primary contact for customer
     *
     * @param customer    Customer entity
     * @param mobileNumber Mobile number to save/update as primary contact
     */
    private void saveOrUpdatePrimaryContact(Customer customer, String mobileNumber,Boolean isVerified) {
        ContactTypes mobileContactType = contactTypesRepository
                .findActiveContact("Mobile")
                .orElseThrow(() -> new BusinessException(CommonConstants.MOBILE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));

        CustomerContact contact = contactRepository
                .findByCustomerAndContactTypeAndIsPrimaryTrue(customer, mobileContactType)
                .orElseGet(() -> customerMapper.toCustomerContact(customer, mobileNumber, mobileContactType,isVerified));

        customerMapper.updateCustomerContact(contact, mobileNumber,isVerified);
        contactRepository.save(contact);

        log.info("Primary contact saved/updated for customer: {}", customer.getIdentity());
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

    /**
     * get customer with customer code
     * @param customerCode
     * @return
     */
    public CustomerDto getCustomerWithCustomerId(String customerCode) {

        Customer customer= customerRepository.findByCustomerCode(customerCode)
                .orElseThrow(() -> new BusinessException(CommonConstants.CUSTOMER_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));


        CustomerDto  customerDto =new CustomerDto();
        customerDto.setFirstname(customer.getFirstName());
        customerDto.setIdentity(customer.getIdentity());
        customerDto.setLastname(customer.getLastName());
        return  customerDto;
    }

    /**
     * get customer with customer identity
     * @param customerIdentity
     * @return
     */
    public CustomerDto getCustomerWithCustomerIdentity(UUID customerIdentity) {

        Customer customer= customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new BusinessException(CommonConstants.CUSTOMER_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));


        CustomerDto  customerDto =new CustomerDto();
        customerDto.setFirstname(customer.getFirstName());
        customerDto.setIdentity(customer.getIdentity());
        customerDto.setLastname(customer.getLastName());
        return  customerDto;
    }

    /**
     * Get  full customer details
     * @param customerId
     * @return
     */


    @Transactional(readOnly = true)
    public CustomerDetailResponseDto getCustomerWithDetails(UUID customerId) {
        Customer customer = customerRepository.findByIdentityAndIsDelFalse(customerId)
                .orElseThrow(() -> new RuntimeException(CommonConstants.CUSTOMER_NOT_FOUND));

        List<CustomerAddress> addresses = customerAddressRepository.findByCustomerCustomerIdAndIsDelFalse(customer.getCustomerId());
        List<CustomerPhoto> photos = customerPhotoRepository.findByCustomerCustomerIdAndIsDelFalse(customer.getCustomerId());
        List<Nominee> nominees = nomineeRepository.findByCustomerCustomerIdAndIsDelFalse(customer.getCustomerId());
        List<CustomerBankAccount> bankAccounts = bankAccountRepository.findByCustomerCustomerIdAndIsDelFalse(customer.getCustomerId());
        List<CustomerContact> contacts = customerContactRepository.findByCustomerCustomerIdAndIsDelFalse(customer.getCustomerId());
        CustomerAdditionalInfoResponseDto additionalInfo = customerAdditionalInfoService.getAdditionalInfo(customer.getIdentity());

        return customerMapper.toCustomerDetailResponse(customer, addresses, photos, nominees, bankAccounts, contacts, additionalInfo);
    }

}