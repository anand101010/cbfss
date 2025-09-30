//package com.incede.nbfc.core.monolith.customer.service;
//
//import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
//import com.incede.nbfc.core.monolith.customer.dto.BasicInformationRequestDto;
//import com.incede.nbfc.core.monolith.customer.dto.BasicInformationResponseDto;
//import com.incede.nbfc.core.monolith.customer.mapper.BasicInformationMapper;
//import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
//import com.incede.nbfc.core.monolith.exception.BusinessException;
//import com.incede.nbfc.core.monolith.exception.ErrorCodes;
//import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
//import com.incede.nbfc.core.monolith.masterdata.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.dao.DataIntegrityViolationException;
//
//import java.time.LocalDate;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//@RequiredArgsConstructor
//class BasicInformationServiceTest {
//
//    @Mock private CustomerRepository customerRepository;
//    @Mock private BasicInformationMapper customerMapper;
//    @Mock private GendersRepository gendersRepository;
//    @Mock private MaritalStatusRepository maritalStatusRepository;
//    @Mock private NationalityRepository nationalityRepository;
//    @Mock private TaxCategoryRepository taxCategoryRepository;
//    @Mock private OccupationRepository occupationRepository;
//    @Mock private LanguagesRepository languagesRepository;
//    @Mock private BranchesRepository branchesRepository;
//    @Mock private CustomerStatusRepository customerStatusRepository;
//    @Mock private ResidentialStatusesRepository residentialStatusesRepository;
//    @Mock private SalutationTypesRepository salutationRepository;
//
//    @InjectMocks
//    private BasicInformationService service;
//
//    private BasicInformationRequestDto requestDto;
//    private Customer customer;
//    private BasicInformationResponseDto responseDto;
//
//    private UUID genderId, maritalId, taxCatId, occId, branchId, salutationId, custStatusId;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        genderId = UUID.randomUUID();
//        maritalId = UUID.randomUUID();
//        taxCatId = UUID.randomUUID();
//        occId = UUID.randomUUID();
//        branchId = UUID.randomUUID();
//        salutationId = UUID.randomUUID();
//        custStatusId = UUID.randomUUID();
//
//        requestDto = new BasicInformationRequestDto();
//        requestDto.setAadharVault("vault123");
//        requestDto.setFirstName("John");
//        requestDto.setLastName("Doe");
//        requestDto.setAadharName("John Doe");
//        requestDto.setDob(LocalDate.of(1990, 1, 1));
//        requestDto.setMobileNumber("9999999999");
//        requestDto.setOtpVerified(true);
//        requestDto.setIsBusiness(true);
//        requestDto.setIsFirm(false);
//
//        requestDto.setGender(genderId);
//        requestDto.setMaritalStatus(maritalId);
//        requestDto.setTaxCategory(taxCatId);
//        requestDto.setOccupation(occId);
//        requestDto.setBranchId(branchId);
//        requestDto.setSalutation(salutationId);
//        requestDto.setCustomerStatus(custStatusId);
//
//        when(gendersRepository.findByIdentity(genderId)).thenReturn(Optional.of(new Genders()));
//        when(maritalStatusRepository.findByIdentity(maritalId)).thenReturn(Optional.of(new MaritalStatus()));
//        when(taxCategoryRepository.findByIdentity(taxCatId)).thenReturn(Optional.of(new TaxCategory()));
//        when(occupationRepository.findByIdentity(occId)).thenReturn(Optional.of(new Occupation()));
//        when(branchesRepository.findByIdentity(branchId)).thenReturn(Optional.of(new Branches()));
//        when(salutationRepository.findByIdentity(salutationId)).thenReturn(Optional.of(new SalutationTypes()));
//        when(customerStatusRepository.findByIdentity(custStatusId)).thenReturn(Optional.of(new CustomerStatus()));
//
//        customer = new Customer();
//        customer.setIdentity(UUID.randomUUID());
//        customer.setAadharVaultId("vault123");
//
//        responseDto = new BasicInformationResponseDto();
//    }
//
//
//    @Test
//    void saveBasicInformation_success() {
//        when(customerRepository.existsByTenantAndAadharVaultId(UUID.randomUUID(), "vault123")).thenReturn(false);
//        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
//        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
//        when(customerMapper.toResponseDto(customer)).thenReturn(responseDto);
//
//        BasicInformationResponseDto result = service.saveBasicInformation(requestDto);
//
//        assertNotNull(result);
//        verify(customerRepository).save(any(Customer.class));
//    }
//
//    @Test
//    void saveBasicInformation_duplicate() {
//        when(customerRepository.existsByTenantIdAndAadharVaultId(1, "vault123")).thenReturn(true);
//
//        BusinessException ex = assertThrows(BusinessException.class,
//                () -> service.saveBasicInformation(requestDto));
//
//        assertEquals(ErrorCodes.CONFLICT, ex.getErrorCode());
//    }
//
//    @Test
//    void saveBasicInformation_dataIntegrityViolation() {
//        when(customerRepository.existsByTenantIdAndAadharVaultId(1, "vault123")).thenReturn(false);
//        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
//        when(customerRepository.save(any())).thenThrow(new DataIntegrityViolationException("duplicate"));
//
//        BusinessException ex = assertThrows(BusinessException.class,
//                () -> service.saveBasicInformation(requestDto));
//
//        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, ex.getErrorCode());
//    }
//
//
//    @Test
//    void updateBasicInformation_notFound() {
//        UUID id = UUID.randomUUID();
//        when(customerRepository.findByIdentity(id)).thenReturn(Optional.empty());
//
//        BusinessException ex = assertThrows(BusinessException.class,
//                () -> service.updateBasicInformation(id, requestDto));
//
//        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
//    }
//
//    @Test
//    void updateBasicInformation_duplicate() {
//        UUID id = UUID.randomUUID();
//        customer.setIdentity(id);
//
//        Customer other = new Customer();
//        other.setIdentity(UUID.randomUUID());
//
//        when(customerRepository.findByIdentity(id)).thenReturn(Optional.of(customer));
//        when(customerRepository.findByTenantIdAndAadharVaultId(1, "vault123"))
//                .thenReturn(Optional.of(other));
//
//        BusinessException ex = assertThrows(BusinessException.class,
//                () -> service.updateBasicInformation(id, requestDto));
//
//        assertEquals(ErrorCodes.CONFLICT, ex.getErrorCode());
//    }
//
//    @Test
//    void updateBasicInformation_success() {
//        UUID id = UUID.randomUUID();
//        customer.setIdentity(id);
//
//        when(customerRepository.findByIdentity(id)).thenReturn(Optional.of(customer));
//        when(customerRepository.findByTenantIdAndAadharVaultId(1, "vault123"))
//                .thenReturn(Optional.of(customer));
//        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
//        when(customerMapper.toResponseDto(customer)).thenReturn(responseDto);
//
//        BasicInformationResponseDto result = service.updateBasicInformation(id, requestDto);
//
//        assertNotNull(result);
//        verify(customerRepository).save(customer);
//    }
//
//    @Test
//    void updateBasicInformation_dataIntegrityViolation() {
//        UUID id = UUID.randomUUID();
//        customer.setIdentity(id);
//
//        when(customerRepository.findByIdentity(id)).thenReturn(Optional.of(customer));
//        when(customerRepository.findByTenantIdAndAadharVaultId(1, "vault123"))
//                .thenReturn(Optional.of(customer));
//        when(customerRepository.save(any()))
//                .thenThrow(new DataIntegrityViolationException("duplicate"));
//
//        BusinessException ex = assertThrows(BusinessException.class,
//                () -> service.updateBasicInformation(id, requestDto));
//
//        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, ex.getErrorCode());
//    }
//
//
//
//    @Test
//    void getBasicInformationByUuid_success() {
//        UUID id = UUID.randomUUID();
//        when(customerRepository.findByIdentity(id)).thenReturn(Optional.of(customer));
//        when(customerMapper.toResponseDto(customer)).thenReturn(responseDto);
//
//        BasicInformationResponseDto result = service.getBasicInformationByUuid(id);
//
//        assertNotNull(result);
//    }
//
//    @Test
//    void getBasicInformationByUuid_notFound() {
//        UUID id = UUID.randomUUID();
//        when(customerRepository.findByIdentity(id)).thenReturn(Optional.empty());
//
//        BusinessException ex = assertThrows(BusinessException.class,
//                () -> service.getBasicInformationByUuid(id));
//
//        assertEquals(ErrorCodes.NOT_FOUND, ex.getErrorCode());
//    }
//}
//
