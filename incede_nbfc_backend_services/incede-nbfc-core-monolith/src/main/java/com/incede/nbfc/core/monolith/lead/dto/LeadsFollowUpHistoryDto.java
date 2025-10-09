package com.incede.nbfc.core.monolith.lead.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadsFollowUpHistoryDto
{
        private UUID leadFollowuphistoryIdentity;
        private UUID leadIdentity;
        private UUID leadFollowUpIdentity;
        private int staffId;
        private UUID followUpTypeIdentity;
        private LocalDate followUpDate;
        private LocalDate nextFollowUpDate;
        private String followUpNotes;
        private String changeType;
        private String message;
}
