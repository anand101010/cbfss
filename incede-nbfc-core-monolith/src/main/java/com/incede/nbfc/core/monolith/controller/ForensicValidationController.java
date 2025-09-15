package com.incede.nbfc.core.monolith.controller;
import com.incede.nbfc.core.monolith.client.dto.FaceMatchingResponseDto;
import com.incede.nbfc.core.monolith.client.dto.PhotoLivenessCheckResponseDto;
import com.incede.nbfc.core.monolith.domain.dto.FaceMatchingRequestDto;
import com.incede.nbfc.core.monolith.domain.dto.PhotoLivenessRequestDto;
import com.incede.nbfc.core.monolith.service.ForensicValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ext/forensic")
@Slf4j
@RequiredArgsConstructor
public class ForensicValidationController
{

    private final ForensicValidationService ForensicValidationService;

    /**
     *
     * @param faceMatchingRequestDto  this have 2 images one from live captured and other from aadhaar  response or OCR
     * @return FaceMatchingResponseDto   this will give photo matching percentage
     */
    @PostMapping ("image/verify")
    public ResponseEntity<FaceMatchingResponseDto> getPhotoMatchingPercentage (@Valid @ModelAttribute FaceMatchingRequestDto faceMatchingRequestDto)
    {
        log.info("Photo matching Request Received");
        FaceMatchingResponseDto response = ForensicValidationService.checkFaceMatching(faceMatchingRequestDto);
        return ResponseEntity.ok(response);

    }

    /**
     *
     * @param photoLivenessRequestDto  this will upload the images to decentro
     * @return PhotoLivenessCheckResponseDto   will give the liveness of the photo will compare the clarity size etc
     */

    @PostMapping ("image/liveness")
    public ResponseEntity<PhotoLivenessCheckResponseDto> getPhotoLivenessPercentage (@Valid @RequestBody PhotoLivenessRequestDto photoLivenessRequestDto)
    {
        log.info("Photo Liveness Request Received");
        PhotoLivenessCheckResponseDto response = ForensicValidationService.checkPhotoLiveness(photoLivenessRequestDto);
        return ResponseEntity.ok(response);

    }

}
