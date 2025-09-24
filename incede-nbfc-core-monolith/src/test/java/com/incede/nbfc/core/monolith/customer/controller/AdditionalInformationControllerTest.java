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
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.eq;
//import static org.mockito.Mockito.when;
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
//
//        responseDto = CustomerAdditionalInfoResponseDto.builder()
//                .identity(customerId)
//                .customerCode("CUST123")
//                .status("ACTIVE")
//                .additional(CustomerAdditionalInfoResponseDto.AdditionalInfoDto.builder()
//                        .employment(CustomerEmploymentDto.builder()
//                                .employer("ABC Corp")
//                                .occupationId(1)
//                                .designationId(1)
//                                .incomeSourceId(1)
//                                .monthlySalary(BigDecimal.valueOf(20000))
//                                .annualIncome(BigDecimal.valueOf(2000000))
//                                .build())
//                        .referrals(CustomerReferralDto.builder()
//                                .referralSourceId(10)
//                                .build())
//                        .pep(CustomerPepDto.builder()
//                                .status("active")
//                                .categoryId(5)
//                                .build())
//                        .profileExtra(CustomerProfileExtraDto.builder()
//                                .educationLevelId(3)
//                                .purposeId(4)
//                                .build())
//                        .assets(CustomerAssetDto.builder()
//                                .assetId(100)
//                                .description("House")
//                                .build())
//                        .additionalInfoCustomerDto(AdditionalInfoCustomerDto.builder()
//                                .nationality(1)
//                                .preferredLanguageId(2)
//                                .residentialStatusId(3)
//                                .build())
//                        .build())
//                .build();
//
//
//        requestDto = CustomerAdditionalInfoRequestDto.builder()
//                .additional(AdditionalInfoDto.builder()
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
//    }
//}
