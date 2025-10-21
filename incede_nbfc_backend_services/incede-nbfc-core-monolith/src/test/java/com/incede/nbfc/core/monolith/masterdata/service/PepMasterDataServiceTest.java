package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.masterdata.dto.PepCategoriesView;
import com.incede.nbfc.core.monolith.masterdata.dto.PepRelationshipsView;
import com.incede.nbfc.core.monolith.masterdata.dto.PepVerificationSourceView;
import com.incede.nbfc.core.monolith.masterdata.repository.PepCategoriesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.PepRelationshipRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.PepVerificationSourceRepository;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import com.incede.nbfc.core.monolith.tenant.repository.TenantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PepMasterDataServiceTest {

    @Mock
    PepCategoriesRepository pepCategoriesRepository;

    @Mock
    PepRelationshipRepository pepRelationshipRepository;

    @Mock
    PepVerificationSourceRepository pepVerificationSourceRepository;

    @Mock
    TenantRepository tenantRepository;

    @InjectMocks
    PepMasterDataService pepMasterDataService;


    @Test
    void testGetAllPepCategories_WhenDataExists() {
        UUID id = UUID.randomUUID();

        PepCategoriesView mockCategory = org.mockito.Mockito.mock(PepCategoriesView.class);

        when(pepCategoriesRepository.findAllByTenantIdOrAll(null))
                .thenReturn(List.of(mockCategory));

        List<PepCategoriesView> result = pepMasterDataService.getAllPepCategories(null);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllPepCategories_WhenDataEmpty() {
        when(pepCategoriesRepository.findAllByTenantIdOrAll(null))
                .thenReturn(Collections.emptyList());

        List<PepCategoriesView> result = pepMasterDataService.getAllPepCategories(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllPepCategories_WhenRepositoryThrowsException() {
        when(pepCategoriesRepository.findAllByTenantIdOrAll(null))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pepMasterDataService.getAllPepCategories(null));

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllPepRelationships_WhenDataExists() {
        PepRelationshipsView mockRelationship = org.mockito.Mockito.mock(PepRelationshipsView.class);

        when(pepRelationshipRepository.findAllByTenantIdOrAll(null))
                .thenReturn(List.of(mockRelationship));

        List<PepRelationshipsView> result = pepMasterDataService.getAllPepRelationships(null);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllPepRelationships_WhenDataEmpty() {
        when(pepRelationshipRepository.findAllByTenantIdOrAll(null))
                .thenReturn(Collections.emptyList());

        List<PepRelationshipsView> result = pepMasterDataService.getAllPepRelationships(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllPepRelationships_WhenRepositoryThrowsException() {
        when(pepRelationshipRepository.findAllByTenantIdOrAll(null))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pepMasterDataService.getAllPepRelationships(null));

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllPepVerificationSource_WhenDataExists() {
        PepVerificationSourceView mockSource = org.mockito.Mockito.mock(PepVerificationSourceView.class);

        when(pepVerificationSourceRepository.findAllByTenantIdOrAll(null))
                .thenReturn(List.of(mockSource));

        List<PepVerificationSourceView> result = pepMasterDataService.getAllPepVerificationSource(null);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllPepVerificationSource_WhenDataEmpty() {
        when(pepVerificationSourceRepository.findAllByTenantIdOrAll(null))
                .thenReturn(Collections.emptyList());

        List<PepVerificationSourceView> result = pepMasterDataService.getAllPepVerificationSource(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllPepVerificationSource_WhenRepositoryThrowsException() {
        when(pepVerificationSourceRepository.findAllByTenantIdOrAll(null))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pepMasterDataService.getAllPepVerificationSource(null));

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetTenantId_WhenTenantExists() {
        UUID tenantIdentity = UUID.randomUUID();
        Tenant mockTenant = new Tenant();
        mockTenant.setTenantId(1);

        when(tenantRepository.findByIdentity(tenantIdentity))
                .thenReturn(Optional.of(mockTenant));

        Integer tenantId = pepMasterDataService.getTenantId(tenantIdentity);

        assertNotNull(tenantId);
        assertEquals(1, tenantId);
    }

}
