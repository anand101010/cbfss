package com.incede.nbfc.core.monolith.masterdata.controller;

import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.service.ReferenceMasterDataService;
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
@Tag(name = "Master Data", description = "Master Data Management APIs")
public class ReferenceMasterDataController
{
    private final ReferenceMasterDataService referenceMasterDataService;

    /**
     * Get all address-types.
     *
     * @return List of all address-types
     */
    @GetMapping("/address-types")
    @Operation(summary = "Get all address types", description = "Retrieves all address types from the system")
    public ResponseEntity<List<AddressTypeView>> getAllAddressTypes(){
        log.info("Fetching all address types");
        List<AddressTypeView> addressTypes = referenceMasterDataService.getAllAddressTypes();
        log.info("Found {} address types", addressTypes.size());
        return ResponseEntity.ok(addressTypes);
    }

    /**
     * Get all Address Proof Types .
     *
     * @return List of all Address Proof Types
     */
    @GetMapping("/address-proof-type")
    @Operation(summary = "Get all Address Proof Types", description = "Retrieves all Address Proof Types from the system")
    public ResponseEntity<List<AddressProofTypeView>> getAllAddressProofTypes(){
        log.info("Fetching all Address Proof Types");
        List<AddressProofTypeView> addressProofTypeView =referenceMasterDataService.getAllAddressProofTypes();
        log.info("Found {} Address Proof Types", addressProofTypeView.size());
        return ResponseEntity.ok(addressProofTypeView);
    }


    /**
     * Get all Residential Statuses .
     *
     */
    @GetMapping("/residential-statuses")
    @Operation(summary = "Get all Residential Statuses", description = "Retrieves all Residential Statuses from the system")
    public ResponseEntity<List<ResidentialStatusesView>> getAllResidentialStatuses(){
        log.info("Fetching all Residential Statuses");
        List<ResidentialStatusesView> residentialStatusesView =referenceMasterDataService.getAllResidentialStatuses();
        log.info("Found {} Residential Statuses", residentialStatusesView.size());
        return ResponseEntity.ok(residentialStatusesView);
    }

    /**
     * Get all contact-types.
     *
     * @return List of all contact-types
     */

    @GetMapping("/contact-types")
    @Operation(summary = "Get all contact types", description = "Retrieves all contact types from the system")
    public ResponseEntity<List<ContactTypesView>> getAllContactTypes() {
        log.info("Fetching all contact types");
        List<ContactTypesView> contactTypes = referenceMasterDataService.getAllContactTypes();
        log.info("Found {} contact types", contactTypes.size());

        return ResponseEntity.ok(contactTypes);
    }





}
