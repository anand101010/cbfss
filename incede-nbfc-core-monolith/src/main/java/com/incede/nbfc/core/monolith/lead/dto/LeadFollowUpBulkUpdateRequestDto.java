package com.incede.nbfc.core.monolith.lead.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadFollowUpBulkUpdateRequestDto {
    private UUID leadIdentity;
    private UUID followUpId;
    private LeadFollowUpDetailsRequestDto followUpDetails;
}
