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
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerKycServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerKycRepository customerKycRepository;

    @Mock
    private CustomerKycMapper customerKycMapper;

    @Mock
    private BranchesRepository branchesRepository;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private CustomerKycUploadRepository customerKycUploadRepository;

    @Mock
    private KycTypesRepository kycTypesRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private CustomerKycService customerKycService;

    private CustomerKycRequestDto validRequest;
    private Customer customer;
    private CustomerKyc customerKyc;
    private CustomerKycUpload customerKycUpload;
    private KycTypes kycType;
    private Branches branch;
    private Tenant tenant;
    private UUID customerId;
    private UUID kycTypeId;
    private UUID branchId;
    private UUID tenantId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        kycTypeId = UUID.randomUUID();
        branchId = UUID.randomUUID();
        tenantId = UUID.randomUUID();

        validRequest = CustomerKycRequestDto.builder()
                .idType(kycTypeId)
                .idNumber("A123456789")
                .placeOfIssue("New York")
                .issuingAuthority("US Government")
                .validFrom(LocalDate.now().minusYears(1))
                .validTo(LocalDate.now().plusYears(5))
                .branchId(branchId)
                .tenantId(tenantId)
                .branchCode("NYC")
                .fileName("document.pdf")
                .fileType("PDF")
                .filePath("/uploads/document.pdf")
                .documentRefId("DOC123")
                .isVerified(true)
                .isActive(true)
                .build();

        kycType = new KycTypes();
        kycType.setIdentity(kycTypeId);
        kycType.setDisplayName("Passport");

        branch = new Branches();
        branch.setIdentity(branchId);

        tenant = new Tenant();
        tenant.setIdentity(tenantId);

        customer = new Customer();
        customer.setIdentity(customerId);
        customer.setCustomerCode("NYC-CUST-00001");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setDob(LocalDate.of(1990, 1, 1));
        customer.setBranchId(branch);
        customer.setTenant(tenant);

        customerKyc = new CustomerKyc();
        customerKyc.setIdentity(UUID.randomUUID());
        customerKyc.setIdType(kycType);
        customerKyc.setIdNumber("A123456789");
        customerKyc.setCustomer(customer);
        customerKyc.setPlaceOfIssue("New York");
        customerKyc.setValidFrom(LocalDate.now().minusYears(1));
        customerKyc.setValidTo(LocalDate.now().plusYears(5));
        customerKyc.setIsVerified(true);
        customerKyc.setIsActive(true);

        customerKycUpload = new CustomerKycUpload();
        customerKycUpload.setIdentity(UUID.randomUUID());
        customerKycUpload.setCustomer(customer);
        customerKycUpload.setKyc(customerKyc);
        customerKycUpload.setFileName("document.pdf");
        customerKycUpload.setFileType("PDF");
        customerKycUpload.setFilePath("/uploads/document.pdf");
        customerKycUpload.setDocumentReference("DOC123");
    }

    @Test
    void createInitialCustomer_Success() {
        // Arrange
        when(validator.validate(any(CustomerKycRequestDto.class))).thenReturn(Collections.emptySet());
        when(kycTypesRepository.findByIdentity(kycTypeId)).thenReturn(Optional.of(kycType));
        when(customerKycRepository.findByIdTypeAndIdNumber(any(KycTypes.class), anyString())).thenReturn(Optional.empty());
        when(customerKycMapper.generateCustomerCode(anyString(), anyString())).thenReturn("NYC-CUST-00001");
        when(customerKycMapper.toCustomerEntity(any(CustomerKycRequestDto.class), anyString(), any(UUID.class))).thenReturn(customer);
        when(branchesRepository.findByIdentity(branchId)).thenReturn(Optional.of(branch));
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerKycMapper.toKycEntity(any(CustomerKycRequestDto.class), any(Customer.class))).thenReturn(customerKyc);
        when(customerKycRepository.save(any(CustomerKyc.class))).thenReturn(customerKyc);
        when(customerKycMapper.toKycUploadEntity(any(CustomerKyc.class), any(CustomerKycRequestDto.class))).thenReturn(customerKycUpload);
        when(customerKycUploadRepository.save(any(CustomerKycUpload.class))).thenReturn(customerKycUpload);

        List<CustomerKyc> kycList = Arrays.asList(customerKyc);
        List<CustomerKycUpload> uploadList = Arrays.asList(customerKycUpload);
        when(customerKycRepository.findByCustomer(customer)).thenReturn(Optional.of(kycList));
        when(customerKycUploadRepository.findByCustomer(customer)).thenReturn(Optional.of(uploadList));

        CustomerKycResponseDto expectedResponse = CustomerKycResponseDto.builder().build();
        when(customerKycMapper.toResponseDto(customer, kycList, uploadList)).thenReturn(expectedResponse);

        // Act
        CustomerKycResponseDto result = customerKycService.createInitialCustomer(validRequest);

        // Assert
        assertNotNull(result);
        verify(customerRepository).save(any(Customer.class));
        verify(customerKycRepository).save(any(CustomerKyc.class));
        verify(customerKycUploadRepository).save(any(CustomerKycUpload.class));
    }





    @Test
    void createInitialCustomer_KycTypeNotFound_ThrowsException() {
        // Arrange
        when(validator.validate(any(CustomerKycRequestDto.class))).thenReturn(Collections.emptySet());
        when(kycTypesRepository.findByIdentity(kycTypeId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerKycService.createInitialCustomer(validRequest));
        assertEquals(CommonConstants.DOCUMENT_TYPE_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void createInitialCustomer_DuplicateKyc_ThrowsConflictException() {
        // Arrange
        when(validator.validate(any(CustomerKycRequestDto.class))).thenReturn(Collections.emptySet());
        when(kycTypesRepository.findByIdentity(kycTypeId)).thenReturn(Optional.of(kycType));
        when(customerKycRepository.findByIdTypeAndIdNumber(any(KycTypes.class), anyString())).thenReturn(Optional.of(customerKyc));

        List<CustomerKyc> kycList = Arrays.asList(customerKyc);
        List<CustomerKycUpload> uploadList = Arrays.asList(customerKycUpload);
        when(customerKycRepository.findByCustomer(customer)).thenReturn(Optional.of(kycList));
        when(customerKycUploadRepository.findByCustomer(customer)).thenReturn(Optional.of(uploadList));

        CustomerKycResponseDto mockResponse = CustomerKycResponseDto.builder().build();
        when(customerKycMapper.toResponseDto(customer, kycList, uploadList)).thenReturn(mockResponse);

        // Act & Assert
        BusinessConflictException exception = assertThrows(BusinessConflictException.class,
                () -> customerKycService.createInitialCustomer(validRequest));
        assertTrue(exception.getMessage().contains("Customer already exists"));
    }

    @Test
    void createInitialCustomer_BranchNotFound_ThrowsException() {
        // Arrange
        when(validator.validate(any(CustomerKycRequestDto.class))).thenReturn(Collections.emptySet());
        when(kycTypesRepository.findByIdentity(kycTypeId)).thenReturn(Optional.of(kycType));
        when(customerKycRepository.findByIdTypeAndIdNumber(any(KycTypes.class), anyString())).thenReturn(Optional.empty());
        when(customerKycMapper.generateCustomerCode(anyString(), anyString())).thenReturn("NYC-CUST-00001");
        when(customerKycMapper.toCustomerEntity(any(CustomerKycRequestDto.class), anyString(), any(UUID.class))).thenReturn(customer);
        when(branchesRepository.findByIdentity(branchId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerKycService.createInitialCustomer(validRequest));
        assertEquals(CommonConstants.INVALID_BRANCH, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void createInitialCustomer_TenantNotFound_ThrowsException() {
        // Arrange
        when(validator.validate(any(CustomerKycRequestDto.class))).thenReturn(Collections.emptySet());
        when(kycTypesRepository.findByIdentity(kycTypeId)).thenReturn(Optional.of(kycType));
        when(customerKycRepository.findByIdTypeAndIdNumber(any(KycTypes.class), anyString())).thenReturn(Optional.empty());
        when(customerKycMapper.generateCustomerCode(anyString(), anyString())).thenReturn("NYC-CUST-00001");
        when(customerKycMapper.toCustomerEntity(any(CustomerKycRequestDto.class), anyString(), any(UUID.class))).thenReturn(customer);
        when(branchesRepository.findByIdentity(branchId)).thenReturn(Optional.of(branch));
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerKycService.createInitialCustomer(validRequest));
        assertEquals(CommonConstants.TENANT_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void createInitialCustomer_DataIntegrityViolation_ThrowsException() {
        // Arrange
        when(validator.validate(any(CustomerKycRequestDto.class))).thenReturn(Collections.emptySet());
        when(kycTypesRepository.findByIdentity(kycTypeId)).thenReturn(Optional.of(kycType));
        when(customerKycRepository.findByIdTypeAndIdNumber(any(KycTypes.class), anyString())).thenReturn(Optional.empty());
        when(customerKycMapper.generateCustomerCode(anyString(), anyString())).thenReturn("NYC-CUST-00001");
        when(customerKycMapper.toCustomerEntity(any(CustomerKycRequestDto.class), anyString(), any(UUID.class))).thenReturn(customer);
        when(branchesRepository.findByIdentity(branchId)).thenReturn(Optional.of(branch));
        when(tenantRepository.findByIdentity(tenantId)).thenReturn(Optional.of(tenant));
        when(customerRepository.save(any(Customer.class))).thenThrow(new DataIntegrityViolationException("Constraint violation"));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerKycService.createInitialCustomer(validRequest));
        assertEquals(CommonConstants.CONSTRAIN_VIOLATION, exception.getMessage());
        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, exception.getErrorCode());
    }

    @Test
    void addKycDocument_Success() {
        // Arrange
        when(validator.validate(any(CustomerKycRequestDto.class))).thenReturn(Collections.emptySet());
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(kycTypesRepository.findByIdentity(kycTypeId)).thenReturn(Optional.of(kycType));
        when(customerKycRepository.findByIdTypeAndIdNumber(any(KycTypes.class), anyString())).thenReturn(Optional.empty());
        when(customerKycRepository.existsByIdTypeAndCustomer(kycType, customer)).thenReturn(false);
        when(customerKycMapper.toKycEntity(any(CustomerKycRequestDto.class), any(Customer.class))).thenReturn(customerKyc);
        when(customerKycRepository.save(any(CustomerKyc.class))).thenReturn(customerKyc);
        when(customerKycMapper.toKycUploadEntity(any(CustomerKyc.class), any(CustomerKycRequestDto.class))).thenReturn(customerKycUpload);
        when(customerKycUploadRepository.save(any(CustomerKycUpload.class))).thenReturn(customerKycUpload);

        List<CustomerKyc> kycList = Arrays.asList(customerKyc);
        List<CustomerKycUpload> uploadList = Arrays.asList(customerKycUpload);
        when(customerKycRepository.findByCustomer(customer)).thenReturn(Optional.of(kycList));
        when(customerKycUploadRepository.findByCustomer(customer)).thenReturn(Optional.of(uploadList));

        CustomerKycResponseDto expectedResponse = CustomerKycResponseDto.builder().build();
        when(customerKycMapper.toResponseDto(customer, kycList, uploadList)).thenReturn(expectedResponse);

        // Act
        CustomerKycResponseDto result = customerKycService.addKycDocument(validRequest, customerId);

        // Assert
        assertNotNull(result);
        verify(customerKycRepository).save(any(CustomerKyc.class));
        verify(customerKycUploadRepository).save(any(CustomerKycUpload.class));
    }

    @Test
    void addKycDocument_CustomerNotFound_ThrowsException() {
        // Arrange
        when(validator.validate(any(CustomerKycRequestDto.class))).thenReturn(Collections.emptySet());
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerKycService.addKycDocument(validRequest, customerId));
        assertEquals(CommonConstants.CUSTOMER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void addKycDocument_DocumentAlreadyExists_ThrowsException() {
        // Arrange
        when(validator.validate(any(CustomerKycRequestDto.class))).thenReturn(Collections.emptySet());
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(kycTypesRepository.findByIdentity(kycTypeId)).thenReturn(Optional.of(kycType));
        when(customerKycRepository.findByIdTypeAndIdNumber(any(KycTypes.class), anyString())).thenReturn(Optional.empty());
        when(customerKycRepository.existsByIdTypeAndCustomer(kycType, customer)).thenReturn(true);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerKycService.addKycDocument(validRequest, customerId));
        assertEquals(CommonConstants.DOCUMENT_ALREADY_EXISTS, exception.getMessage());
        assertEquals(ErrorCodes.CONFLICT, exception.getErrorCode());
    }

    @Test
    void addKycDocument_DuplicateKycDifferentCustomer_ThrowsConflictException() {
        // Arrange
        Customer differentCustomer = new Customer();
        differentCustomer.setIdentity(UUID.randomUUID());

        CustomerKyc existingKyc = new CustomerKyc();
        existingKyc.setCustomer(differentCustomer);
        existingKyc.setIdType(kycType);

        when(validator.validate(any(CustomerKycRequestDto.class))).thenReturn(Collections.emptySet());
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(kycTypesRepository.findByIdentity(kycTypeId)).thenReturn(Optional.of(kycType));
        when(customerKycRepository.findByIdTypeAndIdNumber(any(KycTypes.class), anyString())).thenReturn(Optional.of(existingKyc));

        List<CustomerKyc> kycList = Arrays.asList(existingKyc);
        List<CustomerKycUpload> uploadList = Arrays.asList(customerKycUpload);
        when(customerKycRepository.findByCustomer(differentCustomer)).thenReturn(Optional.of(kycList));
        when(customerKycUploadRepository.findByCustomer(differentCustomer)).thenReturn(Optional.of(uploadList));

        CustomerKycResponseDto mockResponse = CustomerKycResponseDto.builder().build();
        when(customerKycMapper.toResponseDto(differentCustomer, kycList, uploadList)).thenReturn(mockResponse);

        // Act & Assert
        BusinessConflictException exception = assertThrows(BusinessConflictException.class,
                () -> customerKycService.addKycDocument(validRequest, customerId));
        assertTrue(exception.getMessage().contains("Customer already exists"));
    }

    @Test
    void getKycDocuments_Success() {
        // Arrange
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        List<CustomerKyc> kycList = Arrays.asList(customerKyc);
        List<CustomerKycUpload> uploadList = Arrays.asList(customerKycUpload);
        when(customerKycRepository.findByCustomer(customer)).thenReturn(Optional.of(kycList));
        when(customerKycUploadRepository.findByCustomer(customer)).thenReturn(Optional.of(uploadList));

        CustomerKycResponseDto expectedResponse = CustomerKycResponseDto.builder().build();
        when(customerKycMapper.toResponseDto(customer, kycList, uploadList)).thenReturn(expectedResponse);

        // Act
        CustomerKycResponseDto result = customerKycService.getKycDocuments(customerId);

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByIdentity(customerId);
        verify(customerKycRepository).findByCustomer(customer);
    }

    @Test
    void getKycDocuments_CustomerNotFound_ThrowsException() {
        // Arrange
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerKycService.getKycDocuments(customerId));
        assertEquals(CommonConstants.CUSTOMER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void getKycDocuments_KycNotFound_ThrowsException() {
        // Arrange
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerKycRepository.findByCustomer(customer)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerKycService.getKycDocuments(customerId));
        assertEquals(CommonConstants.KYC_NOT_FOUND_FOR_CUSTOMER, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void getKycDocuments_UploadsNotFound_ThrowsException() {
        // Arrange
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        List<CustomerKyc> kycList = Arrays.asList(customerKyc);
        when(customerKycRepository.findByCustomer(customer)).thenReturn(Optional.of(kycList));
        when(customerKycUploadRepository.findByCustomer(customer)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerKycService.getKycDocuments(customerId));
        assertEquals(CommonConstants.KYC_UPLOAD_NOT_FOUND_FOR_CUSTOMER, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void maskIdNumber_NullInput_ReturnsNull() throws Exception {
        // Use reflection to test private method
        var method = CustomerKycService.class.getDeclaredMethod("maskIdNumber", String.class);
        method.setAccessible(true);

        // Act
        String result = (String) method.invoke(customerKycService, (String) null);

        // Assert
        assertNull(result);
    }

    @Test
    void maskIdNumber_ShortInput_ReturnsOriginal() throws Exception {
        // Use reflection to test private method
        var method = CustomerKycService.class.getDeclaredMethod("maskIdNumber", String.class);
        method.setAccessible(true);

        // Act
        String result = (String) method.invoke(customerKycService, "123");

        // Assert
        assertEquals("123", result);
    }

    @Test
    void maskIdNumber_ValidInput_ReturnsMasked() throws Exception {
        // Use reflection to test private method
        var method = CustomerKycService.class.getDeclaredMethod("maskIdNumber", String.class);
        method.setAccessible(true);

        // Act
        String result = (String) method.invoke(customerKycService, "A123456789");

        // Assert
        assertEquals("XXXX XXXX 6789", result);
    }
}