package com.incede.nbfc.core.monolith.lead.controller;

import com.incede.nbfc.core.monolith.lead.dto.LeadRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadSearchResponseDto;
import com.incede.nbfc.core.monolith.lead.service.LeadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/leads")
@RequiredArgsConstructor
@Tag(name = "Lead Controller", description = "APIs for managing leads")
public class LeadController {

    private final LeadService leadService;

    /**
     * Create a new lead
     */
    @PostMapping
    @Operation(summary = "Create Lead", description = "Creates a new lead and returns created details")
    public ResponseEntity<LeadResponseDto> createLead(
            @Valid @RequestBody LeadRequestDto request) {

        LeadResponseDto response = leadService.createLead(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get lead by UUID
     */
    @GetMapping("/{leadIdentity}")
    @Operation(summary = "Get Lead", description = "Retrieve lead details by UUID")
    public ResponseEntity<LeadResponseDto> getLead(
            @PathVariable UUID leadIdentity) {

        LeadResponseDto response = leadService.getLeadById(leadIdentity);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/search")
    @Operation(
            summary = "Search Leads",
            description = "Search leads by optional parameters: fullName, contactNumber, email with pagination"
    )
    public ResponseEntity<Page<LeadSearchResponseDto>> searchLeads(
            @RequestParam(name="fullName",required = true) String fullName,
            @RequestParam(name="contactNumber",required = true) String contactNumber,
            @RequestParam(name="email",required = true) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<LeadSearchResponseDto> response = leadService.searchLeads(fullName, contactNumber, email, page, size);
        return ResponseEntity.ok(response);
    }
}
