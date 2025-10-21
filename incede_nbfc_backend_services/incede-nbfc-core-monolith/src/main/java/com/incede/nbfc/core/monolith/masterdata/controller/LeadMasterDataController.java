package com.incede.nbfc.core.monolith.masterdata.controller;


import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.service.LeadMasterDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "Master Data", description = "Lead Master Data APIs")
public class LeadMasterDataController {

    private final LeadMasterDataService leadMasterDataService;

    /**
     * Retrieves all additional reference configurations.
     *
     * @return HTTP 200 OK with a list of {@link AdditionalReferenceConfigView}
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/additional-reference-configs")
    @Operation(summary = "Get all additional reference configs", description = "Retrieves all active reference configs")
    public ResponseEntity<List<AdditionalReferenceConfigView>> getAllAdditionalReferenceConfigs(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity) {
        log.info("Fetching all additional reference configs by tenantIdentity={}\",tenantIdentity");
        List<AdditionalReferenceConfigView> configs = leadMasterDataService.getAllAdditionalReferenceConfigs(tenantIdentity);
        return ResponseEntity.ok(configs);
    }

    /**
     * Retrieves all lead sources.
     *
     * @return HTTP 200 OK with a list of {@link LeadSourceView}
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/lead-sources")
    @Operation(summary = "Get all lead sources", description = "Retrieves all active lead sources")
    public ResponseEntity<List<LeadSourceView>> getAllLeadSources(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity) {
        log.info("Fetching all lead sources by tenantIdentity={}\",tenantIdentity");
        List<LeadSourceView> sources = leadMasterDataService.getAllLeadSources(tenantIdentity);
        return ResponseEntity.ok(sources);
    }

    /**
     * Retrieves all lead stages.
     *
     * @return HTTP 200 OK with a list of {@link LeadStageView}
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/lead-stages")
    @Operation(summary = "Get all lead stages", description = "Retrieves all active lead stages")
    public ResponseEntity<List<LeadStageView>> getAllLeadStages(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity) {
        log.info("Fetching all lead stages by tenantIdentity={}\",tenantIdentity");
        List<LeadStageView> stages = leadMasterDataService.getAllLeadStages(tenantIdentity);
        return ResponseEntity.ok(stages);
    }

    /**
     * Retrieves all follow-up types.
     *
     * @return HTTP 200 OK with a list of {@link FollowUpTypeView}
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/follow-up-types")
    @Operation(summary = "Get all follow-up types", description = "Retrieves all active follow-up types")
    public ResponseEntity<List<FollowUpTypeView>> getAllFollowUpTypes(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity) {
        log.info("Fetching all follow-up types by tenantIdentity={}\",tenantIdentity");
        List<FollowUpTypeView> types = leadMasterDataService.getAllFollowUpTypes(tenantIdentity);
        return ResponseEntity.ok(types);
    }

    /**
     * Retrieves all lead statuses.
     *
     * @return HTTP 200 OK with a list of {@link LeadStatusView}
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/lead-statuses")
    @Operation(summary = "Get all lead statuses", description = "Retrieves all active lead statuses")
    public ResponseEntity<List<LeadStatusView>> getAllLeadStatuses(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity) {
        log.info("Fetching all lead statuses by tenantIdentity={}\",tenantIdentity");
        List<LeadStatusView> statuses = leadMasterDataService.getAllLeadStatuses(tenantIdentity);
        return ResponseEntity.ok(statuses);
    }

    /**
     * Retrieves all product services.
     *
     * @return HTTP 200 OK with a list of {@link ProductServiceView}
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/product-services")
    @Operation(summary = "Get all product services", description = "Retrieves all active product services")
    public ResponseEntity<List<ProductServiceView>> getAllProductServices(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity) {
        log.info("Fetching all product services by tenantIdentity={}\",tenantIdentity");
        List<ProductServiceView> products = leadMasterDataService.getAllProductServices(tenantIdentity);
        return ResponseEntity.ok(products);
    }

}
