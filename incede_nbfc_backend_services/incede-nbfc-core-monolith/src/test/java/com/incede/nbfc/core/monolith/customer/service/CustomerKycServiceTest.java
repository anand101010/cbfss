package com.incede.nbfc.core.monolith.customer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import jakarta.validation.Validator;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerKycServiceTest {

    @Mock private CustomerRepository customerRepository;
    @Mock private CustomerKycRepository customerKycRepository;
    @Mock private CustomerKycUploadRepository customerKycUploadRepository;
    @Mock private CustomerKycMapper customerKycMapper;
    @Mock private ObjectMapper objectMapper;
    @Mock private KycTypesRepository kycTypesRepository; // Updated from kycTypes to kycTypesRepository
    @Mock private BranchesRepository branchesRepository;
    @Mock private TenantRepository tenantRepository;
    @Mock private MultipartFile file;

    @Spy
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @InjectMocks private CustomerKycService service;

    private CustomerKycRequestDto request;
    private Customer customer;
    private CustomerKyc customerKyc;
    private KycTypes kycType; // Updated from DocumentType to KycTypes
    private Branches branch;
    private Tenant tenant;
    private CustomerKycUpload kycUpload;
    private CustomerKycResponseDto responseDto;

    @BeforeEach
    void setUp() {
        request = new CustomerKycRequestDto();
        request.setIdNumber("ID1234567890");
        request.setIdType(UUID.randomUUID());
        request.setBranchId(UUID.randomUUID());
        request.setTenantId(UUID.randomUUID());
        request.setBranchCode("BR001");

        customer = new Customer();
        customer.setIdentity(UUID.randomUUID());
        customer.setCustomerCode("BR001-CUS-001");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setDob(LocalDate.of(1990, 1, 1));

        customerKyc = new CustomerKyc();
        customerKyc.setIdentity(UUID.randomUUID());
        customerKyc.setCustomer(customer);
        customerKyc.setIdNumber("ID1234567890");

        kycType = new KycTypes(); // Updated from DocumentType to KycTypes
        kycType.setIdentity(request.getIdType());
        kycType.setDisplayName("Aadhaar");

        branch = new Branches();
        branch.setIdentity(request.getBranchId());

        tenant = new Tenant();
        tenant.setIdentity(request.getTenantId());

        kycUpload = new CustomerKycUpload();
        kycUpload.setDocumentReference(123456);

        responseDto = new CustomerKycResponseDto();
    }

    @Test
    void testCreateInitialCustomer_Success() throws Exception {
        when(objectMapper.readValue(anyString(), eq(CustomerKycRequestDto.class))).thenReturn(request);
        when(kycTypesRepository.findByIdentity(request.getIdType())).thenReturn(Optional.of(kycType)); // Updated repository call
        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, request.getIdNumber())).thenReturn(Optional.empty()); // Updated parameter
        when(customerKycMapper.generateCustomerCode(anyString(), anyString())).thenReturn("BR001-CUS-001");
        when(customerKycMapper.toCustomerEntity(any(), any(), any())).thenReturn(customer);
        when(branchesRepository.findByIdentity(request.getBranchId())).thenReturn(Optional.of(branch));
        when(tenantRepository.findByIdentity(request.getTenantId())).thenReturn(Optional.of(tenant));
        when(customerRepository.save(any())).thenReturn(customer);
        when(customerKycMapper.toKycEntity(any(), any())).thenReturn(customerKyc);
        when(customerKycRepository.save(any())).thenReturn(customerKyc);
        when(customerKycMapper.toKycUploadEntity(any(), any())).thenReturn(kycUpload);
        when(customerKycUploadRepository.save(any())).thenReturn(kycUpload);
        when(customerKycRepository.findByCustomer(customer)).thenReturn(List.of(customerKyc));
        when(customerKycMapper.toResponseDto(any(), any(), any())).thenReturn(responseDto);
        when(file.isEmpty()).thenReturn(false);

        CustomerKycResponseDto result = service.createInitialCustomer("{}", file);

        assertNotNull(result);
        verify(customerRepository).save(customer);
        verify(customerKycRepository, times(2)).save(customerKyc);
        verify(customerKycUploadRepository).save(kycUpload);
        verify(kycTypesRepository).findByIdentity(request.getIdType()); // Verify correct repository
    }

    @Test
    void testCreateInitialCustomer_NullRequestJson() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.createInitialCustomer(null, file));

        assertEquals(CommonConstants.INVALID_REQUEST, exception.getMessage());
        assertEquals(ErrorCodes.VALIDATION_FAILED, exception.getErrorCode());
    }

    @Test
    void testCreateInitialCustomer_EmptyRequestJson() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.createInitialCustomer("", file));

        assertEquals(CommonConstants.INVALID_REQUEST, exception.getMessage());
        assertEquals(ErrorCodes.VALIDATION_FAILED, exception.getErrorCode());
    }

    @Test
    void testCreateInitialCustomer_BlankRequestJson() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.createInitialCustomer("   ", file));

        assertEquals(CommonConstants.INVALID_REQUEST, exception.getMessage());
        assertEquals(ErrorCodes.VALIDATION_FAILED, exception.getErrorCode());
    }

    @Test
    void testCreateInitialCustomer_JsonProcessingException() throws Exception {
        when(objectMapper.readValue(anyString(), eq(CustomerKycRequestDto.class)))
                .thenThrow(JsonProcessingException.class);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.createInitialCustomer("invalid", file));

        assertEquals(CommonConstants.INVALID_JSON, exception.getMessage());
        assertEquals(ErrorCodes.VALIDATION_FAILED, exception.getErrorCode());
        assertNotNull(exception.getCause());
    }

    @Test
    void testCreateInitialCustomer_KycTypeNotFound() throws Exception { // Updated method name
        when(objectMapper.readValue(anyString(), eq(CustomerKycRequestDto.class))).thenReturn(request);
        when(kycTypesRepository.findByIdentity(request.getIdType())).thenReturn(Optional.empty()); // Updated repository call

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.createInitialCustomer("{}", file));

        assertEquals(CommonConstants.DOCUMENT_TYPE_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testCreateInitialCustomer_DuplicateIdNumber() throws Exception {
        when(objectMapper.readValue(anyString(), eq(CustomerKycRequestDto.class))).thenReturn(request);
        when(kycTypesRepository.findByIdentity(request.getIdType())).thenReturn(Optional.of(kycType)); // Updated
        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, request.getIdNumber())).thenReturn(Optional.of(customerKyc)); // Updated parameter
        when(customerKycRepository.findByCustomer(customer)).thenReturn(List.of(customerKyc));
        when(customerKycMapper.toResponseDto(any(), any(), any())).thenReturn(responseDto);

        BusinessConflictException exception = assertThrows(BusinessConflictException.class,
                () -> service.createInitialCustomer("{}", file));

        assertTrue(exception.getMessage().contains("Customer already exists"));
        assertEquals(CommonConstants.DUPLICATE_IDENTIFIER, exception.getErrorCode());
        assertNotNull(exception.getExistingIdentity());
        assertNotNull(exception.getExistingDetails());
    }

    @Test
    void testCreateInitialCustomer_BranchNotFound() throws Exception {
        when(objectMapper.readValue(anyString(), eq(CustomerKycRequestDto.class))).thenReturn(request);
        when(kycTypesRepository.findByIdentity(request.getIdType())).thenReturn(Optional.of(kycType)); // Updated
        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, request.getIdNumber())).thenReturn(Optional.empty()); // Updated parameter
        when(customerKycMapper.generateCustomerCode(anyString(), anyString())).thenReturn("BR001-CUS-001");
        when(customerKycMapper.toCustomerEntity(any(), any(), any())).thenReturn(customer);
        when(branchesRepository.findByIdentity(request.getBranchId())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.createInitialCustomer("{}", file));

        assertEquals(CommonConstants.INVALID_BRANCH, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testCreateInitialCustomer_TenantNotFound() throws Exception {
        when(objectMapper.readValue(anyString(), eq(CustomerKycRequestDto.class))).thenReturn(request);
        when(kycTypesRepository.findByIdentity(request.getIdType())).thenReturn(Optional.of(kycType)); // Updated
        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, request.getIdNumber())).thenReturn(Optional.empty()); // Updated parameter
        when(customerKycMapper.generateCustomerCode(anyString(), anyString())).thenReturn("BR001-CUS-001");
        when(customerKycMapper.toCustomerEntity(any(), any(), any())).thenReturn(customer);
        when(branchesRepository.findByIdentity(request.getBranchId())).thenReturn(Optional.of(branch));
        when(tenantRepository.findByIdentity(request.getTenantId())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.createInitialCustomer("{}", file));

        assertEquals(CommonConstants.TENANT_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testCreateInitialCustomer_DataIntegrityViolation() throws Exception {
        when(objectMapper.readValue(anyString(), eq(CustomerKycRequestDto.class))).thenReturn(request);
        when(kycTypesRepository.findByIdentity(request.getIdType())).thenReturn(Optional.of(kycType)); // Updated
        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, request.getIdNumber())).thenReturn(Optional.empty()); // Updated parameter
        when(customerKycMapper.generateCustomerCode(anyString(), anyString())).thenReturn("BR001-CUS-001");
        when(customerKycMapper.toCustomerEntity(any(), any(), any())).thenReturn(customer);
        when(branchesRepository.findByIdentity(request.getBranchId())).thenReturn(Optional.of(branch));
        when(tenantRepository.findByIdentity(request.getTenantId())).thenReturn(Optional.of(tenant));
        when(customerRepository.save(any())).thenThrow(new DataIntegrityViolationException("Constraint violation"));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.createInitialCustomer("{}", file));

        assertEquals(CommonConstants.CONSTRAIN_VIOLATION, exception.getMessage());
        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, exception.getErrorCode());
        assertNotNull(exception.getCause());
    }

    @Test
    void testCreateInitialCustomer_FileUploadException() throws Exception {
        when(objectMapper.readValue(anyString(), eq(CustomerKycRequestDto.class))).thenReturn(request);
        when(kycTypesRepository.findByIdentity(request.getIdType())).thenReturn(Optional.of(kycType)); // Updated
        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, request.getIdNumber())).thenReturn(Optional.empty()); // Updated parameter
        when(customerKycMapper.generateCustomerCode(anyString(), anyString())).thenReturn("BR001-CUS-001");
        when(customerKycMapper.toCustomerEntity(any(), any(), any())).thenReturn(customer);
        when(branchesRepository.findByIdentity(request.getBranchId())).thenReturn(Optional.of(branch));
        when(tenantRepository.findByIdentity(request.getTenantId())).thenReturn(Optional.of(tenant));
        when(customerRepository.save(any())).thenReturn(customer);
        when(customerKycMapper.toKycEntity(any(), any())).thenReturn(customerKyc);
        when(customerKycRepository.save(any())).thenReturn(customerKyc);
        when(customerKycMapper.toKycUploadEntity(any(), any())).thenReturn(kycUpload);
        when(customerKycUploadRepository.save(any())).thenThrow(new RuntimeException("File upload failed"));
        when(file.isEmpty()).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.createInitialCustomer("{}", file));

        assertEquals(CommonConstants.FILE_UPLOAD_FAILED, exception.getMessage());
        assertEquals(ErrorCodes.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        assertNotNull(exception.getCause());
    }

    @Test
    void testAddKycDocument_Success() throws Exception {
        when(objectMapper.readValue(anyString(), eq(CustomerKycRequestDto.class))).thenReturn(request);
        when(customerRepository.findByIdentity(customer.getIdentity())).thenReturn(Optional.of(customer));
        when(kycTypesRepository.findByIdentity(request.getIdType())).thenReturn(Optional.of(kycType)); // Updated
        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, request.getIdNumber())).thenReturn(Optional.empty()); // Updated parameter
        when(customerKycRepository.existsByIdTypeAndCustomer(kycType, customer)).thenReturn(false); // Updated parameter
        when(customerKycMapper.toKycEntity(any(), any())).thenReturn(customerKyc);
        when(customerKycRepository.save(any())).thenReturn(customerKyc);
        when(customerKycMapper.toKycUploadEntity(any(), any())).thenReturn(kycUpload);
        when(customerKycUploadRepository.save(any())).thenReturn(kycUpload);
        when(customerKycRepository.findByCustomer(customer)).thenReturn(List.of(customerKyc));
        when(customerKycMapper.toResponseDto(any(), any(), any())).thenReturn(responseDto);
        when(file.isEmpty()).thenReturn(false);

        CustomerKycResponseDto result = service.addKycDocument("{}", file, customer.getIdentity());

        assertNotNull(result);
        verify(customerKycRepository, times(2)).save(customerKyc);
        verify(customerKycUploadRepository).save(kycUpload);
        verify(kycTypesRepository).findByIdentity(request.getIdType()); // Verify correct repository
    }

    @Test
    void testAddKycDocument_NullRequestJson() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.addKycDocument(null, file, customer.getIdentity()));

        assertEquals(CommonConstants.INVALID_REQUEST, exception.getMessage());
        assertEquals(ErrorCodes.VALIDATION_FAILED, exception.getErrorCode());
    }

    @Test
    void testAddKycDocument_CustomerNotFound() throws Exception {
        when(objectMapper.readValue(anyString(), eq(CustomerKycRequestDto.class))).thenReturn(request);
        when(customerRepository.findByIdentity(any())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.addKycDocument("{}", file, UUID.randomUUID()));

        assertEquals(CommonConstants.CUSTOMER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testAddKycDocument_KycTypeNotFound() throws Exception { // Updated method name
        when(objectMapper.readValue(anyString(), eq(CustomerKycRequestDto.class))).thenReturn(request);
        when(customerRepository.findByIdentity(customer.getIdentity())).thenReturn(Optional.of(customer));
        when(kycTypesRepository.findByIdentity(request.getIdType())).thenReturn(Optional.empty()); // Updated repository call

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.addKycDocument("{}", file, customer.getIdentity()));

        assertEquals(CommonConstants.DOCUMENT_TYPE_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testAddKycDocument_DuplicateIdNumber() throws Exception {
        Customer conflictCustomer = new Customer();
        conflictCustomer.setIdentity(UUID.randomUUID());
        conflictCustomer.setCustomerCode("C001");
        conflictCustomer.setFirstName("John");
        conflictCustomer.setLastName("Doe");

        CustomerKyc conflictKyc = new CustomerKyc();
        conflictKyc.setCustomer(conflictCustomer);

        when(objectMapper.readValue(anyString(), eq(CustomerKycRequestDto.class))).thenReturn(request);
        when(customerRepository.findByIdentity(customer.getIdentity())).thenReturn(Optional.of(customer));
        when(kycTypesRepository.findByIdentity(request.getIdType())).thenReturn(Optional.of(kycType)); // Updated
        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, request.getIdNumber())).thenReturn(Optional.of(conflictKyc)); // Updated parameter
        when(customerKycRepository.findByCustomer(conflictCustomer)).thenReturn(List.of(conflictKyc));
        when(customerKycMapper.toResponseDto(any(), any(), any())).thenReturn(responseDto);

        BusinessConflictException exception = assertThrows(BusinessConflictException.class,
                () -> service.addKycDocument("{}", file, customer.getIdentity()));

        assertTrue(exception.getMessage().contains("Customer already exists"));
        assertEquals(CommonConstants.DUPLICATE_IDENTIFIER, exception.getErrorCode());
    }

    @Test
    void testAddKycDocument_DocumentAlreadyExistsForCustomer() throws Exception {
        when(objectMapper.readValue(anyString(), eq(CustomerKycRequestDto.class))).thenReturn(request);
        when(customerRepository.findByIdentity(customer.getIdentity())).thenReturn(Optional.of(customer));
        when(kycTypesRepository.findByIdentity(request.getIdType())).thenReturn(Optional.of(kycType)); // Updated
        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, request.getIdNumber())).thenReturn(Optional.empty()); // Updated parameter
        when(customerKycRepository.existsByIdTypeAndCustomer(kycType, customer)).thenReturn(true); // Updated parameter

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.addKycDocument("{}", file, customer.getIdentity()));

        assertEquals(CommonConstants.DOCUMENT_ALREADY_EXISTS, exception.getMessage());
        assertEquals(ErrorCodes.CONFLICT, exception.getErrorCode());
    }

    @Test
    void testAddKycDocument_DataIntegrityViolation() throws Exception {
        when(objectMapper.readValue(anyString(), eq(CustomerKycRequestDto.class))).thenReturn(request);
        when(customerRepository.findByIdentity(customer.getIdentity())).thenReturn(Optional.of(customer));
        when(kycTypesRepository.findByIdentity(request.getIdType())).thenReturn(Optional.of(kycType)); // Updated
        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, request.getIdNumber())).thenReturn(Optional.empty()); // Updated parameter
        when(customerKycRepository.existsByIdTypeAndCustomer(kycType, customer)).thenReturn(false); // Updated parameter
        when(customerKycMapper.toKycEntity(any(), any())).thenReturn(customerKyc);
        when(customerKycRepository.save(any())).thenThrow(new DataIntegrityViolationException("Constraint violation"));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.addKycDocument("{}", file, customer.getIdentity()));

        assertEquals(CommonConstants.CONSTRAIN_VIOLATION, exception.getMessage());
        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, exception.getErrorCode());
        assertNotNull(exception.getCause());
    }

    @Test
    void testAddKycDocument_JsonProcessingException() throws Exception {
        when(objectMapper.readValue(anyString(), eq(CustomerKycRequestDto.class)))
                .thenThrow(JsonProcessingException.class);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.addKycDocument("invalid", file, customer.getIdentity()));

        assertEquals(CommonConstants.INVALID_JSON, exception.getMessage());
        assertEquals(ErrorCodes.VALIDATION_FAILED, exception.getErrorCode());
        assertNotNull(exception.getCause());
    }

    @Test
    void testMaskIdNumber_ThroughConflict() throws Exception {
        when(objectMapper.readValue(anyString(), eq(CustomerKycRequestDto.class))).thenReturn(request);
        when(kycTypesRepository.findByIdentity(request.getIdType())).thenReturn(Optional.of(kycType)); // Updated
        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, request.getIdNumber())).thenReturn(Optional.of(customerKyc)); // Updated parameter
        when(customerKycRepository.findByCustomer(customer)).thenReturn(List.of(customerKyc));
        when(customerKycMapper.toResponseDto(any(), any(), any())).thenReturn(responseDto);

        BusinessConflictException exception = assertThrows(BusinessConflictException.class,
                () -> service.createInitialCustomer("{}", file));

        assertTrue(exception.getMessage().contains("XXXX XXXX"));
    }

    @Test
    void testThrowConflict_WithNullCustomer() throws Exception {
        when(objectMapper.readValue(anyString(), eq(CustomerKycRequestDto.class))).thenReturn(request);
        when(kycTypesRepository.findByIdentity(request.getIdType())).thenReturn(Optional.of(kycType)); // Updated

        // Create a kyc with null customer to trigger the null customer check in throwConflict
        CustomerKyc kycWithNullCustomer = new CustomerKyc();
        kycWithNullCustomer.setCustomer(null);

        when(customerKycRepository.findByIdTypeAndIdNumber(kycType, request.getIdNumber())) // Updated parameter
                .thenReturn(Optional.of(kycWithNullCustomer));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.createInitialCustomer("{}", file));

        assertEquals(CommonConstants.CUSTOMER_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testGetKycDocuments_Success() {
        UUID customerIdentity = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setIdentity(customerIdentity);
        customer.setCustomerCode("CUST001");
        customer.setFirstName("John");
        customer.setLastName("Doe");

        CustomerKyc kyc = new CustomerKyc();
        kyc.setIdentity(UUID.randomUUID());
        kyc.setIdNumber("ID123456");
        kyc.setIsVerified(true);
        kyc.setIsActive(true);
        kyc.setIdType(kycType);

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerKycRepository.findByCustomer(customer)).thenReturn(List.of(kyc));

        CustomerKycResponseDto result = service.getKycDocuments(customerIdentity);

        assertNotNull(result);
        assertEquals(customerIdentity, result.getIdentity());
        assertEquals("CUST001", result.getCustomerCode());
        verify(customerRepository).findByIdentity(customerIdentity);
        verify(customerKycRepository).findByCustomer(customer);
    }

    @Test
    void testGetKycDocuments_CustomerNotFound() {
        UUID customerIdentity = UUID.randomUUID();
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.getKycDocuments(customerIdentity));

        assertTrue(exception.getMessage().contains("Customer not found"));
    }

    @Test
    void testGetKycDocuments_WithNullRelations() {
        UUID customerIdentity = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setIdentity(customerIdentity);
        customer.setCustomerCode("CUST001");

        CustomerKyc kyc = new CustomerKyc();
        kyc.setIdentity(UUID.randomUUID());
        kyc.setIdNumber("ID123456");
        kyc.setIsVerified(true);
        kyc.setIsActive(true);
        kyc.setIdType(null);

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerKycRepository.findByCustomer(customer)).thenReturn(List.of(kyc));

        CustomerKycResponseDto result = service.getKycDocuments(customerIdentity);

        assertNotNull(result);
        assertEquals(customerIdentity, result.getIdentity());
    }
}