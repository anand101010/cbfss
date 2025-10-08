package com.incede.nbfc.core.monolith.lead.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.dto.LeadRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadSearchResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Mapper for converting between Lead entity and DTOs.
 * Includes null checks and logging for traceability.
 *
 * Author: Incede NBFC Development Team
 * Version: 1.0.0
 */
@Slf4j
@Component
public class LeadMapper {

    /**
     * Convert LeadRequestDto to Lead entity.
     *
     * @param dto Lead request DTO
     * @return Lead entity
     */
    public Lead toEntity(LeadRequestDto dto) {
        Objects.requireNonNull(dto, "LeadRequestDto must not be null");
        log.debug("Mapping LeadRequestDto to Lead entity for tenantId={}", dto.getTenantId());

        Lead lead = new Lead();
        lead.setTenantId(dto.getTenantId());
        lead.setFullName(dto.getFullName());
        lead.setContactNumber(dto.getContactNumber());
        lead.setEmail(dto.getEmail());
        lead.setRemarks(dto.getRemarks());
        lead.setCreatedBy(getCreatedBy());

        log.debug("Lead entity created: {}", lead);
        return lead;
    }

    /**
     * Update an existing Lead entity from DTO.
     *
     * @param lead Existing Lead entity
     * @param dto  Lead request DTO
     */
    public void updateEntityFromDto(Lead lead, LeadRequestDto dto) {
        Objects.requireNonNull(lead, "Lead must not be null");
        Objects.requireNonNull(dto, "LeadRequestDto must not be null");
        log.debug("Updating Lead entity id={} from LeadRequestDto", lead.getIdentity());

        lead.setTenantId(dto.getTenantId());
        lead.setFullName(dto.getFullName());
        lead.setContactNumber(dto.getContactNumber());
        lead.setEmail(dto.getEmail());
        lead.setRemarks(dto.getRemarks());
        lead.setUpdatedBy(getUpdatedBy());

        log.debug("Lead entity updated: {}", lead);
    }

    /**
     * Convert Lead entity to LeadResponseDto.
     * Performs null checks for referenced entities.
     *
     * @param lead   Lead entity
     * @param status Response status
     * @return LeadResponseDto
     */
    public LeadResponseDto toResponseDto(Lead lead, String status) {
        Objects.requireNonNull(lead, "Lead must not be null");
        log.debug("Mapping Lead entity id={} to LeadResponseDto", lead.getIdentity());

        LeadResponseDto.LeadDetails details = LeadResponseDto.LeadDetails.builder()
                .fullName(lead.getFullName())
                .contactNumber(lead.getContactNumber())
                .email(lead.getEmail())
                .assignTo(lead.getAssignToUser() != null ? lead.getAssignToUser().getIdentity() : null)
                .remarks(lead.getRemarks())
                .gender(lead.getGender() != null ? lead.getGender().getIdentity() : null)
                .leadSourceIdentity(lead.getLeadSource() != null ? lead.getLeadSource().getIdentity() : null)
                .leadStageIdentity(lead.getLeadStage() != null ? lead.getLeadStage().getIdentity() : null)
                .leadStatusIdentity(lead.getLeadStatus() != null ? lead.getLeadStatus().getIdentity() : null)
                .interestedProductIdentity(lead.getProductService() != null ? lead.getProductService().getIdentity() : null)
                .build();

        LeadResponseDto response = LeadResponseDto.builder()
                .leadIdentity(lead.getIdentity())
                .leadCode(lead.getLeadCode())
                .status(status)
                .leadDetails(details)
                .build();

        log.debug("LeadResponseDto created: {}", response);
        return response;
    }

    /**
     * Convert Lead entity to LeadSearchResponseDto.
     * Performs null checks for referenced entities.
     *
     * @param lead Lead entity
     * @return LeadSearchResponseDto
     */
    public LeadSearchResponseDto toSearchResponseDto(Lead lead) {
        Objects.requireNonNull(lead, "Lead must not be null");
        log.debug("Mapping Lead entity id={} to LeadSearchResponseDto", lead.getIdentity());

        LeadSearchResponseDto dto = LeadSearchResponseDto.builder()
                .leadIdentity(lead.getIdentity() != null ? lead.getIdentity().toString() : null)
                .leadCode(lead.getLeadCode())
                .fullName(lead.getFullName())
                .gender(lead.getGender() != null ? lead.getGender().getIdentity() : null)
                .contactNumber(lead.getContactNumber())
                .email(lead.getEmail())
                .interestedProduct(lead.getProductService() != null ? lead.getProductService().getIdentity() : null)
                .leadSource(lead.getLeadSource() != null ? lead.getLeadSource().getIdentity() : null)
                .leadStage(lead.getLeadStage() != null ? lead.getLeadStage().getIdentity() : null)
                .leadStatus(lead.getLeadStatus() != null ? lead.getLeadStatus().getIdentity() : null)
                .build();

        log.debug("LeadSearchResponseDto created: {}", dto);
        return dto;
    }

    /**
     * Return constant createdBy value.
     *
     * @return createdBy
     */
    public Integer getCreatedBy() {
        return CommonConstants.CREATED_BY;
    }

    /**
     * Return constant updatedBy value.
     *
     * @return updatedBy
     */
    public Integer getUpdatedBy() {
        return CommonConstants.UPDATED_BY;
    }
}
