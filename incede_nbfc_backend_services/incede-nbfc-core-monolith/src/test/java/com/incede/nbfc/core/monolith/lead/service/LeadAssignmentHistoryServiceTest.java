package com.incede.nbfc.core.monolith.lead.service;

import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadAssignmentHistory;
import com.incede.nbfc.core.monolith.lead.dto.*;
import com.incede.nbfc.core.monolith.lead.mapper.LeadAssignmentHistoryMapper;
import com.incede.nbfc.core.monolith.lead.mapper.LeadMapper;
import com.incede.nbfc.core.monolith.lead.repository.LeadAssignmentHistoryRepository;
import com.incede.nbfc.core.monolith.lead.repository.LeadRepository;
import com.incede.nbfc.core.monolith.user.domain.entity.User;
import com.incede.nbfc.core.monolith.user.dto.UserResponseDto;
import com.incede.nbfc.core.monolith.user.mapper.UserMapper;
import com.incede.nbfc.core.monolith.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LeadAssignmentHistoryServiceTest {

    @Mock
    private LeadAssignmentHistoryRepository leadAssignmentHistoryRepository;

    @Mock
    private LeadRepository leadRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LeadAssignmentHistoryMapper leadAssignmentHistoryMapper;

    @Mock
    private LeadMapper leadMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private LeadAssignmentHistoryService service;

    private Lead lead;
    private User user;
    private LeadAssignmentHistoryRequestDto requestDto;
    private UUID leadId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        leadId = UUID.randomUUID();
        userId = UUID.randomUUID();

        lead = new Lead();
        lead.setIdentity(leadId);
        lead.setLeadId(1); // important for avoiding NPE

        user = new User();
        user.setIdentity(userId);
        user.setUserId(1); // for mapping

        requestDto = new LeadAssignmentHistoryRequestDto();
        requestDto.setAssignedToUserIdentity(userId);
        requestDto.setLeadIdentities(List.of(leadId));
        requestDto.setAssignedOn(LocalDate.now());
    }

    @Test
    void testSearchLeadsForAssignment_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Lead> page = new PageImpl<>(List.of(lead), pageable, 1);

        when(leadRepository.searchLeadsForAssignment(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(page);
        when(leadAssignmentHistoryMapper.toSearchResponseDto(any()))
                .thenReturn(new LeadAssignmentSearchResponseDto());

        Page<LeadAssignmentSearchResponseDto> result = service.searchLeadsForAssignment(
                UUID.randomUUID(), null, null, null, null, LocalDate.now(), 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(leadRepository).searchLeadsForAssignment(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void testSearchLeadsForAssignment_NoResults() {
        when(leadRepository.searchLeadsForAssignment(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Page.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.searchLeadsForAssignment(UUID.randomUUID(), null, null, null, null, null, 0, 10)
        );

        assertEquals("Lead with id 'No leads found for given search criteria' not found", exception.getMessage());
        verify(leadRepository, times(1))
                .searchLeadsForAssignment(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void testBulkUpdateLeadAssignments_Success() {
        when(userRepository.findByIdentity(any())).thenReturn(Optional.of(user));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(leadRepository.findByIdentity(any())).thenReturn(Optional.of(lead));
        when(leadAssignmentHistoryRepository.findAllByLeadAndIsDelFalse(any())).thenReturn(List.of());
        when(leadAssignmentHistoryMapper.toEntityList(any(), anyList(), any(), any()))
                .thenReturn(List.of(new LeadAssignmentHistory()));
        when(leadAssignmentHistoryRepository.saveAll(anyList()))
                .thenReturn(List.of(new LeadAssignmentHistory()));

        String result = service.bulkUpdateLeadAssignments(requestDto);

        assertTrue(result.contains("assigned successfully"));
        verify(leadAssignmentHistoryRepository, atLeastOnce()).saveAll(anyList());
        verify(leadRepository, atLeastOnce()).save(any());
    }

    @Test
    void testBulkUpdateLeadAssignments_InvalidAssignedToUser() {
        when(userRepository.findByIdentity(any())).thenReturn(Optional.empty());
        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.bulkUpdateLeadAssignments(requestDto));
        assertTrue(ex.getMessage().contains("Invalid user"));
    }

    @Test
    void testBulkUpdateLeadAssignments_InvalidAssignedByUser() {
        when(userRepository.findByIdentity(any())).thenReturn(Optional.of(user));
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.bulkUpdateLeadAssignments(requestDto));
        assertTrue(ex.getMessage().contains("Invalid assigned-by user"));
    }

    @Test
    void testBulkUpdateLeadAssignments_LeadNotFound() {
        when(userRepository.findByIdentity(any())).thenReturn(Optional.of(user));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(leadRepository.findByIdentity(any())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.bulkUpdateLeadAssignments(requestDto));
        assertTrue(ex.getMessage().contains("Lead not found"));
    }

    @Test
    void testBulkUpdateLeadAssignments_WithExistingAssignment() {
        LeadAssignmentHistory existing = new LeadAssignmentHistory();
        existing.setLead(lead);
        existing.setStatus("ACTIVE");

        when(userRepository.findByIdentity(any())).thenReturn(Optional.of(user));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(leadRepository.findByIdentity(any())).thenReturn(Optional.of(lead));
        when(leadAssignmentHistoryRepository.findAllByLeadAndIsDelFalse(any()))
                .thenReturn(List.of(existing));
        when(leadAssignmentHistoryMapper.toEntityList(any(), anyList(), any(), any()))
                .thenReturn(List.of(new LeadAssignmentHistory()));
        when(leadAssignmentHistoryRepository.saveAll(anyList()))
                .thenReturn(List.of(new LeadAssignmentHistory()));

        String result = service.bulkUpdateLeadAssignments(requestDto);

        assertTrue(result.contains("assigned successfully"));
        verify(leadAssignmentHistoryRepository, atLeastOnce()).saveAll(anyList());
        verify(leadRepository, atLeastOnce()).save(any());
    }

    @Test
    void testFetchLeadAssignmentHistory_Success() {
        LeadAssignmentHistory history = new LeadAssignmentHistory();
        history.setLead(lead);
        history.setAssignedOn(LocalDate.now());
        history.setStatus("ACTIVE");
        history.setRemarks("ok");
        history.setAssignedToUserId(user);
        history.setAssignedByUserId(user);

        when(leadRepository.findByIdentity(any())).thenReturn(Optional.of(lead));
        when(leadAssignmentHistoryRepository.findAllByLeadAndIsDelFalse(any())).thenReturn(List.of(history));
        when(leadMapper.toResponseDto(any(), any())).thenReturn(new LeadResponseDto());
        when(userMapper.toResponseDto(any())).thenReturn(new UserResponseDto());

        List<LeadAssignmentResponseDto> result = service.fetchLeadAssignmentHistory(UUID.randomUUID());
        assertEquals(1, result.size());
        assertEquals("ACTIVE", result.get(0).getStatus());
    }

    @Test
    void testFetchLeadAssignmentHistory_LeadNotFound() {
        when(leadRepository.findByIdentity(any())).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> service.fetchLeadAssignmentHistory(UUID.randomUUID()));
    }

    @Test
    void testFetchLeadAssignmentHistory_DataAccessException() {
        when(leadRepository.findByIdentity(any())).thenReturn(Optional.of(lead));
        when(leadAssignmentHistoryRepository.findAllByLeadAndIsDelFalse(any()))
                .thenThrow(mock(DataAccessException.class));
        assertThrows(BusinessException.class, () -> service.fetchLeadAssignmentHistory(UUID.randomUUID()));
    }

    @Test
    void testFetchLeadAssignmentHistory_MappingException() {
        LeadAssignmentHistory history = new LeadAssignmentHistory();
        history.setLead(lead);
        history.setAssignedOn(LocalDate.now());
        history.setStatus("ACTIVE");
        history.setAssignedToUserId(user);
        history.setAssignedByUserId(user);

        when(leadRepository.findByIdentity(any())).thenReturn(Optional.of(lead));
        when(leadAssignmentHistoryRepository.findAllByLeadAndIsDelFalse(any())).thenReturn(List.of(history));
        when(leadMapper.toResponseDto(any(), any())).thenThrow(new RuntimeException("mapping failed"));

        assertThrows(BusinessException.class, () -> service.fetchLeadAssignmentHistory(UUID.randomUUID()));
    }

    @Test
    void testUpdateLeadAssignment_Success() {
        LeadAssignmentHistory existing = new LeadAssignmentHistory();
        existing.setStatus("ACTIVE");
        existing.setLead(lead);
        when(leadRepository.findByIdentity(any())).thenReturn(Optional.of(lead));
        when(userRepository.findByIdentity(any())).thenReturn(Optional.of(user));
        when(leadAssignmentHistoryRepository.findAllByLeadAndIsDelFalse(any())).thenReturn(List.of(existing));

        LeadResponseDto response = service.updateLeadAssignment(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDate.now());
        assertEquals("Assignment updated", response.getStatus());
        verify(leadAssignmentHistoryRepository, atLeastOnce()).saveAll(anyList());
        verify(leadAssignmentHistoryRepository, atLeastOnce()).save(any());
    }

    @Test
    void testUpdateLeadAssignment_LeadNotFound() {
        when(leadRepository.findByIdentity(any())).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () ->
                service.updateLeadAssignment(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDate.now()));
    }

    @Test
    void testUpdateLeadAssignment_AssignedToNotFound() {
        when(leadRepository.findByIdentity(any())).thenReturn(Optional.of(lead));
        when(userRepository.findByIdentity(any())).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () ->
                service.updateLeadAssignment(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDate.now()));
    }

    @Test
    void testUpdateLeadAssignment_AssignedByNotFound() {
        when(leadRepository.findByIdentity(any())).thenReturn(Optional.of(lead));
        when(userRepository.findByIdentity(any()))
                .thenReturn(Optional.of(user))
                .thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () ->
                service.updateLeadAssignment(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDate.now()));
    }

    @Test
    void testUpdateLeadAssignment_DataIntegrityViolation() {
        when(leadRepository.findByIdentity(any())).thenReturn(Optional.of(lead));
        when(userRepository.findByIdentity(any())).thenReturn(Optional.of(user));
        when(leadAssignmentHistoryRepository.save(any())).thenThrow(new DataIntegrityViolationException("DB error"));
        assertThrows(BusinessException.class, () ->
                service.updateLeadAssignment(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDate.now()));
    }
}
