package com.incede.nbfc.core.monolith.lead.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.dto.LeadRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadSearchResponseDto;
import com.incede.nbfc.core.monolith.lead.mapper.LeadMapper;
import com.incede.nbfc.core.monolith.lead.repository.LeadRepository;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import com.incede.nbfc.core.monolith.user.domain.entity.User;
import com.incede.nbfc.core.monolith.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeadServiceTest {

    @Mock
    private LeadRepository leadRepository;

    @Mock
    private LeadMapper leadMapper;

    @Mock
    private GendersRepository genderRepository;

    @Mock
    private LeadSourceRepository leadSourceRepository;

    @Mock
    private LeadStageRepository leadStageRepository;

    @Mock
    private LeadStatusRepository leadStatusRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductServiceRepository productServiceRepository;

    @InjectMocks
    private LeadService leadService;

    private LeadRequestDto leadRequestDto;
    private Lead leadEntity;
    private LeadResponseDto leadResponseDto;
    private UUID leadIdentity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        leadIdentity = UUID.randomUUID();

      
        leadRequestDto = new LeadRequestDto();
        leadRequestDto.setTenantId(1);
        leadRequestDto.setFullName("John Doe");
        leadRequestDto.setGender(UUID.randomUUID());
        leadRequestDto.setContactNumber("9876543210");
        leadRequestDto.setEmail("john@example.com");
        leadRequestDto.setLeadSourceIdentity(UUID.randomUUID());
        leadRequestDto.setLeadStageIdentity(UUID.randomUUID());
        leadRequestDto.setLeadStatusIdentity(UUID.randomUUID());
        leadRequestDto.setAssignTo(UUID.randomUUID());
        leadRequestDto.setInterestedProductIdentity(UUID.randomUUID());

        leadEntity = new Lead();
        leadEntity.setIdentity(leadIdentity);
        leadEntity.setLeadCode("LD-2025-001");
        leadEntity.setFullName("John Doe");
        leadEntity.setEmail("john@example.com");

        leadResponseDto = LeadResponseDto.builder()
                .leadIdentity(leadIdentity)
                .leadCode("LD-2025-001")
                .status(CommonConstants.SUCCESS)
                .leadDetails(LeadResponseDto.LeadDetails.builder()
                        .fullName("John Doe")
                        .build())
                .build();
    }

    @Test
    void createLead_Success() {
        when(leadRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(genderRepository.findByIdentity(leadRequestDto.getGender())).thenReturn(Optional.of(new Genders()));
        when(leadSourceRepository.findByIdentity(leadRequestDto.getLeadSourceIdentity())).thenReturn(Optional.of(new LeadSource()));
        when(leadStageRepository.findByIdentity(leadRequestDto.getLeadStageIdentity())).thenReturn(Optional.of(new LeadStage()));
        when(leadStatusRepository.findByIdentity(leadRequestDto.getLeadStatusIdentity())).thenReturn(Optional.of(new LeadStatus()));
        when(userRepository.findByIdentity(leadRequestDto.getAssignTo())).thenReturn(Optional.of(new User()));
        when(productServiceRepository.findByIdentity(leadRequestDto.getInterestedProductIdentity())).thenReturn(Optional.of(new ProductService()));
        when(leadMapper.toEntity(leadRequestDto)).thenReturn(leadEntity);
        when(leadRepository.save(leadEntity)).thenReturn(leadEntity);
        when(leadMapper.toResponseDto(leadEntity, CommonConstants.SUCCESS)).thenReturn(leadResponseDto);

        LeadResponseDto result = leadService.createLead(leadRequestDto);

        assertNotNull(result);
        assertEquals("LD-2025-001", result.getLeadCode());
        assertEquals("John Doe", result.getLeadDetails().getFullName());
        verify(leadRepository, times(1)).save(leadEntity);
    }

    @Test
    void createLead_EmailAlreadyExists_ThrowsException() {
        when(leadRepository.existsByEmail("john@example.com")).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> leadService.createLead(leadRequestDto));
        assertEquals("Email already exists", ex.getMessage());
    }

    @Test
    void getLeadById_Success() {
        when(leadRepository.findByIdentity(leadIdentity)).thenReturn(Optional.of(leadEntity));
        when(leadMapper.toResponseDto(leadEntity, CommonConstants.SUCCESS)).thenReturn(leadResponseDto);

        LeadResponseDto result = leadService.getLeadById(leadIdentity);

        assertNotNull(result);
        assertEquals("LD-2025-001", result.getLeadCode());
        assertEquals("John Doe", result.getLeadDetails().getFullName());
    }

    @Test
    void getLeadById_NotFound_ThrowsException() {
        when(leadRepository.findByIdentity(leadIdentity)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> leadService.getLeadById(leadIdentity));
    }

    @Test
    void createLead_ValidationFails_ThrowsException() {
       
        LeadRequestDto invalidDto = new LeadRequestDto();
        invalidDto.setFullName("Jane Doe");

        BusinessException ex = assertThrows(BusinessException.class, () -> leadService.createLead(invalidDto));
        assertTrue(ex.getMessage().contains("Tenant ID is required"));
        assertTrue(ex.getMessage().contains("Contact number is required"));
    }


    @Test
    void searchLeads_ReturnsPaginatedResults() {

        Lead lead = new Lead();
        lead.setLeadId(1);
        List<Lead> leads = List.of(lead);
        Page<Lead> leadsPage = new PageImpl<>(leads);

        when(leadRepository.searchLeads(any(), any(), any(), any(Pageable.class)))
                .thenReturn(leadsPage);

        when(leadMapper.toSearchResponseDto(any(Lead.class)))
                .thenReturn(new LeadSearchResponseDto(/* fill fields if needed */));

        Page<LeadSearchResponseDto> result = leadService.searchLeads("John", "1234567890", "abc@test.com", 0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
    }

    @Test
    void searchLeads_ThrowsResourceNotFoundException_WhenNoLeadsFound() {

        Page<Lead> emptyPage = new PageImpl<>(Collections.emptyList());
        when(leadRepository.searchLeads(any(), any(), any(), any(Pageable.class)))
                .thenReturn(emptyPage);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                leadService.searchLeads("John", "1234567890", "abc@test.com", 0, 10)
        );

        assertEquals("Lead with id 'No leads found for given search criteria' not found", exception.getMessage());
    }
}