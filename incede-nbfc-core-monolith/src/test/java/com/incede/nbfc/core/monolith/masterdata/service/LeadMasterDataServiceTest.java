package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeadMasterDataServiceTest {

    private AdditionalReferenceConfigRepository additionalReferenceConfigRepository;
    private LeadSourceRepository leadSourceRepository;
    private LeadStageRepository leadStageRepository;
    private FollowUpTypeRepository followUpTypeRepository;
    private LeadStatusRepository leadStatusRepository;

    private LeadMasterDataService leadMasterDataService;

    @BeforeEach
    void setUp() {
        additionalReferenceConfigRepository = mock(AdditionalReferenceConfigRepository.class);
        leadSourceRepository = mock(LeadSourceRepository.class);
        leadStageRepository = mock(LeadStageRepository.class);
        followUpTypeRepository = mock(FollowUpTypeRepository.class);
        leadStatusRepository = mock(LeadStatusRepository.class);

        leadMasterDataService = new LeadMasterDataService(
                additionalReferenceConfigRepository,
                leadSourceRepository,
                leadStageRepository,
                followUpTypeRepository,
                leadStatusRepository
        );
    }

    @Test
    void testGetAllAdditionalReferenceConfigs_WhenDataExists() {
        AdditionalReferenceConfigView mockView = mock(AdditionalReferenceConfigView.class);
        when(mockView.getReferenceFieldName()).thenReturn("PAN");
        when(mockView.getIdentity()).thenReturn(UUID.randomUUID());

        when(additionalReferenceConfigRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(List.of(mockView));

        List<AdditionalReferenceConfigView> result = leadMasterDataService.getAllAdditionalReferenceConfigs();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PAN", result.get(0).getReferenceFieldName());
    }

    @Test
    void testGetAllAdditionalReferenceConfigs_WhenEmpty() {
        when(additionalReferenceConfigRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(Collections.emptyList());

        List<AdditionalReferenceConfigView> result = leadMasterDataService.getAllAdditionalReferenceConfigs();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllAdditionalReferenceConfigs_WhenException() {
        when(additionalReferenceConfigRepository.findByIsDelFalseAndIsActiveTrue()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> leadMasterDataService.getAllAdditionalReferenceConfigs());
        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllLeadSources_WhenDataExists() {
        LeadSourceView mockView = mock(LeadSourceView.class);
        when(mockView.getName()).thenReturn("Website");

        when(leadSourceRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(List.of(mockView));

        List<LeadSourceView> result = leadMasterDataService.getAllLeadSources();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Website", result.get(0).getName());
    }

    @Test
    void testGetAllLeadSources_WhenEmpty() {
        when(leadSourceRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(Collections.emptyList());

        List<LeadSourceView> result = leadMasterDataService.getAllLeadSources();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllLeadSources_WhenException() {
        when(leadSourceRepository.findByIsDelFalseAndIsActiveTrue()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> leadMasterDataService.getAllLeadSources());
        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllLeadStages_WhenDataExists() {
        LeadStageView mockView = mock(LeadStageView.class);
        when(mockView.getName()).thenReturn("Qualified");

        when(leadStageRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(List.of(mockView));

        List<LeadStageView> result = leadMasterDataService.getAllLeadStages();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Qualified", result.get(0).getName());
    }

    @Test
    void testGetAllLeadStages_WhenEmpty() {
        when(leadStageRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(Collections.emptyList());

        List<LeadStageView> result = leadMasterDataService.getAllLeadStages();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllLeadStages_WhenException() {
        when(leadStageRepository.findByIsDelFalseAndIsActiveTrue()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> leadMasterDataService.getAllLeadStages());
        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllFollowUpTypes_WhenDataExists() {
        FollowUpTypeView mockView = mock(FollowUpTypeView.class);
        when(mockView.getName()).thenReturn("Call");

        when(followUpTypeRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(List.of(mockView));

        List<FollowUpTypeView> result = leadMasterDataService.getAllFollowUpTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Call", result.get(0).getName());
    }

    @Test
    void testGetAllFollowUpTypes_WhenEmpty() {
        when(followUpTypeRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(Collections.emptyList());

        List<FollowUpTypeView> result = leadMasterDataService.getAllFollowUpTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllFollowUpTypes_WhenException() {
        when(followUpTypeRepository.findByIsDelFalseAndIsActiveTrue()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> leadMasterDataService.getAllFollowUpTypes());
        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllLeadStatuses_WhenDataExists() {
        LeadStatusView mockView = mock(LeadStatusView.class);
        when(mockView.getName()).thenReturn("Converted");

        when(leadStatusRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(List.of(mockView));

        List<LeadStatusView> result = leadMasterDataService.getAllLeadStatuses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Converted", result.get(0).getName());
    }

    @Test
    void testGetAllLeadStatuses_WhenEmpty() {
        when(leadStatusRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(Collections.emptyList());

        List<LeadStatusView> result = leadMasterDataService.getAllLeadStatuses();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllLeadStatuses_WhenException() {
        when(leadStatusRepository.findByIsDelFalseAndIsActiveTrue()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> leadMasterDataService.getAllLeadStatuses());
        assertEquals("DB error", ex.getMessage());
    }
}
