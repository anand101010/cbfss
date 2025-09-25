package com.incede.nbfc.core.monolith.client;
import com.incede.nbfc.core.monolith.client.dto.AadhaarOtpResponse;
import com.incede.nbfc.core.monolith.client.dto.AadhaarOtpValidatedResponseDto;
import com.incede.nbfc.core.monolith.client.dto.KycResponseDto;
import com.incede.nbfc.core.monolith.client.fallback.CustomErrorDecoder;
import com.incede.nbfc.core.monolith.client.fallback.FallBackHelper;
import com.incede.nbfc.core.monolith.config.interceptors.DecentroClientInterceptorConfig;
import com.incede.nbfc.core.monolith.domain.dto.*;
import com.incede.nbfc.core.monolith.client.dto.KycRequestDto;
import com.incede.nbfc.core.monolith.client.dto.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * these feign interface will handle neccessary kyc validation details under decentro vendor
 */

@FeignClient(name = "kycService", url = "${decentro.api.url}", configuration = DecentroClientInterceptorConfig.class)
public interface KycClient
{
    /**
     *
     * @param aadhaarOtpRequestDto AadhaarOtpRequestDto include the details  of Customer Aadhar number
     * @return AadhaarOtpResponse  includes the details of OTP
     */

    @PostMapping("${decentro.api.aadhaar-otp-path}")
    @CircuitBreaker(name = "post-apis", fallbackMethod = "getOtpGenertionFallback")
    AadhaarOtpResponse generateAadhaarOtp(@RequestBody AadhaarOtpRequestDto aadhaarOtpRequestDto);

    /**
     *
     * @paramrequest aadhaarOtpValidateRequestDto  will have  the neccessary details with OTP
     * @return AadhaarOtpValidatedResponseDto that will have entire data for the Aadhaar number
     */

    @PostMapping("${decentro.api.aadhaar-otp-validation-path}")
    @CircuitBreaker(name = "post-api", fallbackMethod = "getOtpValidationFallback")
    AadhaarOtpValidatedResponseDto validateAadhaarOtp(@RequestBody AadhaarOtpValidateRequestDto aadhaarOtpValidateRequestDto);


    /**
     *
     * @param nameMatchRequestDto  it will have the same payload as from the decentro vendor and  have the both matching name
     * @return NameMatchResponseDto it will retrun the name matching percentage as 100% if it match
     */
    @PostMapping("${decentro.api.name-match}")
    @CircuitBreaker(name = "post-api", fallbackMethod = "getNameMatchFallBack")
    NameMatchResponseDto getNameMatchingpercentage(@RequestBody NameMatchRequestDto nameMatchRequestDto);

    /**
     *
     * @param throwable used to catch the error from the errordecoder
     * @return AadhaarOtpResponse with return type  AadhaarOtpResponse  that will customise response . if it reach the fallback
     */
    default AadhaarOtpResponse getOtpGenertionFallback(Throwable throwable, CustomErrorDecoder exe)
    {
        return FallBackHelper.FallbackResponse( new AadhaarOtpResponse(),throwable);
    }

    /**
     *
     * @param throwable  used to catch the error from the errordecoder
     * @return  AadhaarOtpValidatedResponseDto  that will customise response . if it reach the fallback
     */

    default AadhaarOtpValidatedResponseDto getOtpValidationFallback(Throwable throwable)
    {
        return FallBackHelper.FallbackResponse( new AadhaarOtpValidatedResponseDto(),throwable);
    }



    /**
     * This API will validate the KYC details of a customer using Decentro service.
     *
     * @param kycrequestdto KycRequestDto object that contains the necessary KYC details
     * @return Map<String, Object> a response map that includes the status and details of KYC validation
     */


        @PostMapping("${decentro.api.kyc-validate}")
        @CircuitBreaker(name = "post-api", fallbackMethod = "validateKycFallback")
        KycResponseDto validateKyc (@RequestBody KycRequestDto kycrequestdto);




        /**
         * Fallback method for validate_kyc in case of API errors or service unavailability.
         *
         * @param throwable captures the error/exception during API call
         * @return Map<String, Object> a fallback response containing failure status, message and status code (if available)
         */

        default KycResponseDto validateKycFallback(KycRequestDto kycrequestdto, Throwable throwable) {
            KycResponseDto fallbackResponse = new KycResponseDto();

            return FallBackHelper.FallbackResponse( new KycResponseDto(),throwable);
        }
    /**
     *
     * @param image1
     * @param image2
     * @param referenceId
     * @param consent
     * @param purpose   these will pass a body thrugh the formdata
     * @return   FaceMatchingResponseDto this will return the matching percentage as comparing the images
     */

    @PostMapping(value = "${decentro.api.face-match}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CircuitBreaker(name = "post-api", fallbackMethod = "getFaceMatchingPercentageFallBack")
    FaceMatchingResponseDto getFaceMatchingPercentage(@RequestPart("image1") MultipartFile image1, @RequestPart("image2") MultipartFile image2, @RequestPart("reference_id") String referenceId, @RequestPart("consent") String consent, @RequestPart("consent_purpose") String purpose);


    default FaceMatchingResponseDto getFaceMatchingPercentageFallBack(Throwable throwable)
    {
        return FallBackHelper.FallbackResponse( new FaceMatchingResponseDto(),throwable);
    }

    /**
     *
     * @param throwable
     * @return
     */


    default NameMatchResponseDto getNameMatchFallBack(Throwable throwable)
    {
        return FallBackHelper.FallbackResponse( new NameMatchResponseDto(),throwable);
    }


}
