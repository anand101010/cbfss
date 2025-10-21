package com.incede.nbfc.notification.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.notification.client.OtpNotificationFeignService;
import com.incede.nbfc.notification.crypto.OtpCrypto;
import com.incede.nbfc.notification.domain.Channel;
import com.incede.nbfc.notification.domain.OtpRequest;
import com.incede.nbfc.notification.domain.TemplateContents;
import com.incede.nbfc.notification.dto.RequestOtpDto;
import com.incede.nbfc.notification.dto.VerifyOtpDto;
import com.incede.nbfc.notification.dto.response.RequestOtpResponseDto;
import com.incede.nbfc.notification.dto.response.VerifyOtpResponseDto;
import com.incede.nbfc.notification.exception.GlobalExceptionHandler;
import com.incede.nbfc.notification.repository.OtpRequestRepository;
import com.incede.nbfc.notification.repository.TemplateCatalogRepository;
import com.incede.nbfc.notification.repository.TemplateContentsRepository;
import com.incede.nbfc.notification.service.OtpRequestService;
import com.incede.nbfc.notification.service.OtpVerifyService;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {OtpRequestController.class, GlobalExceptionHandler.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(OtpRequestController.class)
class OtpRequestControllerTest {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private OtpRequestController otpRequestController;

    @InjectMocks
    private OtpRequestController otpRequestController2;

    @MockBean
    private OtpRequestService otpRequestService;

    @MockBean
    private OtpVerifyService otpVerifyService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRequestOtp() {
        UUID requestId = UUID.randomUUID();
        OffsetDateTime createdAt = OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);

        RequestOtpResponseDto responseDto = new RequestOtpResponseDto(requestId, createdAt, "Status");

        when(otpRequestService.requestOtp(anyString(), any(RequestOtpDto.class))).thenReturn(responseDto);

        ResponseEntity<RequestOtpResponseDto> actual = otpRequestController.requestOtp(
                "Idempotency-Key",
                new RequestOtpDto(
                        1,
                        "HM1",
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        "Target",
                        1,
                        3,
                        300,
                        "Context",
                        createdAt,
                        createdAt
                )
        );

        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertTrue(actual.hasBody());
        assertSame(responseDto, actual.getBody());

        verify(otpRequestService).requestOtp(eq("Idempotency-Key"), any(RequestOtpDto.class));
    }



    @Test
    void testVerifyException() throws Exception {
        when(otpVerifyService.verify(any(UUID.class), any(VerifyOtpDto.class)))
                .thenReturn(new VerifyOtpResponseDto("Result", 1));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/api/v1/otp/{requestId}/verify", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new VerifyOtpDto("", null)));

        MockMvcBuilders.standaloneSetup(otpRequestController2)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
