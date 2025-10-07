package com.incede.nbfc.core.monolith.controller;
import com.incede.nbfc.core.monolith.client.dto.AadhaarMaskingResponseDto;
import com.incede.nbfc.core.monolith.client.dto.AadhaarOtpResponse;
import com.incede.nbfc.core.monolith.client.dto.AadhaarOtpValidatedResponseDto;
import com.incede.nbfc.core.monolith.client.dto.KycResponseDto;
import com.incede.nbfc.core.monolith.domain.dto.AadhaarMaskingRequestDto;
import com.incede.nbfc.core.monolith.domain.dto.AadhaarOtpRequestDto;
import com.incede.nbfc.core.monolith.domain.dto.AadhaarOtpValidateRequestDto;
import com.incede.nbfc.core.monolith.enums.KycType;
import com.incede.nbfc.core.monolith.client.dto.*;
import com.incede.nbfc.core.monolith.domain.dto.*;
import com.incede.nbfc.core.monolith.service.KycService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * this controller will navigate the service with its method the validate the kyc details of customers
 * these are connected with the service
 */
@RestController
@PreAuthorize("hasRole('STAFF')")
@RequestMapping("/ext/ekyc")
@Slf4j
public class KycController
{
    private final KycService kycService;

    public KycController(KycService kycService)
    {
        this.kycService = kycService;

    }

    /**
     *
     * @paramincludes the aadhaar Number
     * @return AadhaarOtpResponse  just return the expected  success status
     */

    @PostMapping("/otp/send")
    public ResponseEntity<AadhaarOtpResponse> generateOtp(@RequestBody AadhaarOtpRequestDto aadhaarOtpRequestDto )
    {
        log.info("Get Aadhaar request received");
        AadhaarOtpResponse response = kycService.generateOtp(aadhaarOtpRequestDto);
        return ResponseEntity.ok(response);
    }
    /**
     *
     * @paramwith transaction id and otp
     * @return  AadhaarOtpValidatedResponseDto entire dto of Aadhaar
     */
    @PostMapping("/otp/verify")
    public ResponseEntity<AadhaarOtpValidatedResponseDto> validateOtp(@RequestBody AadhaarOtpValidateRequestDto aadhaarOtpValidateRequestDto)
    {
        log.info("Get Aadhaar OTP request received");
        AadhaarOtpValidatedResponseDto response = kycService.validateAaadhaarOtp(aadhaarOtpValidateRequestDto);
        return ResponseEntity.ok(response);
    }

    /**
     *
     * @param aadhaarMaskingRequestDto contain base64 converted pdf and imagetype (optional)
     * @return   AadhaarMaskingResponseDto  contains the masking of aadhaar card
     */
    @PostMapping("/masking")
    public ResponseEntity<AadhaarMaskingResponseDto> aadhaarMasking(@Valid @RequestBody AadhaarMaskingRequestDto aadhaarMaskingRequestDto) {

        log.info("Get Aaadhar Masking Image Received");
        AadhaarMaskingResponseDto response = kycService.maskAadhaarImage(aadhaarMaskingRequestDto);
        return ResponseEntity.ok(response);

    }
    /**
     * Validates a KYC document based on its type and ID number.
     *
     * @param idNumber the KYC document number (e.g., PAN, Aadhaar, DL)
     * @param kycId    the KYC type ID (mapped from {@link KycType})
     * @param dob      the date of birth (required if KYC type is Driving License)
     * @return a response containing validation status and details
     */

    @GetMapping("validate/kyc")
    public ResponseEntity<KycResponseDto> validateKyc(
            @RequestParam String idNumber,
            @RequestParam Integer kycId,
            @RequestParam(required = false) String dob) {
        KycResponseDto response = kycService.validateKyc(idNumber, kycId, dob);
        return ResponseEntity.ok(response);
    }

    /**
     *
     * @param nameMatchRequestDto  when it  have 2 names to get the name matching percentage
     * @return NameMatchResponseDto
     */
    @PostMapping("/name/matching")
    public ResponseEntity<NameMatchResponseDto> getNameMatchingPercentage(@Valid @RequestBody NameMatchRequestDto nameMatchRequestDto) {

        log.info("Get name trigger Matching Received");
        NameMatchResponseDto response = kycService.getNameMatching(nameMatchRequestDto);
        return ResponseEntity.ok(response);

    }

    /**
     *
     * @param upiId will receive teh UPIid from fronte end
     * @return will m UpiAccountDetailsResponseDto  return the consolidated  upidetaisl
     */
    @GetMapping("/upi/matching")
    public ResponseEntity<UpiAccountDetailsResponseDto> getUpiMatching( @RequestParam("upiId") String upiId) {
        log.info("Get UPI Validation  Received");
        UpiAccountDetailsResponseDto response = kycService.getUpiIdValidationResponse(upiId);
        return ResponseEntity.ok(response);

    }


}
