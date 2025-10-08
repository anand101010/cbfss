package com.incede.nbfc.core.monolith.lead.controller;

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

    @InjectMocks
    private LeadController leadController;

    @Mock
    private LeadService leadService;

    private LeadRequestDto leadRequestDto;
    private LeadResponseDto leadResponseDto;
    private UUID leadId;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);

        leadId = UUID.randomUUID();

        leadRequestDto = new LeadRequestDto();
        leadRequestDto.setFullName("John Doe");

        leadResponseDto = LeadResponseDto.builder()
                .leadIdentity(leadId)
                .leadCode("LD-2025-001")
                .status("SUCCESS")
                .leadDetails(null)
                .build();
    }

    @Test
    void testCreateLead_Success() {
        when(leadService.createLead(leadRequestDto)).thenReturn(leadResponseDto);

        ResponseEntity<LeadResponseDto> response = leadController.createLead(leadRequestDto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(leadResponseDto, response.getBody());

        verify(leadService, times(1)).createLead(leadRequestDto);
    }

    @Test
    void testGetLead_Success() {
        when(leadService.getLeadById(leadId)).thenReturn(leadResponseDto);

        ResponseEntity<LeadResponseDto> response = leadController.getLead(leadId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(leadResponseDto, response.getBody());

        verify(leadService, times(1)).getLeadById(leadId);
    }

    @Test
    void testSearchLeads_Success() {
        LeadSearchResponseDto searchDto = new LeadSearchResponseDto();
        Page<LeadSearchResponseDto> page = new PageImpl<>(List.of(searchDto));

        when(leadService.searchLeads("John", "9876543210", "john@example.com", 0, 10))
                .thenReturn(page);

        ResponseEntity<Page<LeadSearchResponseDto>> response = leadController.searchLeads(
                "John", "9876543210", "john@example.com", 0, 10
        );

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(page, response.getBody());

        verify(leadService, times(1))
                .searchLeads("John", "9876543210", "john@example.com", 0, 10);
    }
}
