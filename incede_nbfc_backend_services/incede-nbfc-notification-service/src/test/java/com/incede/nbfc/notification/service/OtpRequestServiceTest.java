package com.incede.nbfc.notification.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.incede.nbfc.notification.client.OtpNotificationFeignService;
import com.incede.nbfc.notification.crypto.OtpCrypto;
import com.incede.nbfc.notification.domain.Channel;
import com.incede.nbfc.notification.domain.OtpRequest;
import com.incede.nbfc.notification.domain.Purpose;
import com.incede.nbfc.notification.domain.TemplateCatalog;
import com.incede.nbfc.notification.domain.TemplateContents;
import com.incede.nbfc.notification.dto.RequestOtpDto;
import com.incede.nbfc.notification.dto.response.RequestOtpResponseDto;
import com.incede.nbfc.notification.exception.ResourceNotFoundException;
import com.incede.nbfc.notification.repository.OtpRequestRepository;
import com.incede.nbfc.notification.repository.TemplateCatalogRepository;
import com.incede.nbfc.notification.repository.TemplateContentsRepository;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OtpRequestService.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class OtpRequestServiceTest {
  @MockBean private OtpCrypto otpCrypto;

  @MockBean private OtpNotificationFeignService otpNotificationFeignService;

  @MockBean private OtpRequestRepository otpRequestRepository;

  @Autowired private OtpRequestService otpRequestService;

  @MockBean private TemplateCatalogRepository templateCatalogRepository;

  @MockBean private TemplateContentsRepository templateContentsRepository;

  /**
   * Test {@link OtpRequestService#requestOtp(String, RequestOtpDto)}.
   *
   * <p>Method under test: {@link OtpRequestService#requestOtp(String, RequestOtpDto)}
   */
  @Test
  void testRequestOtp() throws UnsupportedEncodingException {

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
    when(otpRequestRepository.saveAndFlush(Mockito.<OtpRequest>any())).thenReturn(otpRequest);
    when(otpCrypto.randomSalt(anyInt())).thenReturn("AXAXAXAX".getBytes("UTF-8"));
    when(otpCrypto.hashOtpCode(Mockito.<String>any(), Mockito.<String>any(), Mockito.<byte[]>any()))
        .thenReturn("Hash Otp Code");
    when(otpCrypto.normalizeTarget(Mockito.<String>any(), Mockito.<String>any()))
        .thenReturn("Normalize Target");
    when(otpCrypto.secureDigits(anyInt())).thenReturn("Secure Digits");
    when(otpCrypto.slotKey(
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
    Purpose purpose = new Purpose();
    purpose.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    purpose.setCreatedBy(1);
    purpose.setDescription("The characteristics of someone or something");
    purpose.setIdentity(UUID.randomUUID());
    purpose.setIsActive(true);
    purpose.setIsDelete(true);
    purpose.setName("Name");
    purpose.setPurposeId(1);
    purpose.setTenantId(1);
    purpose.setUpdatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    purpose.setUpdatedBy(1);
    TemplateCatalog template = new TemplateCatalog();
    template.setCategory(1);
    template.setChannel(1);
    template.setCode("Code");
    template.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    template.setCreatedBy(1);
    template.setDescription("The characteristics of someone or something");
    template.setIdentity(UUID.randomUUID());
    template.setIsActive(true);
    template.setName("Name");
    template.setTemplateCatalogId(1);
    template.setTenantId(1);
    template.setUpdatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    template.setUpdatedBy(1);
    TemplateContents templateContents = new TemplateContents();
    templateContents.setBody("Not all who wander are lost");
    templateContents.setChannel(channel);
    templateContents.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    templateContents.setCreatedBy(1);
    templateContents.setFooter("Footer");
    templateContents.setIdentity(UUID.randomUUID());
    templateContents.setIsActive(true);
    templateContents.setIsDelete(true);
    templateContents.setLanguageCode("en");
    templateContents.setPurpose(purpose);
    templateContents.setSubject("Hello from the Dreaming Spires");
    templateContents.setTemplate(template);
    templateContents.setTemplateContentId(1);
    templateContents.setUpdatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    templateContents.setUpdatedBy(1);
    Optional<TemplateContents> ofResult = Optional.of(templateContents);
    when(templateContentsRepository.findBodyByTenantIdAndTemplateCatalogIdAndPurposeAndChannel(
            Mockito.<Integer>any(),
            Mockito.<Integer>any(),
            Mockito.<Integer>any(),
            Mockito.<Integer>any()))
        .thenThrow(new DataIntegrityViolationException("Otp service request received "));
    when(templateContentsRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
    OffsetDateTime createdAt =
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);
    
    assertThrows(
        DataIntegrityViolationException.class,
        () ->
            otpRequestService.requestOtp(
                null,
                new RequestOtpDto(
                    1,
                    "HMCA",
                    1,
                    1,
                    "Target",
                    1,
                    null,
                    null,
                    "Context",
                    createdAt,
                    OffsetDateTime.of(
                        LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC))));
    verify(otpCrypto)
        .hashOtpCode(eq("${otp.hash.secretKey}"), eq("Secure Digits"), isA(byte[].class));
    verify(otpCrypto).normalizeTarget("Name", "Target");
    verify(otpCrypto).randomSalt(16);
    verify(otpCrypto).secureDigits(6);
    verify(otpCrypto).slotKey(1, "HMCA", "1", "Normalize Target");
    verify(templateContentsRepository)
        .findBodyByTenantIdAndTemplateCatalogIdAndPurposeAndChannel(1, 1, 1, 1);
    verify(otpRequestRepository).saveAndFlush(isA(OtpRequest.class));
    verify(templateContentsRepository).findById(1);
  }


  /**
   * Test {@link OtpRequestService#requestOtp(String, RequestOtpDto)}.
   
   * <p>Method under test: {@link OtpRequestService#requestOtp(String, RequestOtpDto)}
   */
  @Test
  
  void testRequestOtpGivenOtpCrypto() {

    when(templateContentsRepository.findById(Mockito.<Integer>any()))
        .thenThrow(new DataIntegrityViolationException("Otp service request received "));
    OffsetDateTime createdAt =
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);

    assertThrows(
        DataIntegrityViolationException.class,
        () ->
            otpRequestService.requestOtp(
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
                    OffsetDateTime.of(
                        LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC))));
    verify(templateContentsRepository).findById(1);
  }

  /**
   * Test {@link OtpRequestService#requestOtp(String, RequestOtpDto)}.
 
   * <p>Method under test: {@link OtpRequestService#requestOtp(String, RequestOtpDto)}
   */
  @Test
  void testRequestOtpGivenOtpRequestRepository() {

    when(otpCrypto.normalizeTarget(Mockito.<String>any(), Mockito.<String>any()))
        .thenThrow(new DataIntegrityViolationException("Otp service request received "));
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
    Purpose purpose = new Purpose();
    purpose.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    purpose.setCreatedBy(1);
    purpose.setDescription("The characteristics of someone or something");
    purpose.setIdentity(UUID.randomUUID());
    purpose.setIsActive(true);
    purpose.setIsDelete(true);
    purpose.setName("Name");
    purpose.setPurposeId(1);
    purpose.setTenantId(1);
    purpose.setUpdatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    purpose.setUpdatedBy(1);
    TemplateCatalog template = new TemplateCatalog();
    template.setCategory(1);
    template.setChannel(1);
    template.setCode("Code");
    template.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    template.setCreatedBy(1);
    template.setDescription("The characteristics of someone or something");
    template.setIdentity(UUID.randomUUID());
    template.setIsActive(true);
    template.setName("Name");
    template.setTemplateCatalogId(1);
    template.setTenantId(1);
    template.setUpdatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    template.setUpdatedBy(1);
    TemplateContents templateContents = new TemplateContents();
    templateContents.setBody("Not all who wander are lost");
    templateContents.setChannel(channel);
    templateContents.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    templateContents.setCreatedBy(1);
    templateContents.setFooter("Footer");
    templateContents.setIdentity(UUID.randomUUID());
    templateContents.setIsActive(true);
    templateContents.setIsDelete(true);
    templateContents.setLanguageCode("en");
    templateContents.setPurpose(purpose);
    templateContents.setSubject("Hello from the Dreaming Spires");
    templateContents.setTemplate(template);
    templateContents.setTemplateContentId(1);
    templateContents.setUpdatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    templateContents.setUpdatedBy(1);
    Optional<TemplateContents> ofResult = Optional.of(templateContents);
    when(templateContentsRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
    OffsetDateTime createdAt =
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);
    assertThrows(
        DataIntegrityViolationException.class,
        () ->
            otpRequestService.requestOtp(
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
                    OffsetDateTime.of(
                        LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC))));
    verify(otpCrypto).normalizeTarget("Name", "Target");
    verify(templateContentsRepository).findById(1);
  }

  /**
   * Test {@link OtpRequestService#requestOtp(String, RequestOtpDto)}.
   * <p>Method under test: {@link OtpRequestService#requestOtp(String, RequestOtpDto)}
   */
  @Test
  void testRequestOtpThenCallsFindByIdentityForUpdate() throws UnsupportedEncodingException {
 
    OtpRequest otpRequest = new OtpRequest();
    otpRequest.setActiveSlotKey("Active Slot Key");
    otpRequest.setAttemptCount((short) 1);
    otpRequest.setBranchCode("jjHM1");
    otpRequest.setBranchCode("jjHM1");
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
    when(otpRequestRepository.findByIdentityForUpdate(Mockito.<UUID>any()))
        .thenThrow(new DataIntegrityViolationException("Otp service request received "));
    when(otpRequestRepository.saveAndFlush(Mockito.<OtpRequest>any())).thenReturn(otpRequest);
    when(otpCrypto.appendOtpMessage(Mockito.<String>any(), Mockito.<String>any()))
        .thenReturn("Append Otp Message");
    when(otpCrypto.randomSalt(anyInt())).thenReturn("AXAXAXAX".getBytes("UTF-8"));
    when(otpCrypto.hashOtpCode(Mockito.<String>any(), Mockito.<String>any(), Mockito.<byte[]>any()))
        .thenReturn("Hash Otp Code");
    when(otpCrypto.normalizeTarget(Mockito.<String>any(), Mockito.<String>any()))
        .thenReturn("Normalize Target");
    when(otpCrypto.secureDigits(anyInt())).thenReturn("Secure Digits");
    when(otpCrypto.slotKey(
            Mockito.<Integer>any(),
            Mockito.<String>any(),
            Mockito.<String>any(),
            Mockito.<String>any()))
        .thenReturn("Slot Key");
    when(otpNotificationFeignService.generateOtpNotification(
            Mockito.<String>any(),
            Mockito.<String>any(),
            Mockito.<String>any(),
            Mockito.<String>any(),
            Mockito.<String>any(),
            Mockito.<String>any(),
            Mockito.<String>any()))
        .thenReturn("Generate Otp Notification");

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

    Purpose purpose = new Purpose();
    purpose.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    purpose.setCreatedBy(1);
    purpose.setDescription("The characteristics of someone or something");
    purpose.setIdentity(UUID.randomUUID());
    purpose.setIsActive(true);
    purpose.setIsDelete(true);
    purpose.setName("Name");
    purpose.setPurposeId(1);
    purpose.setTenantId(1);
    purpose.setUpdatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    purpose.setUpdatedBy(1);

    TemplateCatalog template = new TemplateCatalog();
    template.setCategory(1);
    template.setChannel(1);
    template.setCode("Code");
    template.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    template.setCreatedBy(1);
    template.setDescription("The characteristics of someone or something");
    template.setIdentity(UUID.randomUUID());
    template.setIsActive(true);
    template.setName("Name");
    template.setTemplateCatalogId(1);
    template.setTenantId(1);
    template.setUpdatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    template.setUpdatedBy(1);

    TemplateContents templateContents = new TemplateContents();
    templateContents.setBody("Not all who wander are lost");
    templateContents.setChannel(channel);
    templateContents.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    templateContents.setCreatedBy(1);
    templateContents.setFooter("Footer");
    templateContents.setIdentity(UUID.randomUUID());
    templateContents.setIsActive(true);
    templateContents.setIsDelete(true);
    templateContents.setLanguageCode("en");
    templateContents.setPurpose(purpose);
    templateContents.setSubject("Hello from the Dreaming Spires");
    templateContents.setTemplate(template);
    templateContents.setTemplateContentId(1);
    templateContents.setUpdatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    templateContents.setUpdatedBy(1);
    Optional<TemplateContents> ofResult = Optional.of(templateContents);
    Optional<String> ofResult2 = Optional.of("foo");
    when(templateContentsRepository.findBodyByTenantIdAndTemplateCatalogIdAndPurposeAndChannel(
            Mockito.<Integer>any(),
            Mockito.<Integer>any(),
            Mockito.<Integer>any(),
            Mockito.<Integer>any()))
        .thenReturn(ofResult2);
    when(templateContentsRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
    OffsetDateTime createdAt =
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);
    
    assertThrows(
        DataIntegrityViolationException.class,
        () ->
            otpRequestService.requestOtp(
                null,
                new RequestOtpDto(
                    1,
                    "HM1",
                    1,
                    1,
                    "Target",
                    1,
                    null,
                    null,
                    "Context",
                    createdAt,
                    OffsetDateTime.of(
                        LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC))));
    verify(otpNotificationFeignService)
        .generateOtpNotification(
            "${otp-vendor.api.enterpriseid}",
            "${otp-vendor.api.subEnterpriseid}",
            "${otp-vendor.api.pusheid}",
            "${otp-vendor.api.pushepwd}",
            "Normalize Target",
            "${otp-vendor.api.sender}",
            "Append Otp Message");
    verify(otpCrypto).appendOtpMessage("foo", "Secure Digits");
    verify(otpCrypto)
        .hashOtpCode(eq("${otp.hash.secretKey}"), eq("Secure Digits"), isA(byte[].class));
    verify(otpCrypto).normalizeTarget("Name", "Target");
    verify(otpCrypto).randomSalt(16);
    verify(otpCrypto).secureDigits(6);
    verify(otpCrypto).slotKey(1, "HM1", "1", "Normalize Target");
    verify(otpRequestRepository).findByIdentityForUpdate(isA(UUID.class));
    verify(templateContentsRepository)
        .findBodyByTenantIdAndTemplateCatalogIdAndPurposeAndChannel(1, 1, 1, 1);
    verify(otpRequestRepository).saveAndFlush(isA(OtpRequest.class));
    verify(templateContentsRepository).findById(1);
  }

  /**
   * Test {@link OtpRequestService#requestOtp(String, RequestOtpDto)}.
  
   * <p>Method under test: {@link OtpRequestService#requestOtp(String, RequestOtpDto)}
   */
  @Test
  
  void testRequestOtpThenCallsFindFirstByTenantIdAndBranchCodeAndIdempotencyKey() {

    when(otpRequestRepository.findFirstByTenantIdAndBranchCodeAndIdempotencyKey(
            Mockito.<Integer>any(), Mockito.<String>any(), Mockito.<String>any()))
        .thenThrow(new DataIntegrityViolationException("Otp service request received "));
    when(otpCrypto.normalizeTarget(Mockito.<String>any(), Mockito.<String>any()))
        .thenReturn("Normalize Target");
    when(otpCrypto.slotKey(
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
    Purpose purpose = new Purpose();
    purpose.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    purpose.setCreatedBy(1);
    purpose.setDescription("The characteristics of someone or something");
    purpose.setIdentity(UUID.randomUUID());
    purpose.setIsActive(true);
    purpose.setIsDelete(true);
    purpose.setName("Name");
    purpose.setPurposeId(1);
    purpose.setTenantId(1);
    purpose.setUpdatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    purpose.setUpdatedBy(1);
    TemplateCatalog template = new TemplateCatalog();
    template.setCategory(1);
    template.setChannel(1);
    template.setCode("Code");
    template.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    template.setCreatedBy(1);
    template.setDescription("The characteristics of someone or something");
    template.setIdentity(UUID.randomUUID());
    template.setIsActive(true);
    template.setName("Name");
    template.setTemplateCatalogId(1);
    template.setTenantId(1);
    template.setUpdatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    template.setUpdatedBy(1);
    TemplateContents templateContents = new TemplateContents();
    templateContents.setBody("Not all who wander are lost");
    templateContents.setChannel(channel);
    templateContents.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    templateContents.setCreatedBy(1);
    templateContents.setFooter("Footer");
    templateContents.setIdentity(UUID.randomUUID());
    templateContents.setIsActive(true);
    templateContents.setIsDelete(true);
    templateContents.setLanguageCode("en");
    templateContents.setPurpose(purpose);
    templateContents.setSubject("Hello from the Dreaming Spires");
    templateContents.setTemplate(template);
    templateContents.setTemplateContentId(1);
    templateContents.setUpdatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    templateContents.setUpdatedBy(1);
    Optional<TemplateContents> ofResult = Optional.of(templateContents);
    when(templateContentsRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
    OffsetDateTime createdAt =
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);
    
    assertThrows(
        DataIntegrityViolationException.class,
        () ->
            otpRequestService.requestOtp(
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
                    OffsetDateTime.of(
                        LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC))));
    verify(otpCrypto).normalizeTarget("Name", "Target");
    verify(otpCrypto).slotKey(1, "HM1", "1", "Normalize Target");
    verify(otpRequestRepository)
        .findFirstByTenantIdAndBranchCodeAndIdempotencyKey(
            1, "HM1", "Idempotency Key");
    verify(templateContentsRepository).findById(1);
  }

  /**
   * Test {@link OtpRequestService#requestOtp(String, RequestOtpDto)}.
   * <p>Method under test: {@link OtpRequestService#requestOtp(String, RequestOtpDto)}
   */
  @Test
  void testRequestOtpThenReturnStatus() throws UnsupportedEncodingException {
    
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

    Purpose purpose = new Purpose();
    purpose.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    purpose.setCreatedBy(1);
    purpose.setDescription("The characteristics of someone or something");
    purpose.setIdentity(UUID.randomUUID());
    purpose.setIsActive(true);
    purpose.setIsDelete(true);
    purpose.setName("Name");
    purpose.setPurposeId(1);
    purpose.setTenantId(1);
    purpose.setUpdatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    purpose.setUpdatedBy(1);

    TemplateCatalog template = new TemplateCatalog();
    template.setCategory(1);
    template.setChannel(1);
    template.setCode("Code");
    template.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    template.setCreatedBy(1);
    template.setDescription("The characteristics of someone or something");
    template.setIdentity(UUID.randomUUID());
    template.setIsActive(true);
    template.setName("Name");
    template.setTemplateCatalogId(1);
    template.setTenantId(1);
    template.setUpdatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    template.setUpdatedBy(1);

    TemplateContents templateContents = new TemplateContents();
    templateContents.setBody("Not all who wander are lost");
    templateContents.setChannel(channel);
    templateContents.setCreatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    templateContents.setCreatedBy(1);
    templateContents.setFooter("Footer");
    templateContents.setIdentity(UUID.randomUUID());
    templateContents.setIsActive(true);
    templateContents.setIsDelete(true);
    templateContents.setLanguageCode("en");
    templateContents.setPurpose(purpose);
    templateContents.setSubject("Hello from the Dreaming Spires");
    templateContents.setTemplate(template);
    templateContents.setTemplateContentId(1);
    templateContents.setUpdatedAt(
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
    templateContents.setUpdatedBy(1);
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
    OffsetDateTime createdAt =
        OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);
    
    RequestOtpResponseDto actualRequestOtpResult =
        otpRequestService.requestOtp(
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

    
    verify(repository)
        .findFirstByTenantIdAndBranchCodeAndIdempotencyKey(
            1, "HM1", "Idempotency Key");
    verify(templateContentsRepository).findById(1);
    assertEquals("Status", actualRequestOtpResult.status());
    assertSame(expiresAt, actualRequestOtpResult.expiresAt());
    assertSame(identity, actualRequestOtpResult.requestId());
  }



  /**
   * Test {@link OtpRequestService#getTemplateBody(Integer, Integer, Integer, Integer)}.
   *
   * <p>Method under test: {@link OtpRequestService#getTemplateBody(Integer, Integer, Integer,
   * Integer)}
   */
  @Test
  void testGetTemplateBodyThenThrowDataIntegrityViolationException() {
 
    when(templateContentsRepository.findBodyByTenantIdAndTemplateCatalogIdAndPurposeAndChannel(
            Mockito.<Integer>any(),
            Mockito.<Integer>any(),
            Mockito.<Integer>any(),
            Mockito.<Integer>any()))
        .thenThrow(new DataIntegrityViolationException("Template not found for given parameters"));

    
    assertThrows(
        DataIntegrityViolationException.class, () -> otpRequestService.getTemplateBody(1, 1, 1, 1));
    verify(templateContentsRepository)
        .findBodyByTenantIdAndTemplateCatalogIdAndPurposeAndChannel(1, 1, 1, 1);
  }

  /**
   * Test {@link OtpRequestService#getTemplateBody(Integer, Integer, Integer, Integer)}.
   *
   
   * <p>Method under test: {@link OtpRequestService#getTemplateBody(Integer, Integer, Integer,
   * Integer)}
   */
  @Test
  
  void testGetTemplateBodyThenThrowResourceNotFoundException() {

    Optional<String> emptyResult = Optional.empty();
    when(templateContentsRepository.findBodyByTenantIdAndTemplateCatalogIdAndPurposeAndChannel(
            Mockito.<Integer>any(),
            Mockito.<Integer>any(),
            Mockito.<Integer>any(),
            Mockito.<Integer>any()))
        .thenReturn(emptyResult);
    
    assertThrows(
        ResourceNotFoundException.class, () -> otpRequestService.getTemplateBody(1, 1, 1, 1));
    verify(templateContentsRepository)
        .findBodyByTenantIdAndTemplateCatalogIdAndPurposeAndChannel(1, 1, 1, 1);
  }
}
