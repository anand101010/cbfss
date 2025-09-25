package com.incede.nbfc.core.monolith.service;
import com.incede.nbfc.core.monolith.client.ForensicPhotoLivenessClient;
import com.incede.nbfc.core.monolith.client.KycClient;
import com.incede.nbfc.core.monolith.client.dto.FaceMatchingResponseDto;
import com.incede.nbfc.core.monolith.client.dto.PhotoLivenessCheckResponseDto;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.domain.dto.FaceMatchingRequestDto;
import com.incede.nbfc.core.monolith.domain.dto.PhotoLivenessRequestDto;
import com.incede.nbfc.core.monolith.exception.FeignCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ForensicValidationService
{

   private final KycClient KycClient;
   private final  ForensicPhotoLivenessClient ForensicPhotoLivenessClient;

    /**
     *
     * @param faceMatchingRequestDto it have two images image 1 and image 2 that is going to decentro to make the matching percentage
     * @return FaceMatchingResponseDto that will have the details  of the  photo matching percentage and the status
     */
    public FaceMatchingResponseDto checkFaceMatching(FaceMatchingRequestDto faceMatchingRequestDto)
    {
        log.info("Fetching image file details");
        try
        {
            String referenceId = UUID.randomUUID().toString();
            faceMatchingRequestDto.setReferenceId(referenceId);
            faceMatchingRequestDto.setConsent(CommonConstants.FACE_MATCH_CONSENT);
            faceMatchingRequestDto.setConsentPurpose(CommonConstants.CONSENT_PURPOSE);
            faceMatchingRequestDto.setImage1(faceMatchingRequestDto.getImage1());
            faceMatchingRequestDto.setImage2(faceMatchingRequestDto.getImage2());
            FaceMatchingResponseDto  response=KycClient.getFaceMatchingPercentage(faceMatchingRequestDto.getImage1(),faceMatchingRequestDto.getImage2(),faceMatchingRequestDto.getReferenceId(),faceMatchingRequestDto.getConsent(),faceMatchingRequestDto.getConsentPurpose());
            log.info("Face Matching Percentage Fetched Successfully ");
            return response;
        }  catch (FeignCustomException exception)
        {
            throw exception;
        }
        catch (Exception exception)
        {

            log.error("Error triggering Face Matching Percentage {}", exception);
            throw new RuntimeException("Failed to Fetch Face Matching Percentage", exception);
        }

    }
    /**
     *
     * @param photoLivenessRequestDto  this will upload the images to decentro
     * @return PhotoLivenessCheckResponseDto   will give the liveness of the photo will compare the clarity size etc
     */

    public PhotoLivenessCheckResponseDto checkPhotoLiveness(PhotoLivenessRequestDto photoLivenessRequestDto)
    {
        log.info("Fetching photo Liveness image file details");
        try
        {
            String referenceId = UUID.randomUUID().toString();
            photoLivenessRequestDto.setReferenceId(referenceId);
            photoLivenessRequestDto.setConsent(CommonConstants.CONSENT);
            photoLivenessRequestDto.setPurpose(CommonConstants.CONSENT_PURPOSE);
            photoLivenessRequestDto.setBase64EncodedImage(photoLivenessRequestDto.getBase64EncodedImage());
            PhotoLivenessCheckResponseDto  response=ForensicPhotoLivenessClient.generatePhotoLivenessPercentage(photoLivenessRequestDto);
            log.info("photo Liveness Percentage Fetched Successfully ");
            return response;
        }  catch (FeignCustomException exception)
        {
            throw exception;
        }
        catch (Exception exception)
        {
            log.error("Error triggering photo Liveness Percentage", exception);
            throw new RuntimeException("Failed to Fetch photo Liveness Percentage", exception);
        }

    }



}
