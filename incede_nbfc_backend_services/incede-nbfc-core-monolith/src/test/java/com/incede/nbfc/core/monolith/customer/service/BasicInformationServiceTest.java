package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerContact;
import com.incede.nbfc.core.monolith.customer.dto.BasicInformationRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.BasicInformationResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerDto;
import com.incede.nbfc.core.monolith.customer.mapper.BasicInformationMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerContactRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import com.incede.nbfc.core.monolith.tenant.repository.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicInformationServiceTest {

    @Mock private CustomerRepository customerRepository;
    @Mock private BasicInformationMapper customerMapper;
    @Mock private GendersRepository gendersRepository;
    @Mock private MaritalStatusRepository maritalStatusRepository;
    @Mock private NationalityRepository nationalityRepository;
    @Mock private TaxCategoryRepository taxCategoryRepository;
    @Mock private OccupationRepository occupationRepository;
    @Mock private LanguagesRepository languagesRepository;
    @Mock private BranchesRepository branchesRepository;
    @Mock private CustomerStatusRepository customerStatusRepository;
    @Mock private ResidentialStatusesRepository residentialStatusesRepository;
    @Mock private SalutationTypesRepository salutationRepository;
    @Mock private TenantRepository tenantRepository;
    @Mock private CustomerContactRepository contactRepository;
    @Mock private ContactTypesRepository contactTypesRepository;

    @InjectMocks
    private BasicInformationService service;

    private BasicInformationRequestDto requestDto;
    private Customer customer;
    private BasicInformationResponseDto responseDto;
    private Tenant tenant;
    private UUID customerId;
    private UUID tenantId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        tenantId = UUID.randomUUID();

        tenant = new Tenant();
        tenant.setTenantId(1);
        tenant.setIdentity(tenantId);

        requestDto = new BasicInformationRequestDto();
        requestDto.setTenantId(tenantId);
        requestDto.setAadharVault("vault123");
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setAadharName("John Doe");
        requestDto.setDob(LocalDate.of(1990, 1, 1));
        requestDto.setMobileNumber("9999999999");
        requestDto.setIsVerified(true);
        requestDto.setIsBusiness(true);
        requestDto.setIsFirm(false);
        requestDto.setGender(UUID.randomUUID());
        requestDto.setMaritalStatus(UUID.randomUUID());
        requestDto.setTaxCategory(UUID.randomUUID());
        requestDto.setOccupation(UUID.randomUUID());
        requestDto.setBranchId(UUID.randomUUID());
        requestDto.setSalutation(UUID.randomUUID());
        requestDto.setCustomerStatus(UUID.randomUUID());
        requestDto.setIsMinor(false);
        requestDto.setGuardianCustomerId(null);

        customer = new Customer();
        customer.setIdentity(customerId);
        customer.setAadharVaultId("vault123");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setCreatedAt(LocalDateTime.now());

        responseDto = new BasicInformationResponseDto();
        responseDto.setIdentity(customerId);
    }

    private void mockSavePrimaryContact() {
        ContactTypes mobileType = new ContactTypes();
        when(contactTypesRepository.findActiveContact("MOBILE")).thenReturn(Optional.of(mobileType));
        CustomerContact contact = new CustomerContact();
        when(customerMapper.toCustomerContact(any(Customer.class), anyString(), any(ContactTypes.class), anyBoolean()))
                .thenReturn(contact);
        when(contactRepository.save(any(CustomerContact.class))).thenReturn(contact);
    }

    private void mockSaveOrUpdatePrimaryContact(boolean hasExistingContact) {
        ContactTypes mobileType = new ContactTypes();
        when(contactTypesRepository.findActiveContact("Mobile")).thenReturn(Optional.of(mobileType));
        CustomerContact contact = new CustomerContact();
        when(customerMapper.toCustomerContact(any(Customer.class), anyString(), any(ContactTypes.class), anyBoolean()))
                .thenReturn(contact);
        if (hasExistingContact) {
            when(contactRepository.findByCustomerAndContactTypeAndIsPrimaryTrue(any(Customer.class), any(ContactTypes.class)))
                    .thenReturn(Optional.of(contact));
            doNothing().when(customerMapper).updateCustomerContact(any(CustomerContact.class), anyString(), anyBoolean());
        } else {
            when(contactRepository.findByCustomerAndContactTypeAndIsPrimaryTrue(any(Customer.class), any(ContactTypes.class)))
                    .thenReturn(Optional.empty());
        }
        when(contactRepository.save(any(CustomerContact.class))).thenReturn(contact);
    }

    @Test
    void saveBasicInformation_success() {
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));
        when(customerRepository.existsByTenantAndAadharVaultId(tenant, "vault123")).thenReturn(false);
        when(customerRepository.existsByTenantAndMobileNumber(tenant, "9999999999")).thenReturn(false);
        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toResponseDto(customer)).thenReturn(responseDto);

        mockAllReferenceRepositories();
        mockSavePrimaryContact();

        BasicInformationResponseDto result = service.saveBasicInformation(requestDto);

        assertNotNull(result);
        assertEquals(customerId, result.getIdentity());
        verify(customerRepository).save(any(Customer.class));
        verify(customerMapper).toResponseDto(customer);
        verify(contactRepository).save(any(CustomerContact.class));
    }

    @Test
    void saveBasicInformation_tenantNotFound() {
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.saveBasicInformation(requestDto));

        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
        assertEquals(CommonConstants.TENANT_NOT_FOUND, ex.getMessage());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void saveBasicInformation_duplicateAadhar() {
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));
        when(customerRepository.existsByTenantAndAadharVaultId(tenant, "vault123")).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.saveBasicInformation(requestDto));

        assertEquals(ErrorCodes.CONFLICT, ex.getErrorCode());
        assertEquals(CommonConstants.CUSTOMER_CONFLICT_MESSAGE, ex.getMessage());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void saveBasicInformation_dataIntegrityViolation() {
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));
        when(customerRepository.existsByTenantAndAadharVaultId(tenant, "vault123")).thenReturn(false);
        when(customerRepository.existsByTenantAndMobileNumber(tenant, "9999999999")).thenReturn(false);
        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
        when(customerRepository.save(any())).thenThrow(new DataIntegrityViolationException("duplicate"));

        mockAllReferenceRepositories();

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.saveBasicInformation(requestDto));

        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, ex.getErrorCode());
        assertEquals(CommonConstants.CONSTRAIN_VIOLATION, ex.getMessage());
        assertNotNull(ex.getCause());
    }

    @Test
    void saveBasicInformation_withMinorAndGuardian() {
        UUID guardianId = UUID.randomUUID();
        Customer guardian = new Customer();
        guardian.setIdentity(guardianId);

        requestDto.setIsMinor(true);
        requestDto.setGuardianCustomerId(guardianId);

        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));
        when(customerRepository.existsByTenantAndAadharVaultId(tenant, "vault123")).thenReturn(false);
        when(customerRepository.existsByTenantAndMobileNumber(tenant, "9999999999")).thenReturn(false);
        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
        when(customerRepository.findByIdentity(guardianId)).thenReturn(Optional.of(guardian));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toResponseDto(customer)).thenReturn(responseDto);

        mockAllReferenceRepositories();
        mockSavePrimaryContact();

        BasicInformationResponseDto result = service.saveBasicInformation(requestDto);

        assertNotNull(result);
        verify(customerRepository).findByIdentity(guardianId);
        verify(contactRepository).save(any(CustomerContact.class));
    }

    @Test
    void updateBasicInformation_success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));
        when(customerRepository.findByTenantAndAadharVaultId(tenant, "vault123")).thenReturn(Optional.of(customer));
        doNothing().when(customerMapper).updateEntityFromDto(any(Customer.class), any(BasicInformationRequestDto.class));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toResponseDto(customer)).thenReturn(responseDto);

        mockAllReferenceRepositories();
        mockSaveOrUpdatePrimaryContact(false);

        BasicInformationResponseDto result = service.updateBasicInformation(customerId, requestDto);

        assertNotNull(result);
        assertEquals(customerId, result.getIdentity());
        verify(customerMapper).updateEntityFromDto(customer, requestDto);
        verify(customerRepository).save(customer);
        verify(contactRepository).save(any(CustomerContact.class));
    }

    @Test
    void updateBasicInformation_customerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.updateBasicInformation(customerId, requestDto));

        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
        assertEquals(CommonConstants.CUSTOMER_NOT_FOUND, ex.getMessage());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void updateBasicInformation_tenantNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.updateBasicInformation(customerId, requestDto));

        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
        assertEquals(CommonConstants.TENANT_NOT_FOUND, ex.getMessage());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void updateBasicInformation_duplicateAadharDifferentCustomer() {
        Customer differentCustomer = new Customer();
        differentCustomer.setIdentity(UUID.randomUUID());

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));
        when(customerRepository.findByTenantAndAadharVaultId(tenant, "vault123")).thenReturn(Optional.of(differentCustomer));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.updateBasicInformation(customerId, requestDto));

        assertEquals(ErrorCodes.CONFLICT, ex.getErrorCode());
        assertEquals(CommonConstants.CUSTOMER_CONFLICT_MESSAGE, ex.getMessage());
    }

    @Test
    void updateBasicInformation_dataIntegrityViolation() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));
        when(customerRepository.findByTenantAndAadharVaultId(tenant, "vault123")).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenThrow(new DataIntegrityViolationException("duplicate"));

        mockAllReferenceRepositories();

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.updateBasicInformation(customerId, requestDto));

        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, ex.getErrorCode());
        assertEquals(CommonConstants.CONSTRAIN_VIOLATION, ex.getMessage());
    }

    @Test
    void updateBasicInformation_withMinorAndGuardian() {
        UUID guardianId = UUID.randomUUID();
        Customer guardian = new Customer();
        guardian.setIdentity(guardianId);

        requestDto.setIsMinor(true);
        requestDto.setGuardianCustomerId(guardianId);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));
        when(customerRepository.findByTenantAndAadharVaultId(tenant, "vault123")).thenReturn(Optional.of(customer));
        when(customerRepository.findByIdentity(guardianId)).thenReturn(Optional.of(guardian));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toResponseDto(customer)).thenReturn(responseDto);

        mockAllReferenceRepositories();
        mockSaveOrUpdatePrimaryContact(false);

        BasicInformationResponseDto result = service.updateBasicInformation(customerId, requestDto);

        assertNotNull(result);
        verify(customerRepository).findByIdentity(guardianId);
        verify(contactRepository).save(any(CustomerContact.class));
    }

    @Test
    void getBasicInformationByUuid_success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.toResponseDto(customer)).thenReturn(responseDto);

        BasicInformationResponseDto result = service.getBasicInformationByUuid(customerId);

        assertNotNull(result);
        assertEquals(customerId, result.getIdentity());
        verify(customerRepository).findByIdentity(customerId);
        verify(customerMapper).toResponseDto(customer);
    }

    @Test
    void getBasicInformationByUuid_notFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.getBasicInformationByUuid(customerId));

        assertEquals(ErrorCodes.NOT_FOUND, ex.getErrorCode());
        assertEquals(CommonConstants.CUSTOMER_NOT_FOUND, ex.getMessage());
    }

    @Test
    void getCustomerWithCustomerId_success() {
        String customerCode = "CUST123";
        when(customerRepository.findByCustomerCode(customerCode)).thenReturn(Optional.of(customer));

        CustomerDto result = service.getCustomerWithCustomerId(customerCode);

        assertNotNull(result);
        assertEquals(customer.getFirstName(), result.getFirstname());
        assertEquals(customer.getLastName(), result.getLastname());
        assertEquals(customer.getIdentity(), result.getIdentity());
        verify(customerRepository).findByCustomerCode(customerCode);
    }

    @Test
    void getCustomerWithCustomerId_notFound() {
        String customerCode = "CUST123";
        when(customerRepository.findByCustomerCode(customerCode)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.getCustomerWithCustomerId(customerCode));

        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
        assertEquals(CommonConstants.CUSTOMER_NOT_FOUND, ex.getMessage());
    }

    @Test
    void getCustomerWithCustomerIdentity_success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        CustomerDto result = service.getCustomerWithCustomerIdentity(customerId);

        assertNotNull(result);
        assertEquals(customer.getFirstName(), result.getFirstname());
        assertEquals(customer.getLastName(), result.getLastname());
        assertEquals(customer.getIdentity(), result.getIdentity());
        verify(customerRepository).findByIdentity(customerId);
    }

    @Test
    void getCustomerWithCustomerIdentity_notFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.getCustomerWithCustomerIdentity(customerId));

        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
        assertEquals(CommonConstants.CUSTOMER_NOT_FOUND, ex.getMessage());
    }

    @Test
    void generateCustomerCode_nullTenantId() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.generateCustomerCode(null));

        assertEquals("Tenant ID cannot be null", ex.getMessage());
    }

    @Test
    void saveBasicInformation_genderNotFound() {
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));
        when(customerRepository.existsByTenantAndAadharVaultId(tenant, "vault123")).thenReturn(false);
        when(customerRepository.existsByTenantAndMobileNumber(tenant, "9999999999")).thenReturn(false);
        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
        when(gendersRepository.findByIdentity(requestDto.getGender())).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.saveBasicInformation(requestDto));

        assertEquals(ErrorCodes.VALIDATION_FAILED, ex.getErrorCode());
        assertEquals(CommonConstants.INVALID_GENDER, ex.getMessage());
    }

    @Test
    void saveBasicInformation_maritalStatusNotFound() {
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));
        when(customerRepository.existsByTenantAndAadharVaultId(tenant, "vault123")).thenReturn(false);
        when(customerRepository.existsByTenantAndMobileNumber(tenant, "9999999999")).thenReturn(false);
        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
        when(gendersRepository.findByIdentity(requestDto.getGender())).thenReturn(Optional.of(new Genders()));
        when(maritalStatusRepository.findByIdentity(requestDto.getMaritalStatus())).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.saveBasicInformation(requestDto));

        assertEquals(ErrorCodes.VALIDATION_FAILED, ex.getErrorCode());
        assertEquals(CommonConstants.INVALID_MARITAL_STATUS, ex.getMessage());
    }

    @Test
    void savePrimaryContact_mobileContactTypeNotFound() {
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));
        when(customerRepository.existsByTenantAndAadharVaultId(tenant, "vault123")).thenReturn(false);
        when(customerRepository.existsByTenantAndMobileNumber(tenant, "9999999999")).thenReturn(false);
        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
        when(contactTypesRepository.findActiveContact("MOBILE")).thenReturn(Optional.empty());

        mockAllReferenceRepositories();

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.saveBasicInformation(requestDto));

        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
        assertEquals(CommonConstants.MOBILE_NOT_FOUND, ex.getMessage());
    }

    @Test
    void saveOrUpdatePrimaryContact_mobileContactTypeNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));
        when(customerRepository.findByTenantAndAadharVaultId(tenant, "vault123")).thenReturn(Optional.of(customer));
        when(contactTypesRepository.findActiveContact("Mobile")).thenReturn(Optional.empty());

        mockAllReferenceRepositories();

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.updateBasicInformation(customerId, requestDto));

        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
        assertEquals(CommonConstants.MOBILE_NOT_FOUND, ex.getMessage());
    }



    @Test
    void saveBasicInformation_withIsVerifiedFalse() {
        requestDto.setIsVerified(false);

        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));
        when(customerRepository.existsByTenantAndAadharVaultId(tenant, "vault123")).thenReturn(false);
        when(customerRepository.existsByTenantAndMobileNumber(tenant, "9999999999")).thenReturn(false);
        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toResponseDto(customer)).thenReturn(responseDto);

        mockAllReferenceRepositories();
        mockSavePrimaryContact();

        BasicInformationResponseDto result = service.saveBasicInformation(requestDto);

        assertNotNull(result);
        assertEquals(customerId, result.getIdentity());
        verify(contactRepository).save(any(CustomerContact.class));
    }

    @Test
    void updateBasicInformation_withIsVerifiedFalse() {
        requestDto.setIsVerified(false);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));
        when(customerRepository.findByTenantAndAadharVaultId(tenant, "vault123")).thenReturn(Optional.of(customer));
        doNothing().when(customerMapper).updateEntityFromDto(any(Customer.class), any(BasicInformationRequestDto.class));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toResponseDto(customer)).thenReturn(responseDto);

        mockAllReferenceRepositories();
        mockSaveOrUpdatePrimaryContact(false);

        BasicInformationResponseDto result = service.updateBasicInformation(customerId, requestDto);

        assertNotNull(result);
        assertEquals(customerId, result.getIdentity());
        verify(contactRepository).save(any(CustomerContact.class));
    }

    private void mockAllReferenceRepositories() {
        when(gendersRepository.findByIdentity(any(UUID.class))).thenReturn(Optional.of(new Genders()));
        when(maritalStatusRepository.findByIdentity(any(UUID.class))).thenReturn(Optional.of(new MaritalStatus()));
        when(taxCategoryRepository.findByIdentity(any(UUID.class))).thenReturn(Optional.of(new TaxCategory()));
        when(occupationRepository.findByIdentity(any(UUID.class))).thenReturn(Optional.of(new Occupation()));
        when(branchesRepository.findByIdentity(any(UUID.class))).thenReturn(Optional.of(new Branches()));
        when(salutationRepository.findByIdentity(any(UUID.class))).thenReturn(Optional.of(new SalutationTypes()));
        when(customerStatusRepository.findByIdentity(any(UUID.class))).thenReturn(Optional.of(new CustomerStatus()));
    }
}