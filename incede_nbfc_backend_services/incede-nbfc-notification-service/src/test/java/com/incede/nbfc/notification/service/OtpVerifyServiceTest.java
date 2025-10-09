package com.incede.nbfc.notification.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyShort;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.incede.nbfc.notification.crypto.OtpCrypto;
import com.incede.nbfc.notification.domain.OtpRequest;
import com.incede.nbfc.notification.dto.VerifyOtpDto;
import com.incede.nbfc.notification.dto.response.VerifyOtpResponseDto;
import com.incede.nbfc.notification.repository.OtpRequestRepository;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OtpVerifyService.class, String.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class OtpVerifyServiceTest {
  @MockBean private OtpRequestRepository otpRequestRepository;

  @Autowired private OtpVerifyService otpVerifyService;
    @MockBean
    private OtpCrypto otpCrypto;
  /**
   * Test {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}.
   *
   * <p>Method under test: {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}
   */
  @Test
  void testVerify() {

    when(otpRequestRepository.findByIdentityForUpdate(Mockito.<UUID>any()))
        .thenThrow(new IllegalArgumentException());
    UUID requestId = UUID.randomUUID();

    assertThrows(
        IllegalArgumentException.class,
        () ->
            otpVerifyService.verify(
                requestId,
                new VerifyOtpDto(
                    "Code",
                    OffsetDateTime.of(
                        LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC))));
    verify(otpRequestRepository).findByIdentityForUpdate(isA(UUID.class));
  }

  /**
   * Test {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}.
   *
   * <p>Method under test: {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}
   */
  @Test
  void testVerifyGivenOtpRequestGetStatusReturnCancelledThenReturnResultIsCancelled()
      throws UnsupportedEncodingException {
    OtpRequest otpRequest = mock(OtpRequest.class);
    when(otpRequest.getStatus()).thenReturn("CANCELLED");
    doNothing().when(otpRequest).setActiveSlotKey(Mockito.<String>any());
    doNothing().when(otpRequest).setAttemptCount(anyShort());
    doNothing().when(otpRequest).setBranchCode(Mockito.<String>any());
    doNothing().when(otpRequest).setCreatedAt(Mockito.<OffsetDateTime>any());
    doNothing().when(otpRequest).setCreatedBy(Mockito.<Integer>any());
    doNothing().when(otpRequest).setCustomerIdentity(Mockito.<Integer>any());
    doNothing().when(otpRequest).setExpiresAt(Mockito.<OffsetDateTime>any());
    doNothing().when(otpRequest).setIdempotencyKey(Mockito.<String>any());
    doNothing().when(otpRequest).setIdentity(Mockito.<UUID>any());
    doNothing().when(otpRequest).setMaxAttempts(anyShort());
    doNothing().when(otpRequest).setMsisdn(Mockito.<String>any());
    doNothing().when(otpRequest).setOtpHash(Mockito.<String>any());
    doNothing().when(otpRequest).setOtpLength(anyShort());
    doNothing().when(otpRequest).setOtpRequestId(Mockito.<Long>any());
    doNothing().when(otpRequest).setOtpSalt(Mockito.<byte[]>any());
    doNothing().when(otpRequest).setProviderRequestJson(Mockito.<String>any());
    doNothing().when(otpRequest).setProviderResponseId(Mockito.<String>any());
    doNothing().when(otpRequest).setStatus(Mockito.<String>any());
    doNothing().when(otpRequest).setTemplateContentId(Mockito.<Integer>any());
    doNothing().when(otpRequest).setTemplateCatalogId(Mockito.<Integer>any());
    doNothing().when(otpRequest).setTenantId(Mockito.<Integer>any());
    doNothing().when(otpRequest).setUpdatedAt(Mockito.<OffsetDateTime>any());
    doNothing().when(otpRequest).setUpdatedBy(Mockito.<Integer>any());
    otpRequest.setActiveSlotKey("Active Slot Key");
    otpRequest.setAttemptCount((short) 1);
    otpRequest.setBranchCode("HM1");
    otpRequest.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest.setCreatedBy(1);
    otpRequest.setCustomerIdentity(1);
    otpRequest.setExpiresAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest.setIdempotencyKey("Idempotency Key");
    otpRequest.setIdentity(UUID.randomUUID());
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
    when(repository.findByIdentityForUpdate(Mockito.<UUID>any())).thenReturn(ofResult);
    OtpVerifyService otpVerifyService =
        new OtpVerifyService(
            repository,
            new OtpCrypto(3, 19088743, 1, 1, 1, "Slot Key Secret"),
            "EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
    UUID requestId = UUID.randomUUID();

    VerifyOtpResponseDto actualVerifyResult =
        otpVerifyService.verify(
            requestId,
            new VerifyOtpDto(
                "Code",
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC)));
    verify(otpRequest, atLeast(1)).getStatus();
    verify(otpRequest).setActiveSlotKey("Active Slot Key");
    verify(otpRequest).setAttemptCount((short) 1);
    verify(otpRequest).setBranchCode("HM1");
    verify(otpRequest).setCreatedAt(isA(OffsetDateTime.class));
    verify(otpRequest).setCreatedBy(1);
    verify(otpRequest).setCustomerIdentity(1);
    verify(otpRequest).setExpiresAt(isA(OffsetDateTime.class));
    verify(otpRequest).setIdempotencyKey("Idempotency Key");
    verify(otpRequest).setIdentity(isA(UUID.class));
    verify(otpRequest).setMaxAttempts((short) 1);
    verify(otpRequest).setMsisdn("Msisdn");
    verify(otpRequest).setOtpHash("Otp Hash");
    verify(otpRequest).setOtpLength((short) 1);
    verify(otpRequest).setOtpRequestId(1L);
    verify(otpRequest).setOtpSalt(isA(byte[].class));
    verify(otpRequest).setProviderRequestJson("Metadata Json");
    verify(otpRequest).setProviderResponseId("42");
    verify(otpRequest).setStatus("Status");
    verify(otpRequest).setTemplateContentId(1);
    verify(otpRequest).setTemplateCatalogId(1);
    verify(otpRequest).setTenantId(1);
    verify(otpRequest).setUpdatedAt(isA(OffsetDateTime.class));
    verify(otpRequest).setUpdatedBy(1);
    verify(repository).findByIdentityForUpdate(isA(UUID.class));
    assertEquals("CANCELLED", actualVerifyResult.result());
    assertEquals(0, actualVerifyResult.attemptsRemaining());
  }

  /**
   * Test {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}.
   *
   * <p>Method under test: {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}
   */
  @Test
  void testVerifyGivenOtpRequestGetStatusReturnExpiredThenReturnResultIsExpired()
      throws UnsupportedEncodingException {
    
    OtpRequest otpRequest = mock(OtpRequest.class);
    when(otpRequest.getStatus()).thenReturn("EXPIRED");
    doNothing().when(otpRequest).setActiveSlotKey(Mockito.<String>any());
    doNothing().when(otpRequest).setAttemptCount(anyShort());
    doNothing().when(otpRequest).setBranchCode(Mockito.<String>any());
    doNothing().when(otpRequest).setCreatedAt(Mockito.<OffsetDateTime>any());
    doNothing().when(otpRequest).setCreatedBy(Mockito.<Integer>any());
    doNothing().when(otpRequest).setCustomerIdentity(Mockito.<Integer>any());
    doNothing().when(otpRequest).setExpiresAt(Mockito.<OffsetDateTime>any());
    doNothing().when(otpRequest).setIdempotencyKey(Mockito.<String>any());
    doNothing().when(otpRequest).setIdentity(Mockito.<UUID>any());
    doNothing().when(otpRequest).setMaxAttempts(anyShort());
    doNothing().when(otpRequest).setMsisdn(Mockito.<String>any());
    doNothing().when(otpRequest).setOtpHash(Mockito.<String>any());
    doNothing().when(otpRequest).setOtpLength(anyShort());
    doNothing().when(otpRequest).setOtpRequestId(Mockito.<Long>any());
    doNothing().when(otpRequest).setOtpSalt(Mockito.<byte[]>any());
    doNothing().when(otpRequest).setProviderRequestJson(Mockito.<String>any());
    doNothing().when(otpRequest).setProviderResponseId(Mockito.<String>any());
    doNothing().when(otpRequest).setStatus(Mockito.<String>any());
    doNothing().when(otpRequest).setTemplateContentId(Mockito.<Integer>any());
    doNothing().when(otpRequest).setTemplateCatalogId(Mockito.<Integer>any());
    doNothing().when(otpRequest).setTenantId(Mockito.<Integer>any());
    doNothing().when(otpRequest).setUpdatedAt(Mockito.<OffsetDateTime>any());
    doNothing().when(otpRequest).setUpdatedBy(Mockito.<Integer>any());
    otpRequest.setActiveSlotKey("Active Slot Key");
    otpRequest.setAttemptCount((short) 1);
    otpRequest.setBranchCode("HM1");
    otpRequest.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest.setCreatedBy(1);
    otpRequest.setCustomerIdentity(1);
    otpRequest.setExpiresAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest.setIdempotencyKey("Idempotency Key");
    otpRequest.setIdentity(UUID.randomUUID());
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
    when(repository.findByIdentityForUpdate(Mockito.<UUID>any())).thenReturn(ofResult);
    OtpVerifyService otpVerifyService =
        new OtpVerifyService(
            repository,
            new OtpCrypto(3, 19088743, 1, 1, 1, "Slot Key Secret"),
            "EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
    UUID requestId = UUID.randomUUID();

    VerifyOtpResponseDto actualVerifyResult =
        otpVerifyService.verify(
            requestId,
            new VerifyOtpDto(
                "Code",
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC)));

    verify(otpRequest, atLeast(1)).getStatus();
    verify(otpRequest).setActiveSlotKey("Active Slot Key");
    verify(otpRequest).setAttemptCount((short) 1);
    verify(otpRequest).setBranchCode("HM1");
    verify(otpRequest).setCreatedAt(isA(OffsetDateTime.class));
    verify(otpRequest).setCreatedBy(1);
    verify(otpRequest).setCustomerIdentity(1);
    verify(otpRequest).setExpiresAt(isA(OffsetDateTime.class));
    verify(otpRequest).setIdempotencyKey("Idempotency Key");
    verify(otpRequest).setIdentity(isA(UUID.class));
    verify(otpRequest).setMaxAttempts((short) 1);
    verify(otpRequest).setMsisdn("Msisdn");
    verify(otpRequest).setOtpHash("Otp Hash");
    verify(otpRequest).setOtpLength((short) 1);
    verify(otpRequest).setOtpRequestId(1L);
    verify(otpRequest).setOtpSalt(isA(byte[].class));
    verify(otpRequest).setProviderRequestJson("Metadata Json");
    verify(otpRequest).setProviderResponseId("42");
    verify(otpRequest).setStatus("Status");
    verify(otpRequest).setTemplateContentId(1);
    verify(otpRequest).setTemplateCatalogId(1);
    verify(otpRequest).setTenantId(1);
    verify(otpRequest).setUpdatedAt(isA(OffsetDateTime.class));
    verify(otpRequest).setUpdatedBy(1);
    verify(repository).findByIdentityForUpdate(isA(UUID.class));
    assertEquals("EXPIRED", actualVerifyResult.result());
    assertEquals(0, actualVerifyResult.attemptsRemaining());
  }

  /**
   * Test {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}.
   *
   * <p>Method under test: {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}
   */
  @Test
  void testVerifyGivenOtpRequestGetStatusReturnFailed()
      throws UnsupportedEncodingException {

    OtpRequest otpRequest = mock(OtpRequest.class);
    when(otpRequest.getStatus()).thenReturn("FAILED");
    doNothing().when(otpRequest).setActiveSlotKey(Mockito.<String>any());
    doNothing().when(otpRequest).setAttemptCount(anyShort());
    doNothing().when(otpRequest).setBranchCode(Mockito.<String>any());
    doNothing().when(otpRequest).setCreatedAt(Mockito.<OffsetDateTime>any());
    doNothing().when(otpRequest).setCreatedBy(Mockito.<Integer>any());
    doNothing().when(otpRequest).setCustomerIdentity(Mockito.<Integer>any());
    doNothing().when(otpRequest).setExpiresAt(Mockito.<OffsetDateTime>any());
    doNothing().when(otpRequest).setIdempotencyKey(Mockito.<String>any());
    doNothing().when(otpRequest).setIdentity(Mockito.<UUID>any());
    doNothing().when(otpRequest).setMaxAttempts(anyShort());
    doNothing().when(otpRequest).setMsisdn(Mockito.<String>any());
    doNothing().when(otpRequest).setOtpHash(Mockito.<String>any());
    doNothing().when(otpRequest).setOtpLength(anyShort());
    doNothing().when(otpRequest).setOtpRequestId(Mockito.<Long>any());
    doNothing().when(otpRequest).setOtpSalt(Mockito.<byte[]>any());
    doNothing().when(otpRequest).setProviderRequestJson(Mockito.<String>any());
    doNothing().when(otpRequest).setProviderResponseId(Mockito.<String>any());
    doNothing().when(otpRequest).setStatus(Mockito.<String>any());
    doNothing().when(otpRequest).setTemplateContentId(Mockito.<Integer>any());
    doNothing().when(otpRequest).setTemplateCatalogId(Mockito.<Integer>any());
    doNothing().when(otpRequest).setTenantId(Mockito.<Integer>any());
    doNothing().when(otpRequest).setUpdatedAt(Mockito.<OffsetDateTime>any());
    doNothing().when(otpRequest).setUpdatedBy(Mockito.<Integer>any());
    otpRequest.setActiveSlotKey("Active Slot Key");
    otpRequest.setAttemptCount((short) 1);
    otpRequest.setBranchCode("HM1");
    otpRequest.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest.setCreatedBy(1);
    otpRequest.setCustomerIdentity(1);
    otpRequest.setExpiresAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest.setIdempotencyKey("Idempotency Key");
    otpRequest.setIdentity(UUID.randomUUID());
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
    when(repository.findByIdentityForUpdate(Mockito.<UUID>any())).thenReturn(ofResult);
    OtpVerifyService otpVerifyService =
        new OtpVerifyService(
            repository,
            new OtpCrypto(3, 19088743, 1, 1, 1, "Slot Key Secret"),
            "EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
    UUID requestId = UUID.randomUUID();

    VerifyOtpResponseDto actualVerifyResult =
        otpVerifyService.verify(
            requestId,
            new VerifyOtpDto(
                "Code",
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC)));

    verify(otpRequest, atLeast(1)).getStatus();
    verify(otpRequest).setActiveSlotKey("Active Slot Key");
    verify(otpRequest).setAttemptCount((short) 1);
    verify(otpRequest).setBranchCode("HM1");
    verify(otpRequest).setCreatedAt(isA(OffsetDateTime.class));
    verify(otpRequest).setCreatedBy(1);
    verify(otpRequest).setCustomerIdentity(1);
    verify(otpRequest).setExpiresAt(isA(OffsetDateTime.class));
    verify(otpRequest).setIdempotencyKey("Idempotency Key");
    verify(otpRequest).setIdentity(isA(UUID.class));
    verify(otpRequest).setMaxAttempts((short) 1);
    verify(otpRequest).setMsisdn("Msisdn");
    verify(otpRequest).setOtpHash("Otp Hash");
    verify(otpRequest).setOtpLength((short) 1);
    verify(otpRequest).setOtpRequestId(1L);
    verify(otpRequest).setOtpSalt(isA(byte[].class));
    verify(otpRequest).setProviderRequestJson("Metadata Json");
    verify(otpRequest).setProviderResponseId("42");
    verify(otpRequest).setStatus("Status");
    verify(otpRequest).setTemplateContentId(1);
    verify(otpRequest).setTemplateCatalogId(1);
    verify(otpRequest).setTenantId(1);
    verify(otpRequest).setUpdatedAt(isA(OffsetDateTime.class));
    verify(otpRequest).setUpdatedBy(1);
    verify(repository).findByIdentityForUpdate(isA(UUID.class));
    assertEquals("FAILED", actualVerifyResult.result());
    assertEquals(0, actualVerifyResult.attemptsRemaining());
  }

  /**
   * Test {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}.
   *
   * <p>Method under test: {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}
   */
  @Test
  void testVerifyGivenOtpRequestGetStatusReturnVerified()
      throws UnsupportedEncodingException {
    OtpRequest otpRequest = mock(OtpRequest.class);
    when(otpRequest.getStatus()).thenReturn("VERIFIED");
    doNothing().when(otpRequest).setActiveSlotKey(Mockito.<String>any());
    doNothing().when(otpRequest).setAttemptCount(anyShort());
    doNothing().when(otpRequest).setBranchCode(Mockito.<String>any());
    doNothing().when(otpRequest).setCreatedAt(Mockito.<OffsetDateTime>any());
    doNothing().when(otpRequest).setCreatedBy(Mockito.<Integer>any());
    doNothing().when(otpRequest).setCustomerIdentity(Mockito.<Integer>any());
    doNothing().when(otpRequest).setExpiresAt(Mockito.<OffsetDateTime>any());
    doNothing().when(otpRequest).setIdempotencyKey(Mockito.<String>any());
    doNothing().when(otpRequest).setIdentity(Mockito.<UUID>any());
    doNothing().when(otpRequest).setMaxAttempts(anyShort());
    doNothing().when(otpRequest).setMsisdn(Mockito.<String>any());
    doNothing().when(otpRequest).setOtpHash(Mockito.<String>any());
    doNothing().when(otpRequest).setOtpLength(anyShort());
    doNothing().when(otpRequest).setOtpRequestId(Mockito.<Long>any());
    doNothing().when(otpRequest).setOtpSalt(Mockito.<byte[]>any());
    doNothing().when(otpRequest).setProviderRequestJson(Mockito.<String>any());
    doNothing().when(otpRequest).setProviderResponseId(Mockito.<String>any());
    doNothing().when(otpRequest).setStatus(Mockito.<String>any());
    doNothing().when(otpRequest).setTemplateContentId(Mockito.<Integer>any());
    doNothing().when(otpRequest).setTemplateCatalogId(Mockito.<Integer>any());
    doNothing().when(otpRequest).setTenantId(Mockito.<Integer>any());
    doNothing().when(otpRequest).setUpdatedAt(Mockito.<OffsetDateTime>any());
    doNothing().when(otpRequest).setUpdatedBy(Mockito.<Integer>any());
    otpRequest.setActiveSlotKey("Active Slot Key");
    otpRequest.setAttemptCount((short) 1);
    otpRequest.setBranchCode("HM1");
    otpRequest.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest.setCreatedBy(1);
    otpRequest.setCustomerIdentity(1);
    otpRequest.setExpiresAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest.setIdempotencyKey("Idempotency Key");
    otpRequest.setIdentity(UUID.randomUUID());
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
    when(repository.findByIdentityForUpdate(Mockito.<UUID>any())).thenReturn(ofResult);
    OtpVerifyService otpVerifyService =
        new OtpVerifyService(
            repository,
            new OtpCrypto(3, 19088743, 1, 1, 1, "Slot Key Secret"),
            "EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
    UUID requestId = UUID.randomUUID();

    VerifyOtpResponseDto actualVerifyResult =
        otpVerifyService.verify(
            requestId,
            new VerifyOtpDto(
                "Code",
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC)));

    verify(otpRequest, atLeast(1)).getStatus();
    verify(otpRequest).setActiveSlotKey("Active Slot Key");
    verify(otpRequest).setAttemptCount((short) 1);
    verify(otpRequest).setBranchCode("HM1");
    verify(otpRequest).setCreatedAt(isA(OffsetDateTime.class));
    verify(otpRequest).setCreatedBy(1);
    verify(otpRequest).setCustomerIdentity(1);
    verify(otpRequest).setExpiresAt(isA(OffsetDateTime.class));
    verify(otpRequest).setIdempotencyKey("Idempotency Key");
    verify(otpRequest).setIdentity(isA(UUID.class));
    verify(otpRequest).setMaxAttempts((short) 1);
    verify(otpRequest).setMsisdn("Msisdn");
    verify(otpRequest).setOtpHash("Otp Hash");
    verify(otpRequest).setOtpLength((short) 1);
    verify(otpRequest).setOtpRequestId(1L);
    verify(otpRequest).setOtpSalt(isA(byte[].class));
    verify(otpRequest).setProviderRequestJson("Metadata Json");
    verify(otpRequest).setProviderResponseId("42");
    verify(otpRequest).setStatus("Status");
    verify(otpRequest).setTemplateContentId(1);
    verify(otpRequest).setTemplateCatalogId(1);
    verify(otpRequest).setTenantId(1);
    verify(otpRequest).setUpdatedAt(isA(OffsetDateTime.class));
    verify(otpRequest).setUpdatedBy(1);
    verify(repository).findByIdentityForUpdate(isA(UUID.class));
    assertEquals("VERIFIED", actualVerifyResult.result());
    assertEquals(0, actualVerifyResult.attemptsRemaining());
  }

  /**
   * Test {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}.
   *
   * <p>Method under test: {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}
   */
  @Test
  void testVerifyGivenOtpRequestRepositoryFindByIdentityForUpdateReturnEmpty() {
    OtpRequestRepository repository = mock(OtpRequestRepository.class);
    Optional<OtpRequest> emptyResult = Optional.empty();
    when(repository.findByIdentityForUpdate(Mockito.<UUID>any())).thenReturn(emptyResult);
    OtpVerifyService otpVerifyService =
        new OtpVerifyService(
            repository,
            new OtpCrypto(3, 19088743, 1, 1, 1, "Slot Key Secret"),
            "EXAMPLEKEYwjalrXUtnFEMI/K7MDENG/bPxRfiCY");
    UUID requestId = UUID.randomUUID();

    assertThrows(
        IllegalArgumentException.class,
        () ->
            otpVerifyService.verify(
                requestId,
                new VerifyOtpDto(
                    "Code",
                    OffsetDateTime.of(
                        LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC))));
    verify(repository).findByIdentityForUpdate(isA(UUID.class));
  }

  /**
   * Test {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}.
   *
   *
   * <p>Method under test: {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}
   */
  @Test
  void testVerifyGivenOtpRequestRepositoryFindByIdentityForUpdateReturnOfOtpRequest()
      throws UnsupportedEncodingException {
    OtpRequest otpRequest = new OtpRequest();
    otpRequest.setActiveSlotKey("Active Slot Key");
    otpRequest.setAttemptCount((short) 1);
    otpRequest.setBranchCode("HM1");
    otpRequest.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest.setCreatedBy(1);
    otpRequest.setCustomerIdentity(1);
    otpRequest.setExpiresAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest.setIdempotencyKey("Idempotency Key");
    otpRequest.setIdentity(UUID.randomUUID());
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
    OtpRequest otpRequest2 = new OtpRequest();
    otpRequest2.setActiveSlotKey("Active Slot Key");
    otpRequest2.setAttemptCount((short) 1);
    otpRequest2.setBranchCode("HM1");
    otpRequest2.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest2.setCreatedBy(1);
    otpRequest2.setCustomerIdentity(1);
    otpRequest2.setExpiresAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest2.setIdempotencyKey("Idempotency Key");
    otpRequest2.setIdentity(UUID.randomUUID());
    otpRequest2.setMaxAttempts((short) 1);
    otpRequest2.setMsisdn("Msisdn");
    otpRequest2.setOtpHash("Otp Hash");
    otpRequest2.setOtpLength((short) 1);
    otpRequest2.setOtpRequestId(1L);
    otpRequest2.setOtpSalt("AXAXAXAX".getBytes("UTF-8"));
    otpRequest2.setProviderRequestJson("Metadata Json");
    otpRequest2.setProviderResponseId("42");
    otpRequest2.setStatus("Status");
    otpRequest2.setTemplateContentId(1);
    otpRequest2.setTemplateCatalogId(1);
    otpRequest2.setTenantId(1);
    otpRequest2.setUpdatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest2.setUpdatedBy(1);
    when(otpRequestRepository.save(Mockito.<OtpRequest>any())).thenReturn(otpRequest2);
    when(otpRequestRepository.findByIdentityForUpdate(Mockito.<UUID>any())).thenReturn(ofResult);
    UUID requestId = UUID.randomUUID();

    VerifyOtpResponseDto actualVerifyResult =
        otpVerifyService.verify(
            requestId,
            new VerifyOtpDto(
                "Code",
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC)));
    verify(otpRequestRepository).findByIdentityForUpdate(isA(UUID.class));
    verify(otpRequestRepository).save(isA(OtpRequest.class));
    assertEquals(0, actualVerifyResult.attemptsRemaining());
  }

  /**
   * Test {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}.
   * <p>Method under test: {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}
   */
  @Test
  void testVerifyGivenOtpRequestRepositorySaveThrowIllegalArgumentException()
      throws UnsupportedEncodingException {
    OtpRequest otpRequest = new OtpRequest();
    otpRequest.setActiveSlotKey("Active Slot Key");
    otpRequest.setAttemptCount((short) 1);
    otpRequest.setBranchCode("HM1");
    otpRequest.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest.setCreatedBy(1);
    otpRequest.setCustomerIdentity(1);
    otpRequest.setExpiresAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest.setIdempotencyKey("Idempotency Key");
    otpRequest.setIdentity(UUID.randomUUID());
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
    when(otpRequestRepository.save(Mockito.<OtpRequest>any()))
        .thenThrow(new IllegalArgumentException());
    when(otpRequestRepository.findByIdentityForUpdate(Mockito.<UUID>any())).thenReturn(ofResult);
    UUID requestId = UUID.randomUUID();
    assertThrows(
        IllegalArgumentException.class,
        () ->
            otpVerifyService.verify(
                requestId,
                new VerifyOtpDto(
                    "Code",
                    OffsetDateTime.of(
                        LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC))));
    verify(otpRequestRepository).findByIdentityForUpdate(isA(UUID.class));
    verify(otpRequestRepository).save(isA(OtpRequest.class));
  }

  /**
   * Test {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}.

   * <p>Method under test: {@link OtpVerifyService#verify(UUID, VerifyOtpDto)}
   */
  @Test
  void testVerifywhenVerifyOtpDtoWithCodeAndUpdatedAtIsNullThenCallsGetExpiresAt()
      throws UnsupportedEncodingException {
    OtpRequest otpRequest = mock(OtpRequest.class);
    when(otpRequest.getStatus()).thenReturn("Status");
    when(otpRequest.getExpiresAt())
        .thenReturn(
            OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    doNothing().when(otpRequest).setActiveSlotKey(Mockito.<String>any());
    doNothing().when(otpRequest).setAttemptCount(anyShort());
    doNothing().when(otpRequest).setBranchCode(Mockito.<String>any());
    doNothing().when(otpRequest).setCreatedAt(Mockito.<OffsetDateTime>any());
    doNothing().when(otpRequest).setCreatedBy(Mockito.<Integer>any());
    doNothing().when(otpRequest).setCustomerIdentity(Mockito.<Integer>any());
    doNothing().when(otpRequest).setExpiresAt(Mockito.<OffsetDateTime>any());
    doNothing().when(otpRequest).setIdempotencyKey(Mockito.<String>any());
    doNothing().when(otpRequest).setIdentity(Mockito.<UUID>any());
    doNothing().when(otpRequest).setMaxAttempts(anyShort());
    doNothing().when(otpRequest).setMsisdn(Mockito.<String>any());
    doNothing().when(otpRequest).setOtpHash(Mockito.<String>any());
    doNothing().when(otpRequest).setOtpLength(anyShort());
    doNothing().when(otpRequest).setOtpRequestId(Mockito.<Long>any());
    doNothing().when(otpRequest).setOtpSalt(Mockito.<byte[]>any());
    doNothing().when(otpRequest).setProviderRequestJson(Mockito.<String>any());
    doNothing().when(otpRequest).setProviderResponseId(Mockito.<String>any());
    doNothing().when(otpRequest).setStatus(Mockito.<String>any());
    doNothing().when(otpRequest).setTemplateContentId(Mockito.<Integer>any());
    doNothing().when(otpRequest).setTemplateCatalogId(Mockito.<Integer>any());
    doNothing().when(otpRequest).setTenantId(Mockito.<Integer>any());
    doNothing().when(otpRequest).setUpdatedAt(Mockito.<OffsetDateTime>any());
    doNothing().when(otpRequest).setUpdatedBy(Mockito.<Integer>any());
    otpRequest.setActiveSlotKey("Active Slot Key");
    otpRequest.setAttemptCount((short) 1);
    otpRequest.setBranchCode("HM1");
    otpRequest.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest.setCreatedBy(1);
    otpRequest.setCustomerIdentity(1);
    otpRequest.setExpiresAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest.setIdempotencyKey("Idempotency Key");
    otpRequest.setIdentity(UUID.randomUUID());
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
    OtpRequest otpRequest2 = new OtpRequest();
    otpRequest2.setActiveSlotKey("Active Slot Key");
    otpRequest2.setAttemptCount((short) 1);
    otpRequest2.setBranchCode("HM1");
    otpRequest2.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest2.setCreatedBy(1);
    otpRequest2.setCustomerIdentity(1);
    otpRequest2.setExpiresAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest2.setIdempotencyKey("Idempotency Key");
    otpRequest2.setIdentity(UUID.randomUUID());
    otpRequest2.setMaxAttempts((short) 1);
    otpRequest2.setMsisdn("Msisdn");
    otpRequest2.setOtpHash("Otp Hash");
    otpRequest2.setOtpLength((short) 1);
    otpRequest2.setOtpRequestId(1L);
    otpRequest2.setOtpSalt("AXAXAXAX".getBytes("UTF-8"));
    otpRequest2.setProviderRequestJson("Metadata Json");
    otpRequest2.setProviderResponseId("42");
    otpRequest2.setStatus("Status");
    otpRequest2.setTemplateContentId(1);
    otpRequest2.setTemplateCatalogId(1);
    otpRequest2.setTenantId(1);
    otpRequest2.setUpdatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    otpRequest2.setUpdatedBy(1);
    when(otpRequestRepository.save(Mockito.<OtpRequest>any())).thenReturn(otpRequest2);
    when(otpRequestRepository.findByIdentityForUpdate(Mockito.<UUID>any())).thenReturn(ofResult);
    UUID requestId = UUID.randomUUID();

    VerifyOtpResponseDto actualVerifyResult =
        otpVerifyService.verify(requestId, new VerifyOtpDto("Code", null));
    verify(otpRequest).getExpiresAt();
    verify(otpRequest, atLeast(1)).getStatus();
    verify(otpRequest).setActiveSlotKey("Active Slot Key");
    verify(otpRequest).setAttemptCount((short) 1);
    verify(otpRequest).setBranchCode("HM1");
    verify(otpRequest).setCreatedAt(isA(OffsetDateTime.class));
    verify(otpRequest).setCreatedBy(1);
    verify(otpRequest).setCustomerIdentity(1);
    verify(otpRequest).setExpiresAt(isA(OffsetDateTime.class));
    verify(otpRequest).setIdempotencyKey("Idempotency Key");
    verify(otpRequest).setIdentity(isA(UUID.class));
    verify(otpRequest).setMaxAttempts((short) 1);
    verify(otpRequest).setMsisdn("Msisdn");
    verify(otpRequest).setOtpHash("Otp Hash");
    verify(otpRequest).setOtpLength((short) 1);
    verify(otpRequest).setOtpRequestId(1L);
    verify(otpRequest).setOtpSalt(isA(byte[].class));
    verify(otpRequest).setProviderRequestJson("Metadata Json");
    verify(otpRequest).setProviderResponseId("42");
    verify(otpRequest, atLeast(1)).setStatus(Mockito.<String>any());
    verify(otpRequest).setTemplateContentId(1);
    verify(otpRequest).setTemplateCatalogId(1);
    verify(otpRequest).setTenantId(1);
    verify(otpRequest, atLeast(1)).setUpdatedAt(isA(OffsetDateTime.class));
    verify(otpRequestRepository).findByIdentityForUpdate(isA(UUID.class));
    verify(otpRequestRepository).save(isA(OtpRequest.class));
    assertEquals(0, actualVerifyResult.attemptsRemaining());
  }
}
