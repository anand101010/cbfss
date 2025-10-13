package com.incede.nbfc.core.monolith.lead.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadAssignmentHistory;
import com.incede.nbfc.core.monolith.lead.dto.LeadAssignmentHistoryRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadAssignmentSearchResponseDto;
import com.incede.nbfc.core.monolith.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LeadAssignmentHistoryMapper {

    public List<LeadAssignmentHistory> toEntityList(LeadAssignmentHistoryRequestDto dto, List<Lead> leads, User assignedToUser, User assignedByUser) {
        Objects.requireNonNull(dto, "LeadAssignmentHistoryRequestDto must not be null");
        Objects.requireNonNull(leads, "Leads must not be null");
        Objects.requireNonNull(assignedToUser, "AssignedToUser must not be null");

        List<LeadAssignmentHistory> assignments = new ArrayList<>();
        for (Lead lead : leads) {
            LeadAssignmentHistory assignment = new LeadAssignmentHistory();
            assignment.setLead(lead);
            assignment.setAssignedToUserId(assignedToUser);
            assignment.setAssignedByUserId(assignedByUser);
            assignment.setAssignedOn(dto.getAssignedOn());
            assignment.setStatus("ACTIVE");
            assignment.setCreatedBy(getCreatedBy());
            assignment.setCreatedAt(LocalDateTime.now());
            assignment.setIsDel(false);
            assignments.add(assignment);
        }
        return assignments;
    }


    public LeadAssignmentSearchResponseDto toSearchResponseDto(Lead lead) {
        Objects.requireNonNull(lead, "Lead must not be null");

        return LeadAssignmentSearchResponseDto.builder()
                .leadIdentity(lead.getIdentity())
                .leadCode(lead.getLeadCode())
                .fullName(lead.getFullName())
                .gender(lead.getGender().getIdentity())
                .contactNumber(lead.getContactNumber())
                .email(lead.getEmail())
                .interestedProduct(lead.getProductService().getIdentity())
                .leadSource(lead.getLeadSource().getIdentity())
                .leadStage(lead.getLeadStage().getIdentity())
                .leadStatus(lead.getLeadStatus().getIdentity())
                .assignToUser(lead.getAssignToUser().getIdentity())
                .build();
    }

    public Integer getCreatedBy() {
        return CommonConstants.CREATED_BY;
    }

    public Integer getUpdatedBy() {
        return CommonConstants.UPDATED_BY;
    }

}