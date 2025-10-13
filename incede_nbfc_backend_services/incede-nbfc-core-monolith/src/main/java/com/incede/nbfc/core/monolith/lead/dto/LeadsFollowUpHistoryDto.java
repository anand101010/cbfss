package com.incede.nbfc.core.monolith.lead.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeadsFollowUpHistoryDto
{
        private UUID leadFollowuphistoryIdentity;
        private UUID leadIdentity;
        private UUID leadStageIdentity;
        private Integer staffId;
        private UUID followUpTypeIdentity;
        private LocalDate followUpDate;
        private LocalDate nextFollowUpDate;
        private String followUpNotes;
        private String changeType;
        private String leadStage;
        private String message;

    public LeadsFollowUpHistoryDto(UUID leadFollowuphistoryIdentity, UUID leadIdentity, String message) {
        this.leadFollowuphistoryIdentity = leadFollowuphistoryIdentity;
        this.leadIdentity = leadIdentity;
        this.message = message;
    }

}
