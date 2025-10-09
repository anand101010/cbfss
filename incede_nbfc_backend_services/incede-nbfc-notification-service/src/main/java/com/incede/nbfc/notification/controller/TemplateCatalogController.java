package com.incede.nbfc.notification.controller;

import com.incede.nbfc.notification.dto.TemplateCatalogRequestDto;
import com.incede.nbfc.notification.dto.response.TemplateResponseDto;
import com.incede.nbfc.notification.service.TemplateCatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/template-catalog")
@Slf4j
public class TemplateCatalogController
{
    private final TemplateCatalogService templateCatalogService;

    public TemplateCatalogController(TemplateCatalogService templateCatalogService) {
        this.templateCatalogService = templateCatalogService;
    }

    /**
     *
     * @param templateCatalogRequestDto
     * @return TemplateResponseDto
     */
    @PostMapping("/")
    public ResponseEntity<TemplateResponseDto> createTemplate(@RequestBody TemplateCatalogRequestDto templateCatalogRequestDto)
    {
        log.info("triggered template Catalog");
        TemplateResponseDto savedTemplate = templateCatalogService.saveTemplate(templateCatalogRequestDto);
        return ResponseEntity.ok(savedTemplate);
    }


}
