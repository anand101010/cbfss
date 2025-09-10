package com.incede.nbfc.core.monolith.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.incede.nbfc.core.monolith.client.AadhaarMaskingClient;
import com.incede.nbfc.core.monolith.client.KycClient;
import com.incede.nbfc.core.monolith.client.UpiIdValidationClient;
import com.incede.nbfc.core.monolith.client.dto.*;
import com.incede.nbfc.core.monolith.domain.dto.AadhaarMaskingRequestDto;
import com.incede.nbfc.core.monolith.domain.dto.AadhaarOtpRequestDto;
import com.incede.nbfc.core.monolith.domain.dto.AadhaarOtpValidateRequestDto;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.domain.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ContextConfiguration(classes = {KycService.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class KycServiceTest {
    @MockBean
    private KycClient kycClient;
    @Autowired
    private KycService kycService;
    @MockBean
    private AadhaarMaskingClient aadhaarMaskingClient;

    @MockBean
    private UpiIdValidationClient upiIdValidationClient;
    /**
     * Test {@link KycService (AadhaarNumber)}.
     *
     *
     *  Then return {@link AadhaarOtpResponse} (default constructor).
     * {@link KycService   generateOtp(AadhaarNumber)}
     */
    @Test
    void testGenerateOtp_thenReturnAadhaarOtpResponse() {
        AadhaarOtpResponse aadhaarOtpResponse = new AadhaarOtpResponse();
        aadhaarOtpResponse.setDecentroTxnId("42");
        aadhaarOtpResponse.setMessage("Not all who wander are lost");
        aadhaarOtpResponse.setResponseCode("Response Code");
        aadhaarOtpResponse.setResponseKey("Response Key");
        aadhaarOtpResponse.setStatus("Status");
        when(kycClient.generateAadhaarOtp(Mockito.<AadhaarOtpRequestDto>any()))
                .thenReturn(aadhaarOtpResponse);
        AadhaarOtpResponse actualGenerateOtpResult =
                kycService.generateOtp("42");
        verify(kycClient).generateAadhaarOtp(isA(AadhaarOtpRequestDto.class));
        assertSame(aadhaarOtpResponse, actualGenerateOtpResult);
    }

    /**
     * Test  KycService#generateOtp(AadhaarOtpRequestDto)}.
     *   Then throw {@link RuntimeException}.
     *
     */
    @Test
    void testGenerateOtp_thenThrowRuntimeException() {

        when(kycClient.generateAadhaarOtp(Mockito.<AadhaarOtpRequestDto>any()))
                .thenThrow(new RuntimeException());
        assertThrows(
                RuntimeException.class,
                () ->
                        kycService.generateOtp(
                             "42"));
        verify(kycClient).generateAadhaarOtp(isA(AadhaarOtpRequestDto.class));
    }

    /**
     * Test {@link KycService validateAaadhaarOtp(AadhaarOtpValidateRequestDto)}.
     *   Then return {@link AadhaarOtpValidatedResponseDto} (default constructor).
     *
     */
    @Test
    void testValidateAaadhaarOtp_thenReturnAadhaarOtpValidatedResponseDto() {
      
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
        AadhaarOtpValidatedResponseDto aadhaarOtpValidatedResponseDto =
                new AadhaarOtpValidatedResponseDto();
        aadhaarOtpValidatedResponseDto.setData(data);
        aadhaarOtpValidatedResponseDto.setDecentroTxnId("42");
        aadhaarOtpValidatedResponseDto.setMessage("Not all who wander are lost");
        aadhaarOtpValidatedResponseDto.setResponseCode("Response Code");
        aadhaarOtpValidatedResponseDto.setResponseKey("Response Key");
        aadhaarOtpValidatedResponseDto.setStatus("Status");
        when(kycClient.validateAadhaarOtp(Mockito.<AadhaarOtpValidateRequestDto>any()))
                .thenReturn(aadhaarOtpValidatedResponseDto);
        AadhaarOtpValidateRequestDto AadhaarOtpValidateRequestDto = new AadhaarOtpValidateRequestDto();
        AadhaarOtpValidateRequestDto.setConsent(true);
        AadhaarOtpValidateRequestDto.setInitiationTransactionId("Initiation transaction id");
        AadhaarOtpValidateRequestDto.setOtp("Otp");
        AadhaarOtpValidateRequestDto.setPurpose("Purpose");
        AadhaarOtpValidateRequestDto.setReferenceId("Reference id");
        AadhaarOtpValidatedResponseDto actualValidateAaadhaarOtpResult =
                kycService.validateAaadhaarOtp("12233444444","22222222222");
        verify(kycClient).validateAadhaarOtp(isA(AadhaarOtpValidateRequestDto.class));
        assertSame(aadhaarOtpValidatedResponseDto, actualValidateAaadhaarOtpResult);
    }

    /**
     * Test {@link KycService# validateAaadhaarOtp(AadhaarOtpValidateRequestDto)}.
     *    {@link RuntimeException}.
     */
    @Test
    void testValidateAaadhaarOtp_thenThrowRuntimeException() {
        when(kycClient.validateAadhaarOtp(Mockito.<AadhaarOtpValidateRequestDto>any()))
                .thenThrow(new RuntimeException());
        AadhaarOtpValidateRequestDto AadhaarOtpValidateRequestDto = new AadhaarOtpValidateRequestDto();
        AadhaarOtpValidateRequestDto.setConsent(true);
        AadhaarOtpValidateRequestDto.setInitiationTransactionId("Initiation transaction id");
        AadhaarOtpValidateRequestDto.setOtp("Otp");
        AadhaarOtpValidateRequestDto.setPurpose("Purpose");
        AadhaarOtpValidateRequestDto.setReferenceId("Reference id");
        assertThrows(
                RuntimeException.class, () -> kycService.validateAaadhaarOtp("123456789","23456789"));
        verify(kycClient).validateAadhaarOtp(isA(AadhaarOtpValidateRequestDto.class));
    }

    /**
     * Test case for validating KYC when Driving License is provided without DOB.
     *
     * Expectation:
     * - Since DOB is mandatory for Driving License (idType = 3),
     *   the service should throw a BusinessException.
     * - The KycClient should not be called because validation fails early.
     */
    @Test
    void testValidateKyc_DrivingLicenseWithoutDob_ShouldThrowException() {
        String idNumber = "DL12345";
        Integer kycId = 3; // Assuming 3 = DRIVING_LICENSE
        String dob = null;

        assertThrows(BusinessException.class,
                () -> kycService.validateKyc(idNumber, kycId, dob));


        verifyNoInteractions(kycClient);
    }

    /**
<<<<<<< HEAD
     * Test case for validating KYC when PAN is provided without DOB.
     *
     * Expectation:
     * - Since DOB is NOT required for PAN (idType = 2),
     *   the service should allow validation.
     * - The KycClient should be called with the request.
     * - The response should not be null.
     */
    @Test
    void testValidateKyc_PanWithoutDob_ShouldPass() {
        String idNumber = "ABCDE1234F";
        Integer kycId = 2; // Assuming 2 = PAN
        String dob = null;

        // Mock KycClient response
        KycResponseDto mockResponse = new KycResponseDto();
        when(kycClient.validateKyc(Mockito.any(KycRequestDto.class)))
                .thenReturn((KycResponseDto) mockResponse);

        KycResponseDto response = kycService.validateKyc(idNumber, kycId, dob);

        assertNotNull(response);
        verify(kycClient).validateKyc(Mockito.any(KycRequestDto.class));
    }

    /**
     * Test case for validating KYC when Driving License is provided with DOB.
     *
     * Expectation:
     * - Since DOB is required for Driving License and provided,
     *   validation should proceed successfully.
     * - The KycClient should be called.
     * - The response should not be null.
     */
    @Test
    void testValidateKyc_DrivingLicenseWithDob_ShouldPass() {
        String idNumber = "DL12345";
        Integer kycId = 3; // Assuming 3 = DRIVING_LICENSE
        String dob = "1990-01-01";

        // Mock KycClient response
        KycResponseDto mockResponse = new KycResponseDto();
        when(kycClient.validateKyc(Mockito.any(KycRequestDto.class)))
                .thenReturn((KycResponseDto) mockResponse);

        KycResponseDto response = kycService.validateKyc(idNumber, kycId, dob);

        assertNotNull(response);
        verify(kycClient).validateKyc(Mockito.any(KycRequestDto.class));
    }






    /**
     * Test {@link KycService#maskAadhaarImage(AadhaarMaskingRequestDto)}.

=======
     * Test KycService#maskAadhaarImage(AadhaarMaskingRequestDto)}.
>>>>>>> 3aa82c30de339cad0caec4766d993eccc78f4fd7
     *   Then return {@link AadhaarMaskingResponseDto} (default constructor).
     *
     */
    @Test
    void testMaskAadhaarImage() {
        AadhaarMaskingResponseDto aadhaarMaskingResponseDto =
                new AadhaarMaskingResponseDto(
                        "01234567-89AB-CDEF-FEDC-BA9876543210",
                        "Msg",
                        true,
                        true,
                        10,
                        "Utc Time Stamp",
                        "Response Image");

        when(aadhaarMaskingClient.generateAadhaarMasked(Mockito.<AadhaarMaskingRequestDto>any()))
                .thenReturn(aadhaarMaskingResponseDto);
        AadhaarMaskingRequestDto AadhaarMaskingRequestDto =
                new AadhaarMaskingRequestDto(
                        "Aadhaar Image", "-", "01234567-89AB-CDEF-FEDC-BA9876543210", true, true);
        AadhaarMaskingResponseDto actualMaskAadhaarImageResult =
                kycService.maskAadhaarImage(AadhaarMaskingRequestDto);
        verify(aadhaarMaskingClient).generateAadhaarMasked(isA(AadhaarMaskingRequestDto.class));
        assertFalse(AadhaarMaskingRequestDto.isDetectionBasedMasking());
        assertSame(aadhaarMaskingResponseDto, actualMaskAadhaarImageResult);
    }

    /**
     * Test KycService#maskAadhaarImage(AadhaarMaskingRequestDto)}.
     *
     *   Then throw {@link RuntimeException}.
     */
    @Test
    void testMaskAadhaarImage_thenThrowRuntimeException()
    {
        when(aadhaarMaskingClient.generateAadhaarMasked(Mockito.<AadhaarMaskingRequestDto>any()))
                .thenThrow(new RuntimeException());
        assertThrows(
                RuntimeException.class,
                () ->
                        kycService.maskAadhaarImage(
                                new AadhaarMaskingRequestDto(
                                        "Aadhaar Image",
                                        "Image Format",
                                        "01234567-89AB-CDEF-FEDC-BA9876543210",
                                        true,
                                        true)));
        verify(aadhaarMaskingClient).generateAadhaarMasked(isA(AadhaarMaskingRequestDto.class));
    }


    /**
     * Test  KycService#getNameMatching(NameMatchRequestDto)}.
     *  KycService#getNameMatching(NameMatchRequestDto)}
     */
    @Test
    void testGetNameMatching() {

        NameMatchResponseDto nameMatchResponseDto = new NameMatchResponseDto();
        when(kycClient.getNameMatchingpercentage(Mockito.<NameMatchRequestDto>any()))
                .thenReturn(nameMatchResponseDto);
        NameMatchRequestDto NameMatchRequestDto = new NameMatchRequestDto();
        NameMatchResponseDto actualNameMatching = kycService.getNameMatching(NameMatchRequestDto);
        verify(kycClient).getNameMatchingpercentage(isA(NameMatchRequestDto.class));
        NameMatchRequestDto.MatchThreshold matchThreshold = NameMatchRequestDto.getMatchThreshold();
        assertEquals("100", matchThreshold.getPhonetic());
        assertEquals("100", matchThreshold.getTextual());
        assertSame(nameMatchResponseDto, actualNameMatching);
    }

    /**
     * Test {@link KycService#getNameMatching(NameMatchRequestDto)}.
     * KycService#getNameMatching(NameMatchRequestDto)}
     */
    @Test
    void testGetNameMatching_whenExceptionOccured() {
        when(kycClient.getNameMatchingpercentage(Mockito.<NameMatchRequestDto>any()))
                .thenThrow(new RuntimeException());
        assertThrows(
                RuntimeException.class, () -> kycService.getNameMatching(new NameMatchRequestDto()));
        verify(kycClient).getNameMatchingpercentage(isA(NameMatchRequestDto.class));
    }



    /**
     * Test {@link KycService#getUpiIdValidationResponse(String)}.
     *
     * <ul>
     *   <li>Then return {@link UpiAccountDetailsResponseDto#UpiAccountDetailsResponseDto()}.
     * </ul>
     *
     * <p>Method under test: {@link KycService#getUpiIdValidationResponse(String)}
     */
    @Test
    void testGetUpiIdValidationResponse_thenReturnUpiAccountDetailsResponseDto() {

        UpiAccountDetailsResponseDto upiAccountDetailsResponseDto = new UpiAccountDetailsResponseDto();
        when(upiIdValidationClient.getValidatedUpiIdDetails(Mockito.<UpiAccountDetailsRequestDto>any()))
                .thenReturn(upiAccountDetailsResponseDto);

        UpiAccountDetailsResponseDto actualUpiIdValidationResponse =
                kycService.getUpiIdValidationResponse("42");

        verify(upiIdValidationClient).getValidatedUpiIdDetails(isA(UpiAccountDetailsRequestDto.class));
        assertSame(upiAccountDetailsResponseDto, actualUpiIdValidationResponse);
    }

    /**
     * Test {@link KycService#getUpiIdValidationResponse(String)}.
     *
     * <ul>
     *   <li>Then throw {@link RuntimeException}.
     * </ul>
     *
     * <p>Method under test: {@link KycService#getUpiIdValidationResponse(String)}
     */
    @Test
    void testGetUpiIdValidationResponse_thenThrowRuntimeException() {

        when(upiIdValidationClient.getValidatedUpiIdDetails(Mockito.<UpiAccountDetailsRequestDto>any()))
                .thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> kycService.getUpiIdValidationResponse("42"));
        verify(upiIdValidationClient).getValidatedUpiIdDetails(isA(UpiAccountDetailsRequestDto.class));
    }
}


