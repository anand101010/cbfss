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
public class LeadFollowUpDetailsResponseDto {
    private UUID followUpIdentity;
    private UUID leadIdentity;
    private int staffId;
    private String followUpTypeName;
    private LocalDate followUpDate;
    private LocalDate nextFollowUpDate;
    private String followUpNotes;
    private String message;
}
