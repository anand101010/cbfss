package com.incede.nbfc.notification.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.incede.nbfc.notification.crypto.OtpCrypto;
import com.incede.nbfc.notification.domain.TemplateCatalog;
import com.incede.nbfc.notification.dto.TemplateContentsRequestDto;
import com.incede.nbfc.notification.dto.response.TemplateContentsResponse;
import com.incede.nbfc.notification.exception.ResourceNotFoundException;
import com.incede.nbfc.notification.repository.TemplateCatalogRepository;
import com.incede.nbfc.notification.repository.TemplateContentsRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TemplateContentsService.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class TemplateContentsServiceTest {
    @MockBean
    private OtpCrypto otpCrypto;

    @MockBean
    private TemplateCatalogRepository templateCatalogRepository;

    @MockBean
    private TemplateContentsRepository templateContentsRepository;

    @Autowired
    private TemplateContentsService templateContentsService;

    /**
     * Test {@link TemplateContentsService#createTemplateContent(TemplateContentsRequestDto)}.
     * TemplateContentsService#createTemplateContent(TemplateContentsRequest)}
     */
    @Test
    void testCreateTemplateContent() {
        when(templateCatalogRepository.findById(Mockito.<Integer>any()))
                .thenThrow(
                        new DataIntegrityViolationException("triggered template Content service Method"));
        TemplateContentsRequestDto request = new TemplateContentsRequestDto();
        request.setBody("Not all who wander are lost");
        request.setChannel("Channel");
        request.setDltPrincipalId("42");
        request.setDltTemplateId("42");
        request.setFooter("Footer");
        request.setIsActive(true);
        request.setIsDel(true);
        request.setLanguageCode("en");
        request.setMaxLength(3);
        request.setProviderParams("Provider Params");
        request.setPurpose("Purpose");
        request.setSenderId("42");
        request.setSubject("Hello from the Dreaming Spires");
        request.setTemplateId(1);
        assertThrows(
                DataIntegrityViolationException.class,
                () -> templateContentsService.createTemplateContent(request));
        verify(templateCatalogRepository).findById(1);
    }

    /**
     * Test {@link TemplateContentsService#createTemplateContent(TemplateContentsRequestDto)}.
     * TemplateContentsService#createTemplateContent(TemplateContentsRequest)}
     */
    @Test
    void testCreateTemplateContent4() {
        doNothing()
                .when(templateContentsRepository)
                .saveNative(
                        Mockito.<Integer>any(),
                        Mockito.<String>any(),
                        Mockito.<String>any(),
                        Mockito.<String>any(),
                        Mockito.<String>any(),
                        Mockito.<String>any(),
                        Mockito.<Integer>any(),
                        Mockito.<String>any(),
                        Mockito.<String>any(),
                        Mockito.<String>any(),
                        Mockito.<String>any(),
                        Mockito.<String>any(),
                        Mockito.<Boolean>any(),
                        Mockito.<Integer>any(),
                        Mockito.<OffsetDateTime>any(),
                        Mockito.<Integer>any(),
                        Mockito.<OffsetDateTime>any(),
                        Mockito.<Boolean>any(),
                        Mockito.<UUID>any());
        TemplateCatalog templateCatalog = new TemplateCatalog();
        templateCatalog.setCategory(1);
        templateCatalog.setChannel(1);
        templateCatalog.setCode("Code");
        templateCatalog.setCreatedAt(
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        templateCatalog.setCreatedBy(1);
        templateCatalog.setDescription("The characteristics of someone or something");
        templateCatalog.setIdentity(UUID.randomUUID());
        templateCatalog.setIsActive(true);
        templateCatalog.setName("Name");
        templateCatalog.setTemplateCatalogId(1);
        templateCatalog.setTenantId(1);
        templateCatalog.setUpdatedAt(
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        templateCatalog.setUpdatedBy(1);
        Optional<TemplateCatalog> ofResult = Optional.of(templateCatalog);
        when(templateCatalogRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        TemplateContentsRequestDto request = mock(TemplateContentsRequestDto.class);
        when(request.getIsActive()).thenReturn(null);
        when(request.getIsDel()).thenReturn(null);
        when(request.getProviderParams()).thenReturn(null);
        when(request.getMaxLength()).thenReturn(3);
        when(request.getTemplateId()).thenReturn(1);
        when(request.getBody()).thenReturn("Not all who wander are lost");
        when(request.getChannel()).thenReturn("Channel");
        when(request.getDltPrincipalId()).thenReturn("42");
        when(request.getDltTemplateId()).thenReturn("42");
        when(request.getFooter()).thenReturn("Footer");
        when(request.getLanguageCode()).thenReturn("en");
        when(request.getSenderId()).thenReturn("42");
        when(request.getSubject()).thenReturn("Hello from the Dreaming Spires");
        doNothing().when(request).setBody(Mockito.<String>any());
        doNothing().when(request).setChannel(Mockito.<String>any());
        doNothing().when(request).setDltPrincipalId(Mockito.<String>any());
        doNothing().when(request).setDltTemplateId(Mockito.<String>any());
        doNothing().when(request).setFooter(Mockito.<String>any());
        doNothing().when(request).setIsActive(Mockito.<Boolean>any());
        doNothing().when(request).setIsDel(Mockito.<Boolean>any());
        doNothing().when(request).setLanguageCode(Mockito.<String>any());
        doNothing().when(request).setMaxLength(Mockito.<Integer>any());
        doNothing().when(request).setProviderParams(Mockito.<String>any());
        doNothing().when(request).setPurpose(Mockito.<String>any());
        doNothing().when(request).setSenderId(Mockito.<String>any());
        doNothing().when(request).setSubject(Mockito.<String>any());
        doNothing().when(request).setTemplateId(Mockito.<Integer>any());
        request.setBody("Not all who wander are lost");
        request.setChannel("Channel");
        request.setDltPrincipalId("42");
        request.setDltTemplateId("42");
        request.setFooter("Footer");
        request.setIsActive(true);
        request.setIsDel(true);
        request.setLanguageCode("en");
        request.setMaxLength(3);
        request.setProviderParams("Provider Params");
        request.setPurpose("Purpose");
        request.setSenderId("42");
        request.setSubject("Hello from the Dreaming Spires");
        request.setTemplateId(1);


        TemplateContentsResponse actualCreateTemplateContentResult =
                templateContentsService.createTemplateContent(request);

        verify(request, atLeast(1)).getBody();
        verify(request, atLeast(1)).getChannel();
        verify(request, atLeast(1)).getDltPrincipalId();
        verify(request, atLeast(1)).getDltTemplateId();
        verify(request, atLeast(1)).getFooter();
        verify(request).getIsActive();
        verify(request).getIsDel();
        verify(request, atLeast(1)).getLanguageCode();
        verify(request, atLeast(1)).getMaxLength();
        verify(request).getProviderParams();
        verify(request, atLeast(1)).getSenderId();
        verify(request, atLeast(1)).getSubject();
        verify(request).getTemplateId();
        verify(request).setBody("Not all who wander are lost");
        verify(request).setChannel("Channel");
        verify(request).setDltPrincipalId("42");
        verify(request).setDltTemplateId("42");
        verify(request).setFooter("Footer");
        verify(request).setIsActive(true);
        verify(request).setIsDel(true);
        verify(request).setLanguageCode("en");
        verify(request).setMaxLength(3);
        verify(request).setProviderParams("Provider Params");
        verify(request).setPurpose("Purpose");
        verify(request).setSenderId("42");
        verify(request).setSubject("Hello from the Dreaming Spires");
        verify(request).setTemplateId(1);
        verify(templateContentsRepository)
                .saveNative(
                        eq(1),
                        eq("Channel"),
                        eq("en"),
                        eq("Hello from the Dreaming Spires"),
                        eq("Not all who wander are lost"),
                        eq("Footer"),
                        eq(3),
                        eq("42"),
                        eq("{}"),
                        eq("42"),
                        eq("42"),
                        isNull(),
                        eq(true),
                        eq(1),
                        isA(OffsetDateTime.class),
                        isNull(),
                        isA(OffsetDateTime.class),
                        eq(false),
                        isA(UUID.class));
        verify(templateCatalogRepository).findById(1);
        assertEquals("42", actualCreateTemplateContentResult.getDltPrincipalId());
        assertEquals("42", actualCreateTemplateContentResult.getDltTemplateId());
        assertEquals("42", actualCreateTemplateContentResult.getSenderId());
        assertEquals("Channel", actualCreateTemplateContentResult.getChannel());
        assertEquals("Footer", actualCreateTemplateContentResult.getFooter());
        assertEquals("Hello from the Dreaming Spires", actualCreateTemplateContentResult.getSubject());
        assertEquals("Not all who wander are lost", actualCreateTemplateContentResult.getBody());
        assertEquals("en", actualCreateTemplateContentResult.getLanguageCode());
        assertEquals("{}", actualCreateTemplateContentResult.getProviderParams());
        assertNull(actualCreateTemplateContentResult.getChecksum());
        assertEquals(3, actualCreateTemplateContentResult.getMaxLength().intValue());
    }

    /**
     * Test {@link TemplateContentsService#createTemplateContent(TemplateContentsRequestDto)}.
     * TemplateContentsService#createTemplateContent(TemplateContentsRequest)}
     */
    @Test
    void testCreateTemplateContentGivenTemplateCatalogRepositoryFindByIdReturnEmpty() {
        Optional<TemplateCatalog> emptyResult = Optional.empty();
        when(templateCatalogRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);
        TemplateContentsRequestDto request = mock(TemplateContentsRequestDto.class);
        when(request.getTemplateId()).thenReturn(1);
        doNothing().when(request).setBody(Mockito.<String>any());
        doNothing().when(request).setChannel(Mockito.<String>any());
        doNothing().when(request).setDltPrincipalId(Mockito.<String>any());
        doNothing().when(request).setDltTemplateId(Mockito.<String>any());
        doNothing().when(request).setFooter(Mockito.<String>any());
        doNothing().when(request).setIsActive(Mockito.<Boolean>any());
        doNothing().when(request).setIsDel(Mockito.<Boolean>any());
        doNothing().when(request).setLanguageCode(Mockito.<String>any());
        doNothing().when(request).setMaxLength(Mockito.<Integer>any());
        doNothing().when(request).setProviderParams(Mockito.<String>any());
        doNothing().when(request).setPurpose(Mockito.<String>any());
        doNothing().when(request).setSenderId(Mockito.<String>any());
        doNothing().when(request).setSubject(Mockito.<String>any());
        doNothing().when(request).setTemplateId(Mockito.<Integer>any());
        request.setBody("Not all who wander are lost");
        request.setChannel("Channel");
        request.setDltPrincipalId("42");
        request.setDltTemplateId("42");
        request.setFooter("Footer");
        request.setIsActive(true);
        request.setIsDel(true);
        request.setLanguageCode("en");
        request.setMaxLength(3);
        request.setProviderParams("Provider Params");
        request.setPurpose("Purpose");
        request.setSenderId("42");
        request.setSubject("Hello from the Dreaming Spires");
        request.setTemplateId(1);

        assertThrows(
                ResourceNotFoundException.class,
                () -> templateContentsService.createTemplateContent(request));
        verify(request, atLeast(1)).getTemplateId();
        verify(request).setBody("Not all who wander are lost");
        verify(request).setChannel("Channel");
        verify(request).setDltPrincipalId("42");
        verify(request).setDltTemplateId("42");
        verify(request).setFooter("Footer");
        verify(request).setIsActive(true);
        verify(request).setIsDel(true);
        verify(request).setLanguageCode("en");
        verify(request).setMaxLength(3);
        verify(request).setProviderParams("Provider Params");
        verify(request).setPurpose("Purpose");
        verify(request).setSenderId("42");
        verify(request).setSubject("Hello from the Dreaming Spires");
        verify(request).setTemplateId(1);
        verify(templateCatalogRepository).findById(1);
    }

    /**
     * Test {@link TemplateContentsService#createTemplateContent(TemplateContentsRequestDto)}.
     *
     * TemplateContentsService#createTemplateContent(TemplateContentsRequest)}
     */
    @Test
    void testCreateTemplateContentThenReturnProviderParams() {
        doNothing()
                .when(templateContentsRepository)
                .saveNative(
                        Mockito.<Integer>any(),
                        Mockito.<String>any(),
                        Mockito.<String>any(),
                        Mockito.<String>any(),
                        Mockito.<String>any(),
                        Mockito.<String>any(),
                        Mockito.<Integer>any(),
                        Mockito.<String>any(),
                        Mockito.<String>any(),
                        Mockito.<String>any(),
                        Mockito.<String>any(),
                        Mockito.<String>any(),
                        Mockito.<Boolean>any(),
                        Mockito.<Integer>any(),
                        Mockito.<OffsetDateTime>any(),
                        Mockito.<Integer>any(),
                        Mockito.<OffsetDateTime>any(),
                        Mockito.<Boolean>any(),
                        Mockito.<UUID>any());
        TemplateCatalog templateCatalog = new TemplateCatalog();
        templateCatalog.setCategory(1);
        templateCatalog.setChannel(1);
        templateCatalog.setCode("Code");
        templateCatalog.setCreatedAt(
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        templateCatalog.setCreatedBy(1);
        templateCatalog.setDescription("The characteristics of someone or something");
        templateCatalog.setIdentity(UUID.randomUUID());
        templateCatalog.setIsActive(true);
        templateCatalog.setName("Name");
        templateCatalog.setTemplateCatalogId(1);
        templateCatalog.setTenantId(1);
        templateCatalog.setUpdatedAt(
                OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        templateCatalog.setUpdatedBy(1);
        Optional<TemplateCatalog> ofResult = Optional.of(templateCatalog);
        when(templateCatalogRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        TemplateContentsRequestDto request = new TemplateContentsRequestDto();
        request.setBody("Not all who wander are lost");
        request.setChannel("Channel");
        request.setDltPrincipalId("42");
        request.setDltTemplateId("42");
        request.setFooter("Footer");
        request.setIsActive(true);
        request.setIsDel(true);
        request.setLanguageCode("en");
        request.setMaxLength(3);
        request.setProviderParams("Provider Params");
        request.setPurpose("Purpose");
        request.setSenderId("42");
        request.setSubject("Hello from the Dreaming Spires");
        request.setTemplateId(1);

        TemplateContentsResponse actualCreateTemplateContentResult =
                templateContentsService.createTemplateContent(request);

        verify(templateContentsRepository)
                .saveNative(
                        eq(1),
                        eq("Channel"),
                        eq("en"),
                        eq("Hello from the Dreaming Spires"),
                        eq("Not all who wander are lost"),
                        eq("Footer"),
                        eq(3),
                        eq("42"),
                        eq("Provider Params"),
                        eq("42"),
                        eq("42"),
                        isNull(),
                        eq(true),
                        eq(1),
                        isA(OffsetDateTime.class),
                        isNull(),
                        isA(OffsetDateTime.class),
                        eq(true),
                        isA(UUID.class));
        verify(templateCatalogRepository).findById(1);
        assertEquals("42", actualCreateTemplateContentResult.getDltPrincipalId());
        assertEquals("42", actualCreateTemplateContentResult.getDltTemplateId());
        assertEquals("42", actualCreateTemplateContentResult.getSenderId());
        assertEquals("Channel", actualCreateTemplateContentResult.getChannel());
        assertEquals("Footer", actualCreateTemplateContentResult.getFooter());
        assertEquals("Hello from the Dreaming Spires", actualCreateTemplateContentResult.getSubject());
        assertEquals("Not all who wander are lost", actualCreateTemplateContentResult.getBody());
        assertEquals("Provider Params", actualCreateTemplateContentResult.getProviderParams());
        assertEquals("en", actualCreateTemplateContentResult.getLanguageCode());
        assertNull(actualCreateTemplateContentResult.getChecksum());
        assertEquals(3, actualCreateTemplateContentResult.getMaxLength().intValue());
    }
}
