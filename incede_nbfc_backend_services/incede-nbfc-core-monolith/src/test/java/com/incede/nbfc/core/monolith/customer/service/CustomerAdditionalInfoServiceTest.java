package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.*;
import com.incede.nbfc.core.monolith.customer.dto.*;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerAdditionalInfoMapper;
import com.incede.nbfc.core.monolith.customer.repository.*;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CustomerAdditionalInfoServiceTest {

    @InjectMocks
    private CustomerAdditionalInfoService customerAdditionalInfoService;

    @Mock
    private CustomerAdditionalInfoMapper customerAdditionalInfoMapper;

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerEmploymentRepository employmentRepository;
    @Mock
    private ReferralSourceRepository referralRepository;
    @Mock
    private CustomerReferralRepository customerReferralRepository;
    @Mock
    private CustomerProfileExtraRepository profileExtraRepository;
    @Mock
    private CustomerAssetRepository assetRepository;
    @Mock
    private OccupationRepository occupationRepository;
    @Mock
    private DesignationsRepository designationsRepository;
    @Mock
    private SourceOfIncomeTypeRepository sourceOfIncomeTypeRepository;
    @Mock
    private AssetTypesRepository assetTypesRepository;
    @Mock
    private EducationLevelsRepository educationLevelsRepository;
    @Mock
    private PurposeRepository purposeRepository;
    @Mock
    private CanvassedTypesRepository canvassedTypesRepository;
    @Mock
    private NationalityRepository nationalityRepository;
    @Mock
    private ResidentialStatusesRepository residentialStatusesRepository;
    @Mock
    private LanguagesRepository languagesRepository;
    @Mock
    private CustomerAdditionalReferenceNameRepository customerAdditionalReferenceNameRepository;
    @Mock
    private CustomerAdditionalReferenceValueRepository customerAdditionalReferenceValueRepository;
    @Mock
    private CustomerGroupMasterRepository customerGroupMasterRepository;
    @Mock
    private RiskCategoryRepository riskCategoryRepository;
    @Mock
    private CustomerCategoryRepository customerCategoryRepository;

    private Customer customer;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerId = UUID.randomUUID();
        customer = new Customer();
        customer.setIdentity(customerId);
    }

    @Test
    void testSaveAdditionalInfo_success() {
        CustomerEmploymentDto employmentDto = CustomerEmploymentDto.builder()
                .occupationId(UUID.randomUUID())
                .designationId(UUID.randomUUID())
                .employer("Test Employer")
                .incomeSourceId(UUID.randomUUID())
                .monthlySalary(new BigDecimal("5000.00"))
                .annualIncome(new BigDecimal("60000.00"))
                .build();

        CustomerReferralDto referralDto = CustomerReferralDto.builder()
                .referralSourceId(UUID.randomUUID())
                .canvassedTypeId(UUID.randomUUID())
                .canvasserStaffId(123)
                .build();

        CustomerProfileExtraDto profileExtraDto = CustomerProfileExtraDto.builder()
                .educationLevelId(UUID.randomUUID())
                .purposeId(UUID.randomUUID())
                .build();

        CustomerAssetDto assetDto = CustomerAssetDto.builder()
                .assetTypeId(UUID.randomUUID())
                .ownsAsset(true)
                .hasHomeLoan(true)
                .homeLoanCompany("Test Loan Co.")
                .build();

        AdditionalInfoCustomerDto customerDto = AdditionalInfoCustomerDto.builder()
                .nationality(UUID.randomUUID())
                .preferredLanguageId(UUID.randomUUID())
                .residentialStatusId(UUID.randomUUID())
                .customerGroupId(UUID.randomUUID())
                .riskCategory(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .build();

        AdditionalReferenceValueDto referenceValueDto = AdditionalReferenceValueDto.builder()
                .referenceIdentity(UUID.randomUUID())
                .referenceValue("Test Reference")
                .build();

        AdditionalInfoDto additionalInfoDto = AdditionalInfoDto.builder()
                .employment(employmentDto)
                .referrals(referralDto)
                .profileExtra(profileExtraDto)
                .customerAsset(assetDto)
                .customer(customerDto)
                .additionalReferenceValueDto(List.of(referenceValueDto))
                .build();

        CustomerAdditionalInfoRequestDto requestDto = new CustomerAdditionalInfoRequestDto();
        requestDto.setAdditional(additionalInfoDto);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer savedCustomer = invocation.getArgument(0);
            System.out.println("Saving customer: " + savedCustomer);
            return savedCustomer;
        });

        when(occupationRepository.findByIdentity(any())).thenReturn(Optional.of(new Occupation()));
        when(designationsRepository.findByIdentity(any())).thenReturn(Optional.of(new Designations()));
        when(sourceOfIncomeTypeRepository.findByIdentity(any())).thenReturn(Optional.of(new SourceOfIncomeType()));
        when(referralRepository.findByIdentity(any())).thenReturn(Optional.of(new ReferralSources()));
        when(canvassedTypesRepository.findByIdentity(any())).thenReturn(Optional.of(new CanvassedTypes()));
        when(educationLevelsRepository.findByIdentity(any())).thenReturn(Optional.of(new EducationLevels()));
        when(purposeRepository.findByIdentity(any())).thenReturn(Optional.of(new Purpose()));
        when(assetTypesRepository.findByIdentity(any())).thenReturn(Optional.of(new AssetTypes()));
        when(nationalityRepository.findByIdentity(any())).thenReturn(Optional.of(new Nationality()));
        when(languagesRepository.findByIdentity(any())).thenReturn(Optional.of(new Languages()));
        when(residentialStatusesRepository.findByIdentity(any())).thenReturn(Optional.of(new ResidentialStatuses()));
        when(customerGroupMasterRepository.findByIdentity(any())).thenReturn(Optional.of(new CustomerGroupMaster()));
        when(riskCategoryRepository.findByIdentity(any())).thenReturn(Optional.of(new RiskCategory()));
        when(customerCategoryRepository.findByIdentity(any())).thenReturn(Optional.of(new CustomerCategory()));
        when(customerAdditionalReferenceNameRepository.findByIdentity(any()))
                .thenReturn(Optional.of(new CustomerAdditionalReferenceName()));

        when(employmentRepository.findByCustomer(any())).thenReturn(Optional.empty());
        when(customerReferralRepository.findByCustomer(any())).thenReturn(Optional.empty());
        when(profileExtraRepository.findByCustomer(any())).thenReturn(Optional.empty());
        when(assetRepository.findByCustomer(any())).thenReturn(Optional.empty());
        when(customerAdditionalReferenceValueRepository.findByCustomerAndCustomerAdditionalReferenceName(any(), any())).thenReturn(Optional.empty());

        doAnswer(invocation -> {
            CustomerEmployment employment = invocation.getArgument(0);
            employment.setCustomer(customer);
            employment.setOccupationId(new Occupation());
            employment.setDesignationId(new Designations());
            employment.setIncomeSourceId(new SourceOfIncomeType());
            employment.setEmployer(invocation.getArgument(1, CustomerEmploymentDto.class).getEmployer());
            employment.setMonthlySalary(invocation.getArgument(1, CustomerEmploymentDto.class).getMonthlySalary());
            employment.setAnnualIncome(invocation.getArgument(1, CustomerEmploymentDto.class).getAnnualIncome());
            return null;
        }).when(customerAdditionalInfoMapper).createEmployment(any(), any(), any());

        doAnswer(invocation -> {
            CustomerReferral referral = invocation.getArgument(0);
            referral.setCustomer(customer);
            referral.setReferralSources(new ReferralSources());
            referral.setCanvassedTypeId(new CanvassedTypes());
            referral.setCanvasserStaffId(invocation.getArgument(1, CustomerReferralDto.class).getCanvasserStaffId());
            return null;
        }).when(customerAdditionalInfoMapper).createReferral(any(), any(), any());

        doAnswer(invocation -> {
            CustomerProfileExtra profileExtra = invocation.getArgument(0);
            profileExtra.setCustomer(customer);
            profileExtra.setEducationLevelId(new EducationLevels());
            profileExtra.setPurposeId(new Purpose());
            return null;
        }).when(customerAdditionalInfoMapper).createProfileExtra(any(), any(), any());

        doAnswer(invocation -> {
            CustomerAsset asset = invocation.getArgument(0);
            asset.setCustomer(customer);
            asset.setAssetTypeId(new AssetTypes());
            asset.setOwnsAsset(invocation.getArgument(1, CustomerAssetDto.class).getOwnsAsset());
            asset.setHasHomeLoan(invocation.getArgument(1, CustomerAssetDto.class).getHasHomeLoan());
            asset.setHomeLoanCompany(invocation.getArgument(1, CustomerAssetDto.class).getHomeLoanCompany());
            return null;
        }).when(customerAdditionalInfoMapper).createAsset(any(), any(), any());

        when(customerAdditionalInfoMapper.maptoAddtionalRefValue(any(), any())).thenAnswer(invocation -> {
            CustomerAdditionalReferenceValue refValue = invocation.getArgument(0);
            refValue.setCustomer(customer);
            refValue.setCustomerAdditionalReferenceName(new CustomerAdditionalReferenceName());
            refValue.setReferenceValue(invocation.getArgument(1, AdditionalReferenceValueDto.class).getReferenceValue());
            return refValue;
        });

        when(customerAdditionalInfoMapper.updateCustomerFromAdditionalInfo(any(Customer.class), any(AdditionalInfoCustomerDto.class)))
                .thenAnswer(invocation -> {
                    Customer updatedCustomer = invocation.getArgument(0);
                    System.out.println("Updating customer from AdditionalInfoCustomerDto: " + updatedCustomer);
                    return updatedCustomer;
                });

        when(employmentRepository.save(any(CustomerEmployment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(customerReferralRepository.save(any(CustomerReferral.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(profileExtraRepository.save(any(CustomerProfileExtra.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(assetRepository.save(any(CustomerAsset.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(customerAdditionalReferenceValueRepository.save(any(CustomerAdditionalReferenceValue.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(customerAdditionalInfoMapper.buildResponseDto(any(), any(), any(), any(), any(), any()))
                .thenReturn(new CustomerAdditionalInfoResponseDto());

        CustomerAdditionalInfoResponseDto response =
                customerAdditionalInfoService.saveAdditionalInfo(customerId, requestDto);

        assertNotNull(response);

        verify(customerRepository).findByIdentity(customerId);
        verify(employmentRepository).save(any(CustomerEmployment.class));
        verify(customerReferralRepository).save(any(CustomerReferral.class));
        verify(profileExtraRepository).save(any(CustomerProfileExtra.class));
        verify(assetRepository).save(any(CustomerAsset.class));
        verify(customerRepository).save(any(Customer.class));
        verify(customerAdditionalReferenceValueRepository).save(any(CustomerAdditionalReferenceValue.class));
        verify(customerAdditionalInfoMapper).updateCustomerFromAdditionalInfo(any(Customer.class), any(AdditionalInfoCustomerDto.class));
    }

    @Test
    void testSaveAdditionalInfo_dataIntegrityViolationException() {
        CustomerEmploymentDto employmentDto = CustomerEmploymentDto.builder()
                .occupationId(UUID.randomUUID())
                .designationId(UUID.randomUUID())
                .employer("Test Employer")
                .incomeSourceId(UUID.randomUUID())
                .monthlySalary(new BigDecimal("5000.00"))
                .annualIncome(new BigDecimal("60000.00"))
                .build();

        CustomerReferralDto referralDto = CustomerReferralDto.builder()
                .referralSourceId(UUID.randomUUID())
                .canvassedTypeId(UUID.randomUUID())
                .canvasserStaffId(123)
                .build();

        CustomerProfileExtraDto profileExtraDto = CustomerProfileExtraDto.builder()
                .educationLevelId(UUID.randomUUID())
                .purposeId(UUID.randomUUID())
                .build();

        CustomerAssetDto assetDto = CustomerAssetDto.builder()
                .assetTypeId(UUID.randomUUID())
                .ownsAsset(true)
                .hasHomeLoan(true)
                .homeLoanCompany("Test Loan Co.")
                .build();

        AdditionalInfoCustomerDto customerDto = AdditionalInfoCustomerDto.builder()
                .nationality(UUID.randomUUID())
                .preferredLanguageId(UUID.randomUUID())
                .residentialStatusId(UUID.randomUUID())
                .customerGroupId(UUID.randomUUID())
                .riskCategory(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .build();

        AdditionalInfoDto additionalInfoDto = AdditionalInfoDto.builder()
                .employment(employmentDto)
                .referrals(referralDto)
                .profileExtra(profileExtraDto)
                .customerAsset(assetDto)
                .customer(customerDto)
                .build();

        CustomerAdditionalInfoRequestDto requestDto = new CustomerAdditionalInfoRequestDto();
        requestDto.setAdditional(additionalInfoDto);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(employmentRepository.findByCustomer(any())).thenReturn(Optional.empty());
        when(occupationRepository.findByIdentity(any())).thenReturn(Optional.of(new Occupation()));
        when(designationsRepository.findByIdentity(any())).thenReturn(Optional.of(new Designations()));
        when(sourceOfIncomeTypeRepository.findByIdentity(any())).thenReturn(Optional.of(new SourceOfIncomeType()));

        doAnswer(invocation -> {
            CustomerEmployment employment = invocation.getArgument(0);
            employment.setCustomer(customer);
            employment.setOccupationId(new Occupation());
            employment.setDesignationId(new Designations());
            employment.setIncomeSourceId(new SourceOfIncomeType());
            employment.setEmployer(invocation.getArgument(1, CustomerEmploymentDto.class).getEmployer());
            employment.setMonthlySalary(invocation.getArgument(1, CustomerEmploymentDto.class).getMonthlySalary());
            employment.setAnnualIncome(invocation.getArgument(1, CustomerEmploymentDto.class).getAnnualIncome());
            return null;
        }).when(customerAdditionalInfoMapper).createEmployment(any(), any(), any());

        when(employmentRepository.save(any(CustomerEmployment.class)))
                .thenThrow(new DataIntegrityViolationException("Constraint violation"));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                customerAdditionalInfoService.saveAdditionalInfo(customerId, requestDto));

        assertEquals(CommonConstants.CONSTRAIN_VIOLATION, exception.getMessage());
        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, exception.getErrorCode());
        verify(employmentRepository).save(any(CustomerEmployment.class));
    }

    @Test
    void testSaveAdditionalInfo_illegalArgumentException() {
        CustomerEmploymentDto employmentDto = CustomerEmploymentDto.builder()
                .occupationId(UUID.randomUUID())
                .designationId(UUID.randomUUID())
                .employer("Test Employer")
                .incomeSourceId(UUID.randomUUID())
                .monthlySalary(new BigDecimal("5000.00"))
                .annualIncome(new BigDecimal("60000.00"))
                .build();

        CustomerReferralDto referralDto = CustomerReferralDto.builder()
                .referralSourceId(UUID.randomUUID())
                .canvassedTypeId(UUID.randomUUID())
                .canvasserStaffId(123)
                .build();

        CustomerProfileExtraDto profileExtraDto = CustomerProfileExtraDto.builder()
                .educationLevelId(UUID.randomUUID())
                .purposeId(UUID.randomUUID())
                .build();

        CustomerAssetDto assetDto = CustomerAssetDto.builder()
                .assetTypeId(UUID.randomUUID())
                .ownsAsset(true)
                .hasHomeLoan(true)
                .homeLoanCompany("Test Loan Co.")
                .build();

        AdditionalInfoCustomerDto customerDto = AdditionalInfoCustomerDto.builder()
                .nationality(UUID.randomUUID())
                .preferredLanguageId(UUID.randomUUID())
                .residentialStatusId(UUID.randomUUID())
                .customerGroupId(UUID.randomUUID())
                .riskCategory(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .build();

        AdditionalInfoDto additionalInfoDto = AdditionalInfoDto.builder()
                .employment(employmentDto)
                .referrals(referralDto)
                .profileExtra(profileExtraDto)
                .customerAsset(assetDto)
                .customer(customerDto)
                .build();

        CustomerAdditionalInfoRequestDto requestDto = new CustomerAdditionalInfoRequestDto();
        requestDto.setAdditional(additionalInfoDto);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(employmentRepository.findByCustomer(any())).thenReturn(Optional.empty());
        when(occupationRepository.findByIdentity(any())).thenReturn(Optional.of(new Occupation()));
        when(designationsRepository.findByIdentity(any())).thenReturn(Optional.of(new Designations()));
        when(sourceOfIncomeTypeRepository.findByIdentity(any())).thenReturn(Optional.of(new SourceOfIncomeType()));

        doThrow(new IllegalArgumentException("Invalid employment data"))
                .when(customerAdditionalInfoMapper).createEmployment(any(), any(), any());

        BusinessException exception = assertThrows(BusinessException.class, () ->
                customerAdditionalInfoService.saveAdditionalInfo(customerId, requestDto));

        assertEquals("Invalid employment data", exception.getMessage());
        assertEquals(ErrorCodes.VALIDATION_FAILED, exception.getErrorCode());
        verify(customerAdditionalInfoMapper).createEmployment(any(), any(), any());
        verify(employmentRepository, never()).save(any(CustomerEmployment.class));
    }

    @Test
    void testSaveAdditionalInfo_customerNotFound() {
        UUID customerId = UUID.randomUUID();
        when(customerRepository.findByIdentity(any(UUID.class))).thenReturn(Optional.empty());

        CustomerAdditionalInfoRequestDto requestDto = new CustomerAdditionalInfoRequestDto();

        BusinessException exception = assertThrows(BusinessException.class, () ->
                customerAdditionalInfoService.saveAdditionalInfo(customerId, requestDto));

        assertEquals(CommonConstants.CUSTOMER_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testGetAdditionalInfo_success() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setIdentity(customerId);

        CustomerEmployment employment = new CustomerEmployment();
        CustomerReferral referral = new CustomerReferral();
        CustomerProfileExtra profileExtra = new CustomerProfileExtra();
        CustomerAsset asset = new CustomerAsset();
        List<CustomerAdditionalReferenceValue> referenceValues = new ArrayList<>();

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(employmentRepository.findByCustomer(customer)).thenReturn(Optional.of(employment));
        when(customerReferralRepository.findByCustomer(customer)).thenReturn(Optional.of(referral));
        when(profileExtraRepository.findByCustomer(customer)).thenReturn(Optional.of(profileExtra));
        when(assetRepository.findByCustomer(customer)).thenReturn(Optional.of(asset));
        when(customerAdditionalReferenceValueRepository.findAllByCustomer(customer)).thenReturn(referenceValues);

        CustomerAdditionalInfoResponseDto responseDto = new CustomerAdditionalInfoResponseDto();
        when(customerAdditionalInfoMapper.buildResponseDto(customer, employment, referral, profileExtra, asset, referenceValues))
                .thenReturn(responseDto);

        CustomerAdditionalInfoResponseDto result = customerAdditionalInfoService.getAdditionalInfo(customerId);

        assertNotNull(result);
        assertEquals(responseDto, result);
    }

    @Test
    void testGetAdditionalInfo_customerNotFound() {
        UUID customerId = UUID.randomUUID();
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () ->
                customerAdditionalInfoService.getAdditionalInfo(customerId));

        assertEquals(CommonConstants.NOT_FOUND_MESSAGE, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }
}