package com.incede.nbfc.core.monolith.masterdata.controller;


import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.service.LeadMasterDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/master")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Master Data", description = "Lead Master Data APIs")
public class LeadMasterDataController {

    private final LeadMasterDataService leadMasterDataService;

    /**
     * Retrieves all active and non-deleted additional reference configurations.
     *
     * @return HTTP 200 OK with a list of {@link AdditionalReferenceConfigView}
     */
    @GetMapping("/additional-reference-configs")
    @Operation(summary = "Get all additional reference configs", description = "Retrieves all active reference configs")
    public ResponseEntity<List<AdditionalReferenceConfigView>> getAllAdditionalReferenceConfigs() {
        log.info("Fetching all additional reference configs");
        List<AdditionalReferenceConfigView> configs = leadMasterDataService.getAllAdditionalReferenceConfigs();
        return ResponseEntity.ok(configs);
    }

    /**
     * Retrieves all active and non-deleted lead sources.
     *
     * @return HTTP 200 OK with a list of {@link LeadSourceView}
     */
    @GetMapping("/lead-sources")
    @Operation(summary = "Get all lead sources", description = "Retrieves all active lead sources")
    public ResponseEntity<List<LeadSourceView>> getAllLeadSources() {
        log.info("Fetching all lead sources");
        List<LeadSourceView> sources = leadMasterDataService.getAllLeadSources();
        return ResponseEntity.ok(sources);
    }

    /**
     * Retrieves all active and non-deleted lead stages.
     *
     * @return HTTP 200 OK with a list of {@link LeadStageView}
     */
    @GetMapping("/lead-stages")
    @Operation(summary = "Get all lead stages", description = "Retrieves all active lead stages")
    public ResponseEntity<List<LeadStageView>> getAllLeadStages() {
        log.info("Fetching all lead stages");
        List<LeadStageView> stages = leadMasterDataService.getAllLeadStages();
        return ResponseEntity.ok(stages);
    }

    /**
     * Retrieves all active and non-deleted follow-up types.
     *
     * @return HTTP 200 OK with a list of {@link FollowUpTypeView}
     */
    @GetMapping("/follow-up-types")
    @Operation(summary = "Get all follow-up types", description = "Retrieves all active follow-up types")
    public ResponseEntity<List<FollowUpTypeView>> getAllFollowUpTypes() {
        log.info("Fetching all follow-up types");
        List<FollowUpTypeView> types = leadMasterDataService.getAllFollowUpTypes();
        return ResponseEntity.ok(types);
    }

    /**
     * Retrieves all active and non-deleted lead statuses.
     *
     * @return HTTP 200 OK with a list of {@link LeadStatusView}
     */
    @GetMapping("/lead-statuses")
    @Operation(summary = "Get all lead statuses", description = "Retrieves all active lead statuses")
    public ResponseEntity<List<LeadStatusView>> getAllLeadStatuses() {
        log.info("Fetching all lead statuses");
        List<LeadStatusView> statuses = leadMasterDataService.getAllLeadStatuses();
        return ResponseEntity.ok(statuses);
    }

}
