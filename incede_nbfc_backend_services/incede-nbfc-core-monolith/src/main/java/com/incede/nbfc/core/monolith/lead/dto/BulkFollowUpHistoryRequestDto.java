package com.incede.nbfc.core.monolith.lead.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkFollowUpHistoryRequestDto
{
    private UUID leadIdentity;
    private LeadFollowUpHistoryRequestDto followUpHistoryRequest;
}
