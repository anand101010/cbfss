package com.incede.nbfc.core.monolith.service;

import com.incede.nbfc.core.monolith.client.AadhaarMaskingClient;
import com.incede.nbfc.core.monolith.client.KycClient;
import com.incede.nbfc.core.monolith.client.UpiIdValidationClient;
import com.incede.nbfc.core.monolith.client.dto.*;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.domain.dto.AadhaarMaskingRequestDto;
import com.incede.nbfc.core.monolith.domain.dto.AadhaarOtpRequestDto;
import com.incede.nbfc.core.monolith.domain.dto.AadhaarOtpValidateRequestDto;
import com.incede.nbfc.core.monolith.domain.dto.*;

import com.incede.nbfc.core.monolith.enums.KycType;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.FeignCustomException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service for validate the kyc details of the customer through feignclient external Api configuration
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class KycService
{
    private final KycClient KycClient;
    private final AadhaarMaskingClient AadhaarMaskingClient;
    private final UpiIdValidationClient UpiIdValidationClient;
    /**
     *
     * @paramincludes the Aaadhar details from the frontend
     * @return  AadhaarOtpResponse  The valid response to the request
     */
    public AadhaarOtpResponse generateOtp(AadhaarOtpRequestDto aadhaarOtpRequestDto)
    {
        log.info("Fetching Aadhaar No");
        try
        {
            String referenceId = UUID.randomUUID().toString();
            log.info("Successfully triggered ReferenceId");
            aadhaarOtpRequestDto.setReferenceId(referenceId);
            aadhaarOtpRequestDto.setConsent( CommonConstants.CONSENT);
            aadhaarOtpRequestDto.setPurpose(CommonConstants.AADHAAR_PURPOSE);
            AadhaarOtpResponse  response=KycClient.generateAadhaarOtp(aadhaarOtpRequestDto);
            log.info("Successfully triggered OTP: {}", response.getStatus());
            return response;
        }  catch (FeignCustomException exception)
        {
            throw exception;
        }
        catch (Exception exception)
        {
            log.error("Error triggering Aadhaar No ");
            throw new RuntimeException("Failed to fetch Aadhaar No", exception);
        }

    }

    /**
     *
     * @paramthat contains the the details for validation Otp and transactionId
     * @return AadhaarOtpValidatedResponseDto entire details of aadhaar
     */

    public AadhaarOtpValidatedResponseDto validateAaadhaarOtp(AadhaarOtpValidateRequestDto aadhaarOtpValidateRequestDto)
    {
        log.info("Fetching Aadhaar Otp");
        try
        {
            String referenceId = UUID.randomUUID().toString();
            aadhaarOtpValidateRequestDto.setConsent(CommonConstants.CONSENT);
            aadhaarOtpValidateRequestDto.setPurpose(CommonConstants.AADHAAR_PURPOSE);
            aadhaarOtpValidateRequestDto.setReferenceId(referenceId);
            AadhaarOtpValidatedResponseDto  response=KycClient.validateAadhaarOtp(aadhaarOtpValidateRequestDto);
            log.info("Successfully Validated OTP: {}", response.getStatus());
            return response;
        } catch (FeignCustomException exception)
        {
            throw exception;
        }
        catch (Exception exception)
        {
            log.error("Error triggering Validated Aadhaar Otp : {}", exception);
            throw new RuntimeException("Failed to Validate Aadhaar Otp", exception);
        }

    }

    /**
     *
     * AadhaarMaskingRequestDto contain base64 converted pdf and imagetype (optional)
     * @return AadhaarMaskingResponseDto have masked aadhaar pdf document and other information
     */

    public AadhaarMaskingResponseDto maskAadhaarImage(AadhaarMaskingRequestDto aadhaarMaskingRequestDto)
    {
        log.info("Fetching Aadhaar file type : {}", aadhaarMaskingRequestDto.getImageFormat());
        try
        {
            String referenceId = UUID.randomUUID().toString();
            aadhaarMaskingRequestDto.setImageUuid(referenceId);
            aadhaarMaskingRequestDto.setDetectionBasedMasking(CommonConstants.DETECTIONBASEDMASKING);
            aadhaarMaskingRequestDto.setPdfOut(CommonConstants.PDFOUT);
            AadhaarMaskingResponseDto  response=AadhaarMaskingClient.generateAadhaarMasked(aadhaarMaskingRequestDto);
            log.info("Aadhaar Successfully masked");
            return response;
        }  catch (FeignCustomException exception)
        {
            throw exception;
        }
        catch (Exception exception)
        {
            log.error("Error triggering  Aadhaar Masking", exception);
            throw new RuntimeException("Failed to mask Aadhaar", exception);
        }

    }

    /**
     *
     * @param nameMatchRequestDto that will have both 2 name  from the front end
     * @return that will return the name matching percentage on 200ok
     */
    public NameMatchResponseDto getNameMatching(NameMatchRequestDto nameMatchRequestDto)
    {
        log.info("Fetching name matching : {}", nameMatchRequestDto.getName1());
        try
        {
            String referenceId = UUID.randomUUID().toString();
            nameMatchRequestDto.setReferenceId(referenceId);
            nameMatchRequestDto.setMatchThreshold(new NameMatchRequestDto.MatchThreshold(CommonConstants.ACCURACYPERCENTAGE,CommonConstants.ACCURACYPERCENTAGE));
            NameMatchResponseDto  response= KycClient.getNameMatchingpercentage(nameMatchRequestDto);
            log.info("Name matching Successfully done");
            return response;
        }  catch (FeignCustomException exception)
        {
            throw exception;
        }
        catch (Exception exception)
        {
            log.error("Error triggering  Name matching", exception);
            throw new RuntimeException("Failed to Name matching", exception);
        }

    }


    /**
     * Validates the provided KYC document using the external KYC service.
     *
     * <p>This method determines the {@link KycType} from the provided {@code kycId}.
     * If the KYC type requires a date of birth (e.g., Driving License), the method
     * enforces its presence and throws a {@link BusinessException} if missing.</p>
     *
     * <p>It constructs a {@link KycRequestDto} object, sets the required fields,
     * and calls the external KYC service through {@link KycClient}.</p>
     *
     * @param idNumber the KYC document number to be validated (e.g., PAN, Voter ID, DL)
     * @param kycId the ID representing the type of KYC document (mapped to {@link KycType})
     * @param dob the applicant's date of birth (mandatory for Driving License)
     * @return the {@link KycResponseDto} containing the validation result
     *
     * @throws BusinessException if required fields (like DOB for DL) are missing
     * @throws RuntimeException if an error occurs while invoking the external KYC service
     */



    public KycResponseDto validateKyc(@Valid String idNumber, Integer kycId, String dob) {

        KycType kycType = KycType.getKycTypeFromIdType(kycId);
        if (kycType == KycType.DRIVING_LICENSE && (dob == null || dob.isBlank())) {
            throw new BusinessException(
                    CommonConstants.DOB_REQUIRED_FOR_DL,
                    ErrorCodes.MISSING_REQUIRED_FIELD
            );
        }

        log.info("Validating KYC for DocType: {}",kycType.name() );
        try {
            KycRequestDto request = new KycRequestDto();
            request.setReference_id(UUID.randomUUID().toString());
            request.setDocument_type(kycType.name());
            request.setId_number(idNumber);
            request.setConsent(CommonConstants.KYCCONSENT);
            request.setConsent_purpose(CommonConstants.CONSENT_PURPOSE);
            request.setDob(dob);
            KycResponseDto response = KycClient.validateKyc(request);

            log.info("KYC validation response received for IdNumber");

            return response;
        }
        catch (FeignCustomException exception)
        {
            throw exception;
        }
        catch (Exception exception) {
            log.error("Error validating KYC for IdNumber: {}", exception);

            throw new RuntimeException("Failed to validating KYC", exception);
        }
    }

    /**
     *
     * @param upiId will get from the controller
     * @return   consolidated detials of the accountb holder details
     */


    public UpiAccountDetailsResponseDto getUpiIdValidationResponse(String upiId)
    {
        log.info("Fetching UpiId matching ");
        try
        {
            UpiAccountDetailsRequestDto UpiRequestDto=new UpiAccountDetailsRequestDto();
            String referenceId = UUID.randomUUID().toString();
            UpiRequestDto.setReferenceId(referenceId);
            UpiRequestDto.setUpiVpa(upiId);
            UpiAccountDetailsResponseDto  response= UpiIdValidationClient.getValidatedUpiIdDetails(UpiRequestDto);
            log.info("UpiId Validation Successfully done");
            return response;
        }
        catch (FeignCustomException exception)
        {
            throw exception;
        }
        catch (Exception exception)
        {
            log.error("Error triggering  UpiId Validation", exception);
            throw new RuntimeException("Failed to UpiId Validation", exception);
        }

    }

}
