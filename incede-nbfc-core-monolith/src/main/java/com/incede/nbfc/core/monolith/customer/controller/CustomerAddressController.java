package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressRequestDTO;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressResponseDTO;
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
     * @param customerIdentity          UUID of the customer
     * @param addressRequestDto Address request payload
     * @return ResponseEntity with created address DTO
     */
    @PostMapping("/{customerIdentity}/addresses")
    public ResponseEntity<CustomerAddressResponseDTO> createAddress(@PathVariable UUID customerIdentity,
                                                                    @RequestBody @Valid CustomerAddressRequestDTO addressRequestDto) {

        CustomerAddressResponseDTO createdAddress = customerAddressService.createAddress(customerIdentity, addressRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }

    /**
     *
     * @param customerIdentity
     * @param addressId
     * @param requestDTO
     * @return
     */
    @PutMapping("/{customerIdentity}/addresses/{addressId}")
    public ResponseEntity<CustomerAddressResponseDTO> updateAddress(@PathVariable UUID customerIdentity,
                                                                    @PathVariable Integer addressId,
                                                                    @RequestBody CustomerAddressRequestDTO requestDTO) {

        CustomerAddressResponseDTO response = customerAddressService.updateAddress(customerIdentity, addressId, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     *
     * @param customerIdentity
     * @return
     */
    @GetMapping("/{customerIdentity}/addresses")
    public ResponseEntity<CustomerAddressResponseDTO> getActiveAddresses(@PathVariable UUID customerIdentity) {

        CustomerAddressResponseDTO response = customerAddressService.getActiveAddressesByCustomerIdentity(customerIdentity);
        return ResponseEntity.ok(response);
    }

    /**
     *
     * @param customerIdentity
     * @param addressId
     * @return
     */
    @DeleteMapping("/{customerIdentity}/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID customerIdentity, @PathVariable Integer addressId) {

        customerAddressService.deleteAddress(customerIdentity, addressId);
        return ResponseEntity.noContent().build();
    }


}
 