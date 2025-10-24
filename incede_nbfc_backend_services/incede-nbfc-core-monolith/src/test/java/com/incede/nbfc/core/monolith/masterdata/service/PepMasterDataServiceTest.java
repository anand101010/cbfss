package com.incede.nbfc.core.monolith.masterdata.service;


import com.incede.nbfc.core.monolith.masterdata.dto.PepCategoriesView;
import com.incede.nbfc.core.monolith.masterdata.dto.PepRelationshipsView;
import com.incede.nbfc.core.monolith.masterdata.dto.PepVerificationSourceView;
import com.incede.nbfc.core.monolith.masterdata.repository.PepCategoriesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.PepRelationshipRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.PepVerificationSourceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PepMasterDataServiceTest {

    @Mock
    PepCategoriesRepository pepCategoriesRepository;
    @Mock
    PepRelationshipRepository pepRelationshipRepository;
    @Mock
    PepVerificationSourceRepository  pepVerificationSourceRepository;

    @InjectMocks
    PepMasterDataService pepMasterDataService;

    @Test
    void testGetAllPepCategories_WhenDataExists() {
        UUID id = UUID.randomUUID();

        PepCategoriesView mockView = mock(PepCategoriesView.class);
        when(mockView.getCode()).thenReturn("PEP01");
        when(mockView.getName()).thenReturn("High Risk");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(pepCategoriesRepository.findByIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<PepCategoriesView> result = pepMasterDataService.getAllPepCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PEP01", result.get(0).getCode());
        assertEquals("High Risk", result.get(0).getName());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllPepCategories_WhenDataEmpty() {
        when(pepCategoriesRepository.findByIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<PepCategoriesView> result = pepMasterDataService.getAllPepCategories();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllPepCategories_WhenRepositoryThrowsException() {
        when(pepCategoriesRepository.findByIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pepMasterDataService.getAllPepCategories());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllPepRelationships_WhenDataExists() {
        UUID id = UUID.randomUUID();

        PepRelationshipsView mockView = mock(PepRelationshipsView.class);
        when(mockView.getName()).thenReturn("Spouse");
        when(mockView.getCode()).thenReturn("SP");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(pepRelationshipRepository.findByIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<PepRelationshipsView> result = pepMasterDataService.getAllPepRelationships();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Spouse", result.get(0).getName());
        assertEquals("SP", result.get(0).getCode());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllPepRelationships_WhenDataEmpty() {
        when(pepRelationshipRepository.findByIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<PepRelationshipsView> result = pepMasterDataService.getAllPepRelationships();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllPepRelationships_WhenRepositoryThrowsException() {
        when(pepRelationshipRepository.findByIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pepMasterDataService.getAllPepRelationships());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllPepVerificationSources_WhenDataExists() {
        UUID id = UUID.randomUUID();

        PepVerificationSourceView mockView = mock(PepVerificationSourceView.class);
        when(mockView.getName()).thenReturn("Government Database");
        when(mockView.getCode()).thenReturn("GOV_DB");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(pepVerificationSourceRepository.findByIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<PepVerificationSourceView> result = pepMasterDataService.getAllPepVerificationSource();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Government Database", result.get(0).getName());
        assertEquals("GOV_DB", result.get(0).getCode());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllPepVerificationSources_WhenDataEmpty() {
        when(pepVerificationSourceRepository.findByIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<PepVerificationSourceView> result = pepMasterDataService.getAllPepVerificationSource();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllPepVerificationSources_WhenRepositoryThrowsException() {
        when(pepVerificationSourceRepository.findByIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pepMasterDataService.getAllPepVerificationSource());

        assertEquals("DB error", ex.getMessage());
    }
}
