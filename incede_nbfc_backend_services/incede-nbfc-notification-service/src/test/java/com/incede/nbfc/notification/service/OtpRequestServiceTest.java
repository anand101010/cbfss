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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    @MockBean private TemplateCatalogRepository templateCatalogRepository;
    @MockBean private TemplateContentsRepository templateContentsRepository;

    @Autowired private OtpRequestService otpRequestService;

    private OffsetDateTime getEpochTime() {
        return OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);
    }

    private Channel createChannel() {
        Channel channel = new Channel();
        channel.setChannelId(1);
        channel.setCreatedAt(getEpochTime());
        channel.setCreatedBy(1);
        channel.setDescription("Channel Description");
        channel.setIdentity(UUID.randomUUID());
        channel.setIsActive(true);
        channel.setIsDelete(false);
        channel.setName("Channel Name");
        channel.setTenantId(1);
        channel.setUpdatedAt(getEpochTime());
        channel.setUpdatedBy(1);
        return channel;
    }

    private Purpose createPurpose() {
        Purpose purpose = new Purpose();
        purpose.setCreatedAt(getEpochTime());
        purpose.setCreatedBy(1);
        purpose.setDescription("Purpose Description");
        purpose.setIdentity(UUID.randomUUID());
        purpose.setIsActive(true);
        purpose.setIsDelete(false);
        purpose.setName("Purpose Name");
        purpose.setPurposeId(1);
        purpose.setTenantId(1);
        purpose.setUpdatedAt(getEpochTime());
        purpose.setUpdatedBy(1);
        return purpose;
    }

    private TemplateCatalog createTemplateCatalog() {
        TemplateCatalog template = new TemplateCatalog();
        template.setCategory(1);
        template.setChannel(1);
        template.setCode("Code");
        template.setCreatedAt(getEpochTime());
        template.setCreatedBy(1);
        template.setDescription("Template Description");
        template.setIdentity(UUID.randomUUID());
        template.setIsActive(true);
        template.setName("Template Name");
        template.setTemplateCatalogId(1);
        template.setTenantId(1);
        template.setUpdatedAt(getEpochTime());
        template.setUpdatedBy(1);
        return template;
    }

    private TemplateContents createTemplateContents(Channel channel, Purpose purpose, TemplateCatalog template) {
        TemplateContents templateContents = new TemplateContents();
        templateContents.setBody("Template Body");
        templateContents.setChannel(channel);
        templateContents.setCreatedAt(getEpochTime());
        templateContents.setCreatedBy(1);
        templateContents.setFooter("Footer");
        templateContents.setIdentity(UUID.randomUUID());
        templateContents.setIsActive(true);
        templateContents.setIsDelete(false);
        templateContents.setLanguageCode("en");
        templateContents.setPurpose(purpose);
        templateContents.setSubject("Subject");
        templateContents.setTemplate(template);
        templateContents.setTemplateContentId(1);
        templateContents.setUpdatedAt(getEpochTime());
        templateContents.setUpdatedBy(1);
        return templateContents;
    }


    /** Test getTemplateBody throws DataIntegrityViolationException */
    @Test
    void testGetTemplateBodyThrowsDataIntegrityViolationException() {
        when(templateContentsRepository.findBodyByTenantIdAndTemplateCatalogIdAndPurposeAndChannel(anyInt(), anyInt(), anyInt(), anyInt()))
                .thenThrow(new DataIntegrityViolationException("Template not found"));

        assertThrows(
                DataIntegrityViolationException.class,
                () -> otpRequestService.getTemplateBody(1, 1, 1, 1));
    }

    /** Test getTemplateBody throws ResourceNotFoundException */
    @Test
    void testGetTemplateBodyThrowsResourceNotFoundException() {
        when(templateContentsRepository.findBodyByTenantIdAndTemplateCatalogIdAndPurposeAndChannel(anyInt(), anyInt(), anyInt(), anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> otpRequestService.getTemplateBody(1, 1, 1, 1));
    }
}
