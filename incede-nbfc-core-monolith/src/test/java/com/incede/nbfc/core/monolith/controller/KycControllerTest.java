package com.incede.nbfc.core.monolith.controller;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.client.dto.AadhaarMaskingResponseDto;
import com.incede.nbfc.core.monolith.client.dto.AadhaarOtpResponse;
import com.incede.nbfc.core.monolith.client.dto.AadhaarOtpValidatedResponseDto;
import com.incede.nbfc.core.monolith.client.dto.KycResponseDto;
import com.incede.nbfc.core.monolith.domain.dto.AadhaarMaskingRequestDto;
import com.incede.nbfc.core.monolith.domain.dto.AadhaarOtpRequestDto;
import com.incede.nbfc.core.monolith.domain.dto.AadhaarOtpValidateRequestDto;
import com.incede.nbfc.core.monolith.client.dto.*;
import com.incede.nbfc.core.monolith.domain.dto.*;

import com.incede.nbfc.core.monolith.exception.GlobalExceptionHandler;
import com.incede.nbfc.core.monolith.service.KycService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;


@ContextConfiguration(classes = {KycController.class, GlobalExceptionHandler.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class KycControllerTest {
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private KycController kycController;


    @MockBean
    private KycService kycService;
    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(kycController)
                .setControllerAdvice(new GlobalExceptionHandler()) // if you use @ControllerAdvice
                .build();
    }
    /**
     *  KycController#validateAccount(AadhaarOtpRequestDto)}.
     *
     *
     *
     *  KycController#validateAccount(AadhaarOtpRequestDto)}
     */

    @Test
    void test_givenAadhaarOtpResponse() throws Exception {

        AadhaarOtpRequestDto aadhaarOtpRequestDto = new AadhaarOtpRequestDto();
        aadhaarOtpRequestDto.setAadhaarNumber("123444555");


        AadhaarOtpResponse aadhaarOtpResponse = new AadhaarOtpResponse();
        aadhaarOtpResponse.setDecentroTxnId("42");
        aadhaarOtpResponse.setMessage("Not all who wander are lost");
        aadhaarOtpResponse.setResponseCode("Response Code");
        aadhaarOtpResponse.setResponseKey("Response Key");
        aadhaarOtpResponse.setStatus("Status");

        when(kycService.generateOtp(aadhaarOtpRequestDto))
                .thenReturn(aadhaarOtpResponse);
        String content = new ObjectMapper().writeValueAsString(aadhaarOtpRequestDto);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/ext/ekyc/otp/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        MockMvcBuilders.standaloneSetup(kycController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(
                        "{\"decentroTxnId\":\"42\",\"status\":\"Status\",\"responseCode\":\"Response Code\",\"message\":\"Not all who wander are lost\",\"responseKey\":\"Response Key\"}"
                ));
    }



    /**
     *  KycController#generteaadhaarOtp(AadhaarOtpRequestDto)}.
     *
     *
     *
     *  KycController#generteaadhaarOtp(AadhaarOtpRequestDto)}
     */
    @Test
    void testValidateOtp() throws Exception {

        AadhaarOtpValidateRequestDto  aadhaarOtpValidateRequestDto =new AadhaarOtpValidateRequestDto();
        aadhaarOtpValidateRequestDto.setOtp("12345");
        aadhaarOtpValidateRequestDto.setReferenceId("12345");


        AadhaarOtpValidatedResponseDto.ProofOfAddressDto proofOfAddress = new AadhaarOtpValidatedResponseDto.ProofOfAddressDto();
        proofOfAddress.setCareOf("Care Of");
        proofOfAddress.setCountry("GB");
        proofOfAddress.setDistrict("District");
        proofOfAddress.setHouse("House");
        proofOfAddress.setLandmark("Landmark");
        proofOfAddress.setLocality("Locality");
        proofOfAddress.setPincode("Pincode");
        proofOfAddress.setPostOffice("Post Office");
        proofOfAddress.setState("MD");
        proofOfAddress.setStreet("Street");
        proofOfAddress.setSubDistrict("Sub District");
        proofOfAddress.setVtc("Vtc");

        AadhaarOtpValidatedResponseDto.ProofOfIdentityDto proofOfIdentity = new AadhaarOtpValidatedResponseDto.ProofOfIdentityDto();
        proofOfIdentity.setDob("Dob");
        proofOfIdentity.setGender("Gender");
        proofOfIdentity.setHashedEmail("jane.doe@example.org");
        proofOfIdentity.setMobileNumber("42");
        proofOfIdentity.setName("Name");

        AadhaarOtpValidatedResponseDto.DataDto data = new AadhaarOtpValidatedResponseDto.DataDto();
        data.setAadhaarReferenceNumber("42");
        data.setImage("Image");
        data.setProofOfAddress(proofOfAddress);
        data.setProofOfIdentity(proofOfIdentity);

        AadhaarOtpValidatedResponseDto aadhaarOtpValidatedResponseDto = new AadhaarOtpValidatedResponseDto();
        aadhaarOtpValidatedResponseDto.setData(data);
        aadhaarOtpValidatedResponseDto.setDecentroTxnId("42");
        aadhaarOtpValidatedResponseDto.setMessage("Not all who wander are lost");
        aadhaarOtpValidatedResponseDto.setResponseCode("Response Code");
        aadhaarOtpValidatedResponseDto.setResponseKey("Response Key");
        aadhaarOtpValidatedResponseDto.setStatus("Status");

        when(kycService.validateAaadhaarOtp(aadhaarOtpValidateRequestDto))
                .thenReturn(aadhaarOtpValidatedResponseDto);

        String content = new ObjectMapper().writeValueAsString(aadhaarOtpValidateRequestDto);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/ext/ekyc/otp/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        MockMvcBuilders.standaloneSetup(kycController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string(
                                "{\"decentroTxnId\":\"42\",\"status\":\"Status\",\"responseCode\":\"Response Code\",\"message\":\"Not all who wander are lost\",\"data\":{\"aadhaarReferenceNumber\":\"42\",\"proofOfIdentity\":{\"dob\":\"Dob\",\"hashedEmail\":\"jane.doe@example.org\",\"gender\":\"Gender\",\"name\":\"Name\",\"mobileNumber\":\"42\"},\"proofOfAddress\":{\"careOf\":\"Care Of\",\"country\":\"GB\",\"district\":\"District\",\"house\":\"House\",\"landmark\":\"Landmark\",\"locality\":\"Locality\",\"pincode\":\"Pincode\",\"postOffice\":\"Post Office\",\"state\":\"MD\",\"street\":\"Street\",\"subDistrict\":\"Sub District\",\"vtc\":\"Vtc\"},\"image\":\"Image\"},\"responseKey\":\"Response Key\"}"
                        ));
    }



    @Test
    void testValidateKyc_success() throws Exception {

        KycResponseDto mockResponse = new KycResponseDto();
        mockResponse.setStatus("SUCCESS");
        mockResponse.setMessage("KYC validated successfully");


        when(kycService.validateKyc("4288888888", 2, "1983-05-30"))
                .thenReturn((KycResponseDto) mockResponse);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/ext/ekyc/validate/kyc")
                .param("idNumber", "4288888888")
                .param("kycId", "2")   // Integer as String in request param
                .param("dob", "1983-05-30");


        MockMvcBuilders.standaloneSetup(kycController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("KYC validated successfully"));


        verify(kycService).validateKyc("4288888888", 2, "1983-05-30");
    }



    /**
     * Test {@link KycController#aadhaarMasking(AadhaarMaskingRequestDto)}.
     *
     *  {@link KycController#aadhaarMasking(AadhaarMaskingRequestDto)}
     */
    @Test
    @DisplayName("Test aadhaarMasking(AadhaarMaskingRequestDto)")
    void testAadhaarMasking() throws Exception
    {

        when(kycService.maskAadhaarImage(Mockito.<AadhaarMaskingRequestDto>any()))
                .thenReturn(
                        new AadhaarMaskingResponseDto(
                                "01234567-89AB-CDEF-FEDC-BA9876543210",
                                "Msg",
                                true,
                                true,
                                10,
                                "Utc Time Stamp",
                                "Response Image"));

        AadhaarMaskingRequestDto aadhaarMaskingRequestDto = new AadhaarMaskingRequestDto();
        aadhaarMaskingRequestDto.setAadhaarImage("Aadhaar Image");
        aadhaarMaskingRequestDto.setDetectionBasedMasking(true);
        aadhaarMaskingRequestDto.setImageFormat("Image Format");
        aadhaarMaskingRequestDto.setImageUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        aadhaarMaskingRequestDto.setPdfOut(true);
        String content = new ObjectMapper().writeValueAsString(aadhaarMaskingRequestDto);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/ext/ekyc/masking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);
        MockMvcBuilders.standaloneSetup(kycController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content()
                                .string(
                                        "{\"image_uuid\":\"01234567-89AB-CDEF-FEDC-BA9876543210\",\"msg\":\"Msg\",\"aadhaar_detected\":true,\"aadhaar"
                                                + "_masked\":true,\"number_of_pages\":10,\"utc_time_stamp\":\"Utc Time Stamp\",\"response_image\":\"Response"
                                                + " Image\"}"));
    }

    /**
     *
     * @throws Exception   when th expected request  and response doesnt match
     * it will mock controller  with mockMvc Builder
     */
    @Test
    void testAadhaarMasking_givenException() throws Exception {

        when(kycService.maskAadhaarImage(Mockito.<AadhaarMaskingRequestDto>any()))
                .thenThrow(new RuntimeException("Masking failed"));

        AadhaarMaskingRequestDto aadhaarMaskingRequestDto = new AadhaarMaskingRequestDto();
        aadhaarMaskingRequestDto.setAadhaarImage("Aadhaar Image");
        aadhaarMaskingRequestDto.setDetectionBasedMasking(true);
        aadhaarMaskingRequestDto.setImageFormat("Image Format");
        aadhaarMaskingRequestDto.setImageUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        aadhaarMaskingRequestDto.setPdfOut(true);

        String content = new ObjectMapper().writeValueAsString(aadhaarMaskingRequestDto);

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/ext/ekyc/masking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        MockMvcBuilders.standaloneSetup(kycController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
             ;
    }

    /**
     *
     * @throws Exception
     */
    @Test
    void testGetNameMatchingPercentage() throws Exception {
        NameMatchResponseDto responseDto = new NameMatchResponseDto();
        responseDto.setDecentroTxnId("TXN123");
        responseDto.setStatus("SUCCESS");
        responseDto.setResponseCode("S0000");
        responseDto.setMessage("Name matched successfully");
        responseDto.setResponseKey("name_match_success");
        when(kycService.getNameMatching(Mockito.any(NameMatchRequestDto.class)))
                .thenReturn(responseDto);
        NameMatchRequestDto nameMatchRequestDto = new NameMatchRequestDto();
        nameMatchRequestDto.setMatchThreshold(new NameMatchRequestDto.MatchThreshold("Textual", "80"));
        nameMatchRequestDto.setName1("Name1");
        nameMatchRequestDto.setName2("Name2");
        nameMatchRequestDto.setReferenceId("42");
        String content = new ObjectMapper().writeValueAsString(nameMatchRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/ext/ekyc/name/matching")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(kycController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.decentroTxnId").value("TXN123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseCode").value("S0000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Name matched successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseKey").value("name_match_success"));
    }

    /**
     *
     * worst test case for the name matching
     * @throws Exception when the assertion fails
     */
    @Test
    @DisplayName("Negative Test - getNameMatchingPercentage should handle exception")
    void testGetNameMatchingPercentage_exceptionCase() throws Exception {
        when(kycService.getNameMatching(Mockito.any(NameMatchRequestDto.class)))
                .thenThrow(new RuntimeException("Something went wrong in service"));
        NameMatchRequestDto nameMatchRequestDto = new NameMatchRequestDto();
        nameMatchRequestDto.setName1("Name1");
        nameMatchRequestDto.setName2("Name2");
        nameMatchRequestDto.setReferenceId("42");
        String content = new ObjectMapper().writeValueAsString(nameMatchRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/ext/ekyc/name/matching")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(kycController)
                .setControllerAdvice(globalExceptionHandler) // important for exception mapping
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
              ;
    }
    /**
     * Test {@link KycController#getUpiMatching(String)}.
     *
     * <p>Method under test: {@link KycController#getUpiMatching(String)}
     */
    @Test
    void testGetUpiMatching() throws Exception {

        when(kycService.getUpiIdValidationResponse(Mockito.<String>any()))
                .thenReturn(new UpiAccountDetailsResponseDto());
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/ext/ekyc/upi/matching").param("upiID", "foo");


        MockMvcBuilders.standaloneSetup(kycController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content()
                                .string(
                                        "{\"decentroTxnId\":null,\"status\":null,\"responseCode\":null,\"message\":null,\"responseKey\":null}"));
    }
}