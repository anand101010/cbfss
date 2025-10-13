package com.incede.nbfc.core.monolith.lead.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadAdditionalReference;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadAddress;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadAssignmentHistory;
import com.incede.nbfc.core.monolith.lead.dto.*;
import com.incede.nbfc.core.monolith.lead.mapper.LeadAssignmentHistoryMapper;
import com.incede.nbfc.core.monolith.lead.mapper.LeadMapper;
import com.incede.nbfc.core.monolith.lead.mapper.LeadAddressMapper;
import com.incede.nbfc.core.monolith.lead.repository.LeadAdditionalReferenceRepository;
import com.incede.nbfc.core.monolith.lead.repository.LeadAssignmentHistoryRepository;
import com.incede.nbfc.core.monolith.lead.repository.LeadRepository;
import com.incede.nbfc.core.monolith.lead.repository.LeadAddressRepository;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import com.incede.nbfc.core.monolith.user.domain.entity.User;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeadService {

    private final LeadRepository leadRepository;
    private final LeadAddressRepository leadAddressRepository;
    private final LeadMapper leadMapper;
    private final AdditionalReferenceConfigRepository additionalReferenceConfigRepository;
    private final LeadAdditionalReferenceRepository leadAdditionalReferenceRepository;
    private final LeadAddressMapper leadAddressMapper;
    private final GendersRepository genderRepository;
    private final UserRepository userRepository;
    private final LeadSourceRepository leadSourceRepository;
    private final LeadStageRepository leadStageRepository;
    private final LeadStatusRepository leadStatusRepository;
    private final ProductServiceRepository productServiceRepository;
    private final AddressTypeRepository addressTypeRepository;
    private final PostOfficesRepository postOfficeRepository;
    private final AddressProofTypeRepository addressProofTypeRepository;
    private final LeadAssignmentHistoryMapper leadAssignmentHistoryMapper;
    private final LeadAssignmentHistoryRepository leadAssignmentHistoryRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     *
     * @param dto
     * @return
     */
    @Transactional
    public LeadResponseDto createLead(LeadRequestDto dto) {
        log.info("Creating new lead from DTO");
        validateDto(dto);

        if (dto.getContactNumber() != null && leadRepository.existsByContactNumber(dto.getContactNumber())) {
            log.error("Lead creation failed: ContactNumber {} already exists", dto.getContactNumber());
            throw new BusinessException("ContactNumber already exists");
        }
        Lead lead = leadMapper.toEntity(dto);

        lead.setGender(genderRepository.findByIdentity(dto.getGender())
                .orElseThrow(() -> new BusinessException("Invalid gender", ErrorCodes.VALIDATION_FAILED)));

        User assignToUser = userRepository.findByIdentity(dto.getAssignTo())
                .orElseThrow(() -> new BusinessException("Invalid user", ErrorCodes.VALIDATION_FAILED));
        lead.setAssignToUser(assignToUser);

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
        Lead savedLead = leadRepository.save(lead);

        if (dto.getAddress() != null && !dto.getAddress().isEmpty()) {
            saveLeadAddress(savedLead, dto.getAddress().get(0));
        }

        if (dto.getDynamicReferences() != null) {
            for (LeadRequestDto.DynamicReferenceDto refDto : dto.getDynamicReferences()) {
                AdditionalReferenceConfig refConfig = additionalReferenceConfigRepository
                        .findByIdentity(refDto.getReferenceConfigIdentity())
                        .orElseThrow(() -> new BusinessException(
                                "Invalid reference config: " + refDto.getReferenceConfigIdentity(),
                                ErrorCodes.VALIDATION_FAILED));

                LeadAdditionalReference leadRef = new LeadAdditionalReference();
                leadRef.setLead(savedLead);
                leadRef.setReferenceConfigId(refConfig);
                leadRef.setReferenceFieldValue(refDto.getReferenceFieldValue());
                leadAdditionalReferenceRepository.save(leadRef);
            }
        }

        LeadAssignmentHistoryRequestDto assignmentDto = LeadAssignmentHistoryRequestDto.builder()
                .leadIdentities(List.of(savedLead.getIdentity()))
                .assignedToUserIdentity(dto.getAssignTo())
                .assignedOn(LocalDate.now())
                .build();

        List<Lead> leads = List.of(savedLead);
        List<LeadAssignmentHistory> assignments = leadAssignmentHistoryMapper.toEntityList(assignmentDto, leads, assignToUser, assignToUser);
        leadAssignmentHistoryRepository.saveAll(assignments);

        log.info("Lead created successfully with code {} and assigned to userId {}", savedLead.getLeadCode(), dto.getAssignTo());
        return leadMapper.toResponseDto(savedLead, CommonConstants.SUCCESS);
    }

    /**
     *
     * @param leadIdentity
     * @param dto
     * @return
     */
    @Transactional
    public LeadResponseDto updateLead(UUID leadIdentity, LeadRequestDto dto) {
        log.info("Updating lead with identity={} and email={}", leadIdentity, dto.getEmail());

        validateDto(dto);

        Lead existingLead = leadRepository.findByIdentity(leadIdentity)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found", leadIdentity.toString()));

        if (dto.getContactNumber() != null &&
                !dto.getContactNumber().equals(existingLead.getContactNumber()) &&
                leadRepository.existsByContactNumber(dto.getContactNumber())) {
            log.error("Lead update failed: ContactNumber {} already exists", dto.getContactNumber());
            throw new BusinessException("ContactNumber already exists", ErrorCodes.VALIDATION_FAILED);
        }
        leadMapper.updateEntityFromDto(existingLead, dto);
        existingLead.setGender(genderRepository.findByIdentity(dto.getGender())
                .orElseThrow(() -> new BusinessException("Invalid gender", ErrorCodes.VALIDATION_FAILED)));
        existingLead.setAssignToUser(userRepository.findByIdentity(dto.getAssignTo())
                .orElseThrow(() -> new BusinessException("Invalid user", ErrorCodes.VALIDATION_FAILED)));
        existingLead.setLeadSource(leadSourceRepository.findByIdentity(dto.getLeadSourceIdentity())
                .orElseThrow(() -> new BusinessException("Invalid lead source", ErrorCodes.VALIDATION_FAILED)));
        existingLead.setLeadStage(leadStageRepository.findByIdentity(dto.getLeadStageIdentity())
                .orElseThrow(() -> new BusinessException("Invalid lead stage", ErrorCodes.VALIDATION_FAILED)));
        existingLead.setLeadStatus(leadStatusRepository.findByIdentity(dto.getLeadStatusIdentity())
                .orElseThrow(() -> new BusinessException("Invalid lead status", ErrorCodes.VALIDATION_FAILED)));
        existingLead.setProductService(productServiceRepository.findByIdentity(dto.getInterestedProductIdentity())
                .orElseThrow(() -> new BusinessException("Invalid product/service", ErrorCodes.VALIDATION_FAILED)));

        existingLead.setUpdatedBy(CommonConstants.UPDATED_BY);
        existingLead.setUpdatedAt(LocalDateTime.now());

        Lead updatedLead = leadRepository.save(existingLead);

        if (dto.getAddress() != null && !dto.getAddress().isEmpty()) {
            LeadRequestDto.AddressDto addressDto = dto.getAddress().get(0);

            LeadAddress existingAddress = leadAddressRepository.findByLead(existingLead)
                    .stream()
                    .filter(addr -> !Boolean.TRUE.equals(addr.getIsDel()))
                    .findFirst()
                    .orElse(null);

            if (existingAddress != null) {

                leadAddressMapper.updateEntityFromDto(existingAddress, addressDto);
                existingAddress.setUpdatedBy(CommonConstants.UPDATED_BY);
                existingAddress.setUpdatedAt(LocalDateTime.now());
                leadAddressRepository.save(existingAddress);
                log.info("Lead address updated for leadCode={}", updatedLead.getLeadCode());
            } else {
                saveLeadAddress(updatedLead, addressDto);
            }
        }

        if (dto.getDynamicReferences() != null) {
            for (LeadRequestDto.DynamicReferenceDto refDto : dto.getDynamicReferences()) {
                AdditionalReferenceConfig refConfig = additionalReferenceConfigRepository
                        .findByIdentity(refDto.getReferenceConfigIdentity())
                        .orElseThrow(() -> new BusinessException(
                                "Invalid reference config: " + refDto.getReferenceConfigIdentity(),
                                ErrorCodes.VALIDATION_FAILED));

                LeadAdditionalReference existingRef = leadAdditionalReferenceRepository
                        .findByLeadAndReferenceConfigId(existingLead, refConfig)
                        .orElse(null);

                if (existingRef != null) {
                    existingRef.setReferenceFieldValue(refDto.getReferenceFieldValue());
                    existingRef.setUpdatedBy(CommonConstants.UPDATED_BY);
                    existingRef.setUpdatedAt(LocalDateTime.now());
                    leadAdditionalReferenceRepository.save(existingRef);
                    log.info("Updated existing dynamic reference for leadCode={}", updatedLead.getLeadCode());
                } else {
                    LeadAdditionalReference newRef = new LeadAdditionalReference();
                    newRef.setLead(existingLead);
                    newRef.setReferenceConfigId(refConfig);
                    newRef.setReferenceFieldValue(refDto.getReferenceFieldValue());
                    newRef.setCreatedBy(CommonConstants.CREATED_BY);
                    newRef.setCreatedAt(LocalDateTime.now());
                    leadAdditionalReferenceRepository.save(newRef);
                    log.info("Added new dynamic reference for leadCode={}", updatedLead.getLeadCode());
                }
            }
        }

        log.info("Lead updated successfully for leadCode={}", updatedLead.getLeadCode());
        return leadMapper.toResponseDto(updatedLead, CommonConstants.SUCCESS);
    }

    /**
     *
     * @param leadIdentity
     * @return
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
     *
     * @param dto
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
     *
     * @param fullName
     * @param contactNumber
     * @param email
     * @param page
     * @param size
     * @return
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
     *
     * @return
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

    /**
     * Save lead address from DTO to database using mapper
     */
    private void saveLeadAddress(Lead lead, LeadRequestDto.AddressDto addressDto) {
        log.info("Saving address for lead {}", lead.getLeadCode());

        leadMapper.saveLeadAddress(lead, addressDto, addressTypeRepository, postOfficeRepository, leadAddressMapper, leadAddressRepository,addressProofTypeRepository);
        log.info("Successfully saved address for lead {}", lead.getLeadCode());
    }
}