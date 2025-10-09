package com.incede.nbfc.notification.service;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.incede.nbfc.notification.domain.TemplateCatalog;
import com.incede.nbfc.notification.dto.TemplateCatalogRequestDto;
import com.incede.nbfc.notification.dto.response.TemplateResponseDto;
import com.incede.nbfc.notification.repository.TemplateCatalogRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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

@ContextConfiguration(classes = {TemplateCatalogService.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class TemplateCatalogServiceTest {
    @MockBean
    private TemplateCatalogRepository templateCatalogRepository;

    @Autowired
    private TemplateCatalogService templateCatalogService;

    /**
     * Test {@link TemplateCatalogService#saveTemplate(TemplateCatalogRequestDto)}.
     *
     * <p>Method under test: {@link TemplateCatalogService#saveTemplate(TemplateCatalogRequestDto)}
     */
    @Test
    void testSaveTemplateGivenTemplateCatalogCategoryIsOneThenReturnTenantIdIsNull() {
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
        when(templateCatalogRepository.saveAndFlush(Mockito.<TemplateCatalog>any()))
                .thenReturn(templateCatalog);

        TemplateResponseDto actualSaveTemplateResult =
                templateCatalogService.saveTemplate(new TemplateCatalogRequestDto());

        verify(templateCatalogRepository).saveAndFlush(isA(TemplateCatalog.class));
        assertNull(actualSaveTemplateResult.getTenantId());
        assertNull(actualSaveTemplateResult.getCode());
        assertNull(actualSaveTemplateResult.getDescription());
        assertNull(actualSaveTemplateResult.getName());
    }

    /**
     * Test {@link TemplateCatalogService#saveTemplate(TemplateCatalogRequestDto)}.
     * <p>Method under test: {@link TemplateCatalogService#saveTemplate(TemplateCatalogRequestDto)}
     */
    @Test
    void testSaveTemplate_thenThrowDataIntegrityViolationException()
    {
        when(templateCatalogRepository.saveAndFlush(Mockito.<TemplateCatalog>any()))
                .thenThrow(new DataIntegrityViolationException("triggered template Catalog service"));
        assertThrows(
                DataIntegrityViolationException.class,
                () -> templateCatalogService.saveTemplate(new TemplateCatalogRequestDto()));
        verify(templateCatalogRepository).saveAndFlush(isA(TemplateCatalog.class));
    }
}
