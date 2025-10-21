package com.incede.nbfc.core.monolith.lead.controller;

import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.lead.dto.LeadAssignmentHistoryRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadAssignmentResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadAssignmentSearchResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadResponseDto;
import com.incede.nbfc.core.monolith.lead.service.LeadAssignmentHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeadAssignmentHistoryControllerTest {

    @Mock
    private LeadAssignmentHistoryService leadAssignmentHistoryService;

    @InjectMocks
    private LeadAssignmentHistoryController controller;

    private LeadAssignmentHistoryRequestDto requestDto;
    private UUID userId;
    private UUID leadId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        leadId = UUID.randomUUID();

        requestDto = new LeadAssignmentHistoryRequestDto();
        requestDto.setAssignedToUserIdentity(userId);
        requestDto.setLeadIdentities(List.of(leadId));
        requestDto.setAssignedOn(LocalDate.now());
    }


    @Test
    void testBulkUpdateLeadAssignments_Success() {
        when(leadAssignmentHistoryService.bulkUpdateLeadAssignments(any()))
                .thenReturn("{\"message\": \"1 lead assigned successfully\"}");

        ResponseEntity<String> response = controller.bulkUpdateLeadAssignments(requestDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("{\"message\": \"1 lead assigned successfully\"}", response.getBody());

        verify(leadAssignmentHistoryService, times(1)).bulkUpdateLeadAssignments(any());
    }

    @Test
    void testSearchLeadsForAssignment_Success() {
        LeadAssignmentSearchResponseDto searchResponseDto = new LeadAssignmentSearchResponseDto();
        Page<LeadAssignmentSearchResponseDto> page = new PageImpl<>(List.of(searchResponseDto));

        when(leadAssignmentHistoryService.searchLeadsForAssignment(
                any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(page);

        ResponseEntity<Page<LeadAssignmentSearchResponseDto>> response =
                controller.searchLeadsForAssignment(null, null, null, null, null, LocalDate.now(), 0, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getTotalElements());

        verify(leadAssignmentHistoryService, times(1))
                .searchLeadsForAssignment(any(), any(), any(), any(), any(), any(), anyInt(), anyInt());
    }


    @Test
    void testBulkUpdateLeadAssignments_BusinessException() {
        when(leadAssignmentHistoryService.bulkUpdateLeadAssignments(any()))
                .thenThrow(new BusinessException("Invalid user"));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            controller.bulkUpdateLeadAssignments(requestDto);
        });

        assertEquals("Invalid user", exception.getMessage());
        verify(leadAssignmentHistoryService, times(1)).bulkUpdateLeadAssignments(any());
    }

    @Test
    void testSearchLeadsForAssignment_ResourceNotFoundException() {
        when(leadAssignmentHistoryService.searchLeadsForAssignment(
                any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenThrow(new ResourceNotFoundException("Lead", "No leads found"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            controller.searchLeadsForAssignment(null, null, null, null, null, LocalDate.now(), 0, 10);
        });

        assertEquals("Lead with id 'No leads found' not found", exception.getMessage());
        verify(leadAssignmentHistoryService, times(1))
                .searchLeadsForAssignment(any(), any(), any(), any(), any(), any(), anyInt(), anyInt());
    }

    @Test
    void testFetchLeadAssignmentHistory_Success() {
        LeadAssignmentResponseDto dto = new LeadAssignmentResponseDto();
        when(leadAssignmentHistoryService.fetchLeadAssignmentHistory(any(UUID.class)))
                .thenReturn(List.of(dto));

        ResponseEntity<List<LeadAssignmentResponseDto>> response =
                controller.fetchLeadAssignmentHistory(UUID.randomUUID());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(leadAssignmentHistoryService, times(1)).fetchLeadAssignmentHistory(any(UUID.class));
    }

    @Test
    void testFetchLeadAssignmentHistory_ThrowsException() {
        when(leadAssignmentHistoryService.fetchLeadAssignmentHistory(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("Lead", "Assignment not found"));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                controller.fetchLeadAssignmentHistory(UUID.randomUUID())
        );

        assertEquals("Lead with id 'Assignment not found' not found", ex.getMessage());
    }
    @Test
    void testUpdateLeadAssignment_Success() {
        LeadResponseDto responseDto = new LeadResponseDto();
        responseDto.setStatus("Assignment updated");

        when(leadAssignmentHistoryService.updateLeadAssignment(any(), any(), any(), any()))
                .thenReturn(responseDto);

        ResponseEntity<LeadResponseDto> response = controller.updateLeadAssignment(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDate.now());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Assignment updated", response.getBody().getStatus());
        verify(leadAssignmentHistoryService, times(1))
                .updateLeadAssignment(any(), any(), any(), any());
    }

    @Test
    void testUpdateLeadAssignment_BusinessException() {
        when(leadAssignmentHistoryService.updateLeadAssignment(any(), any(), any(), any()))
                .thenThrow(new BusinessException("Invalid assignment"));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.updateLeadAssignment(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDate.now())
        );

        assertEquals("Invalid assignment", ex.getMessage());
    }



}
