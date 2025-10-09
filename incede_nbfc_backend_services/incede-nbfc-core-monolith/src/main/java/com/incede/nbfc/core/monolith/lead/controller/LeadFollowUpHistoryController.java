package com.incede.nbfc.core.monolith.lead.controller;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadFollowUpHistory;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/leads")
@RequiredArgsConstructor
@Slf4j
public class LeadFollowUpHistoryController
{
   private final LeadFollowUpHistoryService  leadFollowUpHistoryService;

    @GetMapping("/{leadIdentity}/follow-up-history")
    public ResponseEntity<LeadFollowUpHistoryResponseDto> getFollowUpHistory(@PathVariable UUID leadIdentity)
    {
        log.info("Request received for follow-up history fetch");
        LeadFollowUpHistoryResponseDto responseList = leadFollowUpHistoryService.getFollowUpHistory(leadIdentity);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/follow-up-history/search")
    public ResponseEntity<LeadFollowUpHistoryResponseDto> searchFollowUpHistory(
            @RequestParam(name = "leadIdentity", required = false) UUID leadIdentity,
            @RequestParam(name = "leadFollowupIdentity", required = false) UUID leadFollowUpIdentity,
            @RequestParam(name = "staffId", required = false) Integer staffId,
            @RequestParam(name = "followUpTypeIdentity", required = false) UUID followUpTypeIdentity,
            @RequestParam(name = "leadDateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate leadDateFrom,
            @RequestParam(name = "leadDateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate leadDateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        log.info("Request received for follow-up history search");
        LeadFollowUpHistoryResponseDto response = leadFollowUpHistoryService.searchFollowUpHistory(
                leadIdentity,leadFollowUpIdentity,staffId, followUpTypeIdentity, leadDateFrom, leadDateTo, page, size
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{leadIdentity}/follow-up-history")
    @Operation(summary = "Save a new follow-up history for a lead")
    public ResponseEntity<LeadsFollowUpHistoryDto> saveFollowUpHistory(
            @PathVariable UUID leadIdentity, @Valid @RequestBody LeadFollowUpHistoryRequestDto leadFollowUpHistoryRequestDto)
    {
        log.info("Received request to save follow-up history");
        LeadsFollowUpHistoryDto response = leadFollowUpHistoryService.saveFollowUpHistory(leadIdentity, leadFollowUpHistoryRequestDto);
        log.info("Follow-up history saved successfully");
        return ResponseEntity.ok(response);
    }


}
