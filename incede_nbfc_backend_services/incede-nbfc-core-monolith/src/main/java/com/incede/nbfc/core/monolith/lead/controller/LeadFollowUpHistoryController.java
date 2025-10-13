package com.incede.nbfc.core.monolith.lead.controller;
import com.incede.nbfc.core.monolith.lead.dto.BulkFollowUpHistoryRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpHistoryRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpHistoryResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadsFollowUpHistoryDto;
import com.incede.nbfc.core.monolith.lead.service.LeadFollowUpHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class LeadFollowUpHistoryController
{
   private final LeadFollowUpHistoryService  leadFollowUpHistoryService;

    /**
     *
     * @param leadIdentity
     * @return LeadFollowUpHistoryResponseDto
     */

    @GetMapping("/{leadIdentity}/follow-up-history")
    @Operation(summary = "get Lead Follow-up History")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<LeadFollowUpHistoryResponseDto> getFollowUpHistory(@PathVariable UUID leadIdentity)
    {
        log.info("Request received for follow-up history fetch");
        LeadFollowUpHistoryResponseDto responseList = leadFollowUpHistoryService.getFollowUpHistory(leadIdentity);
        return ResponseEntity.ok(responseList);
    }

    /**
     *
     * @param leadIdentity
     * @param staffId
     * @param followUpTypeIdentity
     * @param leadStageIdentity
     * @param leadDateFrom
     * @param leadDateTo
     * @param page
     * @param size
     * @return LeadFollowUpHistoryResponseDto
     */
    @GetMapping("/follow-up-history/search")
    @Operation(summary = "Search Lead Follow-up History")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<LeadFollowUpHistoryResponseDto> searchFollowUpHistory(
            @RequestParam(name = "leadIdentity", required = false) UUID leadIdentity,
            @RequestParam(name = "staffId", required = false) Integer staffId,
            @RequestParam(name = "followUpTypeIdentity", required = false) UUID followUpTypeIdentity,
            @RequestParam(name = "leadStageIdentity", required = false) UUID leadStageIdentity,
            @RequestParam(name = "leadDateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate leadDateFrom,
            @RequestParam(name = "leadDateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate leadDateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        log.info("Request received for follow-up history search");
        LeadFollowUpHistoryResponseDto response = leadFollowUpHistoryService.searchFollowUpHistory(
                leadIdentity,staffId, followUpTypeIdentity,leadStageIdentity,leadDateFrom,leadDateTo,page,size
        );
        return ResponseEntity.ok(response);
    }

    /**
     *
     * @param leadIdentity
     * @param followUpHistoryIdentity
     * @param leadFollowUpHistoryRequestDto
     * @return LeadsFollowUpHistoryDto
     */
    @PutMapping("{leadIdentity}/follow-up-history/{followUpHistoryIdentity}")
    @Operation(summary = "Update Lead Follow-up History")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<LeadsFollowUpHistoryDto> updateFollowUpHistory(
            @PathVariable UUID leadIdentity,
            @PathVariable UUID followUpHistoryIdentity,
           @Valid @RequestBody LeadFollowUpHistoryRequestDto leadFollowUpHistoryRequestDto)
    {

        log.info("Update follow-up history request received for lead: {}", leadIdentity);

        LeadsFollowUpHistoryDto response = leadFollowUpHistoryService
                .updateFollowUpHistory(leadIdentity, followUpHistoryIdentity, leadFollowUpHistoryRequestDto);

        return ResponseEntity.ok(response);
    }

    /**
     *
     * @param leadIdentity
     * @param leadFollowUpHistoryRequestDto
     * @return LeadsFollowUpHistoryDto
     */
    @PostMapping("/{leadIdentity}/follow-up-history")
    @Operation(summary = "Save a new follow-up history for a lead")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<LeadsFollowUpHistoryDto> saveFollowUpHistory(
            @PathVariable UUID leadIdentity, @Valid @RequestBody LeadFollowUpHistoryRequestDto leadFollowUpHistoryRequestDto)
    {
        log.info("Received request to save follow-up history");
        LeadsFollowUpHistoryDto response = leadFollowUpHistoryService.saveFollowUpHistory(leadIdentity, leadFollowUpHistoryRequestDto);
        log.info("Follow-up history saved successfully");
        return ResponseEntity.ok(response);
    }

    /**
     *
     * @paramList<LeadFollowUpHistoryRequestDto>requestList
     * @returnList<LeadsFollowUpHistoryDto>
     */
    @PostMapping("/follow-up-history/bulk")
    @Operation(summary = "Bulk Save Lead Follow-Up History")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<List<LeadsFollowUpHistoryDto>> bulkSaveFollowUpHistory(
            @Valid @RequestBody List<LeadFollowUpHistoryRequestDto> requestList)
    {
        log.info("Bulk follow-up history save request received. Count: {}", requestList.size());
        List<LeadsFollowUpHistoryDto> responses = leadFollowUpHistoryService.bulkSaveFollowUpHistory(requestList);
        return ResponseEntity.ok(responses);
    }





}
