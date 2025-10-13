package com.incede.nbfc.core.monolith.lead.controller;

import com.incede.nbfc.core.monolith.lead.dto.LeadAssignmentHistoryRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadAssignmentResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadAssignmentSearchResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadResponseDto;
import com.incede.nbfc.core.monolith.lead.service.LeadAssignmentHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/leads")
@RequiredArgsConstructor
@Tag(name = "Lead Assignment Controller", description = "APIs for managing lead assignments")
public class LeadAssignmentHistoryController {

    private final LeadAssignmentHistoryService leadAssignmentHistoryService;

    /**
     *
     * @param leadAssignmentRequestDto
     * @return
     */
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("lead-assignments/bulk")
    @Operation(summary = "Bulk Update Lead Assignments", description = "Update the assigned user for multiple leads")
    public ResponseEntity<String> bulkUpdateLeadAssignments(
            @Valid @RequestBody LeadAssignmentHistoryRequestDto leadAssignmentRequestDto) {
        String response = leadAssignmentHistoryService.bulkUpdateLeadAssignments(leadAssignmentRequestDto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("lead-assignments/search")
    @Operation(summary = "Search Leads for Assignment", description = "Search leads by product, lead source, lead stage, gender, assigned user, and lead date with pagination")
    public ResponseEntity<Page<LeadAssignmentSearchResponseDto>> searchLeadsForAssignment(
            @RequestParam(name = "productIdentity", required = false) UUID productIdentity,
            @RequestParam(name = "leadSourceIdentity", required = false) UUID leadSourceIdentity,
            @RequestParam(name = "leadStageIdentity", required = false) UUID leadStageIdentity,
            @RequestParam(name = "gender", required = false) UUID gender,
            @RequestParam(name = "assignTo", required = false) UUID assignTo,
            @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate leadDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<LeadAssignmentSearchResponseDto> response = leadAssignmentHistoryService.searchLeadsForAssignment(
                productIdentity,
                leadSourceIdentity,
                leadStageIdentity,
                gender,
                assignTo,
                leadDate,
                page,
                size);
        return ResponseEntity.ok(response);
    }

    /*
     *Fetch all Lead assigned details by leadIdentity
     * Param : leadIdentity
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("{leadIdentity}/assignment-history")
    @Operation(summary = "Get all Lead Assignments", description = "Get the assigned details of the leads")
    public ResponseEntity<List<LeadAssignmentResponseDto>> fetchLeadAssignmentHistory(@PathVariable UUID leadIdentity) {
        List<LeadAssignmentResponseDto> response = leadAssignmentHistoryService.fetchLeadAssignmentHistory(leadIdentity);
        return ResponseEntity.ok(response);
    }

    /*
     *Lead update Individual
     * Param :
     */
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("{leadIdentity}/assignment-history")
    @Operation(summary = "Update Lead Assignment", description = "update assigned details of the leads")
    public ResponseEntity<LeadResponseDto> updateLeadAssignment(@PathVariable UUID leadIdentity,
                                                                @RequestParam UUID assignedToIdentity,
                                                                @RequestParam UUID assignedByIdentity,
                                                                @RequestParam LocalDate assignedOn) {
        LeadResponseDto response = leadAssignmentHistoryService.updateLeadAssignment(leadIdentity,assignedToIdentity,assignedByIdentity,assignedOn);
        return ResponseEntity.ok(response);
    }



}