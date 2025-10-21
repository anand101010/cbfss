package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerAddressController {

    private final CustomerAddressService customerAddressService;

    /**
     * Saving Customer Address
     * @param customerIdentity
     * @param customerAddressRequestDto
     * @return
     */
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping(value = "/{customerIdentity}/addresses")
    public ResponseEntity<CustomerAddressResponseDto> createAddress(
            @PathVariable UUID customerIdentity,
            @RequestBody CustomerAddressRequestDto customerAddressRequestDto) {

        CustomerAddressResponseDto createdAddress = customerAddressService.createAddress(customerIdentity, customerAddressRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }

    /**
     * Updating Existing Customer Address Service
     * @param customerIdentity
     * @param addressIdentity
     * @param customerAddressRequestDto
     * @return
     */
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping(value = "/{customerIdentity}/addresses/{addressIdentity}")
    public ResponseEntity<CustomerAddressResponseDto> updateAddress(
            @PathVariable UUID customerIdentity,
            @PathVariable UUID addressIdentity,
            @RequestBody CustomerAddressRequestDto customerAddressRequestDto) {

        CustomerAddressResponseDto response = customerAddressService.updateAddress(customerIdentity, addressIdentity, customerAddressRequestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Get Customer Addresses for the customer
     * @param customerIdentity
     * @return
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/{customerIdentity}/addresses")
    public ResponseEntity<CustomerAddressResponseDto> getActiveAddresses(@PathVariable UUID customerIdentity) {
        CustomerAddressResponseDto response = customerAddressService.getActiveAddressesByCustomerIdentity(customerIdentity);
        return ResponseEntity.ok(response);
    }

    /**
     * Soft deleting Customer Address By Identity
     * @param customerIdentity
     * @param addressIdentity
     * @return
     */
    @PreAuthorize("hasRole('STAFF')")
    @DeleteMapping("/{customerIdentity}/addresses/{addressIdentity}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID customerIdentity,
                                              @PathVariable UUID addressIdentity) {
        customerAddressService.deleteAddress(customerIdentity, addressIdentity);
        return ResponseEntity.noContent().build();
    }
}