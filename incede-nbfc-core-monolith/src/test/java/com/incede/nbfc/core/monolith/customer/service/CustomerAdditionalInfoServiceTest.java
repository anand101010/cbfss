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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerAdditionalInfoServiceTest {

    @Mock
    private CustomerAdditionalInfoMapper customerAdditionalInfoMapper;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerEmploymentRepository employmentRepository;

    @Mock
    private CustomerReferralRepository referralRepository;

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

//    @Mock
//    private ReferralSourcesRepository referralSourcesRepository;

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
    private CustomerGroupRepository customerGroupRepository;

    @Mock
    private CustomerRiskProfileRepository customerRiskProfileRepository;

    @Mock
    private CustomerCategoryMappingRepository categoryMappingRepository;

    @InjectMocks
    private CustomerAdditionalInfoService customerAdditionalInfoService;

    private UUID customerIdentity;
    private Customer customer;
    private CustomerAdditionalInfoRequestDto requestDto;
    private CustomerAdditionalInfoResponseDto responseDto;

    @BeforeEach
    void setUp() {
        customerIdentity = UUID.randomUUID();
        customer = new Customer();
        customer.setIdentity(customerIdentity);
        customer.setCustomerCode("CUST123");
        customer.setOnboardingStatus("ACTIVE");

        // Sample DTO setup
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
                .hasHomeLoan(false)
                .homeLoanCompany(null)
                .build();

        AdditionalInfoCustomerDto additionalCustomerDto = AdditionalInfoCustomerDto.builder()
                .nationality(UUID.randomUUID())
                .preferredLanguageId(UUID.randomUUID())
                .residentialStatusId(UUID.randomUUID())
                .customerGroupId(UUID.randomUUID())
                .riskCategory(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .build();

        AdditionalReferenceValueDto additionalRefDto = AdditionalReferenceValueDto.builder()
                .referenceIdentity(UUID.randomUUID())
                .referenceValue("Test Value")
                .build();

        CustomerAdditionalInfoResponseDto.AdditionalInfoDto additionalInfoDto = CustomerAdditionalInfoResponseDto.AdditionalInfoDto.builder()
                .employment(employmentDto)
                .referrals(referralDto)
                .profileExtra(profileExtraDto)
                .assets(assetDto)
                .additionalInfoCustomerDto(additionalCustomerDto)
                .additionalReferenceValueDto(additionalRefDto)
                .build();

        requestDto = CustomerAdditionalInfoRequestDto.builder()
                .additional(AdditionalInfoDto.builder()
                        .employment(employmentDto)
                        .referrals(referralDto)
                        .profileExtra(profileExtraDto)
                        .customerAsset(assetDto)
                        .customer(additionalCustomerDto)
                        .additionalReferenceValueDto(additionalRefDto)
                        .build())
                .build();

        responseDto = CustomerAdditionalInfoResponseDto.builder()
                .identity(customerIdentity)
                .customerCode("CUST123")
                .status("ACTIVE")
                .additional(additionalInfoDto)
                .build();
    }



    @Test
    void saveAdditionalInfo_CustomerNotFound() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () ->
                customerAdditionalInfoService.saveAdditionalInfo(customerIdentity, requestDto));

        assertEquals(CommonConstants.NOT_FOUND_MESSAGE, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void saveAdditionalInfo_DataIntegrityViolation() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(employmentRepository.findByCustomer(customer)).thenReturn(Optional.empty());
        when(occupationRepository.findByIdentity(any(UUID.class))).thenReturn(Optional.of(new Occupation())); // Added to ensure occupation is found
        when(designationsRepository.findByIdentity(any(UUID.class))).thenReturn(Optional.of(new Designations()));
        when(sourceOfIncomeTypeRepository.findByIdentity(any(UUID.class))).thenReturn(Optional.of(new SourceOfIncomeType()));
        doThrow(DataIntegrityViolationException.class).when(employmentRepository).save(any());

        BusinessException exception = assertThrows(BusinessException.class, () ->
                customerAdditionalInfoService.saveAdditionalInfo(customerIdentity, requestDto));

        assertEquals(CommonConstants.CONSTRAIN_VIOLATION, exception.getMessage());
        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, exception.getErrorCode());
    }

    @Test
    void getAdditionalInfo_Success() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));

        CustomerEmployment employment = new CustomerEmployment();
        when(employmentRepository.findByCustomer(customer)).thenReturn(Optional.of(employment));

        CustomerReferral referral = new CustomerReferral();
        when(referralRepository.findByCustomer(customer)).thenReturn(Optional.of(referral));

        CustomerProfileExtra profileExtra = new CustomerProfileExtra();
        when(profileExtraRepository.findByCustomer(customer)).thenReturn(Optional.of(profileExtra));

        CustomerAsset asset = new CustomerAsset();
        when(assetRepository.findByCustomer(customer)).thenReturn(Optional.of(asset));

        CustomerAdditionalReferenceValue refValue = new CustomerAdditionalReferenceValue();
        when(customerAdditionalReferenceValueRepository.findByCustomer(customer)).thenReturn(Optional.of(refValue));

        when(customerAdditionalInfoMapper.buildResponseDto(customer, employment, referral, profileExtra, asset, refValue))
                .thenReturn(responseDto);

        CustomerAdditionalInfoResponseDto result = customerAdditionalInfoService.getAdditionalInfo(customerIdentity);

        assertNotNull(result);
        assertEquals(responseDto, result);
    }

    @Test
    void getAdditionalInfo_CustomerNotFound() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () ->
                customerAdditionalInfoService.getAdditionalInfo(customerIdentity));

        assertEquals(CommonConstants.NOT_FOUND_MESSAGE, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }
}