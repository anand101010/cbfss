//package com.incede.nbfc.core.monolith.customer.controller;
//
//import com.incede.nbfc.core.monolith.customer.dto.*;
//import com.incede.nbfc.core.monolith.customer.service.CustomerAdditionalInfoService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.math.BigDecimal;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class CustomerAdditionalInfoControllerTest {
//
//    @Mock
//    private CustomerAdditionalInfoService additionalInfoService;
//
//    @InjectMocks
//    private CustomerAdditionalInfoController controller;
//
//    private UUID customerId;
//    private CustomerAdditionalInfoResponseDto responseDto;
//    private CustomerAdditionalInfoRequestDto requestDto;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        customerId = UUID.randomUUID();
//
//        responseDto = CustomerAdditionalInfoResponseDto.builder()
//                .identity(customerId)
//                .customerCode("CUST123")
//                .status("ACTIVE")
//                .additional(CustomerAdditionalInfoResponseDto.AdditionalInfoDto.builder()
//                        .employment(CustomerEmploymentDto.builder()
//                                .employer("ABC Corp")
//                                .occupationId(UUID.randomUUID())
//                                .designationId(UUID.randomUUID())
//                                .incomeSourceId(UUID.randomUUID())
//                                .monthlySalary(BigDecimal.valueOf(20000))
//                                .annualIncome(BigDecimal.valueOf(2000000))
//                                .build())
//                        .referrals(CustomerReferralDto.builder()
//                                .referralSourceId(UUID.randomUUID())
//                                .build())
//
//                        .profileExtra(CustomerProfileExtraDto.builder()
//                                .educationLevelId(UUID.randomUUID())
//                                .purposeId(UUID.randomUUID())
//                                .build())
//                        .assets(CustomerAssetDto.builder()
//                                .assetTypeId(UUID.randomUUID()) // FIXED: added UUID.randomUUID()
//                                .build())
//                        .additionalInfoCustomerDto(AdditionalInfoCustomerDto.builder()
//                                .nationality(UUID.randomUUID())
//                                .preferredLanguageId(UUID.randomUUID())
//                                .residentialStatusId(UUID.randomUUID())
//                                .build())
//                        .build())
//                .build();
//
//        requestDto = CustomerAdditionalInfoRequestDto.builder()
//                .additional(CustomerAdditionalInfoRequestDto.AdditionalInfoDto.builder()
//                        .employment(CustomerEmploymentDto.builder()
//                                .employer("ABC Corp")
//                                .build())
//                        .build())
//                .build();
//    }
//
//    @Test
//    void testUpdateAdditionalInfo() {
//        when(additionalInfoService.saveAdditionalInfo(eq(customerId), any(CustomerAdditionalInfoRequestDto.class)))
//                .thenReturn(responseDto);
//
//        ResponseEntity<CustomerAdditionalInfoResponseDto> result =
//                controller.updateAdditionalInfo(customerId, requestDto);
//
//        assertEquals(HttpStatus.CREATED, result.getStatusCode());
//        assertEquals(responseDto, result.getBody());
//
//        verify(additionalInfoService, times(1))
//                .saveAdditionalInfo(eq(customerId), eq(requestDto));
//    }
//
//    @Test
//    void testGetAdditionalInfo() {
//        when(additionalInfoService.getAdditionalInfo(eq(customerId)))
//                .thenReturn(responseDto);
//
//        ResponseEntity<CustomerAdditionalInfoResponseDto> result =
//                controller.getAdditionalInfo(customerId);
//
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals(responseDto, result.getBody());
//
//        verify(additionalInfoService, times(1))
//                .getAdditionalInfo(customerId);
//    }
//
//    @Test
//    void testUpdateAdditionalInfo_whenServiceThrowsException() {
//        when(additionalInfoService.saveAdditionalInfo(eq(customerId), any(CustomerAdditionalInfoRequestDto.class)))
//                .thenThrow(new RuntimeException("Service error"));
//
//        try {
//            controller.updateAdditionalInfo(customerId, requestDto);
//        } catch (Exception e) {
//            assertEquals("Service error", e.getMessage());
//        }
//
//        verify(additionalInfoService, times(1))
//                .saveAdditionalInfo(eq(customerId), eq(requestDto));
//    }
//
//    @Test
//    void testGetAdditionalInfo_whenServiceThrowsException() {
//        when(additionalInfoService.getAdditionalInfo(eq(customerId)))
//                .thenThrow(new RuntimeException("Service error"));
//
//        try {
//            controller.getAdditionalInfo(customerId);
//        } catch (Exception e) {
//            assertEquals("Service error", e.getMessage());
//        }
//
//        verify(additionalInfoService, times(1))
//                .getAdditionalInfo(customerId);
//    }
//}
