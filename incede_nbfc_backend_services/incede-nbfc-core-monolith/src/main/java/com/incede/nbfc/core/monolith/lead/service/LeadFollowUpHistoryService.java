package com.incede.nbfc.core.monolith.lead.service;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadFollowUpHistory;
import com.incede.nbfc.core.monolith.lead.dto.BulkFollowUpHistoryRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpHistoryRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpHistoryResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadsFollowUpHistoryDto;
import com.incede.nbfc.core.monolith.lead.repository.LeadFollowUpHistoryRepository;
import com.incede.nbfc.core.monolith.lead.repository.LeadRepository;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.FollowUpType;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.LeadStage;
import com.incede.nbfc.core.monolith.masterdata.repository.FollowUpTypeRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.LeadStageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeadFollowUpHistoryService {

    private final LeadFollowUpHistoryRepository leadFollowUpHistoryRepository;
    private final LeadRepository leadRepository;
    private final FollowUpTypeRepository followUpTypeRepository;
    private final LeadStageRepository leadStageRepository;

    /**
     *
     * @param leadIdentity  lead identity
     * @param leadFollowUpHistoryRequestDto
     * @return LeadsFollowUpHistoryDto
     */
    @Transactional
    public LeadsFollowUpHistoryDto saveFollowUpHistory(UUID leadIdentity, LeadFollowUpHistoryRequestDto leadFollowUpHistoryRequestDto)
    {

        Lead lead = leadRepository
                .findByIdentityAndIsDelFalse(leadIdentity)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found"));
        LeadStage leadStage = leadStageRepository.findByIdentityAndIsDelFalseAndIsActiveTrue(leadFollowUpHistoryRequestDto.getLeadStageIdentity())
                .orElseThrow(() -> new ResourceNotFoundException("Lead stage not found"));

        FollowUpType followUpType = followUpTypeRepository
                .findByIdentity(leadFollowUpHistoryRequestDto.getFollowUpTypeIdentity())
                .orElseThrow(() -> new ResourceNotFoundException("FollowUpType not found"));
        try
        {
            LeadFollowUpHistory history = new LeadFollowUpHistory();
            history.setLeadStage(leadStage);
            history.setLead(lead);
            history.setStaffId(leadFollowUpHistoryRequestDto.getStaffId());
            history.setFollowUpTypeId(followUpType);
            history.setFollowUpDate(leadFollowUpHistoryRequestDto.getFollowUpDate());
            history.setNextFollowUpDate(leadFollowUpHistoryRequestDto.getNextFollowUpDate());
            history.setFollowUpNotes(leadFollowUpHistoryRequestDto.getFollowUpNotes());
            history.setChangeType(leadFollowUpHistoryRequestDto.getChangeType());
            history.setStageChangeRemarks(leadFollowUpHistoryRequestDto.getStageChangeRemarks());
            history.setCreatedBy(getCreatedBy());

            LeadFollowUpHistory savedHistory = leadFollowUpHistoryRepository.save(history);
            log.info("Follow-up history saved successfully");

            return new LeadsFollowUpHistoryDto(
                    savedHistory.getIdentity(),
                    savedHistory.getLead().getIdentity()
                    ,CommonConstants.LEAD_FOLLOW_UP_HISTORY_CREATED
            );


        }
        catch (DataIntegrityViolationException exception)
        {
            log.error("Error saving follow-up history", exception);
            throw new BusinessException("Failed to save follow-up history", ErrorCodes.INTERNAL_SERVER_ERROR, exception);
        }

    }

    /**
     *
     * @param leadIdentity
     * @param followUpHistoryIdentity
     * @param leadFollowUpHistoryRequestDto
     * @return LeadsFollowUpHistoryDto
     */
    @Transactional
    public LeadsFollowUpHistoryDto updateFollowUpHistory(UUID leadIdentity, UUID followUpHistoryIdentity, LeadFollowUpHistoryRequestDto leadFollowUpHistoryRequestDto) {

        Lead lead = leadRepository
                .findByIdentityAndIsDelFalse(leadIdentity)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found"));


        LeadFollowUpHistory existingHistory = leadFollowUpHistoryRepository
                .findByIdentityAndIsDelFalse(followUpHistoryIdentity)
                .orElseThrow(() -> new ResourceNotFoundException("Follow-up history not found"));
        LeadStage leadStage = leadStageRepository
                .findByIdentityAndIsDelFalseAndIsActiveTrue(leadFollowUpHistoryRequestDto.getLeadStageIdentity())
                .orElseThrow(() -> new ResourceNotFoundException("Active lead stage not found"));

        FollowUpType followUpType = followUpTypeRepository
                .findByIdentity(leadFollowUpHistoryRequestDto.getFollowUpTypeIdentity())
                .orElseThrow(() -> new ResourceNotFoundException("Follow-up type not found"));

        try {

            existingHistory.setLeadStage(leadStage);
            existingHistory.setLead(lead);
            existingHistory.setStaffId(leadFollowUpHistoryRequestDto.getStaffId());
            existingHistory.setFollowUpTypeId(followUpType);
            existingHistory.setFollowUpDate(leadFollowUpHistoryRequestDto.getFollowUpDate());
            existingHistory.setNextFollowUpDate(leadFollowUpHistoryRequestDto.getNextFollowUpDate());
            existingHistory.setFollowUpNotes(leadFollowUpHistoryRequestDto.getFollowUpNotes());
            existingHistory.setChangeType(leadFollowUpHistoryRequestDto.getChangeType());
            existingHistory.setStageChangeRemarks(leadFollowUpHistoryRequestDto.getStageChangeRemarks());
            existingHistory.setUpdatedBy(getUpdatedBy());
            LeadFollowUpHistory updatedHistory = leadFollowUpHistoryRepository.save(existingHistory);

            log.info("Follow-up history updated successfully for ID: {}", followUpHistoryIdentity);

            return new LeadsFollowUpHistoryDto(
                    updatedHistory.getIdentity(),
                    updatedHistory.getLead().getIdentity(),
                    CommonConstants.LEAD_FOLLOW_UP_UPDATED
            );

        } catch (DataIntegrityViolationException exception) {
            log.error("Error updating follow-up history", exception);
            throw new BusinessException("Failed to update follow-up history", ErrorCodes.INTERNAL_SERVER_ERROR, exception);
        }
    }


    /**
     *
     * @param leadIdentity  using the identity fetch data
     * @return LeadFollowUpHistoryResponseDto
     */

    @Transactional(readOnly = true)
    public LeadFollowUpHistoryResponseDto getFollowUpHistory(UUID leadIdentity) {
        log.info("Fetching follow-up history for lead: {}", leadIdentity);

        Lead lead = leadRepository.findByIdentityAndIsDelFalse(leadIdentity)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid LeadId || Lead not found with ID: " + leadIdentity));

        List<LeadFollowUpHistory> histories = leadFollowUpHistoryRepository
                .findByLeadIdentityAndIsDelFalseOrderByFollowUpDateDesc(leadIdentity);

        if (histories.isEmpty()) {
            throw new ResourceNotFoundException("No follow-up history found for lead ID: " + leadIdentity);
        }
        try {
            List<LeadFollowUpHistoryResponseDto.FollowUpHistory> historyResponse = histories.stream()
                    .map(history -> new LeadFollowUpHistoryResponseDto.FollowUpHistory(
                            history.getIdentity(),
                            history.getLead() != null ? history.getLead().getIdentity() : null,
                            history.getStaffId(),
                            history.getFollowUpDate(),
                            history.getNextFollowUpDate(),
                            history.getFollowUpTypeId() != null
                                    ? new LeadFollowUpHistoryResponseDto.FollowUpType(
                                    history.getFollowUpTypeId().getIdentity(),
                                    history.getFollowUpTypeId().getName())
                                    : null,
                            history.getLeadStage() != null
                                    ? new LeadFollowUpHistoryResponseDto.LeadStage(
                                    history.getLeadStage().getIdentity(),
                                    history.getLeadStage().getName())
                                    : null,
                            history.getStageChangeRemarks(),
                            history.getFollowUpNotes(),
                            history.getChangeType()
                    ))
                    .toList();
            return new LeadFollowUpHistoryResponseDto(historyResponse, CommonConstants.LEAD_FOLLOW_UP_FETCHED);
        } catch (DataAccessException e)
        {
            log.error("Error occurred while fetching follow-up history", e);
            throw new BusinessException("Failed to fetch follow-up history", ErrorCodes.INTERNAL_SERVER_ERROR, e);
        }


    }

    /**
     *
     * @param leadIdentity
     * @param staffIdentity
     * @param followUpTypeIdentity
     * @param leadStageIdentity
     * @param leadDateFrom
     * @param leadDateTo
     * @param page
     * @param size
     * @return LeadFollowUpHistoryResponseDto
     */
    @Transactional(readOnly = true)
    public LeadFollowUpHistoryResponseDto searchFollowUpHistory(
            UUID leadIdentity,
            Integer staffIdentity,
            UUID followUpTypeIdentity,
            UUID leadStageIdentity,
            LocalDate leadDateFrom,
            LocalDate leadDateTo,
            int page,
            int size) {

        log.info("Searching follow-up history with filters for lead: {}", leadIdentity);

        try {
            Pageable pageable = PageRequest.of(page, size);

            Page<LeadFollowUpHistory> histories = leadFollowUpHistoryRepository.searchFollowUpHistory(
                    leadIdentity, staffIdentity, followUpTypeIdentity,leadStageIdentity, leadDateFrom, leadDateTo, pageable
            );

            if (histories.isEmpty()) {
                log.warn("No follow-up history found for the given criteria");
                throw new ResourceNotFoundException("No follow-up history found for the given criteria");
            }

            List<LeadFollowUpHistoryResponseDto.FollowUpHistory> historyResponse = histories.stream()
                    .map(history -> new LeadFollowUpHistoryResponseDto.FollowUpHistory(
                            history.getIdentity(),
                            history.getLead() != null ? history.getLead().getIdentity() : null,
                            history.getStaffId() ,
                            history.getFollowUpDate(),
                            history.getNextFollowUpDate(),
                            history.getFollowUpTypeId() != null
                                    ? new LeadFollowUpHistoryResponseDto.FollowUpType(
                                    history.getFollowUpTypeId().getIdentity(),
                                    history.getFollowUpTypeId().getName())
                                    : null,
                            history.getLeadStage() != null
                                    ? new LeadFollowUpHistoryResponseDto.LeadStage(
                                    history.getLeadStage().getIdentity(),
                                    history.getLeadStage().getName())
                                    : null,
                            history.getStageChangeRemarks(),
                            history.getFollowUpNotes(),
                            history.getChangeType()
                    ))
                    .toList();

            log.info("Follow-up history fetched successfully");
            return new LeadFollowUpHistoryResponseDto(historyResponse, CommonConstants.LEAD_FOLLOW_UP_FETCHED);

        } catch (DataAccessException e) {
            log.error("Error occurred while fetching follow-up history", e);
            throw new BusinessException("Failed to fetch follow-up history", ErrorCodes.INTERNAL_SERVER_ERROR, e);
        }
    }

    /**
     *
     * @paramList<LeadFollowUpHistoryRequestDto>    for bulk saving
     * @return List<LeadsFollowUpHistoryDto>
     */
    @Transactional
    public List<LeadsFollowUpHistoryDto> bulkSaveFollowUpHistory(
            List<LeadFollowUpHistoryRequestDto> requests) {

        List<LeadsFollowUpHistoryDto> responses = new ArrayList<>();

        for (LeadFollowUpHistoryRequestDto dto : requests)
        {


            Lead lead = leadRepository.findByIdentityAndIsDelFalse(dto.getLeadIdentity())
                    .orElseThrow(() -> new ResourceNotFoundException("Lead not found: " + dto.getLeadIdentity()));


            LeadStage leadStage = leadStageRepository
                    .findByIdentityAndIsDelFalseAndIsActiveTrue(dto.getLeadStageIdentity())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Lead stage not found: " + dto.getLeadStageIdentity()));


            FollowUpType followUpType = followUpTypeRepository
                    .findByIdentity(dto.getFollowUpTypeIdentity())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "FollowUpType not found: " + dto.getFollowUpTypeIdentity()));


            try
            {

                if (dto == null || dto.getLeadIdentity() == null) {
                    throw new BusinessException("Invalid input in bulk save request", ErrorCodes.BAD_REQUEST);
                }

                LeadFollowUpHistory history = new LeadFollowUpHistory();
                history.setLead(lead);
                history.setLeadStage(leadStage);
                history.setStaffId(dto.getStaffId());
                history.setFollowUpTypeId(followUpType);
                history.setFollowUpDate(dto.getFollowUpDate());
                history.setNextFollowUpDate(dto.getNextFollowUpDate());
                history.setFollowUpNotes(dto.getFollowUpNotes());
                history.setChangeType(dto.getChangeType());
                history.setStageChangeRemarks(dto.getStageChangeRemarks());
                history.setCreatedBy(getCreatedBy());


                LeadFollowUpHistory saved = leadFollowUpHistoryRepository.save(history);
                log.info("Follow-up history saved successfully for lead: {}", dto.getLeadIdentity());


                responses.add(new LeadsFollowUpHistoryDto(
                        saved.getIdentity(),
                        saved.getLead().getIdentity(),
                        CommonConstants.LEAD_FOLLOW_UP_HISTORY_CREATED
                ));

            } catch (DataIntegrityViolationException ex) {
                log.error("Error saving follow-up history for lead: {}", dto != null ? dto.getLeadIdentity() : "UNKNOWN", ex);
                throw new BusinessException("Failed to save one or more follow-up records",
                        ErrorCodes.INTERNAL_SERVER_ERROR, ex);
            }
        }

        log.info("Bulk follow-up history save completed successfully. Total saved: {}", responses.size());
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
