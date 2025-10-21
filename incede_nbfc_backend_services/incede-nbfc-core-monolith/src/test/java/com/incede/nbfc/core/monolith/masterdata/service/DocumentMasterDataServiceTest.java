package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import com.incede.nbfc.core.monolith.tenant.repository.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentMasterDataServiceTest {

    @Mock
    private KycTypesRepository kycTypesRepository;
    @Mock
    private DocumentMasterRepository documentMasterRepository;
    @Mock
    private DocumentTypeRepository documentTypeRepository;
    @Mock
    private CanvassedTypesRepository canvassedTypesRepository;
    @Mock
    private AssetTypesRepository assetTypesRepository;
    @Mock
    private TenantRepository tenantRepository;

    @InjectMocks
    private DocumentMasterDataService documentMasterDataService;

    private UUID tenantIdentity;
    private Tenant mockTenant;

    @BeforeEach
    void setUp() {
        tenantIdentity = UUID.randomUUID();
        mockTenant = new Tenant();
        mockTenant.setTenantId(100);

        // Using lenient() here prevents unnecessary stubbing error
        lenient().when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.of(mockTenant));
    }

    // ---------- getTenantId() ----------
    @Test
    void testGetTenantId_WhenTenantExists() {
        Integer tenantId = documentMasterDataService.getTenantId(tenantIdentity);
        assertEquals(100, tenantId);
        verify(tenantRepository, times(1)).findByIdentity(tenantIdentity);
    }

    @Test
    void testGetTenantId_WhenTenantNotFound() {
        when(tenantRepository.findByIdentity(any())).thenReturn(Optional.empty());
        BusinessException ex = assertThrows(BusinessException.class,
                () -> documentMasterDataService.getTenantId(UUID.randomUUID()));
        assertEquals(CommonConstants.TENANT_NOT_FOUND, ex.getMessage());
    }

    // ---------- getAllKycTypes() ----------
    @Test
    void testGetAllKycTypes_WhenDataExists() {
        KycTypesView mockView = mock(KycTypesView.class);
        when(mockView.getCode()).thenReturn("PAN");
        when(kycTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(100))
                .thenReturn(List.of(mockView));

        List<KycTypesView> result = documentMasterDataService.getAllKycTypes(tenantIdentity);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PAN", result.get(0).getCode());
    }

    @Test
    void testGetAllKycTypes_WhenDataEmpty() {
        when(kycTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(100))
                .thenReturn(Collections.emptyList());
        List<KycTypesView> result = documentMasterDataService.getAllKycTypes(tenantIdentity);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllKycTypes_WhenRepositoryThrowsException() {
        when(kycTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(100))
                .thenThrow(new RuntimeException("DB error"));
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> documentMasterDataService.getAllKycTypes(tenantIdentity));
        assertEquals("DB error", ex.getMessage());
    }

    // ---------- getAllDocumentMasters() ----------
    @Test
    void testGetAllDocumentMasters_WhenDataExists() {
        DocumentMasterView mockDoc = mock(DocumentMasterView.class);
        when(mockDoc.getDocname()).thenReturn("Passport");
        when(documentMasterRepository.findByIsDelFalseAndIsActiveTrueByTenantId(100))
                .thenReturn(List.of(mockDoc));

        List<DocumentMasterView> result = documentMasterDataService.getAllDocumentMasters(tenantIdentity);

        assertEquals(1, result.size());
        assertEquals("Passport", result.get(0).getDocname());
    }

    @Test
    void testGetAllDocumentMasters_WhenEmpty() {
        when(documentMasterRepository.findByIsDelFalseAndIsActiveTrueByTenantId(100))
                .thenReturn(Collections.emptyList());
        List<DocumentMasterView> result = documentMasterDataService.getAllDocumentMasters(tenantIdentity);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllDocumentMasters_WhenRepositoryThrowsException() {
        when(documentMasterRepository.findByIsDelFalseAndIsActiveTrueByTenantId(100))
                .thenThrow(new RuntimeException("DB error"));
        assertThrows(RuntimeException.class,
                () -> documentMasterDataService.getAllDocumentMasters(tenantIdentity));
    }

    // ---------- getAllDocumentType() ----------
    @Test
    void testGetAllDocumentType_WhenDataExists() {
        DocumentTypeView mockType = mock(DocumentTypeView.class);
        when(mockType.getCode()).thenReturn("ID");
        when(documentTypeRepository.findByIsDelFalseAndIsActiveTrueByTenantId(100))
                .thenReturn(List.of(mockType));

        List<DocumentTypeView> result = documentMasterDataService.getAllDocumentType(tenantIdentity);
        assertEquals(1, result.size());
        assertEquals("ID", result.get(0).getCode());
    }

    @Test
    void testGetAllDocumentType_WhenEmpty() {
        when(documentTypeRepository.findByIsDelFalseAndIsActiveTrueByTenantId(100))
                .thenReturn(Collections.emptyList());
        assertTrue(documentMasterDataService.getAllDocumentType(tenantIdentity).isEmpty());
    }

    // ---------- getAllCanvassedTypes() ----------
    @Test
    void testGetAllCanvassedTypes_WhenDataExists() {
        CanvassedTypesView mockView = mock(CanvassedTypesView.class);
        when(mockView.getCode()).thenReturn("CANV-A");
        when(canvassedTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(100))
                .thenReturn(List.of(mockView));

        List<CanvassedTypesView> result = documentMasterDataService.getAllCanvassedTypes(tenantIdentity);
        assertEquals(1, result.size());
        assertEquals("CANV-A", result.get(0).getCode());
    }

    @Test
    void testGetAllCanvassedTypes_WhenEmpty() {
        when(canvassedTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(100))
                .thenReturn(Collections.emptyList());
        assertTrue(documentMasterDataService.getAllCanvassedTypes(tenantIdentity).isEmpty());
    }

    // ---------- getAllAssetTypes() ----------
    @Test
    void testGetAllAssetTypes_WhenDataExists() {
        AssetTypesView mockView = mock(AssetTypesView.class);
        when(mockView.getCode()).thenReturn("VEH");
        when(assetTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(100))
                .thenReturn(List.of(mockView));

        List<AssetTypesView> result = documentMasterDataService.getAllAssetTypes(tenantIdentity);
        assertEquals(1, result.size());
        assertEquals("VEH", result.get(0).getCode());
    }

    @Test
    void testGetAllAssetTypes_WhenEmpty() {
        when(assetTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(100))
                .thenReturn(Collections.emptyList());
        assertTrue(documentMasterDataService.getAllAssetTypes(tenantIdentity).isEmpty());
    }

    // ---------- Tenant Identity = null (extra branch coverage) ----------
    @Test
    void testGetAllKycTypes_WhenTenantIdentityIsNull() {
        when(kycTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(null))
                .thenReturn(Collections.emptyList());
        assertTrue(documentMasterDataService.getAllKycTypes(null).isEmpty());
        verify(tenantRepository, never()).findByIdentity(any());
    }

    @Test
    void testGetAllDocumentMasters_WhenTenantIdentityIsNull() {
        when(documentMasterRepository.findByIsDelFalseAndIsActiveTrueByTenantId(null))
                .thenReturn(Collections.emptyList());
        assertTrue(documentMasterDataService.getAllDocumentMasters(null).isEmpty());
    }

    @Test
    void testGetAllDocumentType_WhenTenantIdentityIsNull() {
        when(documentTypeRepository.findByIsDelFalseAndIsActiveTrueByTenantId(null))
                .thenReturn(Collections.emptyList());
        assertTrue(documentMasterDataService.getAllDocumentType(null).isEmpty());
    }

    @Test
    void testGetAllCanvassedTypes_WhenTenantIdentityIsNull() {
        when(canvassedTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(null))
                .thenReturn(Collections.emptyList());
        assertTrue(documentMasterDataService.getAllCanvassedTypes(null).isEmpty());
    }

    @Test
    void testGetAllAssetTypes_WhenTenantIdentityIsNull() {
        when(assetTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(null))
                .thenReturn(Collections.emptyList());
        assertTrue(documentMasterDataService.getAllAssetTypes(null).isEmpty());
    }
}
