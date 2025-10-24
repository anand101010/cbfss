package com.incede.nbfc.notification.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private OtpRequestService otpRequestService; // mock dependency
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OtpVerifyService otpVerifyService;   // mock dependency
    /**
     * Test {@link OtpRequestController#requestOtp(String, RequestOtpDto)}.
     *
     * <p>Method under test: {@link OtpRequestController#requestOtp(String, RequestOtpDto)}
     */
    @Test
    void testRequestOtp()
    {
        OtpRequestService otpRequestService = mock(OtpRequestService.class);
        UUID requestId = UUID.randomUUID();
        RequestOtpResponseDto requestOtpResponseDto =
                new RequestOtpResponseDto(
                        requestId,
                        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC),
                        "Status");

        when(otpRequestService.requestOtp(Mockito.<String>any(), Mockito.<RequestOtpDto>any()))
                .thenReturn(requestOtpResponseDto);
        OtpRequestRepository repository = mock(OtpRequestRepository.class);
        OtpRequestController otpRequestController =
                new OtpRequestController(
                        otpRequestService,
                        new OtpVerifyService(
                                repository,
                                new OtpCrypto(3, 19088743, 1, 1, 1, "Slot Key Secret"),
                                "EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY"));
        OffsetDateTime createdAt =
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);

        ResponseEntity<RequestOtpResponseDto> actualRequestOtpResult =
                otpRequestController.requestOtp(
                        "Idempotency Key",
                        new RequestOtpDto(
                                1,
                                "jHM1",
                                1,
                                1,
                                "Target",
                                1,
                                3,
                                1,
                                "Context",
                                createdAt,
                                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC)));

        verify(otpRequestService).requestOtp(eq("Idempotency Key"), isA(RequestOtpDto.class));
        HttpStatusCode statusCode = actualRequestOtpResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals(201, actualRequestOtpResult.getStatusCodeValue());
        assertEquals(HttpStatus.CREATED, statusCode);
        assertTrue(actualRequestOtpResult.hasBody());
        assertTrue(actualRequestOtpResult.getHeaders().isEmpty());
        assertSame(requestOtpResponseDto, actualRequestOtpResult.getBody());
    }


    @Test
    void testRequestOtpThenNormaisedSlot() throws UnsupportedEncodingException {

        OtpRequest otpRequest = new OtpRequest();
        otpRequest.setActiveSlotKey("Active Slot Key");
        otpRequest.setAttemptCount((short) 1);
        otpRequest.setBranchCode("HM1");
        otpRequest.setCreatedAt(
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        otpRequest.setCreatedBy(1);
        otpRequest.setCustomerIdentity(1);
        OffsetDateTime expiresAt =
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);
        otpRequest.setExpiresAt(expiresAt);
        otpRequest.setIdempotencyKey("Idempotency Key");
        UUID identity = UUID.randomUUID();
        otpRequest.setIdentity(identity);
        otpRequest.setMaxAttempts((short) 1);
        otpRequest.setMsisdn("Msisdn");
        otpRequest.setOtpHash("Otp Hash");
        otpRequest.setOtpLength((short) 1);
        otpRequest.setOtpRequestId(1L);
        otpRequest.setOtpSalt("AXAXAXAX".getBytes("UTF-8"));
        otpRequest.setProviderRequestJson("Metadata Json");
        otpRequest.setProviderResponseId("42");
        otpRequest.setStatus("Status");
        otpRequest.setTemplateContentId(1);
        otpRequest.setTemplateCatalogId(1);
        otpRequest.setTenantId(1);
        otpRequest.setUpdatedAt(
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        otpRequest.setUpdatedBy(1);
        Optional<OtpRequest> ofResult = Optional.of(otpRequest);
        OtpRequestRepository repository = mock(OtpRequestRepository.class);
        when(repository.findFirstByTenantIdAndBranchCodeAndIdempotencyKey(
                Mockito.<Integer>any(), Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(ofResult);
        OtpCrypto crypto = mock(OtpCrypto.class);
        when(crypto.normalizeTarget(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn("Normalize Target");
        when(crypto.slotKey(
                Mockito.<Integer>any(),
                Mockito.<String>any(),
                Mockito.<String>any(),
                Mockito.<String>any()))
                .thenReturn("Slot Key");

        Channel channel = new Channel();
        channel.setChannelId(1);
        channel.setCreatedAt(
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        channel.setCreatedBy(1);
        channel.setDescription("The characteristics of someone or something");
        channel.setIdentity(UUID.randomUUID());
        channel.setIsActive(true);
        channel.setIsDelete(true);
        channel.setName("Name");
        channel.setTenantId(1);
        channel.setUpdatedAt(
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        channel.setUpdatedBy(1);
        TemplateContents templateContents = mock(TemplateContents.class);
        when(templateContents.getChannel()).thenReturn(channel);
        Optional<TemplateContents> ofResult2 = Optional.of(templateContents);
        TemplateContentsRepository templateContentsRepository = mock(TemplateContentsRepository.class);
        when(templateContentsRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);
        OtpRequestService otpRequestService =
                new OtpRequestService(
                        repository,
                        crypto,
                        mock(OtpNotificationFeignService.class),
                        templateContentsRepository,
                        mock(TemplateCatalogRepository.class));

        OtpRequestRepository repository2 = mock(OtpRequestRepository.class);
        OtpRequestController otpRequestController =
                new OtpRequestController(
                        otpRequestService,
                        new OtpVerifyService(
                                repository2,
                                new OtpCrypto(3, 19088743, 1, 1, 1, "Slot Key Secret"),
                                "EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY"));
        OffsetDateTime createdAt =
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);

        ResponseEntity<RequestOtpResponseDto> actualRequestOtpResult =
                otpRequestController.requestOtp(
                        "Idempotency Key",
                        new RequestOtpDto(
                                1,
                                "HM1",
                                1,
                                1,
                                "Target",
                                1,
                                3,
                                1,
                                "Context",
                                createdAt,
                                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC)));

        verify(crypto).normalizeTarget("Name", "Target");
        verify(crypto).slotKey(1, "HM1", "1", "Normalize Target");
        verify(templateContents).getChannel();
        verify(repository)
                .findFirstByTenantIdAndBranchCodeAndIdempotencyKey(
                        1, "HM1", "Idempotency Key");
        verify(templateContentsRepository).findById(1);
        HttpStatusCode statusCode = actualRequestOtpResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        RequestOtpResponseDto body = actualRequestOtpResult.getBody();
        assertEquals("Status", body.status());
        assertEquals(201, actualRequestOtpResult.getStatusCodeValue());
        assertEquals(HttpStatus.CREATED, statusCode);
        assertTrue(actualRequestOtpResult.hasBody());
        assertTrue(actualRequestOtpResult.getHeaders().isEmpty());
        assertSame(expiresAt, body.expiresAt());
        assertSame(identity, body.requestId());
    }

    /**
     * Test {@link OtpRequestController#requestOtp(String, RequestOtpDto)}.
     *
     * <ul>
     *   <li>Then return Body status is {@code Status}.
     * </ul>
     *
     * <p>Method under test: {@link OtpRequestController#requestOtp(String, RequestOtpDto)}
     */
    @Test
    void testRequestOtpController() throws UnsupportedEncodingException {
        OtpRequest otpRequest = new OtpRequest();
        otpRequest.setActiveSlotKey("Active Slot Key");
        otpRequest.setAttemptCount((short) 1);
        otpRequest.setBranchCode("HM1");
        otpRequest.setCreatedAt(
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        otpRequest.setCreatedBy(1);
        otpRequest.setCustomerIdentity(1);
        OffsetDateTime expiresAt =
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);
        otpRequest.setExpiresAt(expiresAt);
        otpRequest.setIdempotencyKey("Idempotency Key");
        UUID identity = UUID.randomUUID();
        otpRequest.setIdentity(identity);
        otpRequest.setMaxAttempts((short) 1);
        otpRequest.setMsisdn("Msisdn");
        otpRequest.setOtpHash("Otp Hash");
        otpRequest.setOtpLength((short) 1);
        otpRequest.setOtpRequestId(1L);
        otpRequest.setOtpSalt("AXAXAXAX".getBytes("UTF-8"));
        otpRequest.setProviderRequestJson("Metadata Json");
        otpRequest.setProviderResponseId("42");
        otpRequest.setStatus("Status");
        otpRequest.setTemplateContentId(1);
        otpRequest.setTemplateCatalogId(1);
        otpRequest.setTenantId(1);
        otpRequest.setUpdatedAt(
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        otpRequest.setUpdatedBy(1);
        Optional<OtpRequest> ofResult = Optional.of(otpRequest);
        OtpRequestRepository repository = mock(OtpRequestRepository.class);
        when(repository.findFirstByTenantIdAndBranchCodeAndIdempotencyKey(
                Mockito.<Integer>any(), Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(ofResult);

        Channel channel = new Channel();
        channel.setChannelId(1);
        channel.setCreatedAt(
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        channel.setCreatedBy(1);
        channel.setDescription("The characteristics of someone or something");
        channel.setIdentity(UUID.randomUUID());
        channel.setIsActive(true);
        channel.setIsDelete(true);
        channel.setName("Name");
        channel.setTenantId(1);
        channel.setUpdatedAt(
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        channel.setUpdatedBy(1);
        TemplateContents templateContents = mock(TemplateContents.class);
        when(templateContents.getChannel()).thenReturn(channel);
        Optional<TemplateContents> ofResult2 = Optional.of(templateContents);
        TemplateContentsRepository templateContentsRepository = mock(TemplateContentsRepository.class);
        when(templateContentsRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);
        OtpRequestService otpRequestService =
                new OtpRequestService(
                        repository,
                        new OtpCrypto(3, 19088743, 1, 1, 1, "Slot Key Secret"),
                        mock(OtpNotificationFeignService.class),
                        templateContentsRepository,
                        mock(TemplateCatalogRepository.class));

        OtpRequestRepository repository2 = mock(OtpRequestRepository.class);
        OtpRequestController otpRequestController =
                new OtpRequestController(
                        otpRequestService,
                        new OtpVerifyService(
                                repository2,
                                new OtpCrypto(3, 19088743, 1, 1, 1, "Slot Key Secret"),
                                "EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY"));
        OffsetDateTime createdAt =
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);

        ResponseEntity<RequestOtpResponseDto> actualRequestOtpResult =
                otpRequestController.requestOtp(
                        "Idempotency Key",
                        new RequestOtpDto(
                                1,
                                "HM1",
                                1,
                                1,
                                "Target",
                                1,
                                3,
                                1,
                                "Context",
                                createdAt,
                                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC)));

        verify(templateContents).getChannel();
        verify(repository)
                .findFirstByTenantIdAndBranchCodeAndIdempotencyKey(
                        1, "HM1", "Idempotency Key");
        verify(templateContentsRepository).findById(1);
        HttpStatusCode statusCode = actualRequestOtpResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        RequestOtpResponseDto body = actualRequestOtpResult.getBody();
        assertEquals("Status", body.status());
        assertEquals(201, actualRequestOtpResult.getStatusCodeValue());
        assertEquals(HttpStatus.CREATED, statusCode);
        assertTrue(actualRequestOtpResult.hasBody());
        assertTrue(actualRequestOtpResult.getHeaders().isEmpty());
        assertSame(expiresAt, body.expiresAt());
        assertSame(identity, body.requestId());
    }

    /**
     * Test {@link OtpRequestController#verify(UUID, VerifyOtpDto)}.
     *
     * <ul>
     *   <li>Then status four hundred.
     * </ul>
     *
     * <p>Method under test: {@link OtpRequestController#verify(UUID, VerifyOtpDto)}
     */
    @Test
    void testVerifyException() throws Exception {
        when(otpVerifyService.verify(Mockito.<UUID>any(), Mockito.<VerifyOtpDto>any()))
                .thenReturn(new VerifyOtpResponseDto("Result", 1));
        MockHttpServletRequestBuilder contentTypeResult =
                MockMvcRequestBuilders.post("/api/v1/otp/{requestId}/verify", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder =
                contentTypeResult.content(objectMapper.writeValueAsString(new VerifyOtpDto("", null)));

        MockMvcBuilders.standaloneSetup(otpRequestController2)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(400));
    }


}
