package com.incede.nbfc.core.monolith.masterdata.controller;

import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.service.DocumentMasterDataService;
import com.incede.nbfc.core.monolith.masterdata.service.ProfileMasterDataService;
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
@Tag(name = "Master Data", description = "Document Data Management APIs")
public class DocumentMasterDataController
{

    private final DocumentMasterDataService documentMasterDataService;
    /**
     * Get all Kyc Types   .
     *
     */
    @GetMapping("/kyc-types")
    @Operation(summary = "Get all kyc types", description = "Retrieves all kyc types from the system")
    public ResponseEntity<List<KycTypesView>> getAllKycTypes(){
        log.info("Fetching all kyc types");
        List<KycTypesView> kycTypesView =documentMasterDataService.getAllKycTypes();
        log.info("Found {} kyc types", kycTypesView.size());
        return ResponseEntity.ok(kycTypesView);
    }


    /**
     * Get all document-master.
     *
     * @return List of all document-master
     */
    @GetMapping("/document-master")
    @Operation(summary = "Get all document master", description = "Retrieves all document master from the system")
    public ResponseEntity<List<DocumentMasterView>> getAllDocumentMasters(){
        log.info("Fetching all documents masters");
        List<DocumentMasterView> documentMaster =documentMasterDataService.getAllDocumentMasters();
        log.info("Found {} documents master", documentMaster.size());
        return ResponseEntity.ok(documentMaster);
    }



    /**
     * Get all Document -types   .
     *
     */
    @GetMapping("/document-types")
    @Operation(summary = "Get all document types", description = "Retrieves all document types from the system")
    public ResponseEntity<List<DocumentTypeView>> getAllDocumentType(){
        log.info("Fetching all document types");
        List<DocumentTypeView> documentTypeView =documentMasterDataService.getAllDocumentType();
        log.info("Found {} document types", documentTypeView.size());
        return ResponseEntity.ok(documentTypeView);
    }

    /**
     * Get all Canvassed Types  .
     *
     */
    @GetMapping("/canvassed-types")
    @Operation(summary = "Get all canvassed types", description = "Retrieves all canvassed types from the system")
    public ResponseEntity<List<CanvassedTypesView>> getAllCanvassedTypes(){
        log.info("Fetching all canvassed types");
        List<CanvassedTypesView> canvassedTypesView =documentMasterDataService.getAllCanvassedTypes();
        log.info("Found {} canvassed types", canvassedTypesView.size());
        return ResponseEntity.ok(canvassedTypesView);
    }


    /**
     * Get all Asset Types .
     *
     * @return List of all Asset Types
     */
    @GetMapping("/asset-types")
    @Operation(summary = "Get all asset Types", description = "Retrieves all asset Types from the system")
    public ResponseEntity<List<AssetTypesView>> getAllAssetTypes(){
        log.info("Fetching all asset Types");
        List<AssetTypesView> assetTypesView =documentMasterDataService.getAllAssetTypes();
        log.info("Found {} asset Types", assetTypesView.size());
        return ResponseEntity.ok(assetTypesView);
    }
}
