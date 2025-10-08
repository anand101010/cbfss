package com.incede.nbfc.core.monolith.lead.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.dto.LeadRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadSearchResponseDto;
import com.incede.nbfc.core.monolith.lead.mapper.LeadMapper;
import com.incede.nbfc.core.monolith.lead.repository.LeadRepository;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import com.incede.nbfc.core.monolith.user.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing Lead operations.
 *
 * Author: Incede NBFC Development Team
 * Version: 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeadService {

    private final LeadRepository leadRepository;
    private final LeadMapper leadMapper;

    private final GendersRepository genderRepository;
    private final UserRepository userRepository;
    private final LeadSourceRepository leadSourceRepository;
    private final LeadStageRepository leadStageRepository;
    private final LeadStatusRepository leadStatusRepository;
    private final ProductServiceRepository productServiceRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * Create a new Lead entity from DTO.
     *
     * @param dto LeadRequestDto containing lead details
     * @return LeadResponseDto with created lead details
     */
    @Transactional
    public LeadResponseDto createLead(LeadRequestDto dto) {
        log.info("Creating new lead for tenantId={} and email={}", dto.getTenantId(), dto.getEmail());

        validateDto(dto);

        if (dto.getEmail() != null && leadRepository.existsByEmail(dto.getEmail())) {
            log.error("Lead creation failed: Email {} already exists", dto.getEmail());
            throw new BusinessException("Email already exists");
        }

        Lead lead = leadMapper.toEntity(dto);

        lead.setGender(genderRepository.findByIdentity(dto.getGender())
                .orElseThrow(() -> new BusinessException("Invalid gender", ErrorCodes.VALIDATION_FAILED)));

        lead.setAssignToUser(userRepository.findByIdentity(dto.getAssignTo())
                .orElseThrow(() -> new BusinessException("Invalid user", ErrorCodes.VALIDATION_FAILED)));

        lead.setLeadSource(leadSourceRepository.findByIdentity(dto.getLeadSourceIdentity())
                .orElseThrow(() -> new BusinessException("Invalid lead source", ErrorCodes.VALIDATION_FAILED)));

        lead.setLeadStage(leadStageRepository.findByIdentity(dto.getLeadStageIdentity())
                .orElseThrow(() -> new BusinessException("Invalid lead stage", ErrorCodes.VALIDATION_FAILED)));

        lead.setLeadStatus(leadStatusRepository.findByIdentity(dto.getLeadStatusIdentity())
                .orElseThrow(() -> new BusinessException("Invalid lead status", ErrorCodes.VALIDATION_FAILED)));

        lead.setProductService(productServiceRepository.findByIdentity(dto.getInterestedProductIdentity())
                .orElseThrow(() -> new BusinessException("Invalid product/service", ErrorCodes.VALIDATION_FAILED)));

        lead.setIdentity(UUID.randomUUID());
        lead.setLeadCode(generateLeadCode());
        lead.setCreatedBy(CommonConstants.CREATED_BY);
        lead.setCreatedAt(LocalDateTime.now());
        lead.setIsDel(false);

        log.debug("Saving lead: {}", lead);
        Lead savedLead = leadRepository.save(lead);

        log.info("Lead created successfully with code {}", savedLead.getLeadCode());
        return leadMapper.toResponseDto(savedLead, CommonConstants.SUCCESS);
    }

    /**
     * Retrieve a Lead by its identity.
     *
     * @param leadIdentity UUID of the lead
     * @return LeadResponseDto with lead details
     */
    @Transactional(readOnly = true)
    public LeadResponseDto getLeadById(UUID leadIdentity) {
        log.info("Fetching lead with identity={}", leadIdentity);

        Lead lead = leadRepository.findByIdentity(leadIdentity)
                .orElseThrow(() -> {
                    log.error("Lead not found for identity={}", leadIdentity);
                    return new ResourceNotFoundException("Lead not found", leadIdentity.toString());
                });

        log.debug("Lead found: {}", lead);
        return leadMapper.toResponseDto(lead, CommonConstants.SUCCESS);
    }

    /**
     * Validate LeadRequestDto fields using JSR-380 validation.
     *
     * @param dto LeadRequestDto to validate
     */
    private void validateDto(LeadRequestDto dto) {
        log.debug("Validating LeadRequestDto: {}", dto);
        Set<ConstraintViolation<LeadRequestDto>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .collect(Collectors.joining(", "));
            log.error("LeadRequestDto validation failed: {}", errorMsg);
            throw new BusinessException(errorMsg, ErrorCodes.VALIDATION_FAILED);
        }
    }

    /**
     * Search leads by filters (name, contact, email).
     *
     * @param fullName      Full name
     * @param contactNumber Contact number
     * @param email         Email
     * @param page          Page number
     * @param size          Page size
     * @return Page of LeadSearchResponseDto
     */
    @Transactional(readOnly = true)
    public Page<LeadSearchResponseDto> searchLeads(String fullName, String contactNumber, String email, int page, int size) {
        log.info("Searching leads with fullName={}, contactNumber={}, email={}", fullName, contactNumber, email);

        Pageable pageable = PageRequest.of(page, size);
        Page<Lead> leadsPage = leadRepository.searchLeads(fullName, contactNumber, email, pageable);

        if (leadsPage.isEmpty()) {
            log.warn("No leads found for search criteria: fullName={}, contactNumber={}, email={}", fullName, contactNumber, email);
            throw new ResourceNotFoundException("Lead", "No leads found for given search criteria");
        }

        log.info("Found {} leads matching criteria", leadsPage.getTotalElements());
        return leadsPage.map(leadMapper::toSearchResponseDto);
    }

    /**
     * Generate a unique lead code based on year and count.
     *
     * @return Generated lead code
     */
    private String generateLeadCode() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();

        long count = leadRepository.countByCreatedAtBetween(
                LocalDateTime.of(year, 1, 1, 0, 0),
                LocalDateTime.of(year, 12, 31, 23, 59, 59)
        ) + 1;

        String leadCode = String.format("LD-%d-%03d", year, count);
        log.debug("Generated lead code: {}", leadCode);
        return leadCode;
    }
}
