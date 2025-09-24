//package com.incede.nbfc.core.monolith.customer.service;
//
//import com.incede.nbfc.core.monolith.customer.domain.entity.*;
//import com.incede.nbfc.core.monolith.customer.dto.*;
//import com.incede.nbfc.core.monolith.customer.mapper.CustomerAdditionalInfoMapper;
//import com.incede.nbfc.core.monolith.customer.repository.*;
//import com.incede.nbfc.core.monolith.exception.BusinessException;
//import com.incede.nbfc.core.monolith.exception.ErrorCodes;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import org.springframework.dao.DataIntegrityViolationException;
//
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class CustomerAdditionalInfoServiceTest {
//
//    private CustomerAdditionalInfoMapper mapper;
//    private CustomerRepository customerRepository;
//    private CustomerEmploymentRepository employmentRepository;
//    private CustomerReferralRepository referralRepository;
//    private CustomerPepRepository pepRepository;
//    private CustomerProfileExtraRepository profileExtraRepository;
//    private CustomerAssetRepository assetRepository;
//
//    private CustomerAdditionalInfoService service;
//
//    private UUID customerId;
//    private Customer customer;
//
//    @BeforeEach
//    void setUp() {
//        mapper = mock(CustomerAdditionalInfoMapper.class);
//        customerRepository = mock(CustomerRepository.class);
//        employmentRepository = mock(CustomerEmploymentRepository.class);
//        referralRepository = mock(CustomerReferralRepository.class);
//        pepRepository = mock(CustomerPepRepository.class);
//        profileExtraRepository = mock(CustomerProfileExtraRepository.class);
//        assetRepository = mock(CustomerAssetRepository.class);
//
//        service = new CustomerAdditionalInfoService(
//                mapper,
//                customerRepository,
//                employmentRepository,
//                referralRepository,
//                pepRepository,
//                profileExtraRepository,
//                assetRepository
//        );
//
//        customerId = UUID.randomUUID();
//        customer = new Customer();
//        customer.setIdentity(customerId);
//    }
//
//    private CustomerAdditionalInfoRequestDto buildValidRequestDto() {
//        CustomerAdditionalInfoRequestDto dto = new CustomerAdditionalInfoRequestDto();
//        AdditionalInfoDto additional = new AdditionalInfoDto();
//        additional.setEmployment(new CustomerEmploymentDto());
//        additional.setReferrals(new CustomerReferralDto());
//        additional.setPep(new CustomerPepDto());
//        additional.setProfileExtra(new CustomerProfileExtraDto());
//        additional.setCustomerAsset(new CustomerAssetDto());
//        additional.setCustomer(new AdditionalInfoCustomerDto());
//        dto.setAdditional(additional);
//        return dto;
//    }
//
//
//    @Test
//    void saveAdditionalInfo_success() {
//        CustomerAdditionalInfoRequestDto dto = buildValidRequestDto();
//
//        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
//        when(employmentRepository.findByCustomer(customer)).thenReturn(Optional.empty());
//        when(referralRepository.findByCustomer(customer)).thenReturn(Optional.empty());
//        when(pepRepository.findByCustomer(customer)).thenReturn(Optional.empty());
//        when(profileExtraRepository.findByCustomer(customer)).thenReturn(Optional.empty());
//        when(assetRepository.findByCustomer(customer)).thenReturn(Optional.empty());
//
//        when(mapper.buildResponseDto(any(), any(), any(), any(), any(), any()))
//                .thenReturn(new CustomerAdditionalInfoResponseDto());
//
//        CustomerAdditionalInfoResponseDto response = service.saveAdditionalInfo(customerId, dto);
//
//        assertNotNull(response);
//        verify(employmentRepository).save(any());
//        verify(referralRepository).save(any());
//        verify(pepRepository).save(any());
//        verify(profileExtraRepository).save(any());
//        verify(assetRepository).save(any());
//        verify(customerRepository).save(any());
//    }
//
//    @Test
//    void saveAdditionalInfo_customerNotFound_shouldThrow() {
//        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());
//
//        BusinessException ex = assertThrows(BusinessException.class,
//                () -> service.saveAdditionalInfo(customerId, buildValidRequestDto()));
//
//        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
//        verifyNoInteractions(employmentRepository);
//    }
//
//    @Test
//    void saveAdditionalInfo_constraintViolation_shouldThrow() {
//        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
//        doThrow(DataIntegrityViolationException.class).when(employmentRepository).save(any());
//
//        BusinessException ex = assertThrows(BusinessException.class,
//                () -> service.saveAdditionalInfo(customerId, buildValidRequestDto()));
//
//        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, ex.getErrorCode());
//    }
//
//    @Test
//    void saveAdditionalInfo_validationFailure_shouldThrow() {
//        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
//        doThrow(IllegalArgumentException.class)
//                .when(mapper).createEmployment(any(), any(), any());
//
//        BusinessException ex = assertThrows(BusinessException.class,
//                () -> service.saveAdditionalInfo(customerId, buildValidRequestDto()));
//
//        assertEquals(ErrorCodes.VALIDATION_FAILED, ex.getErrorCode());
//    }
//
//
//    @Test
//    void updateAdditionalInfo_success() {
//        CustomerAdditionalInfoRequestDto dto = buildValidRequestDto();
//        CustomerEmployment employment = new CustomerEmployment();
//        CustomerReferral referral = new CustomerReferral();
//        CustomerPep pep = new CustomerPep();
//        CustomerProfileExtra profileExtra = new CustomerProfileExtra();
//        CustomerAsset asset = new CustomerAsset();
//
//        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
//        when(employmentRepository.findByCustomer(customer)).thenReturn(Optional.of(employment));
//        when(referralRepository.findByCustomer(customer)).thenReturn(Optional.of(referral));
//        when(pepRepository.findByCustomer(customer)).thenReturn(Optional.of(pep));
//        when(profileExtraRepository.findByCustomer(customer)).thenReturn(Optional.of(profileExtra));
//        when(assetRepository.findByCustomer(customer)).thenReturn(Optional.of(asset));
//        when(mapper.buildResponseDto(any(), any(), any(), any(), any(), any()))
//                .thenReturn(new CustomerAdditionalInfoResponseDto());
//
//        CustomerAdditionalInfoResponseDto response = service.updateAdditionalInfo(customerId, dto);
//
//        assertNotNull(response);
//        verify(employmentRepository).save(employment);
//        verify(referralRepository).save(referral);
//        verify(pepRepository).save(pep);
//        verify(profileExtraRepository).save(profileExtra);
//        verify(assetRepository).save(asset);
//        verify(customerRepository).save(any());
//    }
//
//    @Test
//    void updateAdditionalInfo_customerNotFound_shouldThrow() {
//        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());
//
//        BusinessException ex = assertThrows(BusinessException.class,
//                () -> service.updateAdditionalInfo(customerId, buildValidRequestDto()));
//
//        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
//    }
//
//    @Test
//    void updateAdditionalInfo_subEntityNotFound_shouldThrow() {
//        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
//        when(employmentRepository.findByCustomer(customer)).thenReturn(Optional.empty());
//
//        BusinessException ex = assertThrows(BusinessException.class,
//                () -> service.updateAdditionalInfo(customerId, buildValidRequestDto()));
//
//        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
//    }
//
//    @Test
//    void updateAdditionalInfo_constraintViolation_shouldThrow() {
//        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
//        when(employmentRepository.findByCustomer(customer)).thenReturn(Optional.of(new CustomerEmployment()));
//        doThrow(DataIntegrityViolationException.class).when(employmentRepository).save(any());
//
//        BusinessException ex = assertThrows(BusinessException.class,
//                () -> service.updateAdditionalInfo(customerId, buildValidRequestDto()));
//
//        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, ex.getErrorCode());
//    }
//
//    @Test
//    void updateAdditionalInfo_validationFailure_shouldThrow() {
//        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
//        when(employmentRepository.findByCustomer(customer)).thenReturn(Optional.of(new CustomerEmployment()));
//        doThrow(IllegalArgumentException.class).when(mapper).updateEmployment(any(), any(), any());
//
//        BusinessException ex = assertThrows(BusinessException.class,
//                () -> service.updateAdditionalInfo(customerId, buildValidRequestDto()));
//
//        assertEquals(ErrorCodes.VALIDATION_FAILED, ex.getErrorCode());
//    }
//
//
//    @Test
//    void getAdditionalInfo_success() {
//        CustomerEmployment employment = new CustomerEmployment();
//        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
//        when(employmentRepository.findByCustomer(customer)).thenReturn(Optional.of(employment));
//        when(mapper.buildResponseDto(any(), any(), any(), any(), any(), any()))
//                .thenReturn(new CustomerAdditionalInfoResponseDto());
//
//        CustomerAdditionalInfoResponseDto response = service.getAdditionalInfo(customerId);
//
//        assertNotNull(response);
//        verify(mapper).buildResponseDto(any(), any(), any(), any(), any(), any());
//    }
//
//    @Test
//    void getAdditionalInfo_customerNotFound_shouldThrow() {
//        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());
//
//        BusinessException ex = assertThrows(BusinessException.class,
//                () -> service.getAdditionalInfo(customerId));
//
//        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
//    }
//}
