package com.incede.nbfc.core.monolith.masterdata.controller;

import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.service.DocumentMasterDataService;
import com.incede.nbfc.core.monolith.masterdata.service.ProfileMasterDataService;
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
@Tag(name = "Master Data", description = "Document Data Management APIs")
public class DocumentMasterDataController
{

    private final DocumentMasterDataService documentMasterDataService;
    /**
     * Get all Kyc Types   .
     *
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/kyc-types")
    @Operation(summary = "Get all kyc types", description = "Retrieves all kyc types from the system")
    public ResponseEntity<List<KycTypesView>> getAllKycTypes(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all kyc types with the tenant Identity : {tenantIdentity}");
        List<KycTypesView> kycTypesView =documentMasterDataService.getAllKycTypes(tenantIdentity);
        log.info("Found {} kyc types", kycTypesView.size());
        return ResponseEntity.ok(kycTypesView);
    }


    /**
     * Get all document-master.
     *
     * @return List of all document-master
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/document-master")
    @Operation(summary = "Get all document master", description = "Retrieves all document master from the system")
    public ResponseEntity<List<DocumentMasterView>> getAllDocumentMasters(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all documents masters with the tenant Identity : {tenantIdentity}");
        List<DocumentMasterView> documentMaster =documentMasterDataService.getAllDocumentMasters(tenantIdentity);
        log.info("Found {} documents master", documentMaster.size());
        return ResponseEntity.ok(documentMaster);
    }



    /**
     * Get all Document -types   .
     *
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/document-types")
    @Operation(summary = "Get all document types", description = "Retrieves all document types from the system")
    public ResponseEntity<List<DocumentTypeView>> getAllDocumentType(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all document types with the tenant Identity : {tenantIdentity}");
        List<DocumentTypeView> documentTypeView =documentMasterDataService.getAllDocumentType(tenantIdentity);
        log.info("Found {} document types", documentTypeView.size());
        return ResponseEntity.ok(documentTypeView);
    }

    /**
     * Get all Canvassed Types  .
     *
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/canvassed-types")
    @Operation(summary = "Get all canvassed types", description = "Retrieves all canvassed types from the system")
    public ResponseEntity<List<CanvassedTypesView>> getAllCanvassedTypes(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all canvassed types with the tenant Identity : {tenantIdentity}");
        List<CanvassedTypesView> canvassedTypesView =documentMasterDataService.getAllCanvassedTypes(tenantIdentity);
        log.info("Found {} canvassed types", canvassedTypesView.size());
        return ResponseEntity.ok(canvassedTypesView);
    }


    /**
     * Get all Asset Types .
     *
     * @return List of all Asset Types
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/asset-types")
    @Operation(summary = "Get all asset Types", description = "Retrieves all asset Types from the system")
    public ResponseEntity<List<AssetTypesView>> getAllAssetTypes(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all asset Types with the tenant Identity : {tenantIdentity}");
        List<AssetTypesView> assetTypesView =documentMasterDataService.getAllAssetTypes(tenantIdentity);
        log.info("Found {} asset Types", assetTypesView.size());
        return ResponseEntity.ok(assetTypesView);
    }
}
