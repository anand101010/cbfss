package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerContactRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerContactResponceDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Contact API", description = "APIs for managing customer contact information")
public class CustomerContactController {

    private final CustomerContactService contactService;

    /**
     * Create a new contact for a customer.
     *
     * @param customerIdentity Customer UUID
     * @param request    DTO containing contact details
     * @return ResponseEntity with created contact details
     */
    @PostMapping("/{customerIdentity}/contacts")
    @Operation(summary = "Create Contact", description = "Creates a new contact for a customer and returns created details")
    public ResponseEntity<CustomerContactResponceDto> createContact(
            @PathVariable UUID customerIdentity,
            @Valid @RequestBody CustomerContactRequestDto request) {

        CustomerContactResponceDto response = contactService.saveContact(customerIdentity, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update an existing contact of a customer.
     *
     * @param customerIdentity Customer UUID
     * @param contactIdentity  Contact UUID
     * @param request    DTO containing updated contact details
     * @return ResponseEntity with updated contact details
     */
    @PutMapping("/{customerIdentity}/contacts/{contactIdentity}")
    @Operation(summary = "Update Contact", description = "Updates an existing contact for a customer using contact UUID")
    public ResponseEntity<CustomerContactResponceDto> updateContact(
            @PathVariable UUID customerIdentity,
            @PathVariable UUID contactIdentity,
            @Valid @RequestBody CustomerContactRequestDto request) {

        CustomerContactResponceDto response = contactService.updateContact(customerIdentity, contactIdentity, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Fetch all contacts of a customer.
     *
     * @param customerIdentity Customer UUID
     * @return ResponseEntity with list of customer's contacts
     */
    @GetMapping("/{customerIdentity}/contacts")
    @Operation(summary = "Get Contacts", description = "Fetches all contacts for a given customer UUID")
    public ResponseEntity<CustomerContactResponceDto> getContacts(@PathVariable UUID customerIdentity) {
        CustomerContactResponceDto response = contactService.getContacts(customerIdentity);
        return ResponseEntity.ok(response);
    }
}
