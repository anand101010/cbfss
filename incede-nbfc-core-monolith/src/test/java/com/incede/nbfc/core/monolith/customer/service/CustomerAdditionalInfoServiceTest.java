//package com.incede.nbfc.core.monolith.customer.service;
//
//import com.incede.nbfc.core.monolith.common.CommonConstants;
//import com.incede.nbfc.core.monolith.customer.domain.entity.*;
//import com.incede.nbfc.core.monolith.customer.dto.*;
//import com.incede.nbfc.core.monolith.customer.mapper.CustomerAdditionalInfoMapper;
//import com.incede.nbfc.core.monolith.customer.repository.*;
//import com.incede.nbfc.core.monolith.exception.BusinessException;
//import com.incede.nbfc.core.monolith.exception.ErrorCodes;
//import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
//import com.incede.nbfc.core.monolith.masterdata.repository.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.math.BigDecimal;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//class CustomerAdditionalInfoServiceTest {
//
//    @InjectMocks
//    private CustomerAdditionalInfoService customerAdditionalInfoService;
//
//    @Mock
//    private CustomerAdditionalInfoMapper customerAdditionalInfoMapper;
//
//    @Mock
//    private CustomerRepository customerRepository;
//    @Mock
//    private CustomerEmploymentRepository employmentRepository;
//    @Mock
//    private ReferralSourceRepository referralRepository;
//    @Mock
//    private CustomerReferralRepository customerReferralRepository;
//    @Mock
//    private CustomerProfileExtraRepository profileExtraRepository;
//    @Mock
//    private CustomerAssetRepository assetRepository;
//    @Mock
//    private OccupationRepository occupationRepository;
//    @Mock
//    private DesignationsRepository designationsRepository;
//    @Mock
//    private SourceOfIncomeTypeRepository sourceOfIncomeTypeRepository;
//    @Mock
//    private AssetTypesRepository assetTypesRepository;
//    @Mock
//    private EducationLevelsRepository educationLevelsRepository;
//    @Mock
//    private PurposeRepository purposeRepository;
//    @Mock
//    private CanvassedTypesRepository canvassedTypesRepository;
//    @Mock
//    private NationalityRepository nationalityRepository;
//    @Mock
//    private ResidentialStatusesRepository residentialStatusesRepository;
//    @Mock
//    private LanguagesRepository languagesRepository;
//    @Mock
//    private CustomerAdditionalReferenceNameRepository customerAdditionalReferenceNameRepository;
//    @Mock
//    private CustomerAdditionalReferenceValueRepository customerAdditionalReferenceValueRepository;
//    @Mock
//    private CustomerGroupRepository customerGroupRepository;
//    @Mock
//    private CustomerRiskProfileRepository customerRiskProfileRepository;
//    @Mock
//    private CustomerCategoryMappingRepository categoryMappingRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testSaveAdditionalInfo_success() {
//        UUID customerId = UUID.randomUUID();
//
//        // Prepare test data
//        CustomerEmploymentDto employmentDto = CustomerEmploymentDto.builder()
//                .occupationId(UUID.randomUUID())
//                .designationId(UUID.randomUUID())
//                .employer("Test Employer")
//                .incomeSourceId(UUID.randomUUID())
//                .monthlySalary(new BigDecimal("5000.00"))
//                .annualIncome(new BigDecimal("60000.00"))
//                .build();
//
//        CustomerReferralDto referralDto = CustomerReferralDto.builder()
//                .referralSourceId(UUID.randomUUID())
//                .canvassedTypeId(UUID.randomUUID())
//                .canvasserStaffId(123)
//                .build();
//
//        CustomerProfileExtraDto profileExtraDto = CustomerProfileExtraDto.builder()
//                .educationLevelId(UUID.randomUUID())
//                .purposeId(UUID.randomUUID())
//                .build();
//
//        CustomerAssetDto assetDto = CustomerAssetDto.builder()
//                .assetTypeId(UUID.randomUUID())
//                .ownsAsset(true)
//                .hasHomeLoan(true)
//                .homeLoanCompany("Test Loan Co.")
//                .build();
//
//        AdditionalInfoCustomerDto customerDto = AdditionalInfoCustomerDto.builder()
//                .nationality(UUID.randomUUID())
//                .preferredLanguageId(UUID.randomUUID())
//                .residentialStatusId(UUID.randomUUID())
//                .customerGroupId(UUID.randomUUID())
//                .riskCategory(UUID.randomUUID())
//                .categoryId(UUID.randomUUID())
//                .build();
//
//        AdditionalReferenceValueDto referenceValueDto = AdditionalReferenceValueDto.builder()
//                .referenceIdentity(UUID.randomUUID())
//                .referenceValue("Test Reference")
//                .build();
//
//        AdditionalInfoDto additionalInfoDto = AdditionalInfoDto.builder()
//                .employment(employmentDto)
//                .referrals(referralDto)
//                .profileExtra(profileExtraDto)
//                .customerAsset(assetDto)
//                .customer(customerDto)
//                .additionalReferenceValueDto(List.of(referenceValueDto))
//                .build();
//
//        CustomerAdditionalInfoRequestDto requestDto = new CustomerAdditionalInfoRequestDto();
//        requestDto.setAdditional(additionalInfoDto);
//
//        Customer customer = new Customer();
//        customer.setIdentity(customerId); // important
//
//        // MOCK: Ensure customer is returned regardless of UUID instance
//        when(customerRepository.findByIdentity(any(UUID.class)))
//                .thenReturn(Optional.of(customer));
//
//        when(occupationRepository.findByIdentity(any())).thenReturn(Optional.of(new Occupation()));
//        when(designationsRepository.findByIdentity(any())).thenReturn(Optional.of(new Designations()));
//        when(sourceOfIncomeTypeRepository.findByIdentity(any())).thenReturn(Optional.of(new SourceOfIncomeType()));
//        when(referralRepository.findByIdentity(any())).thenReturn(Optional.of(new ReferralSources()));
//        when(canvassedTypesRepository.findByIdentity(any())).thenReturn(Optional.of(new CanvassedTypes()));
//        when(educationLevelsRepository.findByIdentity(any())).thenReturn(Optional.of(new EducationLevels()));
//        when(purposeRepository.findByIdentity(any())).thenReturn(Optional.of(new Purpose()));
//        when(assetTypesRepository.findByIdentity(any())).thenReturn(Optional.of(new AssetTypes()));
//        when(nationalityRepository.findByIdentity(any())).thenReturn(Optional.of(new Nationality()));
//        when(languagesRepository.findByIdentity(any())).thenReturn(Optional.of(new Languages()));
//        when(customerAdditionalReferenceNameRepository.findByIdentity(any()))
//                .thenReturn(Optional.of(new CustomerAdditionalReferenceName()));
//        when(customerAdditionalInfoMapper.buildResponseDto(any(), any(), any(), any(), any(), any()))
//                .thenReturn(new CustomerAdditionalInfoResponseDto());
//
//        // EXECUTE
//        CustomerAdditionalInfoResponseDto response =
//                customerAdditionalInfoService.saveAdditionalInfo(customerId, requestDto);
//
//        // VERIFY
//        assertNotNull(response);
//
//        verify(customerRepository).findByIdentity(any(UUID.class));
//        verify(employmentRepository).save(any(CustomerEmployment.class));
//        verify(customerReferralRepository).save(any(CustomerReferral.class));
//        verify(profileExtraRepository).save(any(CustomerProfileExtra.class));
//        verify(assetRepository).save(any(CustomerAsset.class));
//        verify(customerRepository).save(any(Customer.class));
//        verify(customerAdditionalReferenceValueRepository).save(any(CustomerAdditionalReferenceValue.class));
//    }
//
//    @Test
//    void testSaveAdditionalInfo_customerNotFound() {
//        UUID customerId = UUID.randomUUID();
//        when(customerRepository.findByIdentity(any(UUID.class))).thenReturn(Optional.empty());
//
//        CustomerAdditionalInfoRequestDto requestDto = new CustomerAdditionalInfoRequestDto();
//
//        BusinessException exception = assertThrows(BusinessException.class, () ->
//                customerAdditionalInfoService.saveAdditionalInfo(customerId, requestDto));
//
//        assertEquals(CommonConstants.NOT_FOUND_MESSAGE, exception.getMessage());
//        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
//    }
//}
