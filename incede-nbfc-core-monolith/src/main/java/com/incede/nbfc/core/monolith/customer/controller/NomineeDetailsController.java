package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.NomineeDetailsRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDetailsResponseDto;
import com.incede.nbfc.core.monolith.customer.service.NomineeDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/onboarding")
@RequiredArgsConstructor
public class NomineeDetailsController {

    private final NomineeDetailsService nomineeDetailsService;

    /**
     * Create nominee details for a given customer.
     *
     * @param customerIdentity   the unique identity of the customer
     * @param requestDto DTO containing nominee details
     * @return ResponseEntity with created nominee details including customer identity
     */
    @PostMapping("{customerIdentity}/nominees")
    public ResponseEntity<NomineeDetailsResponseDto> createNominee(
            @PathVariable UUID customerIdentity,
            @RequestBody @Valid NomineeDetailsRequestDto requestDto
    ) {
        NomineeDetailsResponseDto response = nomineeDetailsService.createNominee(customerIdentity, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    /**
     * Update an existing nominee for a customer.
     *
     * @param customerIdentity UUID of the customer
     * @param nomineeIdentity  UUID of the nominee to update
     * @param dto              DTO containing updated nominee details
     * @return ResponseEntity with HTTP 200 OK and the updated nominee details
     */

    @PutMapping("{customerIdentity}/nominees/{nomineeIdentity}")
    public ResponseEntity<NomineeDetailsResponseDto> updateNominee(
            @PathVariable UUID customerIdentity,
            @PathVariable UUID nomineeIdentity,
            @RequestBody @Valid NomineeDetailsRequestDto dto) {

        NomineeDetailsResponseDto response = nomineeDetailsService.updateNominee(customerIdentity, nomineeIdentity, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Fetch all active nominees for a given customer.
     *
     * @param customerIdentity UUID of the customer
     * @return ResponseEntity with HTTP 200 OK and list of nominee details
     */

    @GetMapping("{customerIdentity}/nominees")
    public ResponseEntity<NomineeDetailsResponseDto> getNomineesByCustomerIdentity(
            @PathVariable("customerIdentity") UUID customerIdentity) {

        NomineeDetailsResponseDto response = nomineeDetailsService.getNomineesByCustomerIdentity(customerIdentity);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a nominee for a customer.
     *
     * @param customerIdentity UUID of the customer
     * @param nomineeIdentity  UUID of the nominee to delete
     * @return ResponseEntity with HTTP 204 No Content
     */
    @DeleteMapping("{customerIdentity}/nominees/{nomineeIdentity}")
    public ResponseEntity<NomineeDetailsResponseDto> deleteNominee(
            @PathVariable UUID customerIdentity,
            @PathVariable UUID nomineeIdentity) {

        nomineeDetailsService.deleteNominee(customerIdentity, nomineeIdentity);
        return ResponseEntity.noContent().build();
    }

}

