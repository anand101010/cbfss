package com.incede.nbfc.core.monolith.lead.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadFollowUpHistoryResponseDto {

    private List<FollowUpHistory> history;
    private String message;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowUpHistory {
        private UUID historyIdentity;
        private UUID leadIdentity;
        private Integer StaffId;
        private LocalDate followUpDate;
        private LocalDate nextFollowUpDate;
        private FollowUpType followUpType;
        private LeadStage leadStage;
        private String stageChangeRemarks;
        private String followUpNotes;
        private String changeType;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowUpType {
        private UUID identity;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LeadStage {
        private UUID identity;
        private String name;
    }
}

