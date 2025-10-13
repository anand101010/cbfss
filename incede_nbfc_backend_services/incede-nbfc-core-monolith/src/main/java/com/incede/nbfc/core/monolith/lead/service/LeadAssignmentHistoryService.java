package com.incede.nbfc.core.monolith.lead.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadAssignmentHistory;
import com.incede.nbfc.core.monolith.lead.dto.LeadAssignmentHistoryRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadAssignmentResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadAssignmentSearchResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadResponseDto;
import com.incede.nbfc.core.monolith.lead.mapper.LeadAssignmentHistoryMapper;
import com.incede.nbfc.core.monolith.lead.mapper.LeadMapper;
import com.incede.nbfc.core.monolith.lead.repository.LeadAssignmentHistoryRepository;
import com.incede.nbfc.core.monolith.lead.repository.LeadRepository;
import com.incede.nbfc.core.monolith.user.domain.entity.User;
import com.incede.nbfc.core.monolith.user.mapper.UserMapper;
import com.incede.nbfc.core.monolith.user.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeadAssignmentHistoryService {

    private final LeadAssignmentHistoryRepository leadAssignmentHistoryRepository;
    private final LeadRepository leadRepository;
    private final UserRepository userRepository;
    private final LeadAssignmentHistoryMapper leadAssignmentHistoryMapper;
    private final LeadMapper leadMapper;
    private final UserMapper  userMapper;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     *
     * @param productIdentity
     * @param leadSourceIdentity
     * @param leadStageIdentity
     * @param gender
     * @param assignToUser
     * @param leadDate
     * @param page
     * @param size
     * @return
     */
    @Transactional(readOnly = true)
    public Page<LeadAssignmentSearchResponseDto> searchLeadsForAssignment(
            UUID productIdentity, UUID leadSourceIdentity, UUID leadStageIdentity,
            UUID gender, UUID assignToUser, LocalDate leadDate, int page, int size) {
        log.info("Searching leads for assignment with criteria: product={}, source={}, stage={}, gender={}, assignTo={}, date={}",
                productIdentity, leadSourceIdentity, leadStageIdentity, gender, assignToUser, leadDate);
        LocalDateTime dateTimeStart = leadDate != null ? leadDate.atStartOfDay() : null;
        LocalDateTime dateTimeEnd = leadDate != null ? leadDate.atTime(23, 59, 59) : null;

        Pageable pageable = PageRequest.of(page, size);
        Page<Lead> leadsPage = leadRepository.searchLeadsForAssignment(
                productIdentity, leadSourceIdentity, leadStageIdentity, gender, assignToUser, dateTimeStart, dateTimeEnd, pageable);

        if (leadsPage.isEmpty()) {
            log.warn("No leads found for assignment criteria");
            throw new ResourceNotFoundException("Lead", "No leads found for given search criteria");
        }

        log.info("Found {} leads matching assignment criteria", leadsPage.getTotalElements());
        return leadsPage.map(leadAssignmentHistoryMapper::toSearchResponseDto);
    }


    private void validateDto(LeadAssignmentHistoryRequestDto dto) {
        log.debug("Validating LeadAssignmentHistoryRequestDto: {}", dto);
        Set<ConstraintViolation<LeadAssignmentHistoryRequestDto>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .collect(Collectors.joining(", "));
            log.error("LeadAssignmentHistoryRequestDto validation failed: {}", errorMsg);
            throw new BusinessException(errorMsg, ErrorCodes.VALIDATION_FAILED);
        }
    }

    /**
     *
     * @param dto
     * @return
     */
    @Transactional
    public String bulkUpdateLeadAssignments(LeadAssignmentHistoryRequestDto dto) {
        log.info("Bulk updating assignments for {} leads to userId={}", dto.getLeadIdentities().size(), dto.getAssignedToUserIdentity());
        validateDto(dto);
        User assignedToUser = userRepository.findByIdentity(dto.getAssignedToUserIdentity())
                .orElseThrow(() -> new BusinessException("Invalid user: " + dto.getAssignedToUserIdentity(), ErrorCodes.VALIDATION_FAILED));

        User assignedByUser = userRepository.findById(CommonConstants.UPDATED_BY)
                .orElseThrow(() -> new BusinessException("Invalid assigned-by user", ErrorCodes.VALIDATION_FAILED));
        List<Lead> leads = new ArrayList<>();
        for (UUID leadIdentity : dto.getLeadIdentities()) {
            Lead lead = leadRepository.findByIdentity(leadIdentity)
                    .orElseThrow(() -> new ResourceNotFoundException("Lead not found", leadIdentity.toString()));
            leads.add(lead);
        }

        for (Lead lead : leads) {
            List<LeadAssignmentHistory> existingAssignments =
                    leadAssignmentHistoryRepository.findAllByLeadAndIsDelFalse(lead);

            for (LeadAssignmentHistory existing : existingAssignments) {
                existing.setStatus(CommonConstants.LEAD_ASSIGNMENT_STATUS_REASSIGNED);

                existing.setUpdatedBy(CommonConstants.UPDATED_BY);
                existing.setUpdatedAt(LocalDateTime.now());
            }

            if (!existingAssignments.isEmpty()) {
                leadAssignmentHistoryRepository.saveAll(existingAssignments);
            }
        }

        List<LeadAssignmentHistory> assignments =
                leadAssignmentHistoryMapper.toEntityList(dto, leads, assignedToUser, assignedByUser);

        List<LeadAssignmentHistory> savedAssignments = leadAssignmentHistoryRepository.saveAll(assignments);

        for (Lead lead : leads) {
            lead.setAssignToUser(assignedToUser);
            lead.setUpdatedBy(CommonConstants.UPDATED_BY);
            lead.setUpdatedAt(LocalDateTime.now());
            leadRepository.save(lead);
        }

        int leadCount = savedAssignments.size();
        String leadText = leadCount == 1 ? "lead" : "leads";
        log.info("Successfully updated assignments for {} {} to userId {}", leadCount, leadText, dto.getAssignedToUserIdentity());
        return "{\"message\": \"" + leadCount + " " + leadText + " assigned successfully\"}";
    }
    @Transactional
    public List<LeadAssignmentResponseDto> fetchLeadAssignmentHistory(UUID leadIdentity) {
        try {
            Lead lead = leadRepository.findByIdentity(leadIdentity)
                    .orElseThrow(() -> {
                        log.error("Lead not found for identity={}", leadIdentity);
                        return new BusinessException(
                                "Lead not found for the given identity",
                                ErrorCodes.RESOURCE_NOT_FOUND
                        );
                    });

            log.info("Fetching assignment history for lead identity={}", leadIdentity);

            List<LeadAssignmentHistory> historyList = leadAssignmentHistoryRepository.findAllByLeadAndIsDelFalse(lead).stream()
                    .filter(history -> !Boolean.TRUE.equals(history.getIsDel())) // Exclude deleted
                    .filter(history -> history.getLead() != null &&
                            history.getLead().getLeadId().equals(lead.getLeadId())) // Match lead
                    .sorted(Comparator.comparing(LeadAssignmentHistory::getAssignedOn).reversed()) // Latest first
                    .toList();

            log.info("Total assignment records found for lead {}: {}", leadIdentity, historyList.size());

            return historyList.stream().map(history -> {
                try {
                    LeadAssignmentResponseDto dto = new LeadAssignmentResponseDto();
                    dto.setLeadResponseDto(leadMapper.toResponseDto(lead, history.getStatus()));
                    dto.setAssignedOn(history.getAssignedOn());
                    dto.setStatus(history.getStatus());
                    dto.setRemarks(history.getRemarks());
                    dto.setLeadAssignIdentity(history.getIdentity());
                    dto.setAssignedTo(userMapper.toResponseDto(history.getAssignedToUserId()));
                    dto.setAssignedBy(userMapper.toResponseDto(history.getAssignedByUserId()));
                    return dto;
                } catch (Exception e) {
                    log.error("Error mapping LeadAssignmentHistory to DTO for historyId={}", history.getAssignmentHistoryId(), e);
                    throw new BusinessException(
                            "Failed to map lead assignment history to DTO",
                            ErrorCodes.BUSINESS_RULE_VIOLATION,
                            e
                    );
                }
            }).toList();

        } catch (BusinessException be) {
            throw be;
        } catch (DataAccessException e) {
            log.error("Unexpected error while fetching assignment history for leadIdentity={}", leadIdentity, e);
            throw new BusinessException(
                    "An unexpected error occurred while fetching assignment history",
                    ErrorCodes.INTERNAL_SERVER_ERROR,
                    e
            );
        }
    }


    @Transactional
    public LeadResponseDto updateLeadAssignment(UUID leadIdentity, UUID assignedToIdentity, UUID assignedByIdentity, LocalDate assignedOn) {
        log.info("Starting lead assignment update for leadIdentity={} to assigneeIdentity={}", leadIdentity, assignedToIdentity);

        Lead lead = leadRepository.findByIdentity(leadIdentity)
                .orElseThrow(() -> {
                    log.error("Lead not found for the identity={}", leadIdentity);
                    return new BusinessException("Lead not found for the given identity", ErrorCodes.RESOURCE_NOT_FOUND);
                });

        User assignedTo = userRepository.findByIdentity(assignedToIdentity)
                .orElseThrow(() -> {
                    log.error("Assigned To User not found for identity={}", assignedToIdentity);
                    return new BusinessException("User not found for the given identity", ErrorCodes.RESOURCE_NOT_FOUND);
                });

        User assignedBy = userRepository.findByIdentity(assignedByIdentity)
                .orElseThrow(() -> {
                    log.error("Assigned By User not found for identity={}", assignedByIdentity);
                    return new BusinessException("User not found for the given identity", ErrorCodes.RESOURCE_NOT_FOUND);
                });

        try {
            String reassigned = CommonConstants.LEAD_ASSIGNMENT_STATUS_REASSIGNED;
            List<LeadAssignmentHistory> historyList = leadAssignmentHistoryRepository.findAllByLeadAndIsDelFalse(lead);

            if (historyList != null && !historyList.isEmpty()) {
                historyList.stream()
                        .filter(history -> !reassigned.equalsIgnoreCase(history.getStatus()))
                        .peek(history -> log.debug("Marking assignmentHistoryId={} as REASSIGNED", history.getAssignmentHistoryId()))
                        .forEach(history ->{
                            history.setStatus(reassigned);
                            history.setUpdatedBy(CommonConstants.UPDATED_BY);
                        });

                leadAssignmentHistoryRepository.saveAll(historyList);
                log.info("Previous lead assignments marked as REASSIGNED for leadId={}", lead.getLeadId());
            } else {
                log.info("No previous assignment history found for leadId={}", lead.getLeadId());
            }

            lead.setAssignToUser(assignedTo);
            lead.setCurrentAssignmentDate(assignedOn);
            leadRepository.save(lead);

            LeadAssignmentHistory newHistory = new LeadAssignmentHistory();
            newHistory.setLead(lead);
            newHistory.setAssignedToUserId(assignedTo);
            newHistory.setAssignedOn(assignedOn);
            newHistory.setStatus(CommonConstants.LEAD_ASSIGNMENT_STATUS_ACTIVE);
            newHistory.setRemarks(CommonConstants.LEAD_ASSIGNMENT_REMARK_REASSIGNED);
            newHistory.setAssignedByUserId(assignedBy);
            newHistory.setCreatedBy(CommonConstants.CREATED_BY);
            leadAssignmentHistoryRepository.save(newHistory);

            log.info("Lead reassigned successfully: leadIdentity={}, assigneeUserId={}", leadIdentity, assignedTo.getUserId());

            return LeadResponseDto.builder()
                    .leadIdentity(lead.getIdentity())
                    .status("Assignment updated")
                    .build();

        } catch (DataIntegrityViolationException e) {
            log.error("Error while updating lead assignment for leadIdentity={}", leadIdentity, e);
            throw new BusinessException("Failed to update lead assignment", ErrorCodes.INTERNAL_SERVER_ERROR, e);
        }
    }


}