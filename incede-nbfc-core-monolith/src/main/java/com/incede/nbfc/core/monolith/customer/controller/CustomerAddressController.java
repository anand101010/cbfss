package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @param addressRequestDto Address request payload
     * @return ResponseEntity with created address DTO
     */
    @PostMapping("/{customerIdentity}/addresses")
    public ResponseEntity<CustomerAddressResponseDto> createAddress(@PathVariable UUID customerIdentity,
                                                                    @RequestBody @Valid CustomerAddressRequestDto addressRequestDto) {

        CustomerAddressResponseDto createdAddress = customerAddressService.createAddress(customerIdentity, addressRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }

    /**
     * Update an existing address
     *
     * @param customerIdentity UUID of the customer
     * @param addressId Address ID
     * @param requestDto Address request payload
     * @return ResponseEntity with updated address DTO
     */
    @PutMapping("/{customerIdentity}/addresses/{addressId}")
    public ResponseEntity<CustomerAddressResponseDto> updateAddress(@PathVariable UUID customerIdentity,
                                                                    @PathVariable Integer addressId,
                                                                    @RequestBody CustomerAddressRequestDto requestDto) {

        CustomerAddressResponseDto response = customerAddressService.updateAddress(customerIdentity, addressId, requestDto);
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
     * @param addressId Address ID
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{customerIdentity}/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID customerIdentity, @PathVariable Integer addressId) {

        customerAddressService.deleteAddress(customerIdentity, addressId);
        return ResponseEntity.noContent().build();
    }
}
