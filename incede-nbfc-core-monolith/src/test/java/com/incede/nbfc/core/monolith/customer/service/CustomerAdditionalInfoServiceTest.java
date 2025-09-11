package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.customer.domain.entity.*;
import com.incede.nbfc.core.monolith.customer.dto.*;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerAdditionalInfoMapper;
import com.incede.nbfc.core.monolith.customer.repository.*;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerAdditionalInfoServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerEmploymentRepository employmentRepository;

    @Mock
    private CustomerReferralRepository referralRepository;

    @Mock
    private CustomerPepRepository pepRepository;

    @Mock
    private CustomerProfileExtraRepository profileExtraRepository;

    @Mock
    private CustomerAssetRepository assetRepository;

    @Mock
    private CustomerAdditionalInfoMapper mapper;

    @InjectMocks
    private CustomerAdditionalInfoService service;

    private UUID customerId;
    private Customer customer;
    private CustomerAdditionalInfoRequestDto requestDto;
    private AdditionalInfoDto additionalDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerId = UUID.randomUUID();
        customer = new Customer();
        customer.setIdentity(customerId);
        customer.setCustomerCode("CUST001");
        customer.setOnboardingStatus("ACTIVE");


        CustomerEmploymentDto employmentDto = CustomerEmploymentDto.builder()
                .occupationId(1)
                .designationId(1)
                .employer("Test Employer")
                .incomeSourceId(1)
                .monthlySalary(BigDecimal.valueOf(5000))
                .annualIncome(BigDecimal.valueOf(60000))
                .createdBy(1)
                .updatedBy(1)
                .build();

        CustomerReferralDto referralDto = CustomerReferralDto.builder()
                .referralSourceId(1)
                .canvassedTypeId(1)
                .canvasserStaffId(1)
                .createdBy(1)
                .updatedBy(1)
                .build();

        CustomerPepDto pepDto = CustomerPepDto.builder()
                .status("active")
                .categoryId(1)
                .relationshipId(1)
                .verificationSourceId(1)
                .createdBy(1)
                .updatedBy(1)
                .build();

        CustomerProfileExtraDto profileExtraDto = CustomerProfileExtraDto.builder()
                .educationLevelId(1)
                .purposeId(1)
                .createdBy(1)
                .updatedBy(1)
                .build();

        CustomerAssetDto assetDto = CustomerAssetDto.builder()
                .assetId(1)
                .assetTypeId(1)
                .description("House")
                .approxValue(BigDecimal.valueOf(100000))
                .ownsAsset(true)
                .homeLoanCompany("Bank")
                .hasHomeLoan(true)
                .createdBy(1)
                .updatedBy(1)
                .build();

        AdditionalInfoCustomerDto customerInfoDto = AdditionalInfoCustomerDto.builder()
                .nationality(1)
                .preferredLanguageId(1)
                .residentialStatusId(1)
                .build();

        additionalDto = AdditionalInfoDto.builder()
                .employment(employmentDto)
                .referrals(referralDto)
                .pep(pepDto)
                .profileExtra(profileExtraDto)
                .customerAsset(assetDto)
                .customer(customerInfoDto)
                .build();

        requestDto = CustomerAdditionalInfoRequestDto.builder()
                .additional(additionalDto)
                .build();
    }

    @Test
    void saveAdditionalInfo_shouldSaveAndReturnResponse() {

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        CustomerEmployment employment = new CustomerEmployment();
        CustomerReferral referral = new CustomerReferral();
        CustomerPep pep = new CustomerPep();
        CustomerProfileExtra profileExtra = new CustomerProfileExtra();
        CustomerAsset asset = new CustomerAsset();

        when(employmentRepository.findByCustomer(customer)).thenReturn(Optional.of(employment));
        when(referralRepository.findByCustomer(customer)).thenReturn(Optional.of(referral));
        when(pepRepository.findByCustomer(customer)).thenReturn(Optional.of(pep));
        when(profileExtraRepository.findByCustomer(customer)).thenReturn(Optional.of(profileExtra));
        when(mapper.mapToAsset(additionalDto.getCustomerAsset(), customer)).thenReturn(asset);
        when(mapper.updateCustomerFromAdditionalInfo(customer, additionalDto.getCustomer())).thenReturn(customer);
        when(mapper.buildResponseDto(customer, employment, referral, pep, profileExtra, asset))
                .thenReturn(new CustomerAdditionalInfoResponseDto());


        CustomerAdditionalInfoResponseDto response = service.saveAdditionalInfo(customerId, requestDto);


        verify(employmentRepository).save(employment);
        verify(referralRepository).save(referral);
        verify(pepRepository).save(pep);
        verify(profileExtraRepository).save(profileExtra);
        verify(assetRepository).save(asset);
        verify(customerRepository).save(customer);

        assertNotNull(response);
    }

    @Test
    void saveAdditionalInfo_shouldThrowException_whenCustomerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () ->
                service.saveAdditionalInfo(customerId, requestDto));

        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void getAdditionalInfo_shouldReturnResponse() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        CustomerEmployment employment = new CustomerEmployment();
        CustomerReferral referral = new CustomerReferral();
        CustomerPep pep = new CustomerPep();
        CustomerProfileExtra profileExtra = new CustomerProfileExtra();
        CustomerAsset asset = new CustomerAsset();

        when(employmentRepository.findByCustomer(customer)).thenReturn(Optional.of(employment));
        when(referralRepository.findByCustomer(customer)).thenReturn(Optional.of(referral));
        when(pepRepository.findByCustomer(customer)).thenReturn(Optional.of(pep));
        when(profileExtraRepository.findByCustomer(customer)).thenReturn(Optional.of(profileExtra));
        when(assetRepository.findByCustomer(customer)).thenReturn(asset);
        when(mapper.buildResponseDto(customer, employment, referral, pep, profileExtra, asset))
                .thenReturn(new CustomerAdditionalInfoResponseDto());

        CustomerAdditionalInfoResponseDto response = service.getAdditionalInfo(customerId);

        assertNotNull(response);
        verify(mapper).buildResponseDto(customer, employment, referral, pep, profileExtra, asset);
    }
}
