package com.incede.nbfc.core.monolith.masterdata.controller;

import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.service.ProfileMasterDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ProfileMasterDataControllerTest {

    @Mock
    private ProfileMasterDataService profileMasterDataService;

    @InjectMocks
    private ProfileMasterDataController profileMasterDataController;

    private UUID tenantIdentity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tenantIdentity = UUID.randomUUID();
    }

    @Test
    void testGetAllSalutationTypes() {
        SalutationTypesView mockView = mock(SalutationTypesView.class);
        given(mockView.getSalutation()).willReturn("Mr.");
        given(profileMasterDataService.getAllSalutationTypes(tenantIdentity))
                .willReturn(List.of(mockView));

        ResponseEntity<List<SalutationTypesView>> response =
                profileMasterDataController.getAllSalutationTypes(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getSalutation()).isEqualTo("Mr.");
    }

    @Test
    void testGetAllNationalities() {
        NationalityView mockView = mock(NationalityView.class);
        UUID id = UUID.randomUUID();
        given(mockView.getNationality()).willReturn("Indian");
        given(mockView.getIsActive()).willReturn(true);
        given(mockView.getIdentity()).willReturn(id);
        given(profileMasterDataService.getAllNationalities(tenantIdentity))
                .willReturn(List.of(mockView));

        ResponseEntity<List<NationalityView>> response =
                profileMasterDataController.getAllNationalities(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        NationalityView result = response.getBody().get(0);
        assertThat(result.getNationality()).isEqualTo("Indian");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllOccupations() {
        OccupationView mockView = mock(OccupationView.class);
        UUID id = UUID.randomUUID();
        given(mockView.getOccupationName()).willReturn("Engineer");
        given(mockView.getIsActive()).willReturn(true);
        given(mockView.getIdentity()).willReturn(id);
        given(profileMasterDataService.getAllOccupations(tenantIdentity))
                .willReturn(List.of(mockView));

        ResponseEntity<List<OccupationView>> response =
                profileMasterDataController.getAllOccupations(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        OccupationView result = response.getBody().get(0);
        assertThat(result.getOccupationName()).isEqualTo("Engineer");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllRelationships() {
        RelationshipsView mockView = mock(RelationshipsView.class);
        given(mockView.getRelationship()).willReturn("Father");
        given(profileMasterDataService.getAllRelationships(tenantIdentity))
                .willReturn(List.of(mockView));

        ResponseEntity<List<RelationshipsView>> response =
                profileMasterDataController.getAllRelationships(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getRelationship()).isEqualTo("Father");
    }

    @Test
    void testGetAllLanguages() {
        LanguagesView mockView = mock(LanguagesView.class);
        given(mockView.getLanguageName()).willReturn("English");
        given(profileMasterDataService.getAllLanguages(tenantIdentity))
                .willReturn(List.of(mockView));

        ResponseEntity<List<LanguagesView>> response =
                profileMasterDataController.getAllLanguages(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getLanguageName()).isEqualTo("English");
    }

    @Test
    void testGetAllGenders() {
        GendersView mockView = mock(GendersView.class);
        UUID id = UUID.randomUUID();
        given(mockView.getGender()).willReturn("Male");
        given(mockView.getIsActive()).willReturn(true);
        given(mockView.getIdentity()).willReturn(id);
        given(profileMasterDataService.getAllGenders(tenantIdentity))
                .willReturn(List.of(mockView));

        ResponseEntity<List<GendersView>> response =
                profileMasterDataController.getAllGenders(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        GendersView result = response.getBody().get(0);
        assertThat(result.getGender()).isEqualTo("Male");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllMaritalStatus() {
        MaritalStatusView mockView = mock(MaritalStatusView.class);
        UUID id = UUID.randomUUID();
        given(mockView.getStatusName()).willReturn("Single");
        given(mockView.getIsActive()).willReturn(true);
        given(mockView.getIdentity()).willReturn(id);
        given(profileMasterDataService.getAllMaritalStatus(tenantIdentity))
                .willReturn(List.of(mockView));

        ResponseEntity<List<MaritalStatusView>> response =
                profileMasterDataController.getAllMaritalStatus(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        MaritalStatusView result = response.getBody().get(0);
        assertThat(result.getStatusName()).isEqualTo("Single");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllDesignations() {
        DesignationsView mockView = mock(DesignationsView.class);
        UUID id = UUID.randomUUID();
        given(mockView.getName()).willReturn("Manager");
        given(mockView.getCode()).willReturn("MGR");
        given(mockView.getDescription()).willReturn("Branch Manager");
        given(mockView.getLevel()).willReturn((short) 2);
        given(mockView.getIdentity()).willReturn(id);
        given(profileMasterDataService.getAllDesignations(tenantIdentity))
                .willReturn(List.of(mockView));

        ResponseEntity<List<DesignationsView>> response =
                profileMasterDataController.getAllDesignations(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        DesignationsView result = response.getBody().get(0);
        assertThat(result.getName()).isEqualTo("Manager");
        assertThat(result.getCode()).isEqualTo("MGR");
        assertThat(result.getDescription()).isEqualTo("Branch Manager");
        assertThat(result.getLevel()).isEqualTo((short) 2);
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllTaxCategories() {
        TaxCategoryView mockView = mock(TaxCategoryView.class);
        UUID id = UUID.randomUUID();
        given(mockView.getTaxCatName()).willReturn("GST");
        given(mockView.getIsActive()).willReturn(true);
        given(mockView.getIdentity()).willReturn(id);
        given(profileMasterDataService.getAllTaxCategories(tenantIdentity))
                .willReturn(List.of(mockView));

        ResponseEntity<List<TaxCategoryView>> response =
                profileMasterDataController.getAllTaxCategories(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        TaxCategoryView result = response.getBody().get(0);
        assertThat(result.getTaxCatName()).isEqualTo("GST");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllPurpose() {
        PurposeView mockView = mock(PurposeView.class);
        UUID id = UUID.randomUUID();
        given(mockView.getName()).willReturn("Loan");
        given(mockView.getCode()).willReturn("LN");
        given(mockView.getIdentity()).willReturn(id);
        given(profileMasterDataService.getAllPurpose(tenantIdentity))
                .willReturn(List.of(mockView));

        ResponseEntity<List<PurposeView>> response =
                profileMasterDataController.getAllPurpose(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        PurposeView result = response.getBody().get(0);
        assertThat(result.getName()).isEqualTo("Loan");
        assertThat(result.getCode()).isEqualTo("LN");
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllSourceOfIncomeType() {
        SourceOfIncomeTypeView mockView = mock(SourceOfIncomeTypeView.class);
        UUID id = UUID.randomUUID();
        given(mockView.getName()).willReturn("Salary");
        given(mockView.getCode()).willReturn("SAL");
        given(mockView.getIdentity()).willReturn(id);
        given(profileMasterDataService.getAllSourceOfIncomeType(tenantIdentity))
                .willReturn(List.of(mockView));

        ResponseEntity<List<SourceOfIncomeTypeView>> response =
                profileMasterDataController.getAllSourceOfIncomeType(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        SourceOfIncomeTypeView result = response.getBody().get(0);
        assertThat(result.getName()).isEqualTo("Salary");
        assertThat(result.getCode()).isEqualTo("SAL");
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllReferralSources() {
        ReferralSourcesView mockView = mock(ReferralSourcesView.class);
        given(mockView.getName()).willReturn("Referral A");
        given(profileMasterDataService.getAllReferralSources(tenantIdentity))
                .willReturn(List.of(mockView));

        ResponseEntity<List<ReferralSourcesView>> response =
                profileMasterDataController.getAllReferralSource(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Referral A");
    }

    @Test
    void testGetAllEducationLevels() {
        EducationLevelsView mockView = mock(EducationLevelsView.class);
        given(mockView.getName()).willReturn("Bachelor's");
        given(profileMasterDataService.getAllEducationLevels(tenantIdentity))
                .willReturn(List.of(mockView));

        ResponseEntity<List<EducationLevelsView>> response =
                profileMasterDataController.getAllEducationLevels(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Bachelor's");
    }
}
