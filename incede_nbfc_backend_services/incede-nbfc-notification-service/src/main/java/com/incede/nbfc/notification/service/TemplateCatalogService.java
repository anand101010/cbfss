package com.incede.nbfc.notification.service;

import com.incede.nbfc.notification.config.UserConfiguration;
import com.incede.nbfc.notification.domain.TemplateCatalog;
import com.incede.nbfc.notification.dto.TemplateCatalogRequestDto;
import com.incede.nbfc.notification.dto.response.TemplateResponseDto;
import com.incede.nbfc.notification.repository.TemplateCatalogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;
@Service
@Slf4j
public class TemplateCatalogService
{
    private final TemplateCatalogRepository templateCatalogRepository;

    public TemplateCatalogService(TemplateCatalogRepository templateCatalogRepository)
    {
        this.templateCatalogRepository = templateCatalogRepository;
    }

    /**
     *
     * @param templateRequestCatalogDto  receive the details of catalog
     * @return  the required details
     */
    public TemplateResponseDto saveTemplate(TemplateCatalogRequestDto templateRequestCatalogDto)
    {

        log.info("triggered template Catalog service");
        TemplateCatalog templateCatalog=new TemplateCatalog();
        templateCatalog.setTenantId(templateRequestCatalogDto.getTenantId());
        templateCatalog.setCode(templateRequestCatalogDto.getCode());
        templateCatalog.setName(templateRequestCatalogDto.getName());
        templateCatalog.setDescription(templateRequestCatalogDto.getDescription());
        templateCatalog.setCategory(templateRequestCatalogDto.getCategory());
        templateCatalog.setChannel(templateRequestCatalogDto.getChannel());
        templateCatalog.setIsActive(templateRequestCatalogDto.getIsActive());
        templateCatalog.setIsDelete(templateRequestCatalogDto.getIsDel());
        templateCatalog.setCreatedBy(UserConfiguration.getUser());
        templateCatalog.setCreatedAt(OffsetDateTime.now());
        templateCatalog.setIdentity(UUID.randomUUID());
        try
        {
            templateCatalogRepository.saveAndFlush(templateCatalog);
            log.info("save method triggered");
        } catch (DataIntegrityViolationException exception)
        {
            log.info("error in save method triggered :{}",exception);
            throw exception;
        }
       return new TemplateResponseDto(templateCatalog.getTenantId(),templateCatalog.getCode(),templateCatalog.getName(),templateCatalog.getDescription(),templateCatalog.getIdentity());
    }
}