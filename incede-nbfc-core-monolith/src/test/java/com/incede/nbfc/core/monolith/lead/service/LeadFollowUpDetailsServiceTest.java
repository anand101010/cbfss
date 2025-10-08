package com.incede.nbfc.core.monolith.lead.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadFollowUp;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpDetailsRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpDetailsResponseDto;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.FollowUpType;
import com.incede.nbfc.core.monolith.lead.repository.LeadFollowUpRepository;
import com.incede.nbfc.core.monolith.lead.repository.LeadRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.FollowUpTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeadFollowUpDetailsServiceTest {

    @Mock
    private LeadFollowUpRepository leadFollowUpRepository;

    @Mock
    private LeadRepository leadRepository;

    @Mock
    private FollowUpTypeRepository followUpTypeRepository;

    @InjectMocks
    private LeadFollowUpDetailsService service;

    private UUID leadId;
    private UUID followUpTypeId;
    private UUID followUpId;
    private Lead lead;
    private FollowUpType followUpType;
    private LeadFollowUp leadFollowUp;
    private LeadFollowUpDetailsRequestDto requestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        leadId = UUID.randomUUID();
        followUpTypeId = UUID.randomUUID();
        followUpId = UUID.randomUUID();

        lead = new Lead();
        lead.setIdentity(leadId);

        followUpType = new FollowUpType();
        followUpType.setIdentity(followUpTypeId);
        followUpType.setName("Call");

        leadFollowUp = new LeadFollowUp();
        leadFollowUp.setIdentity(followUpId);
        leadFollowUp.setLead(lead);
        leadFollowUp.setStaffId(101);
        leadFollowUp.setFollowUpType(followUpType);
        leadFollowUp.setFollowUpDate(LocalDate.now());
        leadFollowUp.setNextFollowUpDate(LocalDate.now().plusDays(1));
        leadFollowUp.setFollowUpNotes("Test follow-up");

        requestDto = new LeadFollowUpDetailsRequestDto();
        requestDto.setStaffId(101);
        requestDto.setFollowUpTypeId(followUpTypeId);
        requestDto.setFollowUpDate(LocalDate.now());
        requestDto.setNextFollowUpDate(LocalDate.now().plusDays(1));
        requestDto.setFollowUpNotes("Test follow-up");
    }

    @Test
    void testCreateFollowUp_Success() {
        when(leadRepository.findByIdentityAndIsDelFalse(leadId)).thenReturn(Optional.of(lead));
        when(followUpTypeRepository.findByIdentity(followUpTypeId)).thenReturn(Optional.of(followUpType));
        when(leadFollowUpRepository.save(any(LeadFollowUp.class))).thenReturn(leadFollowUp);

        LeadFollowUpDetailsResponseDto response = service.createFollowUp(leadId, requestDto);

        assertNotNull(response);
        assertEquals(leadFollowUp.getStaffId(), response.getStaffId());
        assertEquals(leadFollowUp.getFollowUpType().getName(), response.getFollowUpTypeName());
        assertEquals(CommonConstants.LEAD_FOLLOW_UP_CREATED, response.getMessage());

        verify(leadRepository, times(1)).findByIdentityAndIsDelFalse(leadId);
        verify(followUpTypeRepository, times(1)).findByIdentity(followUpTypeId);
        verify(leadFollowUpRepository, times(1)).save(any(LeadFollowUp.class));
    }

    @Test
    void testCreateFollowUp_LeadNotFound() {
        when(leadRepository.findByIdentityAndIsDelFalse(leadId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.createFollowUp(leadId, requestDto));
        assertTrue(exception.getMessage().contains("Lead not found"));

        verify(leadRepository, times(1)).findByIdentityAndIsDelFalse(leadId);
        verifyNoInteractions(followUpTypeRepository);
        verifyNoInteractions(leadFollowUpRepository);
    }

    @Test
    void testCreateFollowUp_FollowUpTypeNotFound() {
        when(leadRepository.findByIdentityAndIsDelFalse(leadId)).thenReturn(Optional.of(lead));
        when(followUpTypeRepository.findByIdentity(followUpTypeId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.createFollowUp(leadId, requestDto));
        assertTrue(exception.getMessage().contains("FollowUpType not found"));

        verify(leadRepository, times(1)).findByIdentityAndIsDelFalse(leadId);
        verify(followUpTypeRepository, times(1)).findByIdentity(followUpTypeId);
        verifyNoInteractions(leadFollowUpRepository);
    }

    @Test
    void testCreateFollowUp_DataIntegrityViolation() {
        when(leadRepository.findByIdentityAndIsDelFalse(leadId)).thenReturn(Optional.of(lead));
        when(followUpTypeRepository.findByIdentity(followUpTypeId)).thenReturn(Optional.of(followUpType));
        when(leadFollowUpRepository.save(any(LeadFollowUp.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(BusinessException.class, () -> service.createFollowUp(leadId, requestDto));

        verify(leadFollowUpRepository, times(1)).save(any(LeadFollowUp.class));
    }

    @Test
    void testSearchFollowUps_Success() {
        Page<LeadFollowUp> page = new PageImpl<>(List.of(leadFollowUp));
        when(leadFollowUpRepository.searchFollowUps(eq(leadId), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(page);

        Page<LeadFollowUpDetailsResponseDto> response = service.searchFollowUps(leadId, null, null, null, null, 0, 10);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(leadFollowUp.getStaffId(), response.getContent().get(0).getStaffId());
    }

    @Test
    void testSearchFollowUps_NotFound() {
        Page<LeadFollowUp> emptyPage = Page.empty();
        when(leadFollowUpRepository.searchFollowUps(eq(leadId), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(emptyPage);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.searchFollowUps(leadId, null, null, null, null, 0, 10));

        assertTrue(exception.getMessage().contains("No follow-up records found"));
    }

}
