package com.incede.nbfc.core.monolith.masterdata.controller;

import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.service.DocumentMasterDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class DocumentMasterDataControllerTest {

    @Mock
    private DocumentMasterDataService documentMasterDataService;

    @InjectMocks
    private DocumentMasterDataController documentMasterDataController;

    private UUID tenantIdentity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tenantIdentity = UUID.randomUUID();
    }

    @Test
    void testGetAllKycTypes() {
        KycTypesView mockKyc = mock(KycTypesView.class);
        given(mockKyc.getCode()).willReturn("KYC1");
        given(mockKyc.getDisplayName()).willReturn("PAN Card");
        given(mockKyc.getDescription()).willReturn("Permanent Account Number");

        given(documentMasterDataService.getAllKycTypes(tenantIdentity))
                .willReturn(List.of(mockKyc));

        ResponseEntity<List<KycTypesView>> response =
                documentMasterDataController.getAllKycTypes(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        KycTypesView result = response.getBody().get(0);
        assertThat(result.getCode()).isEqualTo("KYC1");
        assertThat(result.getDisplayName()).isEqualTo("PAN Card");
        assertThat(result.getDescription()).isEqualTo("Permanent Account Number");
    }

    @Test
    void testGetAllDocumentMasters() {
        DocumentMasterView mockDoc = mock(DocumentMasterView.class);
        UUID id = UUID.randomUUID();
        given(mockDoc.getDocname()).willReturn("Aadhar");
        given(mockDoc.getDocCode()).willReturn("AAD");
        given(mockDoc.getIsIdentityProof()).willReturn(true);
        given(mockDoc.getIsAddressProof()).willReturn(false);
        given(mockDoc.getDocCategory()).willReturn('A');
        given(mockDoc.getIsActive()).willReturn(true);
        given(mockDoc.getIdentity()).willReturn(id);

        given(documentMasterDataService.getAllDocumentMasters(tenantIdentity))
                .willReturn(List.of(mockDoc));

        ResponseEntity<List<DocumentMasterView>> response =
                documentMasterDataController.getAllDocumentMasters(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        DocumentMasterView result = response.getBody().get(0);
        assertThat(result.getDocname()).isEqualTo("Aadhar");
        assertThat(result.getDocCode()).isEqualTo("AAD");
        assertThat(result.getIsIdentityProof()).isTrue();
        assertThat(result.getIsAddressProof()).isFalse();
        assertThat(result.getDocCategory()).isEqualTo('A');
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllDocumentTypes() {
        DocumentTypeView mockDocType = mock(DocumentTypeView.class);
        UUID id = UUID.randomUUID();

        given(mockDocType.getCode()).willReturn("DOC1");
        given(mockDocType.getDisplayName()).willReturn("Passport");
        given(mockDocType.getDescription()).willReturn("Passport Document");
        given(mockDocType.getIdentity()).willReturn(id);

        given(documentMasterDataService.getAllDocumentType(tenantIdentity))
                .willReturn(List.of(mockDocType));

        ResponseEntity<List<DocumentTypeView>> response =
                documentMasterDataController.getAllDocumentType(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        DocumentTypeView result = response.getBody().get(0);
        assertThat(result.getCode()).isEqualTo("DOC1");
        assertThat(result.getDisplayName()).isEqualTo("Passport");
        assertThat(result.getDescription()).isEqualTo("Passport Document");
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllCanvassedTypes() {
        CanvassedTypesView mockCanvassed = mock(CanvassedTypesView.class);
        UUID id = UUID.randomUUID();
        given(mockCanvassed.getName()).willReturn("Type A");
        given(mockCanvassed.getCode()).willReturn("TA");
        given(mockCanvassed.getIdentity()).willReturn(id);

        given(documentMasterDataService.getAllCanvassedTypes(tenantIdentity))
                .willReturn(List.of(mockCanvassed));

        ResponseEntity<List<CanvassedTypesView>> response =
                documentMasterDataController.getAllCanvassedTypes(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        CanvassedTypesView result = response.getBody().get(0);
        assertThat(result.getName()).isEqualTo("Type A");
        assertThat(result.getCode()).isEqualTo("TA");
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllAssetTypes() {
        AssetTypesView mockAsset = mock(AssetTypesView.class);
        UUID id = UUID.randomUUID();
        given(mockAsset.getName()).willReturn("Vehicle");
        given(mockAsset.getCode()).willReturn("VEH");
        given(mockAsset.getIsActive()).willReturn(true);
        given(mockAsset.getIdentity()).willReturn(id);

        given(documentMasterDataService.getAllAssetTypes(tenantIdentity))
                .willReturn(List.of(mockAsset));

        ResponseEntity<List<AssetTypesView>> response =
                documentMasterDataController.getAllAssetTypes(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        AssetTypesView result = response.getBody().get(0);
        assertThat(result.getName()).isEqualTo("Vehicle");
        assertThat(result.getCode()).isEqualTo("VEH");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }
}
