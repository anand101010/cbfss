package com.incede.nbfc.core.monolith.masterdata.controller;

import com.incede.nbfc.core.monolith.masterdata.dto.PepCategoriesView;
import com.incede.nbfc.core.monolith.masterdata.dto.PepRelationshipsView;
import com.incede.nbfc.core.monolith.masterdata.dto.PepVerificationSourceView;
import com.incede.nbfc.core.monolith.masterdata.service.PepMasterDataService;
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

class PepMasterDataControllerTest {

    @Mock
    private PepMasterDataService pepMasterDataService;

    @InjectMocks
    private PepMasterDataController pepMasterDataController;

    private UUID tenantIdentity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tenantIdentity = UUID.randomUUID();
    }

    @Test
    void testGetAllPepCategories() {
        PepCategoriesView mockPep = mock(PepCategoriesView.class);
        UUID id = UUID.randomUUID();

        given(mockPep.getCode()).willReturn("PEP1");
        given(mockPep.getName()).willReturn("High Risk");
        given(mockPep.getIsActive()).willReturn(true);
        given(mockPep.getIdentity()).willReturn(id);

        given(pepMasterDataService.getAllPepCategories(tenantIdentity)).willReturn(List.of(mockPep));

        ResponseEntity<List<PepCategoriesView>> response = pepMasterDataController.getAllPepCategories(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        PepCategoriesView result = response.getBody().get(0);
        assertThat(result.getCode()).isEqualTo("PEP1");
        assertThat(result.getName()).isEqualTo("High Risk");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllPepRelationships() {
        PepRelationshipsView mockPepRel = mock(PepRelationshipsView.class);
        UUID id = UUID.randomUUID();

        given(mockPepRel.getName()).willReturn("Spouse");
        given(mockPepRel.getCode()).willReturn("SP");
        given(mockPepRel.getIsActive()).willReturn(true);
        given(mockPepRel.getIdentity()).willReturn(id);

        given(pepMasterDataService.getAllPepRelationships(tenantIdentity)).willReturn(List.of(mockPepRel));

        ResponseEntity<List<PepRelationshipsView>> response = pepMasterDataController.getAllPepRelationships(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        PepRelationshipsView result = response.getBody().get(0);
        assertThat(result.getName()).isEqualTo("Spouse");
        assertThat(result.getCode()).isEqualTo("SP");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllPepVerificationSource() {
        PepVerificationSourceView mockSource = mock(PepVerificationSourceView.class);
        UUID id = UUID.randomUUID();

        given(mockSource.getName()).willReturn("Government ID");
        given(mockSource.getCode()).willReturn("GID");
        given(mockSource.getIsActive()).willReturn(true);
        given(mockSource.getIdentity()).willReturn(id);

        given(pepMasterDataService.getAllPepVerificationSource(tenantIdentity)).willReturn(List.of(mockSource));

        ResponseEntity<List<PepVerificationSourceView>> response = pepMasterDataController.getAllPepVerificationSource(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        PepVerificationSourceView result = response.getBody().get(0);
        assertThat(result.getName()).isEqualTo("Government ID");
        assertThat(result.getCode()).isEqualTo("GID");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }
}
