package com.incede.nbfc.core.monolith.masterdata.controller;

import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.service.ReferenceMasterDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/master")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Master Data", description = "Master Data Management APIs")
public class ReferenceMasterDataController {
    private final ReferenceMasterDataService referenceMasterDataService;

    /**
     * Get all address-types.
     *
     * @return List of all address-types
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/address-types")
    @Operation(summary = "Get all address types", description = "Retrieves all address types from the system")
    public ResponseEntity<List<AddressTypeView>> getAllAddressTypes() {
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
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/address-proof-type")
    @Operation(summary = "Get all Address Proof Types", description = "Retrieves all Address Proof Types from the system")
    public ResponseEntity<List<AddressProofTypeView>> getAllAddressProofTypes() {
        log.info("Fetching all Address Proof Types");
        List<AddressProofTypeView> addressProofTypeView = referenceMasterDataService.getAllAddressProofTypes();
        log.info("Found {} Address Proof Types", addressProofTypeView.size());
        return ResponseEntity.ok(addressProofTypeView);
    }


    /**
     * Get all Residential Statuses .
     *
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/residential-statuses")
    @Operation(summary = "Get all Residential Statuses", description = "Retrieves all Residential Statuses from the system")
    public ResponseEntity<List<ResidentialStatusesView>> getAllResidentialStatuses() {
        log.info("Fetching all Residential Statuses");
        List<ResidentialStatusesView> residentialStatusesView = referenceMasterDataService.getAllResidentialStatuses();
        log.info("Found {} Residential Statuses", residentialStatusesView.size());
        return ResponseEntity.ok(residentialStatusesView);
    }

    /**
     * Get all contact-types.
     *
     * @return List of all contact-types
     */

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/contact-types")
    @Operation(summary = "Get all contact types", description = "Retrieves all contact types from the system")
    public ResponseEntity<List<ContactTypesView>> getAllContactTypes() {
        log.info("Fetching all contact types");
        List<ContactTypesView> contactTypes = referenceMasterDataService.getAllContactTypes();
        log.info("Found {} contact types", contactTypes.size());

        return ResponseEntity.ok(contactTypes);
    }

    /**
     * Get all pincodes .
     *
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/pincodes")
    @Operation(summary = "Get pincodes with pagination", description = "Retrieves paginated pincodes from the system")
    public ResponseEntity<Page<PincodeDto>> getAllPincodes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Fetching pincodes - page: {}, size: {}", page, size);
        Page<PincodeDto> pincodes = referenceMasterDataService.getAllPincodes(page, size);
        log.info("Found {} pincodes", pincodes.getNumberOfElements());

        return ResponseEntity.ok(pincodes);
    }

    /**
     * Fetch list of District, State, city by Pincode
     *
     * @param pincode pincode number
     * @return list of PincodeDto
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/pincodes/{pincode}")
    @Operation(summary = "Get Pincode Details",
            description = "Fetches all post offices, district and state for given pincode")
    public ResponseEntity<List<PincodeDto>> getPincodeByNumber(@PathVariable Integer pincode) {
        log.info("Fetching pincode details for {}", pincode);
        List<PincodeDto> response = referenceMasterDataService.getPincodeDetails(String.valueOf(((pincode))));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pincodes/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file,
                                              @RequestParam("createdBy") Integer createdBy) {

            log.info("Uploading  Post Office details");
            referenceMasterDataService.importFile(file,createdBy);
            return ResponseEntity.ok("Data imported successfully!");
    }

}