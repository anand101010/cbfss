package com.incede.nbfc.core.monolith.lead.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.lead.dto.LeadRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadSearchResponseDto;
import com.incede.nbfc.core.monolith.lead.service.LeadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LeadControllerTest {

    @Mock
    private LeadService leadService;

    @InjectMocks
    private LeadController leadController;

    private LeadRequestDto leadRequestDto;
    private LeadResponseDto leadResponseDto;
    private UUID leadId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        leadId = UUID.randomUUID();

        leadRequestDto = LeadRequestDto.builder()
                .fullName("John Doe")
                .contactNumber("1234567890")
                .email("john@example.com")
                .assignTo(UUID.randomUUID())
                .build();

        leadResponseDto = LeadResponseDto.builder()
                .leadIdentity(leadId)
                .leadCode("LD-2025-001")
                .status("SUCCESS")
                .build();
    }

    @Test
    void testCreateLead() {
        when(leadService.createLead(any(LeadRequestDto.class))).thenReturn(leadResponseDto);

        ResponseEntity<LeadResponseDto> response = leadController.createLead(leadRequestDto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(leadResponseDto, response.getBody());

        verify(leadService, times(1)).createLead(any(LeadRequestDto.class));
    }

    @Test
    void testGetLead() {
        when(leadService.getLeadById(leadId)).thenReturn(leadResponseDto);

        ResponseEntity<LeadResponseDto> response = leadController.getLead(leadId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(leadResponseDto, response.getBody());

        verify(leadService, times(1)).getLeadById(leadId);
    }

    @Test
    void testSearchLeads() {
        LeadSearchResponseDto searchDto = LeadSearchResponseDto.builder()
                .leadIdentity(leadId.toString())
                .leadCode("LD-2025-001")
                .fullName("John Doe")
                .build();

        Page<LeadSearchResponseDto> page = new PageImpl<>(List.of(searchDto));
        when(leadService.searchLeads("John", "1234567890", "john@example.com", 0, 10))
                .thenReturn(page);

        ResponseEntity<Page<LeadSearchResponseDto>> response = leadController.searchLeads(
                "John", "1234567890", "john@example.com", 0, 10
        );

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getTotalElements());

        verify(leadService, times(1)).searchLeads("John", "1234567890", "john@example.com", 0, 10);
    }

    @Test
    void testUpdateLead() {
        when(leadService.updateLead(eq(leadId), any(LeadRequestDto.class))).thenReturn(leadResponseDto);

        ResponseEntity<LeadResponseDto> response = leadController.updateLead(leadId, leadRequestDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(leadResponseDto, response.getBody());

        verify(leadService, times(1)).updateLead(eq(leadId), any(LeadRequestDto.class));
    }
}
