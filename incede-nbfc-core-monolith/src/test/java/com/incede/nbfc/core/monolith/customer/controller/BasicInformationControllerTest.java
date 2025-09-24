//package com.incede.nbfc.core.monolith.customer.controller;
//
//import com.incede.nbfc.core.monolith.customer.dto.BasicInformationRequestDto;
//import com.incede.nbfc.core.monolith.customer.dto.BasicInformationResponseDto;
//import com.incede.nbfc.core.monolith.customer.service.BasicInformationService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.Month;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.when;
//
//class BasicInformationControllerTest {
//
//    @Mock
//    BasicInformationService customerOnboardingService;
//
//    @InjectMocks
//    BasicInformationController basicInformationController;
//
//    private BasicInformationResponseDto responseDto;
//    private BasicInformationRequestDto requestDto;
//    private UUID identity;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        identity = new UUID(0L, 0L);
//        BasicInformationResponseDto.Basic basic = new BasicInformationResponseDto.Basic(
//                "firstName","lastName","aadharName",
//                LocalDate.of(2025, Month.SEPTEMBER, 2),
//                0,0,0,0,0,"middleName","crmReferenceId",0,
//                "employer",BigDecimal.ZERO,0,true,true,0,
//                "spouseName","fatherName","motherName",false,
//                0,0,"9999999999","ui1213",true
//        );
//        responseDto = new BasicInformationResponseDto(identity,"customerCode","status",basic);
//        requestDto = BasicInformationRequestDto.builder()
//                .tenantId(0).salutation(0).branchId(0)
//                .firstName("firstName").middleName("middleName").lastName("lastName")
//                .gender(0).dob(LocalDate.of(2025, Month.SEPTEMBER, 2))
//                .maritalStatus(0).taxCategory(0).crmReferenceId("crmReferenceId")
//                .occupation(0).employer("employer").annualIncome(BigDecimal.ZERO)
//                .customerListTypeId(0).isBusiness(true).isFirm(true)
//                .guardianCustomerId(1).spouseName("spouseName").fatherName("fatherName").motherName("motherName")
//                .isMinor(false).customerStatus(0).build();
//    }
//
//
//    @Test
//    void testCreateBasicInfo() {
//        when(customerOnboardingService.saveBasicInformation(any(BasicInformationRequestDto.class)))
//                .thenReturn(responseDto);
//
//        ResponseEntity<BasicInformationResponseDto> result =
//                basicInformationController.createBasicInfo(requestDto);
//
//        assertEquals(HttpStatus.CREATED, result.getStatusCode());
//        assertEquals(responseDto, result.getBody());
//    }
//
//    @Test
//    void testUpdateBasicInfo() {
//        when(customerOnboardingService.updateBasicInformation(any(UUID.class), any(BasicInformationRequestDto.class)))
//                .thenReturn(responseDto);
//
//        ResponseEntity<BasicInformationResponseDto> result =
//                basicInformationController.updateBasicInfo(identity, requestDto);
//
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals(responseDto, result.getBody());
//    }
//
//    @Test
//    void testGetBasicInfoByUuid() {
//        when(customerOnboardingService.getBasicInformationByUuid(any(UUID.class)))
//                .thenReturn(responseDto);
//
//        ResponseEntity<BasicInformationResponseDto> result =
//                basicInformationController.getBasicInfoByUuid(identity);
//
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals(responseDto, result.getBody());
//    }
//}
