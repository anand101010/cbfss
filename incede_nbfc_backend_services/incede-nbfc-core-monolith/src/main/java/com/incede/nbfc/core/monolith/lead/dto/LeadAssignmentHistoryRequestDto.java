package com.incede.nbfc.core.monolith.lead.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadAssignmentHistoryRequestDto {

    @NotEmpty(message = "At least one lead reference must be provided")
    private List<UUID> leadIdentities;  // List of Lead UUIDs

    @NotNull(message = "Assigned to user ID must not be null")
    private UUID assignedToUserIdentity;

    @NotNull(message = "Assigned date must not be null")
    private LocalDate assignedOn;

}