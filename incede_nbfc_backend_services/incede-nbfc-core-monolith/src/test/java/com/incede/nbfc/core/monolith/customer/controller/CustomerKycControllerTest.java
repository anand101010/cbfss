package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerKycRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerKycResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerKycService;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CustomerKycControllerTest {

    @Mock
    private CustomerKycService customerKycService;

    @InjectMocks
    private CustomerKycController controller;

    private UUID customerIdentity;
    private UUID idType;
    private UUID branchId;
    private UUID tenantId;
    private CustomerKycRequestDto requestDto;
    private CustomerKycResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerIdentity = UUID.randomUUID();
        idType = UUID.randomUUID();
        branchId = UUID.randomUUID();
        tenantId = UUID.randomUUID();

        // Create valid request DTO
        requestDto = CustomerKycRequestDto.builder()
                .idType(idType)
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

        // Create response DTO
        responseDto = CustomerKycResponseDto.builder()
                .identity(customerIdentity)
                .customerCode("CUST001")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("+1234567890")
                .panNumber("ABCDE1234F")
                .aadhaarNumber("123456789012")
                .dateOfBirth(LocalDateTime.of(1990, 1, 1, 0, 0))
                .gender("MALE")
                .kycStatus("VERIFIED")
                .kycType("AADHAAR")
                .documentNumber("123456789012")
                .documentFrontPath("/documents/front.jpg")
                .documentBackPath("/documents/back.jpg")
                .verifiedBy("STAFF001")
                .verifiedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateInitialCustomer_Success() {
        when(customerKycService.createInitialCustomer(any(CustomerKycRequestDto.class)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerKycResponseDto> result =
                controller.createInitialCustomer(requestDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(responseDto, result.getBody());
        verify(customerKycService, times(1)).createInitialCustomer(any(CustomerKycRequestDto.class));
    }

    @Test
    void testCreateInitialCustomer_WithMinimalRequiredFields() {
        // Test with only required fields
        CustomerKycRequestDto minimalRequest = CustomerKycRequestDto.builder()
                .idType(idType)
                .branchId(branchId)
                .idNumber("ABCDE1234F")
                .branchCode("BR001")
                .documentRefId("DOC_REF_001")
                .isVerified(false)
                .isActive(true)
                .build();

        when(customerKycService.createInitialCustomer(any(CustomerKycRequestDto.class)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerKycResponseDto> result =
                controller.createInitialCustomer(minimalRequest);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(customerKycService, times(1)).createInitialCustomer(any(CustomerKycRequestDto.class));
    }

    @Test
    void testAddKycDocument_Success() {
        when(customerKycService.addKycDocument(any(CustomerKycRequestDto.class), eq(customerIdentity)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerKycResponseDto> result =
                controller.addKycDocument(requestDto, customerIdentity);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responseDto, result.getBody());
        verify(customerKycService, times(1)).addKycDocument(any(CustomerKycRequestDto.class), eq(customerIdentity));
    }

    @Test
    void testGetKycDocuments_Success() {
        when(customerKycService.getKycDocuments(eq(customerIdentity)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerKycResponseDto> result =
                controller.getKycDocuments(customerIdentity);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responseDto, result.getBody());
        verify(customerKycService, times(1)).getKycDocuments(eq(customerIdentity));
    }

    @Test
    void testCreateInitialCustomer_BusinessException() {
        when(customerKycService.createInitialCustomer(any(CustomerKycRequestDto.class)))
                .thenThrow(new BusinessException("Customer already exists", ErrorCodes.CONFLICT));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.createInitialCustomer(requestDto)
        );

        assertEquals("Customer already exists", ex.getMessage());
        assertEquals(ErrorCodes.CONFLICT, ex.getErrorCode());
        verify(customerKycService, times(1)).createInitialCustomer(any(CustomerKycRequestDto.class));
    }

    @Test
    void testAddKycDocument_BusinessException() {
        when(customerKycService.addKycDocument(any(CustomerKycRequestDto.class), eq(customerIdentity)))
                .thenThrow(new BusinessException("Customer not found", ErrorCodes.RESOURCE_NOT_FOUND));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.addKycDocument(requestDto, customerIdentity)
        );

        assertEquals("Customer not found", ex.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
        verify(customerKycService, times(1)).addKycDocument(any(CustomerKycRequestDto.class), eq(customerIdentity));
    }

    @Test
    void testGetKycDocuments_BusinessException() {
        when(customerKycService.getKycDocuments(eq(customerIdentity)))
                .thenThrow(new BusinessException("KYC documents not found", ErrorCodes.RESOURCE_NOT_FOUND));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.getKycDocuments(customerIdentity)
        );

        assertEquals("KYC documents not found", ex.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
        verify(customerKycService, times(1)).getKycDocuments(eq(customerIdentity));
    }

    @Test
    void testCreateInitialCustomer_ValidationException_MissingRequiredFields() {
        // Test with missing required fields
        CustomerKycRequestDto invalidRequest = CustomerKycRequestDto.builder()
                // Missing idType, branchId, idNumber, branchCode, documentRefId
                .build();

        when(customerKycService.createInitialCustomer(any(CustomerKycRequestDto.class)))
                .thenThrow(new BusinessException("Validation failed", ErrorCodes.VALIDATION_ERROR));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.createInitialCustomer(invalidRequest)
        );

        assertEquals("Validation failed", ex.getMessage());
        assertEquals(ErrorCodes.VALIDATION_ERROR, ex.getErrorCode());
    }

    @Test
    void testCreateInitialCustomer_ValidationException_InvalidDateRange() {
        // Test with invalid date range (validFrom in future, validTo in past)
        CustomerKycRequestDto invalidDateRequest = CustomerKycRequestDto.builder()
                .idType(idType)
                .branchId(branchId)
                .idNumber("ABCDE1234F")
                .branchCode("BR001")
                .documentRefId("DOC_REF_001")
                .validFrom(LocalDate.now().plusDays(1)) // Future date - invalid
                .validTo(LocalDate.now().minusDays(1)) // Past date - invalid
                .build();

        when(customerKycService.createInitialCustomer(any(CustomerKycRequestDto.class)))
                .thenThrow(new BusinessException("Invalid date range", ErrorCodes.VALIDATION_ERROR));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.createInitialCustomer(invalidDateRequest)
        );

        assertEquals("Invalid date range", ex.getMessage());
        assertEquals(ErrorCodes.VALIDATION_FAILED, ex.getErrorCode());
    }

    @Test
    void testCreateInitialCustomer_ValidationException_IdNumberTooLong() {
        // Test with ID number exceeding 50 characters
        String longIdNumber = "A".repeat(51); // 51 characters - exceeds limit

        CustomerKycRequestDto invalidRequest = CustomerKycRequestDto.builder()
                .idType(idType)
                .branchId(branchId)
                .idNumber(longIdNumber)
                .branchCode("BR001")
                .documentRefId("DOC_REF_001")
                .build();

        when(customerKycService.createInitialCustomer(any(CustomerKycRequestDto.class)))
                .thenThrow(new BusinessException("ID number too long", ErrorCodes.VALIDATION_ERROR));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.createInitialCustomer(invalidRequest)
        );

        assertEquals("ID number too long", ex.getMessage());
        assertEquals(ErrorCodes.VALIDATION_FAILED, ex.getErrorCode());
    }

    @Test
    void testCreateInitialCustomer_ValidationException_BlankFields() {
        // Test with blank required fields
        CustomerKycRequestDto blankRequest = CustomerKycRequestDto.builder()
                .idType(idType)
                .branchId(branchId)
                .idNumber("") // Blank ID number
                .branchCode("") // Blank branch code
                .documentRefId("") // Blank document ref ID
                .build();

        when(customerKycService.createInitialCustomer(any(CustomerKycRequestDto.class)))
                .thenThrow(new BusinessException("Required fields are blank", ErrorCodes.VALIDATION_ERROR));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.createInitialCustomer(blankRequest)
        );

        assertEquals("Required fields are blank", ex.getMessage());
        assertEquals(ErrorCodes.VALIDATION_FAILED, ex.getErrorCode());
    }

    @Test
    void testAddKycDocument_DuplicateDocumentException() {
        when(customerKycService.addKycDocument(any(CustomerKycRequestDto.class), eq(customerIdentity)))
                .thenThrow(new BusinessException("KYC document already exists", ErrorCodes.DUPLICATE_RESOURCE));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.addKycDocument(requestDto, customerIdentity)
        );

        assertEquals("KYC document already exists", ex.getMessage());
        assertEquals(ErrorCodes.DUPLICATE_RESOURCE, ex.getErrorCode());
    }

    @Test
    void testGetKycDocuments_KycNotCompleted() {
        // Test when KYC is not completed
        CustomerKycResponseDto pendingKycResponse = CustomerKycResponseDto.builder()
                .customerIdentity(customerIdentity)
                .customerCode("CUST001")
                .firstName("John")
                .lastName("Doe")
                .kycStatus("PENDING")
                .kycType(null)
                .documentFrontPath(null)
                .documentBackPath(null)
                .build();

        when(customerKycService.getKycDocuments(eq(customerIdentity)))
                .thenReturn(pendingKycResponse);

        ResponseEntity<CustomerKycResponseDto> result =
                controller.getKycDocuments(customerIdentity);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("PENDING", result.getBody().getKycStatus());
        assertNull(result.getBody().getKycType());
    }

    @Test
    void testCreateInitialCustomer_WithFileDetails() {
        // Test with complete file details
        CustomerKycRequestDto fileRequest = CustomerKycRequestDto.builder()
                .idType(idType)
                .branchId(branchId)
                .idNumber("ABCDE1234F")
                .branchCode("BR001")
                .documentRefId("DOC_REF_001")
                .filePath("/documents/kyc/")
                .fileName("pan_card.jpg")
                .fileType("image/jpeg")
                .isVerified(true)
                .isActive(true)
                .build();

        when(customerKycService.createInitialCustomer(any(CustomerKycRequestDto.class)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerKycResponseDto> result =
                controller.createInitialCustomer(fileRequest);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(customerKycService, times(1)).createInitialCustomer(any(CustomerKycRequestDto.class));
    }

    @Test
    void testCreateInitialCustomer_WithDefaultValues() {
        // Test that default values are set correctly
        CustomerKycRequestDto requestWithDefaults = CustomerKycRequestDto.builder()
                .idType(idType)
                .branchId(branchId)
                .idNumber("ABCDE1234F")
                .branchCode("BR001")
                .documentRefId("DOC_REF_001")
                // isVerified and isActive not set - should use defaults
                .build();

        assertEquals(false, requestWithDefaults.getIsVerified()); // Default value
        assertEquals(true, requestWithDefaults.getIsActive()); // Default value

        when(customerKycService.createInitialCustomer(any(CustomerKycRequestDto.class)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerKycResponseDto> result =
                controller.createInitialCustomer(requestWithDefaults);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        verify(customerKycService, times(1)).createInitialCustomer(any(CustomerKycRequestDto.class));
    }

    @Test
    void testCreateInitialCustomer_ServiceUnavailable() {
        when(customerKycService.createInitialCustomer(any(CustomerKycRequestDto.class)))
                .thenThrow(new BusinessException("DMS service unavailable", ErrorCodes.SERVICE_UNAVAILABLE));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.createInitialCustomer(requestDto)
        );

        assertEquals("DMS service unavailable", ex.getMessage());
        assertEquals(ErrorCodes.SERVICE_UNAVAILABLE, ex.getErrorCode());
    }

    @Test
    void testIdNumber_ExcludedFromToString() {
        // Verify that idNumber is excluded from toString as per @ToString.Exclude
        assertFalse(requestDto.toString().contains("idNumber"));
        assertFalse(requestDto.toString().contains("ABCDE1234F"));
    }
}