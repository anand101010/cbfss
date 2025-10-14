package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.*;
import com.incede.nbfc.core.monolith.customer.service.CustomerKycService;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerKycUpload;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
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
    private UUID gender;
    private UUID customerStatus;
    private CustomerKycRequestDto requestDto;
    private CustomerKycResponseDto responseDto;
    private KycDocumentResponseDto kycDocumentDto;
    private KycUploadResponseDto kycUploadDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerIdentity = UUID.randomUUID();
        idType = UUID.randomUUID();
        branchId = UUID.randomUUID();
        tenantId = UUID.randomUUID();
        gender = UUID.randomUUID();
        customerStatus = UUID.randomUUID();

        // Create KYC upload DTO
        kycUploadDto = KycUploadResponseDto.builder()
                .identity(UUID.randomUUID())
                .documentReference("DOC_REF_001")
                .fileName("aadhaar_front.jpg")
                .fileType("image/jpeg")
                .filePath("/documents/kyc/aadhaar_front.jpg")
                .uploadStatus(CustomerKycUpload.UploadStatus.SUCCESS)
                .version(1)
                .uploadDate(Timestamp.valueOf("2024-01-15 10:30:00"))
                .build();

        // Create KYC document DTO with uploads
        kycDocumentDto = KycDocumentResponseDto.builder()
                .identity(UUID.randomUUID())
                .idType(idType)
                .idNumber("ABCDE1234F")
                .placeOfIssue("New Delhi")
                .issuingAuthority("UIDAI")
                .validFrom("2020-01-01")
                .validTo("2030-01-01")
                .isVerified(true)
                .isActive(true)
                .kycUploads(List.of(kycUploadDto))
                .build();

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

        // Create response DTO with nested structure
        responseDto = CustomerKycResponseDto.builder()
                .identity(customerIdentity)
                .customerCode("CUST001")
                .firstName("John")
                .lastName("Doe")
                .dob("1990-01-01")
                .gender(gender)
                .customerStatus(customerStatus)
                .onboardingStatus("COMPLETED")
                .branchId(branchId)
                .kycDocuments(List.of(kycDocumentDto))
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

        // Verify customer details
        assertEquals("CUST001", result.getBody().getCustomerCode());
        assertEquals("John", result.getBody().getFirstName());
        assertEquals("COMPLETED", result.getBody().getOnboardingStatus());

        // Verify KYC documents
        assertNotNull(result.getBody().getKycDocuments());
        assertEquals(1, result.getBody().getKycDocuments().size());

        KycDocumentResponseDto document = result.getBody().getKycDocuments().get(0);
        assertEquals("ABCDE1234F", document.getIdNumber());
        assertTrue(document.getIsVerified());

        // Verify KYC uploads
        assertNotNull(document.getKycUploads());
        assertEquals(1, document.getKycUploads().size());
        assertEquals("aadhaar_front.jpg", document.getKycUploads().get(0).getFileName());

        verify(customerKycService, times(1)).createInitialCustomer(any(CustomerKycRequestDto.class));
    }

    @Test
    void testCreateInitialCustomer_WithMultipleUploadsPerDocument() {
        // Create multiple uploads for a single document
        KycUploadResponseDto kycUpload2 = KycUploadResponseDto.builder()
                .identity(UUID.randomUUID())
                .documentReference("DOC_REF_001_BACK")
                .fileName("aadhaar_back.jpg")
                .fileType("image/jpeg")
                .filePath("/documents/kyc/aadhaar_back.jpg")
                .uploadStatus(CustomerKycUpload.UploadStatus.SUCCESS)
                .version(1)
                .uploadDate(Timestamp.valueOf("2024-01-15 10:35:00"))
                .build();

        KycDocumentResponseDto documentWithMultipleUploads = KycDocumentResponseDto.builder()
                .identity(UUID.randomUUID())
                .idType(idType)
                .idNumber("ABCDE1234F")
                .isVerified(true)
                .isActive(true)
                .kycUploads(List.of(kycUploadDto, kycUpload2))
                .build();

        CustomerKycResponseDto responseWithMultipleUploads = CustomerKycResponseDto.builder()
                .identity(customerIdentity)
                .customerCode("CUST001")
                .onboardingStatus("COMPLETED")
                .kycDocuments(List.of(documentWithMultipleUploads))
                .build();

        when(customerKycService.createInitialCustomer(any(CustomerKycRequestDto.class)))
                .thenReturn(responseWithMultipleUploads);

        ResponseEntity<CustomerKycResponseDto> result =
                controller.createInitialCustomer(requestDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(1, result.getBody().getKycDocuments().size());
        assertEquals(2, result.getBody().getKycDocuments().get(0).getKycUploads().size());

        List<KycUploadResponseDto> uploads = result.getBody().getKycDocuments().get(0).getKycUploads();
        assertEquals("aadhaar_front.jpg", uploads.get(0).getFileName());
        assertEquals("aadhaar_back.jpg", uploads.get(1).getFileName());
    }

    @Test
    void testCreateInitialCustomer_WithMultipleDocumentsAndUploads() {
        // Create second KYC document
        KycDocumentResponseDto kycDocument2 = KycDocumentResponseDto.builder()
                .identity(UUID.randomUUID())
                .idType(UUID.randomUUID())
                .idNumber("PAN1234567")
                .isVerified(true)
                .isActive(true)
                .kycUploads(List.of(
                        KycUploadResponseDto.builder()
                                .fileName("pan_card.jpg")
                                .fileType("image/jpeg")
                                .uploadStatus(CustomerKycUpload.UploadStatus.SUCCESS)
                                .version(1)
                                .build()
                ))
                .build();

        CustomerKycResponseDto responseWithMultipleDocs = CustomerKycResponseDto.builder()
                .identity(customerIdentity)
                .customerCode("CUST001")
                .onboardingStatus("COMPLETED")
                .kycDocuments(List.of(kycDocumentDto, kycDocument2))
                .build();

        when(customerKycService.createInitialCustomer(any(CustomerKycRequestDto.class)))
                .thenReturn(responseWithMultipleDocs);

        ResponseEntity<CustomerKycResponseDto> result =
                controller.createInitialCustomer(requestDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(2, result.getBody().getKycDocuments().size());
        assertEquals("ABCDE1234F", result.getBody().getKycDocuments().get(0).getIdNumber());
        assertEquals("PAN1234567", result.getBody().getKycDocuments().get(1).getIdNumber());
    }

    @Test
    void testAddKycDocument_Success() {
        when(customerKycService.addKycDocument(any(CustomerKycRequestDto.class), eq(customerIdentity)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerKycResponseDto> result =
                controller.addKycDocument(requestDto, customerIdentity);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responseDto, result.getBody());

        assertNotNull(result.getBody().getKycDocuments());
        KycDocumentResponseDto document = result.getBody().getKycDocuments().get(0);
        assertNotNull(document.getKycUploads());
        assertEquals(CustomerKycUpload.UploadStatus.SUCCESS,
                document.getKycUploads().get(0).getUploadStatus());

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

        KycDocumentResponseDto document = result.getBody().getKycDocuments().get(0);
        KycUploadResponseDto upload = document.getKycUploads().get(0);

        assertEquals("ABCDE1234F", document.getIdNumber());
        assertEquals("2020-01-01", document.getValidFrom());
        assertEquals("2030-01-01", document.getValidTo());
        assertEquals("aadhaar_front.jpg", upload.getFileName());
        assertEquals("image/jpeg", upload.getFileType());
        assertEquals("/documents/kyc/aadhaar_front.jpg", upload.getFilePath());
        assertEquals(Integer.valueOf(1), upload.getVersion());

        verify(customerKycService, times(1)).getKycDocuments(eq(customerIdentity));
    }

    @Test
    void testGetKycDocuments_WithDifferentUploadStatuses() {
        // Create documents with different upload statuses
        KycUploadResponseDto pendingUpload = KycUploadResponseDto.builder()
                .identity(UUID.randomUUID())
                .fileName("pending_doc.pdf")
                .uploadStatus(CustomerKycUpload.UploadStatus.PENDING)
                .version(1)
                .build();

        KycUploadResponseDto failedUpload = KycUploadResponseDto.builder()
                .identity(UUID.randomUUID())
                .fileName("failed_doc.pdf")
                .uploadStatus(CustomerKycUpload.UploadStatus.FAILED)
                .version(1)
                .build();

        KycDocumentResponseDto documentWithMixedStatuses = KycDocumentResponseDto.builder()
                .identity(UUID.randomUUID())
                .idType(idType)
                .idNumber("MIXED123")
                .isVerified(false)
                .isActive(true)
                .kycUploads(List.of(pendingUpload, failedUpload, kycUploadDto)) // Mixed statuses
                .build();

        CustomerKycResponseDto mixedStatusResponse = CustomerKycResponseDto.builder()
                .identity(customerIdentity)
                .customerCode("CUST001")
                .onboardingStatus("IN_PROGRESS")
                .kycDocuments(List.of(documentWithMixedStatuses))
                .build();

        when(customerKycService.getKycDocuments(eq(customerIdentity)))
                .thenReturn(mixedStatusResponse);

        ResponseEntity<CustomerKycResponseDto> result =
                controller.getKycDocuments(customerIdentity);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(3, result.getBody().getKycDocuments().get(0).getKycUploads().size());

        List<KycUploadResponseDto> uploads = result.getBody().getKycDocuments().get(0).getKycUploads();
        assertEquals(CustomerKycUpload.UploadStatus.PENDING, uploads.get(0).getUploadStatus());
        assertEquals(CustomerKycUpload.UploadStatus.FAILED, uploads.get(1).getUploadStatus());
        assertEquals(CustomerKycUpload.UploadStatus.SUCCESS, uploads.get(2).getUploadStatus());
    }

    @Test
    void testGetKycDocuments_WithDocumentVersions() {
        // Create multiple versions of the same document
        KycUploadResponseDto version1Upload = KycUploadResponseDto.builder()
                .identity(UUID.randomUUID())
                .fileName("doc_v1.jpg")
                .uploadStatus(CustomerKycUpload.UploadStatus.SUCCESS)
                .version(1)
                .uploadDate(Timestamp.valueOf("2024-01-10 09:00:00"))
                .build();

        KycUploadResponseDto version2Upload = KycUploadResponseDto.builder()
                .identity(UUID.randomUUID())
                .fileName("doc_v2.jpg")
                .uploadStatus(CustomerKycUpload.UploadStatus.SUCCESS)
                .version(2)
                .uploadDate(Timestamp.valueOf("2024-01-15 14:30:00"))
                .build();

        KycDocumentResponseDto versionedDocument = KycDocumentResponseDto.builder()
                .identity(UUID.randomUUID())
                .idType(idType)
                .idNumber("VERSIONED123")
                .isVerified(true)
                .isActive(true)
                .kycUploads(List.of(version1Upload, version2Upload))
                .build();

        CustomerKycResponseDto versionedResponse = CustomerKycResponseDto.builder()
                .identity(customerIdentity)
                .customerCode("CUST001")
                .onboardingStatus("COMPLETED")
                .kycDocuments(List.of(versionedDocument))
                .build();

        when(customerKycService.getKycDocuments(eq(customerIdentity)))
                .thenReturn(versionedResponse);

        ResponseEntity<CustomerKycResponseDto> result =
                controller.getKycDocuments(customerIdentity);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        List<KycUploadResponseDto> uploads = result.getBody().getKycDocuments().get(0).getKycUploads();

        assertEquals(2, uploads.size());
        assertEquals(Integer.valueOf(1), uploads.get(0).getVersion());
        assertEquals(Integer.valueOf(2), uploads.get(1).getVersion());
        assertEquals("doc_v1.jpg", uploads.get(0).getFileName());
        assertEquals("doc_v2.jpg", uploads.get(1).getFileName());
    }

    @Test
    void testCreateInitialCustomer_EmptyUploads() {
        // Test document without uploads
        KycDocumentResponseDto documentWithoutUploads = KycDocumentResponseDto.builder()
                .identity(UUID.randomUUID())
                .idType(idType)
                .idNumber("NOUPLOAD123")
                .isVerified(false)
                .isActive(true)
                .kycUploads(List.of()) // Empty uploads list
                .build();

        CustomerKycResponseDto responseWithoutUploads = CustomerKycResponseDto.builder()
                .identity(customerIdentity)
                .customerCode("CUST001")
                .onboardingStatus("PENDING")
                .kycDocuments(List.of(documentWithoutUploads))
                .build();

        when(customerKycService.createInitialCustomer(any(CustomerKycRequestDto.class)))
                .thenReturn(responseWithoutUploads);

        ResponseEntity<CustomerKycResponseDto> result =
                controller.createInitialCustomer(requestDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertTrue(result.getBody().getKycDocuments().get(0).getKycUploads().isEmpty());
        assertEquals("PENDING", result.getBody().getOnboardingStatus());
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
    }

    @Test
    void testCreateInitialCustomer_WithFileUploadDetails() {
        // Test with complete file upload metadata
        KycUploadResponseDto detailedUpload = KycUploadResponseDto.builder()
                .identity(UUID.randomUUID())
                .documentReference("AADHAAR_REF_2024")
                .fileName("aadhaar_verified.jpg")
                .fileType("image/jpeg")
                .filePath("/secure/documents/aadhaar_verified.jpg")
                .uploadStatus(CustomerKycUpload.UploadStatus.SUCCESS)
                .version(1)
                .uploadDate(Timestamp.valueOf("2024-01-20 15:45:00"))
                .build();

        KycDocumentResponseDto detailedDocument = KycDocumentResponseDto.builder()
                .identity(UUID.randomUUID())
                .idType(idType)
                .idNumber("AADHAAR123")
                .placeOfIssue("Mumbai")
                .issuingAuthority("UIDAI")
                .validFrom("2020-01-01")
                .validTo("2030-01-01")
                .isVerified(true)
                .isActive(true)
                .kycUploads(List.of(detailedUpload))
                .build();

        CustomerKycResponseDto detailedResponse = CustomerKycResponseDto.builder()
                .identity(customerIdentity)
                .customerCode("CUST001")
                .firstName("John")
                .lastName("Doe")
                .onboardingStatus("COMPLETED")
                .kycDocuments(List.of(detailedDocument))
                .build();

        when(customerKycService.createInitialCustomer(any(CustomerKycRequestDto.class)))
                .thenReturn(detailedResponse);

        ResponseEntity<CustomerKycResponseDto> result =
                controller.createInitialCustomer(requestDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        KycUploadResponseDto upload = result.getBody().getKycDocuments().get(0).getKycUploads().get(0);
        assertEquals("AADHAAR_REF_2024", upload.getDocumentReference());
        assertEquals("/secure/documents/aadhaar_verified.jpg", upload.getFilePath());
        assertNotNull(upload.getUploadDate());
    }
}