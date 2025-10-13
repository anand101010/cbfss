package com.incede.nbfc.core.monolith.lead.controller;
import com.incede.nbfc.core.monolith.lead.dto.*;
import com.incede.nbfc.core.monolith.lead.service.LeadFollowUpHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LeadFollowUpHistoryControllerTest {

    @Mock
    private LeadFollowUpHistoryService leadFollowUpHistoryService;

    @InjectMocks
    private LeadFollowUpHistoryController controller;

    private UUID leadIdentity;
    private UUID followUpHistoryIdentity;
    private UUID followUpTypeIdentity;
    private UUID leadStageIdentity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        leadIdentity = UUID.randomUUID();
        followUpHistoryIdentity = UUID.randomUUID();
        followUpTypeIdentity = UUID.randomUUID();
        leadStageIdentity = UUID.randomUUID();
    }

    @Test
    void testGetFollowUpHistory() {
        LeadFollowUpHistoryResponseDto responseDto = new LeadFollowUpHistoryResponseDto();
        when(leadFollowUpHistoryService.getFollowUpHistory(leadIdentity)).thenReturn(responseDto);

        ResponseEntity<LeadFollowUpHistoryResponseDto> response = controller.getFollowUpHistory(leadIdentity);

        assertEquals(responseDto, response.getBody());
        verify(leadFollowUpHistoryService, times(1)).getFollowUpHistory(leadIdentity);
    }

    @Test
    void testSearchFollowUpHistory() {
        LeadFollowUpHistoryResponseDto responseDto = new LeadFollowUpHistoryResponseDto();
        when(leadFollowUpHistoryService.searchFollowUpHistory(
                leadIdentity, null, null, followUpTypeIdentity,
                LocalDate.of(2025,10,1), LocalDate.of(2025,10,5), 0, 10))
                .thenReturn(responseDto);

        ResponseEntity<LeadFollowUpHistoryResponseDto> response = controller.searchFollowUpHistory(
                leadIdentity, null, null, followUpTypeIdentity,
                LocalDate.of(2025,10,1), LocalDate.of(2025,10,5), 0, 10
        );

        assertEquals(responseDto, response.getBody());
        verify(leadFollowUpHistoryService, times(1)).searchFollowUpHistory(
                leadIdentity, null, null, followUpTypeIdentity,
                LocalDate.of(2025,10,1), LocalDate.of(2025,10,5), 0, 10
        );
    }

    @Test
    void testUpdateFollowUpHistory() {
        LeadFollowUpHistoryRequestDto requestDto = new LeadFollowUpHistoryRequestDto();
        LeadsFollowUpHistoryDto responseDto = new LeadsFollowUpHistoryDto();
        when(leadFollowUpHistoryService.updateFollowUpHistory(leadIdentity, followUpHistoryIdentity, requestDto))
                .thenReturn(responseDto);

        ResponseEntity<LeadsFollowUpHistoryDto> response =
                controller.updateFollowUpHistory(leadIdentity, followUpHistoryIdentity, requestDto);

        assertEquals(responseDto, response.getBody());
        verify(leadFollowUpHistoryService, times(1)).updateFollowUpHistory(leadIdentity, followUpHistoryIdentity, requestDto);
    }

    @Test
    void testSaveFollowUpHistory() {
        LeadFollowUpHistoryRequestDto requestDto = new LeadFollowUpHistoryRequestDto();
        LeadsFollowUpHistoryDto responseDto = new LeadsFollowUpHistoryDto();
        when(leadFollowUpHistoryService.saveFollowUpHistory(leadIdentity, requestDto))
                .thenReturn(responseDto);

        ResponseEntity<LeadsFollowUpHistoryDto> response =
                controller.saveFollowUpHistory(leadIdentity, requestDto);

        assertEquals(responseDto, response.getBody());
        verify(leadFollowUpHistoryService, times(1)).saveFollowUpHistory(leadIdentity, requestDto);
    }

    @Test
    void testBulkSaveFollowUpHistory() {

        LeadFollowUpHistoryRequestDto request1 = new LeadFollowUpHistoryRequestDto();
        LeadFollowUpHistoryRequestDto request2 = new LeadFollowUpHistoryRequestDto();
        List<LeadFollowUpHistoryRequestDto> requestList = Arrays.asList(request1, request2);


        LeadsFollowUpHistoryDto response1 = new LeadsFollowUpHistoryDto();
        LeadsFollowUpHistoryDto response2 = new LeadsFollowUpHistoryDto();
        List<LeadsFollowUpHistoryDto> responseList = Arrays.asList(response1, response2);


        when(leadFollowUpHistoryService.bulkSaveFollowUpHistory(requestList)).thenReturn(responseList);


        ResponseEntity<List<LeadsFollowUpHistoryDto>> response = controller.bulkSaveFollowUpHistory(requestList);


        assertEquals(responseList, response.getBody());
        verify(leadFollowUpHistoryService, times(1)).bulkSaveFollowUpHistory(requestList);
    }

}


