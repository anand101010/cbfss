package com.incede.nbfc.core.monolith.masterdata.controller;


import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.dto.AdditionalReferenceConfigDto;
import com.incede.nbfc.core.monolith.masterdata.service.AdditionalReferenceConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/master")
@RequiredArgsConstructor
@Slf4j
@Tag(name = " Additional Reference  Controller", description = "APIs for managing leads")
public class AdditionalReferenceConfigController
{

    public  final AdditionalReferenceConfigService additionalReferenceConfigService;

    @GetMapping("additional-reference-configurations")
    public ResponseEntity<List<AdditionalReferenceConfigDto>> getAdditionalReferenceConfigs(
            @RequestParam UUID tenantIdentity,
            @RequestParam UUID productServiceIdentity)
    {
        log.info("Fetching Additional Reference Configs for tenant, productService");
        if (tenantIdentity == null || productServiceIdentity == null) {
            throw new ResourceNotFoundException("Tenant Identity and Product Service Identity are required.");
        }
         List<AdditionalReferenceConfigDto> configDetails = additionalReferenceConfigService.getAdditionalReferenceConfig(tenantIdentity, productServiceIdentity);
        if (configDetails.isEmpty())
        {
            throw new ResourceNotFoundException("No active reference configs found for the provided details.");
        }
        return ResponseEntity.ok(configDetails);
    }


}
