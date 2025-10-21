package com.incede.nbfc.core.monolith.lead.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.lead.dto.LeadRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadSearchResponseDto;
import com.incede.nbfc.core.monolith.lead.service.LeadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/leads")
@RequiredArgsConstructor
@Tag(name = "Lead Controller", description = "APIs for managing leads")
public class LeadController {

    private final LeadService leadService;
    private final ObjectMapper objectMapper;
    /**
     *
     * create new lead
     * @param leadRequestDto
     * @return
     */
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping
    @Operation(summary = "Create Lead", description = "Create a new lead with optional address")
    public ResponseEntity<LeadResponseDto> createLead(@RequestBody LeadRequestDto leadRequestDto) {
        LeadResponseDto response = leadService.createLead(leadRequestDto); // now DTO is accepted
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     *
     * @param leadIdentity
     * @return
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/{leadIdentity}")
    @Operation(summary = "Get Lead", description = "Retrieve lead details by UUID")
    public ResponseEntity<LeadResponseDto> getLead(
            @PathVariable UUID leadIdentity) {

        LeadResponseDto response = leadService.getLeadById(leadIdentity);
        return ResponseEntity.ok(response);
    }

    /**
     * search lead
     * @param fullName
     * @param contactNumber
     * @param email
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/search")
    @Operation(
            summary = "Search Leads",
            description = "Search leads by optional parameters: fullName, contactNumber, email with pagination"
    )
    public ResponseEntity<Page<LeadSearchResponseDto>> searchLeads(
            @RequestParam(name="fullName",required = false) String fullName,
            @RequestParam(name="contactNumber",required = false) String contactNumber,
            @RequestParam(name="email",required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<LeadSearchResponseDto> response = leadService.searchLeads(fullName, contactNumber, email, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     *
     * @param leadIdentity
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("/{leadIdentity}")
    public ResponseEntity<LeadResponseDto> updateLead(@PathVariable UUID leadIdentity,
                                                      @RequestBody LeadRequestDto dto) {
        LeadResponseDto response = leadService.updateLead(leadIdentity, dto);
        return ResponseEntity.ok(response);
    }

}
