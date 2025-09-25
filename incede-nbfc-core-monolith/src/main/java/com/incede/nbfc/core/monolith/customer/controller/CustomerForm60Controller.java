package com.incede.nbfc.core.monolith.customer.controller;


import com.incede.nbfc.core.monolith.customer.dto.CustomerForm60RequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerForm60ResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerForm60Service;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerForm60Controller {

    private final CustomerForm60Service form60Service;


    /**
     * Save a new Form 60 for the given customer.
     *
     * @param customerIdentity UUID of the customer for whom Form 60 is being created
     * @param request          Request body containing Form 60 details
     * @return ResponseEntity with the saved Form 60 details and HTTP status CREATED
     */

    @PostMapping(value = "/{customerIdentity}/form60")
    @Operation(summary = "Save Form60", description = "Save Form60 for a new customer and returns generated identity")
    public ResponseEntity<CustomerForm60ResponseDto> saveForm60(
            @PathVariable UUID customerIdentity,
            @RequestBody CustomerForm60RequestDto request) {

        CustomerForm60ResponseDto response = form60Service.saveForm60(request, customerIdentity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update an existing Form 60 for the given customer and Form 60 ID.
     *
     * @param customerIdentity UUID of the customer whose Form 60 needs to be updated
     * @param form60Id         ID of the Form 60 to update
     * @param request          Request body containing updated Form 60 details
     * @return ResponseEntity with the updated Form 60 details
     */

    @PutMapping("/{customerIdentity}/form60/{form60Id}")
    @Operation(summary = "Update Form 60", description = "Updates Form 60 information by customer identity and Form 60 ID")
    public ResponseEntity<CustomerForm60ResponseDto> updateForm60(
            @PathVariable UUID customerIdentity,
            @PathVariable Integer form60Id,
            @Valid @RequestBody CustomerForm60RequestDto request) {
        CustomerForm60ResponseDto response = form60Service.updateForm60(customerIdentity, form60Id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Fetch Form 60 information by customer identity and Form 60 ID
     *
     * @param customerIdentity UUID of the customer
     * @param form60Id ID of the Form 60
     * @return ResponseEntity with Form 60 information
     */
    @GetMapping("/{customerIdentity}/form60/{form60Id}")
    @Operation(summary = "Get Form 60", description = "Fetches Form 60 information by customer identity and Form 60 ID")
    public ResponseEntity<CustomerForm60ResponseDto> getForm60ById(
            @PathVariable UUID customerIdentity,
            @PathVariable Integer form60Id) {
        CustomerForm60ResponseDto response = form60Service.getForm60ById(customerIdentity, form60Id);
        return ResponseEntity.ok(response);
    }

}
