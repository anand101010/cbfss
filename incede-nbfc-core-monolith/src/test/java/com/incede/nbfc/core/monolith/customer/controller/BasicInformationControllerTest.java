package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.BasicInformationRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.BasicInformationResponseDto;
import com.incede.nbfc.core.monolith.customer.service.BasicInformationService;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BasicInformationControllerTest {

    @Mock
    private BasicInformationService basicInformationService;

    @InjectMocks
    private BasicInformationController basicInformationController;

    private UUID customerId;
    private BasicInformationRequestDto requestDto;
    private BasicInformationResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerId = UUID.randomUUID();

        UUID gender = UUID.randomUUID();
        UUID maritalStatus = UUID.randomUUID();
        UUID taxCategory = UUID.randomUUID();
        UUID salutation = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        UUID occupation = UUID.randomUUID();
        UUID customerStatus = UUID.randomUUID();
        UUID guardianCustomerId = UUID.randomUUID();

        // Minimal request DTO
        requestDto = BasicInformationRequestDto.builder()
                .salutation(salutation)
                .branchId(branchId)
                .firstName("John")
                .middleName("M")
                .lastName("Doe")
                .aadharName("John Aadhar")
                .gender(gender)
                .dob(LocalDate.of(1990, 1, 1))
                .maritalStatus(maritalStatus)
                .taxCategory(taxCategory)
                .occupation(occupation)
                .employer("Incede Ltd")
                .annualIncome(BigDecimal.valueOf(500000))
                .isBusiness(true)
                .isFirm(false)
                .fatherName("Mr. Doe")
                .motherName("Mrs. Doe")
                .spouseName("Jane")
                .mobileNumber("9876543210")
                .aadharVault("vault-123")
                .otpVerified(true)
                .isMinor(false)
                .customerStatus(customerStatus)
                .guardianCustomerId(guardianCustomerId)
                .build();

        // Minimal response DTO
        BasicInformationResponseDto.Basic basic = BasicInformationResponseDto.Basic.builder()
                .firstName("John")
                .lastName("Doe")
                .aadharName("John Aadhar")
                .dob(LocalDate.of(1990, 1, 1))
                .gender(gender)
                .maritalStatus(maritalStatus)
                .taxCategory(taxCategory)
                .salutation(salutation)
                .branchId(branchId)
                .middleName("M")
                .crmReferenceId("crm-001")
                .occupation(occupation)
                .employer("Incede Ltd")
                .annualIncome(BigDecimal.valueOf(500000))
                .isBusiness(true)
                .isFirm(false)
                .guardianCustomerId(guardianCustomerId)
                .spouseName("Jane")
                .fatherName("Mr. Doe")
                .motherName("Mrs. Doe")
                .isMinor(false)
                .customerStatus(customerStatus)
                .mobileNumber("9876543210")
                .aadharVaultId("vault-123")
                .otpVerified(true)
                .build();

        responseDto = BasicInformationResponseDto.builder()
                .identity(customerId)
                .customerCode("CUST-001")
                .status("ACTIVE")
                .basic(basic)
                .build();
    }

    @Test
    void testCreateBasicInfo_Success() {
        when(basicInformationService.saveBasicInformation(any()))
                .thenReturn(responseDto);

        BasicInformationResponseDto response = basicInformationController.createBasicInfo(requestDto).getBody();

        assertEquals(responseDto, response);
        assertEquals("John", response.getBasic().getFirstName());
    }

    @Test
    void testUpdateBasicInfo_Success() {
        when(basicInformationService.updateBasicInformation(any(UUID.class), any()))
                .thenReturn(responseDto);

        BasicInformationResponseDto response = basicInformationController.updateBasicInfo(customerId, requestDto).getBody();

        assertEquals(responseDto, response);
        assertEquals("Doe", response.getBasic().getLastName());
    }

    @Test
    void testGetBasicInfoByUuid_Success() {
        when(basicInformationService.getBasicInformationByUuid(customerId))
                .thenReturn(responseDto);

        BasicInformationResponseDto response = basicInformationController.getBasicInfoByUuid(customerId).getBody();

        assertEquals(responseDto, response);
        assertEquals("John Aadhar", response.getBasic().getAadharName());
    }

    @Test
    void testCreateBasicInfo_WhenNotFound_ThrowsException() {
        when(basicInformationService.saveBasicInformation(any()))
                .thenThrow(new ResourceNotFoundException("Customer", customerId.toString()));

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                basicInformationController.createBasicInfo(requestDto));

        assertTrue(exception.getMessage().contains("Customer"));
    }
}
