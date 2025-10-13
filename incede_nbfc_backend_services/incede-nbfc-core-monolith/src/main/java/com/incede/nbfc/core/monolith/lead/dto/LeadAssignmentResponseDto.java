package com.incede.nbfc.core.monolith.lead.dto;

import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.user.domain.entity.User;
import com.incede.nbfc.core.monolith.user.dto.UserResponseDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadAssignmentResponseDto {

    private LeadResponseDto leadResponseDto;
    private UserResponseDto assignedTo;
    private UserResponseDto assignedBy;
    private LocalDate assignedOn;
    private String status;
    private String remarks;
    private UUID LeadAssignIdentity;

}
