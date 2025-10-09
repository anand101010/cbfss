package com.incede.nbfc.core.monolith.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.incede.nbfc.core.monolith.client.ForensicPhotoLivenessClient;
import com.incede.nbfc.core.monolith.client.KycClient;
import com.incede.nbfc.core.monolith.client.dto.FaceMatchingResponseDto;
import com.incede.nbfc.core.monolith.client.dto.FaceMatchingResponseDto.DataDto;
import com.incede.nbfc.core.monolith.client.dto.PhotoLivenessCheckResponseDto;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.domain.dto.FaceMatchingRequestDto;
import com.incede.nbfc.core.monolith.domain.dto.PhotoLivenessRequestDto;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {ForensicValidationService.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class ForensicValidationServiceTest {
  @MockBean private ForensicPhotoLivenessClient forensicPhotoLivenessClient;

  @Autowired private ForensicValidationService forensicValidationService;

  @MockBean private KycClient kycClient;


    /**
     * Test  ForensicValidationService#checkPhotoLiveness(PhotoLivenessRequestDto)}.
     * ForensicValidationService#checkPhotoLiveness(PhotoLivenessRequestDto)}
     */
    @Test
    void testCheckPhotoLiveness() {

        PhotoLivenessCheckResponseDto.DataDto data = new PhotoLivenessCheckResponseDto.DataDto();
        data.setLive(true);
        data.setLivenessScore(10.0d);
        data.setNeedToReview(true);
        data.setStatus("Status");
        PhotoLivenessCheckResponseDto photoLivenessCheckResponseDto =
                new PhotoLivenessCheckResponseDto();
        photoLivenessCheckResponseDto.setData(data);
        photoLivenessCheckResponseDto.setDecentroTxnId("42");
        photoLivenessCheckResponseDto.setMessage("Not all who wander are lost");
        photoLivenessCheckResponseDto.setResponseCode("Response Code");
        photoLivenessCheckResponseDto.setResponseKey("Response Key");
        photoLivenessCheckResponseDto.setStatus("Status");
        when(forensicPhotoLivenessClient.generatePhotoLivenessPercentage(
                Mockito.<PhotoLivenessRequestDto>any()))
                .thenReturn(photoLivenessCheckResponseDto);
        PhotoLivenessRequestDto PhotoLivenessRequestDto = new PhotoLivenessRequestDto();
        PhotoLivenessCheckResponseDto actualCheckPhotoLivenessResult =
                forensicValidationService.checkPhotoLiveness(PhotoLivenessRequestDto);
        verify(forensicPhotoLivenessClient)
                .generatePhotoLivenessPercentage(isA(PhotoLivenessRequestDto.class));
        assertEquals("For bank account purpose only", PhotoLivenessRequestDto.getPurpose());
        assertTrue(PhotoLivenessRequestDto.getConsent());
        assertSame(photoLivenessCheckResponseDto, actualCheckPhotoLivenessResult);
    }

    /**
     * Test ForensicValidationService#checkPhotoLiveness(PhotoLivenessRequestDto)}.
     * ForensicValidationService#checkPhotoLiveness(PhotoLivenessRequestDto)}
     */
    @Test
    void testCheckPhotoLivenesswhenCauseRuntimeException() {
        when(forensicPhotoLivenessClient.generatePhotoLivenessPercentage(
                Mockito.<PhotoLivenessRequestDto>any()))
                .thenThrow(new RuntimeException());
        assertThrows(
                RuntimeException.class,
                () -> forensicValidationService.checkPhotoLiveness(new PhotoLivenessRequestDto()));
        verify(forensicPhotoLivenessClient)
                .generatePhotoLivenessPercentage(isA(PhotoLivenessRequestDto.class));
    }

    /**
     * Test {@link ForensicValidationService#checkFaceMatching(FaceMatchingRequestDto)}.
     * ForensicValidationService#checkFaceMatching(FaceMatchingRequestDto)}
     */
    @Test
    void testCheckFaceMatching() throws IOException {
        // Arrange
        DataDto data = new DataDto();
        data.setMatch("Match");
        data.setStatus("Status");
        FaceMatchingResponseDto mockResponse = new FaceMatchingResponseDto();
        mockResponse.setData(data);
        mockResponse.setDecentroTxnId("42");
        mockResponse.setMessage("Success");
        mockResponse.setResponseCode("S000");
        mockResponse.setResponseKey("KEY1");
        mockResponse.setStatus("Success");
        when(kycClient.getFaceMatchingPercentage(
                any(MultipartFile.class),
                any(MultipartFile.class),
                anyString(),
                anyString(),
                anyString()))
                .thenReturn(mockResponse);
        FaceMatchingRequestDto request = new FaceMatchingRequestDto();
        request.setImage1(new MockMultipartFile("image1", "data".getBytes()));
        request.setImage2(new MockMultipartFile("image2", "data".getBytes()));
        FaceMatchingResponseDto actualResponse = forensicValidationService.checkFaceMatching(request);
        assertNotNull(actualResponse);
        assertSame(mockResponse, actualResponse);
        assertEquals(CommonConstants.FACE_MATCH_CONSENT, request.getConsent());
        assertEquals(CommonConstants.CONSENT_PURPOSE, request.getConsentPurpose());
        verify(kycClient).getFaceMatchingPercentage(
                any(MultipartFile.class),
                any(MultipartFile.class),
                anyString(),
                eq(CommonConstants.FACE_MATCH_CONSENT),
                eq(CommonConstants.CONSENT_PURPOSE)
        );
    }

    /**
     * Test {@link ForensicValidationService#checkFaceMatching(FaceMatchingRequestDto)}.
     * ForensicValidationService#checkFaceMatching(FaceMatchingRequestDto)}
     */

    @Test
    void testCheckFaceMatching_exception() throws IOException {
        when(kycClient.getFaceMatchingPercentage(
                any(MultipartFile.class),
                any(MultipartFile.class),
                anyString(),
                anyString(),
                anyString()))
                .thenThrow(new RuntimeException("Client failed"));

        FaceMatchingRequestDto request = new FaceMatchingRequestDto();
        request.setImage1(new MockMultipartFile("image1", "data".getBytes()));
        request.setImage2(new MockMultipartFile("image2", "data".getBytes()));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> forensicValidationService.checkFaceMatching(request));
        assertEquals("Failed to Fetch Face Matching Percentage", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals("Client failed", exception.getCause().getMessage());
        verify(kycClient).getFaceMatchingPercentage(
                any(MultipartFile.class),
                any(MultipartFile.class),
                anyString(),
                eq(CommonConstants.FACE_MATCH_CONSENT),
                eq(CommonConstants.CONSENT_PURPOSE)
        );
    }



  }
