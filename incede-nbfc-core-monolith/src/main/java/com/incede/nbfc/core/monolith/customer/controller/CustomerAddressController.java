package com.incede.nbfc.core.monolith.customer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/onboarding")
@RequiredArgsConstructor
public class CustomerAddressController {

    private final CustomerAddressService customerAddressService;

    /**
     * Create a new address for a given customer identity (UUID)
     *
     * @param customerIdentity UUID of the customer
     * @return ResponseEntity with created address DTO
     */
    @PostMapping(value = "/{customerIdentity}/addresses", consumes = {"multipart/form-data"})
    public ResponseEntity<CustomerAddressResponseDto> createAddress(
            @PathVariable UUID customerIdentity,
            @RequestPart("request") String requestJson,
            @RequestPart(value = "file", required = false) MultipartFile file) throws JsonProcessingException {

        CustomerAddressResponseDto createdAddress = customerAddressService.createAddress(customerIdentity, requestJson, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }



    /**
     * Update an existing address
     *
     * @param customerIdentity UUID of the customer
     * @param addressIdentity  Address ID
     * @param requestJson      Address request payload in JSON
     * @param file             Optional document file
     * @return ResponseEntity with updated address DTO
     */
    @PutMapping(value = "/{customerIdentity}/addresses/{addressIdentity}", consumes = {"multipart/form-data"})
    public ResponseEntity<CustomerAddressResponseDto> updateAddress(
            @PathVariable UUID customerIdentity,
            @PathVariable UUID addressIdentity,
            @RequestPart("request") String requestJson,
            @RequestPart(value = "file", required = false) MultipartFile file) throws JsonProcessingException {

        CustomerAddressResponseDto response = customerAddressService.updateAddress(customerIdentity, addressIdentity, requestJson, file);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all active addresses for a customer
     *
     * @param customerIdentity UUID of the customer
     * @return ResponseEntity with active addresses
     */
    @GetMapping("/{customerIdentity}/addresses")
    public ResponseEntity<CustomerAddressResponseDto> getActiveAddresses(@PathVariable UUID customerIdentity) {

        CustomerAddressResponseDto response = customerAddressService.getActiveAddressesByCustomerIdentity(customerIdentity);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an address by ID for a customer
     *
     * @param customerIdentity UUID of the customer
     * @param addressIdentity Address ID
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{customerIdentity}/addresses/{addressIdentity}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID customerIdentity,
                                              @PathVariable UUID addressIdentity) {

        customerAddressService.deleteAddress(customerIdentity, addressIdentity);
        return ResponseEntity.noContent().build();
    }
}
