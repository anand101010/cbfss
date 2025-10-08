package com.incede.nbfc.core.monolith.lead.controller;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpBulkUpdateRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpDetailsResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpDetailsRequestDto;
import com.incede.nbfc.core.monolith.lead.service.LeadFollowUpDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/leads")
@RequiredArgsConstructor
@Slf4j
public class LeadFollowUpDetailsController
{

    private final LeadFollowUpDetailsService leadFollowUpService;

    @PostMapping("{leadIdentity}/follow-up-details")
    @Operation(summary = "Save Leads follow up details")
    public ResponseEntity<LeadFollowUpDetailsResponseDto> createLeadFollowUp( @PathVariable UUID leadIdentity,@RequestBody LeadFollowUpDetailsRequestDto leadFollowupDetailsRequestDto)
    {
        log.info("lead follow request save triggered");
        LeadFollowUpDetailsResponseDto created = leadFollowUpService.createFollowUp(leadIdentity,leadFollowupDetailsRequestDto);
        return ResponseEntity.ok(created);
    }
    @PutMapping("{followUpIdentity}/follow-up-details")
    @Operation(summary = "Update Leads follow up details")
    public ResponseEntity<LeadFollowUpDetailsResponseDto> updateLeadFollowUp( @PathVariable UUID leadIdentity,@PathVariable UUID followUpId, @RequestBody LeadFollowUpDetailsRequestDto leadFollowupDetailsRequestDto)
    {
        log.info("lead follow request update triggered");
        LeadFollowUpDetailsResponseDto created = leadFollowUpService.updateFollowUp(leadIdentity,followUpId,leadFollowupDetailsRequestDto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("follow-up-details/getAll")
    @Operation(summary = "Get all Leads follow up details")
    public ResponseEntity<Page<LeadFollowUpDetailsResponseDto>> getAllFollowUps(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        log.info("lead follow request get All triggered");
        Pageable pageable = PageRequest.of(page, size);
        Page<LeadFollowUpDetailsResponseDto> response = leadFollowUpService.getAllActiveFollowUps(pageable);
        return ResponseEntity.ok(response);
    }
    @GetMapping("follow-up-details/search")
    @Operation(summary = "Search Leads follow up details")
    public ResponseEntity<Page<LeadFollowUpDetailsResponseDto>> searchFollowUps(
            @RequestParam(name = "leadIdentity", required = false) UUID leadIdentity,
            @RequestParam(name = "staffId", required = false) Integer staffId,
            @RequestParam(name = "followUpTypeIdentity", required = false) UUID followUpTypeIdentity,
            @RequestParam(name = "leadDateFrom", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate leadDateFrom,
            @RequestParam(name = "leadDateTo", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate leadDateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    )
    {
        log.info("lead search request get All triggered");
        Page<LeadFollowUpDetailsResponseDto> response =
                leadFollowUpService.searchFollowUps(
                        leadIdentity,  staffId, followUpTypeIdentity, leadDateFrom, leadDateTo, page, size);

        return ResponseEntity.ok(response);
    }
    @PutMapping("/follow-up-details/bulk")
    @Operation(summary = "Bulk update Lead follow-up details")
    public ResponseEntity<List<LeadFollowUpDetailsResponseDto>> bulkUpdateLeadFollowUp(
            @RequestBody List<LeadFollowUpBulkUpdateRequestDto> requestList)
    {
        log.info("Bulk update for lead follow-ups triggered with size: {}", requestList.size());
        List<LeadFollowUpDetailsResponseDto> responses = leadFollowUpService.bulkUpdateFollowUps(requestList);
        return ResponseEntity.ok(responses);
    }


}