package com.incede.nbfc.core.monolith.lead.service;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadFollowUp;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadFollowUpHistory;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpHistoryRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpHistoryResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadsFollowUpHistoryDto;
import com.incede.nbfc.core.monolith.lead.repository.LeadFollowUpHistoryRepository;
import com.incede.nbfc.core.monolith.lead.repository.LeadFollowUpRepository;
import com.incede.nbfc.core.monolith.lead.repository.LeadRepository;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.FollowUpType;
import com.incede.nbfc.core.monolith.masterdata.repository.FollowUpTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeadFollowUpHistoryService {

    private final LeadFollowUpHistoryRepository leadFollowUpHistoryRepository;
    private final LeadFollowUpRepository leadFollowUpRepository;
    private final LeadRepository leadRepository;
    private final FollowUpTypeRepository followUpTypeRepository;

        @Transactional
        public LeadsFollowUpHistoryDto saveFollowUpHistory(UUID leadIdentity, LeadFollowUpHistoryRequestDto leadFollowUpHistoryRequestDto)
        {

            log.info("Follow-up history save triggered");
            LeadFollowUp leadFollowUp = leadFollowUpRepository
                    .findByIdentityAndIsDelFalseAndIsActiveTrue(leadFollowUpHistoryRequestDto.getLeadFollowUpIdentity())
                    .orElseThrow(() -> new ResourceNotFoundException("LeadFollowUp not found"));

            Lead lead = leadRepository
                    .findByIdentityAndIsDelFalse(leadIdentity)
                    .orElseThrow(() -> new ResourceNotFoundException("Lead not found"));

            FollowUpType followUpType = followUpTypeRepository
                    .findByIdentity(leadFollowUpHistoryRequestDto.getFollowUpTypeIdentity())
                    .orElseThrow(() -> new ResourceNotFoundException("FollowUpType not found"));
            try
            {
                    LeadFollowUpHistory history = new LeadFollowUpHistory();
                    history.setLeadFollowUp(leadFollowUp);
                    history.setLead(lead);
                    history.setStaffId(leadFollowUpHistoryRequestDto.getStaffId());
                    history.setFollowUpTypeId(followUpType);
                    history.setFollowUpDate(leadFollowUpHistoryRequestDto.getFollowUpDate());
                    history.setNextFollowUpDate(leadFollowUpHistoryRequestDto.getNextFollowUpDate());
                    history.setFollowUpNotes(leadFollowUpHistoryRequestDto.getFollowUpNotes());
                    history.setChangeType(leadFollowUpHistoryRequestDto.getChangeType());
                    history.setCreatedBy(getCreatedBy());
                    LeadFollowUpHistory savedHistory = leadFollowUpHistoryRepository.save(history);
                    log.info("Follow-up history saved successfully");

                return new LeadsFollowUpHistoryDto(
                            savedHistory.getIdentity(),
                            savedHistory.getLead().getIdentity(),
                            savedHistory.getLeadFollowUp().getIdentity(),
                            savedHistory.getStaffId(),
                            savedHistory.getFollowUpTypeId().getIdentity(),
                            savedHistory.getFollowUpDate(),
                            savedHistory.getNextFollowUpDate(),
                            savedHistory.getFollowUpNotes(),
                            savedHistory.getChangeType()
                        ,CommonConstants.LEAD_FOLLOW_UP_HISTORY_CREATED
                    );


            }
            catch (Exception exception)
            {
                log.error("Error saving follow-up history", exception);
                throw new BusinessException("Failed to save follow-up history", ErrorCodes.INTERNAL_SERVER_ERROR, exception);
            }

        }


        @Transactional(readOnly = true)
        public LeadFollowUpHistoryResponseDto getFollowUpHistory(UUID leadIdentity)
        {
            Lead lead = leadRepository.findByIdentityAndIsDelFalse(leadIdentity)
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid LeadId || Lead not found with ID: " + leadIdentity));

            List<LeadFollowUpHistory> histories = leadFollowUpHistoryRepository.findByLeadIdentityAndIsDelFalseOrderByFollowUpDateDesc(leadIdentity);

            if (histories.isEmpty())
            {
                throw new ResourceNotFoundException("No follow-up history found for lead ID: " + leadIdentity);
            }
            List<LeadFollowUpHistoryResponseDto.History> historyResponse = histories.stream()
                    .map(leadFollowUpHistory -> new LeadFollowUpHistoryResponseDto.History(
                            leadFollowUpHistory.getIdentity(),
                            leadFollowUpHistory.getLead() != null ? leadFollowUpHistory.getLead().getIdentity() : null,
                            leadFollowUpHistory.getLeadFollowUp() != null ? leadFollowUpHistory.getLeadFollowUp().getIdentity() : null,
                            leadFollowUpHistory.getStaffId(),
                            leadFollowUpHistory.getFollowUpTypeId() != null ? leadFollowUpHistory.getFollowUpTypeId().getIdentity() : null,
                            leadFollowUpHistory.getFollowUpDate(),
                            leadFollowUpHistory.getNextFollowUpDate(),
                            leadFollowUpHistory.getFollowUpNotes(),
                            leadFollowUpHistory.getChangeType()
                    ))
                    .toList();
            return new LeadFollowUpHistoryResponseDto(historyResponse,CommonConstants.LEAD_FOLLOW_UP_FETCHED);
        }
        @Transactional(readOnly = true)
        public LeadFollowUpHistoryResponseDto searchFollowUpHistory(
                UUID leadIdentity,
                UUID leadFollowUpIdentity,
                Integer staffIdentity,
                UUID followUpTypeIdentity,
                LocalDate leadDateFrom,
                LocalDate leadDateTo,
                int page,
                int size)
        {
                log.info("Searching follow-up history");
             try
            {
                     Pageable pageable = PageRequest.of(page, size);

                     Page<LeadFollowUpHistory> histories = leadFollowUpHistoryRepository.searchFollowUpHistory(
                             leadIdentity, leadFollowUpIdentity, staffIdentity, followUpTypeIdentity, leadDateFrom, leadDateTo, pageable
                     );
                     if (histories.isEmpty())
                     {
                         log.warn("No follow-up history found for the given criteria");
                         throw new ResourceNotFoundException("No follow-up history found for the given criteria");
                     }

                     List<LeadFollowUpHistoryResponseDto.History> historyResponse = histories.stream()
                             .map(history -> new LeadFollowUpHistoryResponseDto.History(
                                     history.getIdentity(),
                                     history.getLead() != null ? history.getLead().getIdentity() : null,
                                     history.getLeadFollowUp() != null ? history.getLeadFollowUp().getIdentity() : null,
                                     history.getStaffId(),
                                     history.getFollowUpTypeId() != null ? history.getFollowUpTypeId().getIdentity() : null,
                                     history.getFollowUpDate(),
                                     history.getNextFollowUpDate(),
                                     history.getFollowUpNotes(),
                                     history.getChangeType()
                             ))
                             .toList();
                     if (historyResponse.isEmpty())
                     {
                         log.warn("No follow-up history records found for given search criteria");
                         throw new ResourceNotFoundException("No follow-up records found for given search criteria");
                     }
                     log.info("data fetched successfully");
                     return new LeadFollowUpHistoryResponseDto(historyResponse, CommonConstants.LEAD_FOLLOW_UP_FETCHED);
             }
             catch (DataAccessException e)
             {
                log.error("Error occurred while fetching active follow-ups", e);
                throw new BusinessException("Failed to fetch active follow-ups", ErrorCodes.INTERNAL_SERVER_ERROR, e);
             }
        }

        public Integer getCreatedBy()
        {
            return CommonConstants.CREATED_BY;
        }

}
