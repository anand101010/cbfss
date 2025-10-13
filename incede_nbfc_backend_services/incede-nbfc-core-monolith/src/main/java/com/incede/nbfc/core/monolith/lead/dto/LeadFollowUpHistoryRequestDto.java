package com.incede.nbfc.core.monolith.lead.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class LeadFollowUpHistoryRequestDto
{

    private  UUID leadIdentity;

    @NotNull(message = "Lead Follow-Up identity must not be null")
    private UUID leadStageIdentity;

    @Positive(message = "Staff ID must be a positive number")
    private int staffId;

    @NotNull(message = "Follow-Up Type identity must not be null")
    private UUID followUpTypeIdentity;

    @NotNull(message = "Follow-Up date must not be null")
    @FutureOrPresent(message = "Follow-Up date cannot be in the past")
    private LocalDate followUpDate;

    private LocalDate nextFollowUpDate;

    @NotEmpty(message = "Follow-Up notes must not be empty")
    private String followUpNotes;

    @NotEmpty(message = "Change type must not be empty")
    private String changeType;

    private String stageChangeRemarks;
}
