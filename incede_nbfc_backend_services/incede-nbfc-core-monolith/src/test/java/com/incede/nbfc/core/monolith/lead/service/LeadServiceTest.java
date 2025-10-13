package com.incede.nbfc.core.monolith.lead.service;

import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.lead.domain.entity.*;
import com.incede.nbfc.core.monolith.lead.dto.LeadRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadResponseDto;
import com.incede.nbfc.core.monolith.lead.mapper.LeadMapper;
import com.incede.nbfc.core.monolith.lead.repository.*;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import com.incede.nbfc.core.monolith.user.domain.entity.User;
import com.incede.nbfc.core.monolith.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeadServiceTest {

    @InjectMocks
    private LeadService leadService;

    @Mock private LeadRepository leadRepository;
    @Mock private LeadMapper leadMapper;
    @Mock private GendersRepository genderRepository;
    @Mock private LeadSourceRepository leadSourceRepository;
    @Mock private LeadStageRepository leadStageRepository;
    @Mock private LeadStatusRepository leadStatusRepository;
    @Mock private ProductServiceRepository productServiceRepository;
    @Mock private UserRepository userRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private LeadRequestDto buildValidDto() {
        UUID genderId = UUID.randomUUID();
        UUID leadSourceId = UUID.randomUUID();
        UUID leadStageId = UUID.randomUUID();
        UUID leadStatusId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID assignToId = UUID.randomUUID();

        LeadRequestDto dto = new LeadRequestDto();
        dto.setFullName("John Doe");
        dto.setContactNumber("9999999999");
        dto.setEmail("john.doe@example.com");
        dto.setGender(genderId);
        dto.setLeadSourceIdentity(leadSourceId);
        dto.setLeadStageIdentity(leadStageId);
        dto.setLeadStatusIdentity(leadStatusId);
        dto.setInterestedProductIdentity(productId);
        dto.setAssignTo(assignToId);
        dto.setTenantId(1);
        dto.setAddress(Collections.emptyList());
        dto.setDynamicReferences(Collections.emptyList());
        return dto;
    }

    @Test
    void testCreateLead_InvalidGender_Throws() {
        LeadRequestDto dto = buildValidDto();
        when(genderRepository.findByIdentity(dto.getGender())).thenReturn(Optional.empty());
        BusinessException ex = assertThrows(BusinessException.class, () -> leadService.createLead(dto));
        assertTrue(ex.getMessage().contains("Invalid gender"));
    }

    @Test
    void testUpdateLead_Success() {
        LeadRequestDto dto = buildValidDto();
        UUID leadId = UUID.randomUUID();

        Lead existingLead = new Lead();
        existingLead.setIdentity(leadId);
        existingLead.setEmail(dto.getEmail());

        when(leadRepository.findByIdentity(leadId)).thenReturn(Optional.of(existingLead));
        when(leadRepository.existsByEmail(dto.getEmail())).thenReturn(false);

        Genders gender = new Genders(); gender.setIdentity(dto.getGender());
        LeadSource leadSource = new LeadSource(); leadSource.setIdentity(dto.getLeadSourceIdentity());
        LeadStage leadStage = new LeadStage(); leadStage.setIdentity(dto.getLeadStageIdentity());
        LeadStatus leadStatus = new LeadStatus(); leadStatus.setIdentity(dto.getLeadStatusIdentity());
        ProductService product = new ProductService(); product.setIdentity(dto.getInterestedProductIdentity());
        User assignToUser = new User(); assignToUser.setIdentity(dto.getAssignTo());

        when(genderRepository.findByIdentity(dto.getGender())).thenReturn(Optional.of(gender));
        when(leadSourceRepository.findByIdentity(dto.getLeadSourceIdentity())).thenReturn(Optional.of(leadSource));
        when(leadStageRepository.findByIdentity(dto.getLeadStageIdentity())).thenReturn(Optional.of(leadStage));
        when(leadStatusRepository.findByIdentity(dto.getLeadStatusIdentity())).thenReturn(Optional.of(leadStatus));
        when(productServiceRepository.findByIdentity(dto.getInterestedProductIdentity())).thenReturn(Optional.of(product));
        when(userRepository.findByIdentity(dto.getAssignTo())).thenReturn(Optional.of(assignToUser));
        when(leadRepository.save(any())).thenReturn(existingLead);
        when(leadMapper.toResponseDto(any(), anyString())).thenReturn(new LeadResponseDto());

        assertDoesNotThrow(() -> leadService.updateLead(leadId, dto));
        verify(leadRepository, times(1)).save(existingLead);
    }

    @Test
    void testUpdateLead_NotFound_Throws() {
        UUID leadId = UUID.randomUUID();
        LeadRequestDto dto = buildValidDto();
        when(leadRepository.findByIdentity(leadId)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> leadService.updateLead(leadId, dto));
        assertTrue(ex.getMessage().contains("Lead not found"));
    }

    @Test
    void testGetLeadById_Success() {
        UUID leadId = UUID.randomUUID();
        Lead lead = new Lead();
        lead.setIdentity(leadId);
        when(leadRepository.findByIdentity(leadId)).thenReturn(Optional.of(lead));
        when(leadMapper.toResponseDto(any(), anyString())).thenReturn(new LeadResponseDto());
        assertDoesNotThrow(() -> leadService.getLeadById(leadId));
    }

    @Test
    void testGetLeadById_NotFound() {
        UUID leadId = UUID.randomUUID();
        when(leadRepository.findByIdentity(leadId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> leadService.getLeadById(leadId));
    }

    @Test
    void testSearchLeads_Found() {
        Lead lead = new Lead();
        Page<Lead> page = new PageImpl<>(List.of(lead));
        when(leadRepository.searchLeads(anyString(), anyString(), anyString(), any(Pageable.class))).thenReturn(page);
        when(leadMapper.toSearchResponseDto(any())).thenReturn(new com.incede.nbfc.core.monolith.lead.dto.LeadSearchResponseDto());
        assertDoesNotThrow(() -> leadService.searchLeads("name", "123456", "email", 0, 10));
    }

    @Test
    void testSearchLeads_NotFound() {
        Page<Lead> page = Page.empty();
        when(leadRepository.searchLeads(anyString(), anyString(), anyString(), any(Pageable.class))).thenReturn(page);
        assertThrows(ResourceNotFoundException.class, () -> leadService.searchLeads("name", "123", "email", 0, 10));
    }
}
