package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentMasterDataServiceTest {

    @Mock
    KycTypesRepository kycTypesRepository;
    @Mock
    DocumentMasterRepository documentMasterRepository;
    @Mock
    DocumentTypeRepository documentTypeRepository;
    @Mock
    CanvassedTypesRepository canvassedTypesRepository;
    @Mock
    AssetTypesRepository assetTypesRepository;

    @InjectMocks
    DocumentMasterDataService documentMasterDataService;

    @Test
    void testGetAllKycTypes_WhenDataExists() {
        KycTypesView mockView = mock(KycTypesView.class);
        when(mockView.getCode()).thenReturn("PAN");
        when(mockView.getDisplayName()).thenReturn("PAN Card");
        when(mockView.getDescription()).thenReturn("Permanent Account Number");

        when(kycTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<KycTypesView> result = documentMasterDataService.getAllKycTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PAN", result.get(0).getCode());
        assertEquals("PAN Card", result.get(0).getDisplayName());
        assertEquals("Permanent Account Number", result.get(0).getDescription());
    }

    @Test
    void testGetAllKycTypes_WhenDataEmpty() {
        when(kycTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<KycTypesView> result = documentMasterDataService.getAllKycTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllKycTypes_WhenRepositoryThrowsException() {
        when(kycTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> documentMasterDataService.getAllKycTypes());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllDocumentMasters_WhenDataExists() {
        UUID id = UUID.randomUUID();

        DocumentMasterView mockDoc = mock(DocumentMasterView.class);
        when(mockDoc.getDocname()).thenReturn("Passport");
        when(mockDoc.getDocCode()).thenReturn("PP");
        when(mockDoc.getIsIdentityProof()).thenReturn(true);
        when(mockDoc.getIsAddressProof()).thenReturn(false);
        when(mockDoc.getDocCategory()).thenReturn('A');
        when(mockDoc.getIsActive()).thenReturn(true);
        when(mockDoc.getIdentity()).thenReturn(id);

        when(documentMasterRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockDoc));

        List<DocumentMasterView> result = documentMasterDataService.getAllDocumentMasters();

        assertNotNull(result);
        assertEquals(1, result.size());

        DocumentMasterView resultDoc = result.get(0);
        assertEquals("Passport", resultDoc.getDocname());
        assertEquals("PP", resultDoc.getDocCode());
        assertTrue(resultDoc.getIsIdentityProof());
        assertFalse(resultDoc.getIsAddressProof());
        assertEquals('A', resultDoc.getDocCategory());
        assertTrue(resultDoc.getIsActive());
        assertEquals(id, resultDoc.getIdentity());
    }

    @Test
    void testGetAllDocumentMasters_WhenDataEmpty() {
        lenient().when(documentMasterRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<DocumentMasterView> result = documentMasterDataService.getAllDocumentMasters();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllDocumentMasters_WhenRepositoryThrowsException() {
        when(documentMasterRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> documentMasterDataService.getAllDocumentMasters());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllDocumentTypes_WhenDataExists() {
        UUID id = UUID.randomUUID();

        DocumentTypeView mockView = mock(DocumentTypeView.class);
        when(mockView.getCode()).thenReturn("ID");
        when(mockView.getDisplayName()).thenReturn("Identity Document");
        when(mockView.getDescription()).thenReturn("Official identity proof document");
        when(mockView.getIdentity()).thenReturn(id);

        when(documentTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<DocumentTypeView> result = documentMasterDataService.getAllDocumentType();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ID", result.get(0).getCode());
        assertEquals("Identity Document", result.get(0).getDisplayName());
        assertEquals("Official identity proof document", result.get(0).getDescription());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllDocumentTypes_WhenDataEmpty() {
        when(documentTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<DocumentTypeView> result = documentMasterDataService.getAllDocumentType();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllDocumentTypes_WhenRepositoryThrowsException() {
        when(documentTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> documentMasterDataService.getAllDocumentType());

        assertEquals("DB error", ex.getMessage());
    }


    @Test
    void testGetAllCanvassedTypes_WhenDataExists() {
        UUID id = UUID.randomUUID();

        CanvassedTypesView mockView = mock(CanvassedTypesView.class);
        when(mockView.getName()).thenReturn("Type A");
        when(mockView.getCode()).thenReturn("A");
        when(mockView.getIdentity()).thenReturn(id);

        when(canvassedTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<CanvassedTypesView> result = documentMasterDataService.getAllCanvassedTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Type A", result.get(0).getName());
        assertEquals("A", result.get(0).getCode());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllCanvassedTypes_WhenDataEmpty() {
        when(canvassedTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<CanvassedTypesView> result = documentMasterDataService.getAllCanvassedTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllCanvassedTypes_WhenRepositoryThrowsException() {
        when(canvassedTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> documentMasterDataService.getAllCanvassedTypes());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllAssetTypes_WhenDataExists() {
        UUID id = UUID.randomUUID();

        AssetTypesView mockView = mock(AssetTypesView.class);
        when(mockView.getName()).thenReturn("Vehicle");
        when(mockView.getCode()).thenReturn("VEH");
        when(mockView.getIdentity()).thenReturn(id);
        when(mockView.getIsActive()).thenReturn(true);

        when(assetTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<AssetTypesView> result = documentMasterDataService.getAllAssetTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Vehicle", result.get(0).getName());
        assertEquals("VEH", result.get(0).getCode());
        assertEquals(id, result.get(0).getIdentity());
        assertTrue(result.get(0).getIsActive());
    }

    @Test
    void testGetAllAssetTypes_WhenDataEmpty() {
        when(assetTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<AssetTypesView> result = documentMasterDataService.getAllAssetTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllAssetTypes_WhenRepositoryThrowsException() {
        when(assetTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> documentMasterDataService.getAllAssetTypes());

        assertEquals("DB error", ex.getMessage());
    }


}
