package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerKyc;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerKycUpload;
import com.incede.nbfc.core.monolith.customer.dto.CustomerKycRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerKycResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerKycMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerKycRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerKycUploadRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessConflictException;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Branches;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.KycTypes;
import com.incede.nbfc.core.monolith.masterdata.repository.BranchesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.KycTypesRepository;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import com.incede.nbfc.core.monolith.tenant.repository.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerKycServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerKycRepository customerKycRepository;

    @Mock
    private CustomerKycUploadRepository customerKycUploadRepository;

    @Mock
    private CustomerKycMapper customerKycMapper;

    @Mock
    private BranchesRepository branchesRepository;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private KycTypesRepository kycTypesRepository;

    @InjectMocks
    private CustomerKycService customerKycService;

    private UUID customerId;
    private UUID kycTypeId;
    private UUID branchId;
    private UUID tenantId;
    private CustomerKycRequestDto requestDto;
    private Customer customer;
    private CustomerKyc customerKyc;
    private Branches branch;
    private Tenant tenant;
    private KycTypes kycType;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        kycTypeId = UUID.randomUUID();
        branchId = UUID.randomUUID();
        tenantId = UUID.randomUUID();

        requestDto = CustomerKycRequestDto.builder()
                .idType(kycTypeId)
                .branchId(branchId)
                .idNumber("ABCDE1234F")
                .placeOfIssue("New Delhi")
                .issuingAuthority("UIDAI")
                .validFrom(LocalDate.of(2020, 1, 1))
                .validTo(LocalDate.of(2030, 1, 1))
                .isVerified(true)
                .isActive(true)
                .tenantId(tenantId)
                .branchCode("BR001")
                .documentRefId("DOC_REF_001")
                .filePath("/documents/kyc/")
                .fileName("aadhaar_front.jpg")
                .fileType("image/jpeg")
                .build();

        // Setup entities
        customer = new Customer();
        customer.setIdentity(customerId);
        customer.setCustomerCode("BR001-CUS-001");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setDob(LocalDate.of(1990, 1, 1));

        customerKyc = new CustomerKyc();
        customerKyc.setIdentity(UUID.randomUUID());
        customerKyc.setCustomer(customer);
        customerKyc.setIdNumber("ABCDE1234F");
        customerKyc.setIsVerified(true);
        customerKyc.setIsActive(true);

        branch = new Branches();
        branch.setIdentity(branchId);

        tenant = new Tenant();
        tenant.setIdentity(tenantId);

        kycType = new KycTypes();
        kycType.setIdentity(kycTypeId);
        kycType.setDisplayName("Aadhaar");
    }

    // TESTS THAT WORK WITHOUT FILE UPLOAD

    @Test
    void testGetKycDocuments_Success() {
        // Given
        List<CustomerKyc> kycList = Arrays.asList(customerKyc);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerKycRepository.findByCustomer(customer)).thenReturn(Optional.of(kycList));

        // When
        CustomerKycResponseDto result = customerKycService.getKycDocuments(customerId);

        // Then
        assertNotNull(result);
        assertEquals(customerId, result.getIdentity());
        assertEquals("BR001-CUS-001", result.getCustomerCode());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(customerRepository, times(1)).findByIdentity(customerId);
        verify(customerKycRepository, times(1)).findByCustomer(customer);
    }

    @Test
    void testGetKycDocuments_CustomerNotFound() {
        // Given
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerKycService.getKycDocuments(customerId));

        assertEquals("Customer not found with ID: " + customerId, exception.getMessage());
        verify(customerRepository, times(1)).findByIdentity(customerId);
        verify(customerKycRepository, never()).findByCustomer(any());
    }

    @Test
    void testGetKycDocuments_KycNotFound() {
        // Given
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerKycRepository.findByCustomer(customer)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerKycService.getKycDocuments(customerId));

        assertEquals("Failed to fetch KYC documents", exception.getMessage());
        assertEquals(ErrorCodes.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(customerKycRepository, times(1)).findByCustomer(customer);
    }

    @Test
    void testGetKycDocuments_WithNullRelations() {
        // Given
        customer.setGender(null);
        customer.setCustomerStatus(null);
        customer.setBranchId(null);

        customerKyc.setIdType(null);
        customerKyc.setValidFrom(null);
        customerKyc.setValidTo(null);

        List<CustomerKyc> kycList = Arrays.asList(customerKyc);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerKycRepository.findByCustomer(customer)).thenReturn(Optional.of(kycList));

        // When
        CustomerKycResponseDto result = customerKycService.getKycDocuments(customerId);

        // Then
        assertNotNull(result);
        assertNull(result.getGender());
        assertNull(result.getCustomerStatus());
        assertNull(result.getBranchId());

        assertFalse(result.getKycDocuments().isEmpty());
        assertNull(result.getKycDocuments().get(0).getIdType());
        assertNull(result.getKycDocuments().get(0).getValidFrom());
        assertNull(result.getKycDocuments().get(0).getValidTo());
    }

    @Test
    void testGetKycDocuments_MultipleKycDocuments() {
        // Given
        CustomerKyc kyc2 = new CustomerKyc();
        kyc2.setIdentity(UUID.randomUUID());
        kyc2.setCustomer(customer);
        kyc2.setIdNumber("PAN1234567");
        kyc2.setIsVerified(true);
        kyc2.setIsActive(true);

        List<CustomerKyc> kycList = Arrays.asList(customerKyc, kyc2);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerKycRepository.findByCustomer(customer)).thenReturn(Optional.of(kycList));

        // When
        CustomerKycResponseDto result = customerKycService.getKycDocuments(customerId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getKycDocuments().size());
        assertEquals("ABCDE1234F", result.getKycDocuments().get(0).getIdNumber());
        assertEquals("PAN1234567", result.getKycDocuments().get(1).getIdNumber());
    }

    @Test
    void testCreateInitialCustomer_DuplicateKyc() {
        // Given
        when(kycTypesRepository.findByIdentity(kycTypeId)).thenReturn(Optional.of(kycType));

        Customer existingCustomer = new Customer();
        existingCustomer.setIdentity(UUID.randomUUID());
        existingCustomer.setCustomerCode("EXISTING-CUST");

        CustomerKyc existingKyc = new CustomerKyc();
        existingKyc.setIdentity(UUID.randomUUID());
        existingKyc.setCustomer(existingCustomer);
        existingKyc.setIdType(kycType);
        existingKyc.setIdNumber("ABCDE1234F");

        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, "ABCDE1234F")).thenReturn(Optional.of(existingKyc));

        // Mock conflict handling
        when(customerKycRepository.findByCustomer(existingCustomer)).thenReturn(Optional.of(Arrays.asList(existingKyc)));
        when(customerKycUploadRepository.findByCustomer(existingCustomer)).thenReturn(Optional.of(Collections.emptyList()));

        CustomerKycResponseDto mockResponse = CustomerKycResponseDto.builder()
                .identity(existingCustomer.getIdentity())
                .customerCode("EXISTING-CUST")
                .build();
        when(customerKycMapper.toResponseDto(any(), any(), any())).thenReturn(mockResponse);

        // When & Then
        BusinessConflictException exception = assertThrows(BusinessConflictException.class,
                () -> customerKycService.createInitialCustomer(requestDto));

        assertTrue(exception.getMessage().contains("Customer already exists with this ID"));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void testAddKycDocument_DuplicateIdNumber() {
        // Given
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(kycTypesRepository.findByIdentity(kycTypeId)).thenReturn(Optional.of(kycType));

        Customer existingCustomer = new Customer();
        existingCustomer.setIdentity(UUID.randomUUID());

        CustomerKyc existingKyc = new CustomerKyc();
        existingKyc.setIdentity(UUID.randomUUID());
        existingKyc.setCustomer(existingCustomer);
        existingKyc.setIdType(kycType);
        existingKyc.setIdNumber("ABCDE1234F");

        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, "ABCDE1234F")).thenReturn(Optional.of(existingKyc));

        // Mock conflict handling
        when(customerKycRepository.findByCustomer(existingCustomer)).thenReturn(Optional.of(Arrays.asList(existingKyc)));
        when(customerKycUploadRepository.findByCustomer(existingCustomer)).thenReturn(Optional.of(Collections.emptyList()));

        CustomerKycResponseDto mockResponse = CustomerKycResponseDto.builder()
                .identity(existingCustomer.getIdentity())
                .customerCode("EXISTING-CUST")
                .build();
        when(customerKycMapper.toResponseDto(any(), any(), any())).thenReturn(mockResponse);

        // When & Then
        BusinessConflictException exception = assertThrows(BusinessConflictException.class,
                () -> customerKycService.addKycDocument(requestDto, customerId));

        assertTrue(exception.getMessage().contains("Customer already exists with this ID"));
        verify(customerKycRepository, never()).save(any());
    }

    @Test
    void testCreateInitialCustomer_InvalidBranch() {
        // Given
        when(kycTypesRepository.findByIdentity(kycTypeId)).thenReturn(Optional.of(kycType));
        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, "ABCDE1234F")).thenReturn(Optional.empty());
        when(customerKycMapper.generateCustomerCode("BR001", CommonConstants.CUSTOMER_TYPE)).thenReturn("BR001-CUS-001");
        when(customerKycMapper.toCustomerEntity(any(), any(), any())).thenReturn(customer);
        when(branchesRepository.findByIdentity(branchId)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerKycService.createInitialCustomer(requestDto));

        assertEquals(CommonConstants.INVALID_BRANCH, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testCreateInitialCustomer_InvalidTenant() {
        // Given
        when(kycTypesRepository.findByIdentity(kycTypeId)).thenReturn(Optional.of(kycType));
        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, "ABCDE1234F")).thenReturn(Optional.empty());
        when(customerKycMapper.generateCustomerCode("BR001", CommonConstants.CUSTOMER_TYPE)).thenReturn("BR001-CUS-001");
        when(customerKycMapper.toCustomerEntity(any(), any(), any())).thenReturn(customer);
        when(branchesRepository.findByIdentity(branchId)).thenReturn(Optional.of(branch));
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerKycService.createInitialCustomer(requestDto));

        assertEquals(CommonConstants.TENANT_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testCreateInitialCustomer_InvalidDocumentType() {
        // Given
        when(kycTypesRepository.findByIdentity(kycTypeId)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerKycService.createInitialCustomer(requestDto));

        assertEquals(CommonConstants.DOCUMENT_TYPE_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testAddKycDocument_CustomerNotFound() {
        // Given
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerKycService.addKycDocument(requestDto, customerId));

        assertEquals(CommonConstants.CUSTOMER_NOT_FOUND, exception.getMessage());
        verify(customerKycRepository, never()).save(any());
    }

    @Test
    void testAddKycDocument_DuplicateDocumentType() {
        // Given
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(kycTypesRepository.findByIdentity(kycTypeId)).thenReturn(Optional.of(kycType));
        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, "ABCDE1234F")).thenReturn(Optional.empty());
        when(customerKycRepository.existsByIdTypeAndCustomer(kycType, customer)).thenReturn(true);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerKycService.addKycDocument(requestDto, customerId));

        assertEquals(CommonConstants.DOCUMENT_ALREADY_EXISTS, exception.getMessage());
        assertEquals(ErrorCodes.CONFLICT, exception.getErrorCode());
        verify(customerKycRepository, never()).save(any());
    }

    @Test
    void testCreateInitialCustomer_NullRequest() {
        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerKycService.createInitialCustomer(null));

        assertEquals(CommonConstants.INVALID_REQUEST, exception.getMessage());
        assertEquals(ErrorCodes.VALIDATION_FAILED, exception.getErrorCode());
    }

    @Test
    void testAddKycDocument_NullRequest() {
        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerKycService.addKycDocument(null, customerId));

        assertEquals(CommonConstants.INVALID_REQUEST, exception.getMessage());
        assertEquals(ErrorCodes.VALIDATION_FAILED, exception.getErrorCode());
    }

    @Test
    void testCreateInitialCustomer_CustomerSaveFails() {
        // Given
        when(kycTypesRepository.findByIdentity(kycTypeId)).thenReturn(Optional.of(kycType));
        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, "ABCDE1234F")).thenReturn(Optional.empty());
        when(customerKycMapper.generateCustomerCode("BR001", CommonConstants.CUSTOMER_TYPE)).thenReturn("BR001-CUS-001");
        when(customerKycMapper.toCustomerEntity(any(), any(), any())).thenReturn(customer);
        when(branchesRepository.findByIdentity(branchId)).thenReturn(Optional.of(branch));
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));

        // Mock customer save failure with DataIntegrityViolationException
        when(customerRepository.save(any(Customer.class)))
                .thenThrow(new org.springframework.dao.DataIntegrityViolationException("Database connection failed"));

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerKycService.createInitialCustomer(requestDto));

        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, exception.getErrorCode());
    }

    @Test
    void testCreateInitialCustomer_KycSaveFails() {
        // Given
        when(kycTypesRepository.findByIdentity(kycTypeId)).thenReturn(Optional.of(kycType));
        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, "ABCDE1234F")).thenReturn(Optional.empty());
        when(customerKycMapper.generateCustomerCode("BR001", CommonConstants.CUSTOMER_TYPE)).thenReturn("BR001-CUS-001");

        Customer newCustomer = new Customer();
        newCustomer.setIdentity(customerId);

        when(customerKycMapper.toCustomerEntity(any(), any(), any())).thenReturn(newCustomer);
        when(branchesRepository.findByIdentity(branchId)).thenReturn(Optional.of(branch));
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));
        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);

        when(customerKycMapper.toKycEntity(any(), any())).thenReturn(customerKyc);

        // Mock KYC save failure with DataIntegrityViolationException
        when(customerKycRepository.save(any(CustomerKyc.class)))
                .thenThrow(new org.springframework.dao.DataIntegrityViolationException("KYC save failed"));

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerKycService.createInitialCustomer(requestDto));

        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, exception.getErrorCode());
    }
}