package com.incede.nbfc.core.monolith.lead.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadFollowUpHistoryResponseDto {

    private List<History> history;
    private String message;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class History {
        private UUID leadFollowuphistoryIdentity;
        private UUID leadIdentity;
        private UUID leadFollowUpIdentity;
        private int staffId;
        private UUID followUpTypeIdentity;
        private LocalDate followUpDate;
        private LocalDate nextFollowUpDate;
        private String followUpNotes;
        private String changeType;
    }
}
