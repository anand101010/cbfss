package com.incede.nbfc.core.monolith.lead.service;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadFollowUp;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpBulkUpdateRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpDetailsResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpDetailsRequestDto;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.FollowUpType;
import com.incede.nbfc.core.monolith.lead.repository.LeadFollowUpRepository;
import com.incede.nbfc.core.monolith.lead.repository.LeadRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.FollowUpTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeadFollowUpDetailsService {

    private final LeadFollowUpRepository leadFollowUpRepository;
    private final LeadRepository leadRepository;
    private final FollowUpTypeRepository followUpTypeRepository;

    public LeadFollowUpDetailsResponseDto createFollowUp(UUID leadIdentity, LeadFollowUpDetailsRequestDto leadFollowupDetailsRequestDto)
    {
        log.info("save method triggred in service ");
        Lead lead = leadRepository.findByIdentityAndIsDelFalse(leadIdentity)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with ID: " + leadIdentity));

        FollowUpType followUpType = followUpTypeRepository.findByIdentity(leadFollowupDetailsRequestDto.getFollowUpTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("FollowUpType not found with ID: " + leadFollowupDetailsRequestDto.getFollowUpTypeId()));

        try
        {
            LeadFollowUp  leadFollowUp = new LeadFollowUp();
            leadFollowUp.setLead(lead);
            leadFollowUp.setStaffId(leadFollowupDetailsRequestDto.getStaffId());
            leadFollowUp.setFollowUpType(followUpType);
            leadFollowUp.setFollowUpDate(leadFollowupDetailsRequestDto.getFollowUpDate());
            leadFollowUp.setNextFollowUpDate(leadFollowupDetailsRequestDto.getNextFollowUpDate());
            leadFollowUp.setFollowUpNotes(leadFollowupDetailsRequestDto.getFollowUpNotes());
            leadFollowUp.setIsActive(leadFollowupDetailsRequestDto.getIsActive() != null ? leadFollowupDetailsRequestDto.getIsActive() : true);
            leadFollowUp.setCreatedBy(getCreatedBy());
            LeadFollowUp savedData = leadFollowUpRepository.save(leadFollowUp);
            LeadFollowUpDetailsResponseDto leadFollowUpDetailsResponseDto = new LeadFollowUpDetailsResponseDto();
            leadFollowUpDetailsResponseDto.setStaffId(savedData.getStaffId());
            leadFollowUpDetailsResponseDto.setFollowUpTypeName(savedData.getFollowUpType().getName());
            leadFollowUpDetailsResponseDto.setFollowUpDate(savedData.getFollowUpDate());
            leadFollowUpDetailsResponseDto.setNextFollowUpDate(savedData.getNextFollowUpDate());
            leadFollowUpDetailsResponseDto.setFollowUpNotes(savedData.getFollowUpNotes());
            leadFollowUpDetailsResponseDto.setFollowUpIdentity(savedData.getIdentity());
            leadFollowUpDetailsResponseDto.setMessage(CommonConstants.LEAD_FOLLOW_UP_CREATED);;
            log.info("data saved successfully");
            return  leadFollowUpDetailsResponseDto;
        }
        catch (DataIntegrityViolationException exception)
        {
            log.info("cant save the values :{}",exception);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION, ErrorCodes.CONSTRAINT_VIOLATION, exception);
        }
    }

    public LeadFollowUpDetailsResponseDto updateFollowUp(UUID leadIdentity, UUID followUpId, LeadFollowUpDetailsRequestDto leadFollowupDetailsRequestDto)
    {
        log.info("Update method triggred in service ");
        LeadFollowUp existingData = leadFollowUpRepository.findByIdentityAndIsDelFalseAndIsActiveTrue(followUpId)
                .orElseThrow(() -> new ResourceNotFoundException("FollowUp not found with ID: " + followUpId));
        Lead lead = leadRepository.findByIdentityAndIsDelFalse(leadIdentity)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with ID: " + leadIdentity));
        FollowUpType followUpType = followUpTypeRepository.findByIdentity(leadFollowupDetailsRequestDto.getFollowUpTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("FollowUpType not found with ID: " + leadFollowupDetailsRequestDto.getFollowUpTypeId()));
        try
        {
            existingData.setLead(lead);
            existingData.setStaffId(leadFollowupDetailsRequestDto.getStaffId());
            existingData.setFollowUpType(followUpType);
            existingData.setFollowUpDate(leadFollowupDetailsRequestDto.getFollowUpDate());
            existingData.setNextFollowUpDate(leadFollowupDetailsRequestDto.getNextFollowUpDate());
            existingData.setFollowUpNotes(leadFollowupDetailsRequestDto.getFollowUpNotes());
            existingData.setIsActive(leadFollowupDetailsRequestDto.getIsActive() != null ? leadFollowupDetailsRequestDto.getIsActive() : true);
            existingData.setUpdatedBy(getUpdatedBy());
            LeadFollowUp leadFollowUp = leadFollowUpRepository.save(existingData);
            LeadFollowUpDetailsResponseDto leadFollowUpDetailsResponseDto = new LeadFollowUpDetailsResponseDto();
            leadFollowUpDetailsResponseDto.setStaffId(leadFollowUp.getStaffId());
            leadFollowUpDetailsResponseDto.setFollowUpTypeName(leadFollowUp.getFollowUpType().getName());
            leadFollowUpDetailsResponseDto.setFollowUpDate(leadFollowUp.getFollowUpDate());
            leadFollowUpDetailsResponseDto.setNextFollowUpDate(leadFollowUp.getNextFollowUpDate());
            leadFollowUpDetailsResponseDto.setFollowUpNotes(leadFollowUp.getFollowUpNotes());
            leadFollowUpDetailsResponseDto.setFollowUpIdentity(leadFollowUp.getIdentity());
            leadFollowUpDetailsResponseDto.setMessage(CommonConstants.LEAD_FOLLOW_UP_CREATED);;
            log.info("data updated successfully");
            return  leadFollowUpDetailsResponseDto;
        }
        catch (DataIntegrityViolationException exception)
        {
            log.info("cant save the values :{}",exception);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION, ErrorCodes.CONSTRAINT_VIOLATION, exception);
        }
    }

    @Transactional
    public Page<LeadFollowUpDetailsResponseDto> getAllActiveFollowUps(Pageable pageable)
    {
        log.info("Fetching all active follow-ups, page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        try
        {
            Page<LeadFollowUpDetailsResponseDto> leadFollowUpDetails = leadFollowUpRepository.findAllByIsDelFalseAndIsActiveTrue(pageable)
                    .map(entity -> new LeadFollowUpDetailsResponseDto(
                            entity.getIdentity(),
                            entity.getLead() != null ? entity.getLead().getIdentity() : null,
                            entity.getStaffId(),
                            entity.getFollowUpType() != null ? entity.getFollowUpType().getName() : null,
                            entity.getFollowUpDate(),
                            entity.getNextFollowUpDate(),
                            entity.getFollowUpNotes(),
                            CommonConstants.LEAD_FOLLOW_UP_FETCHED
                    ));

            if (leadFollowUpDetails.isEmpty())
            {
                log.warn("No active follow-ups found");
                throw new ResourceNotFoundException("No active follow-ups found");
            }

            log.info("data fetched successfully");
            return leadFollowUpDetails;
        }
        catch (Exception e)
        {
            log.error("Error occurred while fetching active follow-ups", e);
            throw new BusinessException("Failed to fetch active follow-ups", ErrorCodes.INTERNAL_SERVER_ERROR, e);
        }
    }





    @Transactional
    public Page<LeadFollowUpDetailsResponseDto> searchFollowUps(
            UUID leadIdentity,
            Integer staffId,
            UUID followUpTypeIdentity,
            LocalDate leadDateFrom,
            LocalDate leadDateTo,
            int page,
            int size)
    {
        log.info("Fetching all active follow-ups, page: {}, size: {}", page, size);
        try
        {
            Pageable pageable = PageRequest.of(page, size);

            Page<LeadFollowUpDetailsResponseDto> leadFollowUpDetails =
                    leadFollowUpRepository.searchFollowUps(
                            leadIdentity, staffId, followUpTypeIdentity, leadDateFrom, leadDateTo, pageable
                    ).map(entity -> new LeadFollowUpDetailsResponseDto(
                            entity.getIdentity(),
                            entity.getLead().getIdentity(),
                            entity.getStaffId(),
                            entity.getFollowUpType() != null ? entity.getFollowUpType().getName() : null,
                            entity.getFollowUpDate(),
                            entity.getNextFollowUpDate(),
                            entity.getFollowUpNotes(),
                            CommonConstants.LEAD_FOLLOW_UP_FETCHED
                    ));

            if (leadFollowUpDetails.isEmpty())
            {
                log.warn("No follow-up records found for given search criteria");
                throw new ResourceNotFoundException("No follow-up records found for given search criteria");
            }
            log.info("data fetched successfully");
            return leadFollowUpDetails;
        }
        catch (DataAccessException e)
        {
            log.error("Error occurred while fetching active follow-ups", e);
            throw new BusinessException("Failed to fetch active follow-ups", ErrorCodes.INTERNAL_SERVER_ERROR, e);
        }

    }
    @Transactional
    public List<LeadFollowUpDetailsResponseDto> bulkUpdateFollowUps(List<LeadFollowUpBulkUpdateRequestDto> requestList) {
        log.info("Bulk update service triggered for {} follow-ups", requestList.size());
        List<LeadFollowUpDetailsResponseDto> responses = new ArrayList<>();

        for (LeadFollowUpBulkUpdateRequestDto request : requestList)
        {
            UUID leadIdentity = request.getLeadIdentity();
            UUID followUpId = request.getFollowUpId();
            LeadFollowUpDetailsRequestDto dto = request.getFollowUpDetails();

            try {
                LeadFollowUp existingData = leadFollowUpRepository.findByIdentityAndIsDelFalseAndIsActiveTrue(followUpId)
                        .orElseThrow(() -> new ResourceNotFoundException("FollowUp not found with ID: " + followUpId));

                Lead lead = leadRepository.findByIdentityAndIsDelFalse(leadIdentity)
                        .orElseThrow(() -> new ResourceNotFoundException("Lead not found with ID: " + leadIdentity));

                FollowUpType followUpType = followUpTypeRepository.findByIdentity(dto.getFollowUpTypeId())
                        .orElseThrow(() -> new ResourceNotFoundException("FollowUpType not found with ID: " + dto.getFollowUpTypeId()));

                existingData.setLead(lead);
                existingData.setStaffId(dto.getStaffId());
                existingData.setFollowUpType(followUpType);
                existingData.setFollowUpDate(dto.getFollowUpDate());
                existingData.setNextFollowUpDate(dto.getNextFollowUpDate());
                existingData.setFollowUpNotes(dto.getFollowUpNotes());
                existingData.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
                existingData.setUpdatedBy(getUpdatedBy());

                LeadFollowUp updated = leadFollowUpRepository.save(existingData);

                LeadFollowUpDetailsResponseDto response = new LeadFollowUpDetailsResponseDto();
                response.setStaffId(updated.getStaffId());
                response.setFollowUpTypeName(updated.getFollowUpType().getName());
                response.setFollowUpDate(updated.getFollowUpDate());
                response.setNextFollowUpDate(updated.getNextFollowUpDate());
                response.setFollowUpNotes(updated.getFollowUpNotes());
                response.setFollowUpIdentity(updated.getIdentity());
                response.setMessage(CommonConstants.LEAD_FOLLOW_UP_UPDATED);

                responses.add(response);
            } catch (DataIntegrityViolationException exception) {
                log.error("Constraint violation while updating follow-up {}: {}", followUpId, exception.getMessage());
                throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION, ErrorCodes.CONSTRAINT_VIOLATION, exception);
            }
        }
        log.info("Bulk update completed successfully");
        return responses;
    }





    public Integer getCreatedBy()
    {
        return CommonConstants.CREATED_BY;
    }

    public Integer getUpdatedBy()
    {
        return CommonConstants.UPDATED_BY;
    }
}

