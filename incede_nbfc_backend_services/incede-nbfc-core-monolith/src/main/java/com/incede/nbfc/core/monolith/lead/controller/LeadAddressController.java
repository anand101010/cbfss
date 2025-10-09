package com.incede.nbfc.core.monolith.lead.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.incede.nbfc.core.monolith.lead.dto.LeadAddressRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadAddressResponseDto;
import com.incede.nbfc.core.monolith.lead.service.LeadAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/leads")
@RequiredArgsConstructor
public class LeadAddressController {

    private final LeadAddressService leadAddressService;

//    /**
//     * Create a new address for a given lead identity (UUID)
//     *
//     * @param leadIdentity UUID of the lead
//     * @return ResponseEntity with created address DTO
//     */
//    @PostMapping(value = "/{leadIdentity}/addresses", consumes = {"multipart/form-data"})
//    public ResponseEntity<LeadAddressResponseDto> createAddress(
//            @PathVariable UUID leadIdentity,
//            @RequestPart("request") String requestJson,
//            @RequestPart(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
//
//        LeadAddressResponseDto createdAddress = leadAddressService.createAddress(leadIdentity, requestJson, file);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
//    }
//
//    /**
//     * Update an existing address
//     *
//     * @param leadIdentity   UUID of the lead
//     * @param addressIdentity Address ID
//     * @param requestJson     Address request payload in JSON
//     * @param file            Optional document file
//     * @return ResponseEntity with updated address DTO
//     */
//    @PutMapping(value = "/{leadIdentity}/addresses/{addressIdentity}", consumes = {"multipart/form-data"})
//    public ResponseEntity<LeadAddressResponseDto> updateAddress(
//            @PathVariable UUID leadIdentity,
//            @PathVariable UUID addressIdentity,
//            @RequestPart("request") String requestJson,
//            @RequestPart(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
//
//        LeadAddressResponseDto response = leadAddressService.updateAddress(leadIdentity, addressIdentity, requestJson, file);
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * Get all active addresses for a lead
//     *
//     * @param leadIdentity UUID of the lead
//     * @return ResponseEntity with active addresses
//     */
//    @GetMapping("/{leadIdentity}/addresses")
//    public ResponseEntity<LeadAddressResponseDto> getActiveAddresses(@PathVariable UUID leadIdentity) {
//
//        LeadAddressResponseDto response = leadAddressService.getActiveAddressByLeadIdentity(leadIdentity);
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * Delete an address by ID for a lead
//     *
//     * @param leadIdentity   UUID of the lead
//     * @param addressIdentity Address ID
//     * @return ResponseEntity with no content
//     */
//    @DeleteMapping("/{leadIdentity}/addresses/{addressIdentity}")
//    public ResponseEntity<Void> deleteAddress(@PathVariable UUID leadIdentity,
//                                              @PathVariable UUID addressIdentity) {
//
//        leadAddressService.deleteAddress(leadIdentity, addressIdentity);
//        return ResponseEntity.noContent().build();
//    }
}
