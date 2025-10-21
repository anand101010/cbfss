package com.incede.nbfc.core.monolith.masterdata.controller;

import com.incede.nbfc.core.monolith.masterdata.dto.PepCategoriesView;
import com.incede.nbfc.core.monolith.masterdata.dto.PepRelationshipsView;
import com.incede.nbfc.core.monolith.masterdata.dto.PepVerificationSourceView;
import com.incede.nbfc.core.monolith.masterdata.service.PepMasterDataService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/master")
@Slf4j
@Tag(name = "Master Data", description = "Master Data Management APIs")
public class PepMasterDataController
{
    private final PepMasterDataService pepMasterDataService;
    /**
     * Get all Pep Categories View   .
     *
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/pep-categories")
    @Operation(summary = "Get all Pep Categories", description = "Retrieves all Pep Categories from the system")
    public ResponseEntity<List<PepCategoriesView>> getAllPepCategories(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching pep categories by tenantIdentity={}\",tenantIdentity");
        List<PepCategoriesView> pepCategoriesView =pepMasterDataService.getAllPepCategories(tenantIdentity);
        log.info("Found {} pep categories", pepCategoriesView.size());
        return ResponseEntity.ok(pepCategoriesView);
    }

    /**
     * Get all Pep relationships View .
     *
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/pep-relationships")
    @Operation(summary = "Get all Pep relationships", description = "Retrieves all Pep relationships from the system")
    public ResponseEntity<List<PepRelationshipsView>> getAllPepRelationships(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching pep relationships by tenantIdentity={}\",tenantIdentity");
        List<PepRelationshipsView> pepRelationshipsView =pepMasterDataService.getAllPepRelationships(tenantIdentity);
        log.info("Found {} pep relation", pepRelationshipsView.size());
        return ResponseEntity.ok(pepRelationshipsView);
    }

    /**
     * Get all Pep verification source .
     *
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/pep-verification-source")
    @Operation(summary = "Get all Pep verification source", description = "Retrieves all Pep verification source from the system")
    public ResponseEntity<List<PepVerificationSourceView>> getAllPepVerificationSource(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching pep verification source by tenantIdentity={}\",tenantIdentity");
        List<PepVerificationSourceView> pepVerificationSourceView =pepMasterDataService.getAllPepVerificationSource(tenantIdentity);
        log.info("Found {} pep verification source", pepVerificationSourceView.size());
        return ResponseEntity.ok(pepVerificationSourceView);
    }

}
