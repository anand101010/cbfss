package com.incede.nbfc.core.monolith.lead.controller;

import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpHistoryRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpHistoryResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadsFollowUpHistoryDto;
import com.incede.nbfc.core.monolith.lead.service.LeadFollowUpHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LeadFollowUpHistoryControllerTest {

    @Mock
    private LeadFollowUpHistoryService leadFollowUpHistoryService;

    @InjectMocks
    private LeadFollowUpHistoryController controller;

    private UUID leadId;
    private UUID followUpTypeId;
    private LeadFollowUpHistoryResponseDto responseDto;
    private LeadsFollowUpHistoryDto saveResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        leadId = UUID.randomUUID();
        followUpTypeId = UUID.randomUUID();

        responseDto = new LeadFollowUpHistoryResponseDto();
        saveResponseDto = new LeadsFollowUpHistoryDto();
    }

    @Test
    void testGetFollowUpHistory() {
        when(leadFollowUpHistoryService.getFollowUpHistory(leadId)).thenReturn(responseDto);

        ResponseEntity<LeadFollowUpHistoryResponseDto> response = controller.getFollowUpHistory(leadId);

        assertEquals(responseDto, response.getBody());
        verify(leadFollowUpHistoryService, times(1)).getFollowUpHistory(leadId);
    }

    @Test
    void testSearchFollowUpHistory() {
        when(leadFollowUpHistoryService.searchFollowUpHistory(
                any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(responseDto);

        ResponseEntity<LeadFollowUpHistoryResponseDto> response = controller.searchFollowUpHistory(
                leadId, UUID.randomUUID(), 123, followUpTypeId,
                LocalDate.now().minusDays(5), LocalDate.now(), 0, 10);

        assertEquals(responseDto, response.getBody());
        verify(leadFollowUpHistoryService, times(1)).searchFollowUpHistory(
                any(), any(), any(), any(), any(), any(), eq(0), eq(10));
    }


    @Test
    void testSaveFollowUpHistory() {
        LeadFollowUpHistoryRequestDto requestDto = new LeadFollowUpHistoryRequestDto();
        when(leadFollowUpHistoryService.saveFollowUpHistory(leadId, requestDto)).thenReturn(saveResponseDto);

        ResponseEntity<LeadsFollowUpHistoryDto> response = controller.saveFollowUpHistory(leadId, requestDto);

        assertEquals(saveResponseDto, response.getBody());
        verify(leadFollowUpHistoryService, times(1)).saveFollowUpHistory(leadId, requestDto);
    }
}
