package com.incede.nbfc.core.monolith.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.client.dto.PhotoLivenessCheckResponseDto;
import com.incede.nbfc.core.monolith.client.dto.PhotoLivenessCheckResponseDto.DataDto;
import com.incede.nbfc.core.monolith.domain.dto.FaceMatchingRequestDto;
import com.incede.nbfc.core.monolith.domain.dto.PhotoLivenessRequestDto;
import com.incede.nbfc.core.monolith.exception.GlobalExceptionHandler;
import com.incede.nbfc.core.monolith.service.ForensicValidationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ForensicValidationController.class, GlobalExceptionHandler.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class ForensicValidationControllerTest {
    @Autowired
    private ForensicValidationController forensicValidationController;

    @MockBean
    private ForensicValidationService forensicValidationService;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    /**
     * Test {@link ForensicValidationController#getPhotoLivenessPercentage(PhotoLivenessRequestDto)}.
     * ForensicValidationController#getPhotoLivenessPercentage(PhotoLivenessRequestDto)}
     */
    @Test
    void testGetPhotoLivenessPercentage_givenSecret_thenStatusIsOk() throws Exception {
        DataDto data = new DataDto();
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
        when(forensicValidationService.checkPhotoLiveness(Mockito.<PhotoLivenessRequestDto>any()))
                .thenReturn(photoLivenessCheckResponseDto);
        PhotoLivenessRequestDto photoLivenessRequestDto = new PhotoLivenessRequestDto();
        photoLivenessRequestDto.setBase64EncodedImage("secret");
        photoLivenessRequestDto.setConsent(true);
        photoLivenessRequestDto.setPurpose("Purpose");
        photoLivenessRequestDto.setReferenceId("42");
        String content = new ObjectMapper().writeValueAsString(photoLivenessRequestDto);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/ext/forensic/image/liveness")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);
        MockMvcBuilders.standaloneSetup(forensicValidationController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content()
                                .string(
                                        "{\"decentroTxnId\":\"42\",\"status\":\"Status\",\"responseCode\":\"Response Code\",\"message\":\"Not all who wander"
                                                + " are lost\",\"data\":{\"status\":\"Status\",\"live\":true,\"livenessScore\":10.0,\"needToReview\":true},\"responseKey"
                                                + "\":\"Response Key\"}"));
    }

    /**
     * Test  ForensicValidationController#getPhotoMatchingPercentage(FaceMatchingRequestDto)}.
     * ForensicValidationController#getPhotoMatchingPercentage(FaceMatchingRequestDto)}
     */
    @Test
    void testGetPhotoMatchingPercentage_givenExtForensicImageVerify_thenStatusIsNotFound()
            throws Exception {
        FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();

        MockMvcBuilders.standaloneSetup(forensicValidationController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test ForensicValidationController#getPhotoLivenessPercentage(PhotoLivenessRequestDto)}.
     *
     * ForensicValidationController#getPhotoLivenessPercentage(PhotoLivenessRequestDto)}
     */
    @Test
    void testGetPhotoLivenessPercentage() throws Exception {
        DataDto data = new DataDto();
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
        when(forensicValidationService.checkPhotoLiveness(Mockito.<PhotoLivenessRequestDto>any()))
                .thenReturn(photoLivenessCheckResponseDto);
        PhotoLivenessRequestDto photoLivenessRequestDto = new PhotoLivenessRequestDto();
        photoLivenessRequestDto.setBase64EncodedImage("secret");
        photoLivenessRequestDto.setConsent(true);
        photoLivenessRequestDto.setPurpose("Purpose");
        photoLivenessRequestDto.setReferenceId("42");
        String content = new ObjectMapper().writeValueAsString(photoLivenessRequestDto);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/ext/forensic/image/liveness")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);
        MockMvcBuilders.standaloneSetup(forensicValidationController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content()
                                .string(
                                        "{\"decentroTxnId\":\"42\",\"status\":\"Status\",\"responseCode\":\"Response Code\",\"message\":\"Not all who wander"
                                                + " are lost\",\"data\":{\"status\":\"Status\",\"live\":true,\"livenessScore\":10.0,\"needToReview\":true},\"responseKey"
                                                + "\":\"Response Key\"}"));
    }
}
