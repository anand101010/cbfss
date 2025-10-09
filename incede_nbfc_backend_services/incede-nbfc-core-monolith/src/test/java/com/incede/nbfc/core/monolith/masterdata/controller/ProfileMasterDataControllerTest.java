package com.incede.nbfc.core.monolith.masterdata.controller;


import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.service.ProfileMasterDataService;
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
public class ProfileMasterDataControllerTest {

    @Mock
    private ProfileMasterDataService profileMasterDataService;

    @InjectMocks
    private ProfileMasterDataController profileMasterDataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetAllSalutationTypes() {
        SalutationTypesView mockSalutation = mock(SalutationTypesView.class);
        given(mockSalutation.getSalutation()).willReturn("Mr.");
        given(profileMasterDataService.getAllSalutationTypes()).willReturn(List.of(mockSalutation));

        ResponseEntity<List<SalutationTypesView>> response = profileMasterDataController.getAllSalutationTypes();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getSalutation()).isEqualTo("Mr.");
    }

    @Test
    void testGetAllNationalities() {
        NationalityView mockNationality = mock(NationalityView.class);
        UUID id = UUID.randomUUID();
        given(mockNationality.getNationality()).willReturn("Indian");
        given(mockNationality.getIsActive()).willReturn(true);
        given(mockNationality.getIdentity()).willReturn(id);

        given(profileMasterDataService.getAllNationalities()).willReturn(List.of(mockNationality));

        ResponseEntity<List<NationalityView>> response = profileMasterDataController.getAllNationalities();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        NationalityView result = response.getBody().get(0);
        assertThat(result.getNationality()).isEqualTo("Indian");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllOccupations() {
        OccupationView mockOccupation = mock(OccupationView.class);
        UUID id = UUID.randomUUID();
        given(mockOccupation.getOccupationName()).willReturn("Engineer");
        given(mockOccupation.getIsActive()).willReturn(true);
        given(mockOccupation.getIdentity()).willReturn(id);

        given(profileMasterDataService.getAllOccupations()).willReturn(List.of(mockOccupation));

        ResponseEntity<List<OccupationView>> response = profileMasterDataController.getAllOccupations();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        OccupationView result = response.getBody().get(0);
        assertThat(result.getOccupationName()).isEqualTo("Engineer");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllRelationships() {
        RelationshipsView mockRelationship = mock(RelationshipsView.class);
        given(mockRelationship.getRelationship()).willReturn("Father");
        given(profileMasterDataService.getAllRelationships()).willReturn(List.of(mockRelationship));

        ResponseEntity<List<RelationshipsView>> response = profileMasterDataController.getAllRelationships();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getRelationship()).isEqualTo("Father");
    }


    @Test
    void testGetAllLanguages() {
        LanguagesView mockLanguage = mock(LanguagesView.class);
        given(mockLanguage.getLanguageName()).willReturn("English");
        given(profileMasterDataService.getAllLanguages()).willReturn(List.of(mockLanguage));

        ResponseEntity<List<LanguagesView>> response = profileMasterDataController.getAllLanguages();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getLanguageName()).isEqualTo("English");
    }

    @Test
    void testGetAllGenders() {
        // Mock the GendersView interface
        GendersView mockGender = mock(GendersView.class);
        UUID id = UUID.randomUUID();
        given(mockGender.getGender()).willReturn("Male");
        given(mockGender.getIsActive()).willReturn(true);
        given(mockGender.getIdentity()).willReturn(id);

        given(profileMasterDataService.getAllGenders()).willReturn(List.of(mockGender));

        ResponseEntity<List<GendersView>> response = profileMasterDataController.getAllGenders();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        GendersView result = response.getBody().get(0);
        assertThat(result.getGender()).isEqualTo("Male");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllMaritalStatus() {

        MaritalStatusView mockStatus = mock(MaritalStatusView.class);
        UUID id = UUID.randomUUID();
        given(mockStatus.getStatusName()).willReturn("Single");
        given(mockStatus.getIsActive()).willReturn(true);
        given(mockStatus.getIdentity()).willReturn(id);

        given(profileMasterDataService.getAllMaritalStatus()).willReturn(List.of(mockStatus));

        ResponseEntity<List<MaritalStatusView>> response = profileMasterDataController.getAllMaritalStatus();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        MaritalStatusView result = response.getBody().get(0);
        assertThat(result.getStatusName()).isEqualTo("Single");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllDesignations() {
        DesignationsView mockDesignation = mock(DesignationsView.class);
        UUID id = UUID.randomUUID();
        given(mockDesignation.getName()).willReturn("Manager");
        given(mockDesignation.getCode()).willReturn("MGR");
        given(mockDesignation.getDescription()).willReturn("Branch Manager");
        given(mockDesignation.getLevel()).willReturn((short) 2);
        given(mockDesignation.getIdentity()).willReturn(id);

        given(profileMasterDataService.getAllDesignations()).willReturn(List.of(mockDesignation));

        ResponseEntity<List<DesignationsView>> response = profileMasterDataController.getAllDesignations();

        assertThat(response).isNotNull();
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

        TaxCategoryView mockTaxCategory = mock(TaxCategoryView.class);
        UUID id = UUID.randomUUID();
        given(mockTaxCategory.getTaxCatName()).willReturn("GST");
        given(mockTaxCategory.getIsActive()).willReturn(true);
        given(mockTaxCategory.getIdentity()).willReturn(id);

        given(profileMasterDataService.getAllTaxCategories()).willReturn(List.of(mockTaxCategory));

        ResponseEntity<List<TaxCategoryView>> response = profileMasterDataController.getAllTaxCategories();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        TaxCategoryView result = response.getBody().get(0);
        assertThat(result.getTaxCatName()).isEqualTo("GST");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllPurpose() {
        PurposeView mockPurpose = mock(PurposeView.class);
        UUID id = UUID.randomUUID();

        given(mockPurpose.getName()).willReturn("Loan");
        given(mockPurpose.getCode()).willReturn("LN");
        given(mockPurpose.getIdentity()).willReturn(id);

        given(profileMasterDataService.getAllPurpose()).willReturn(List.of(mockPurpose));

        ResponseEntity<List<PurposeView>> response = profileMasterDataController.getAllPurpose();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        PurposeView result = response.getBody().get(0);
        assertThat(result.getName()).isEqualTo("Loan");
        assertThat(result.getCode()).isEqualTo("LN");
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllSourceOfIncomeType() {

        SourceOfIncomeTypeView mockIncomeType = mock(SourceOfIncomeTypeView.class);
        UUID id = UUID.randomUUID();

        given(mockIncomeType.getName()).willReturn("Salary");
        given(mockIncomeType.getCode()).willReturn("SAL");
        given(mockIncomeType.getIdentity()).willReturn(id);

        given(profileMasterDataService.getAllSourceOfIncomeType()).willReturn(List.of(mockIncomeType));
        ResponseEntity<List<SourceOfIncomeTypeView>> response = profileMasterDataController.getAllSourceOfIncomeType();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        SourceOfIncomeTypeView result = response.getBody().get(0);
        assertThat(result.getName()).isEqualTo("Salary");
        assertThat(result.getCode()).isEqualTo("SAL");
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllReferralSource() {
        ReferralSourcesView mockView = mock(ReferralSourcesView.class);
        UUID id = UUID.randomUUID();

        given(mockView.getName()).willReturn("Referral A");

        given(profileMasterDataService.getAllReferralSources()).willReturn(List.of(mockView));

        ResponseEntity<List<ReferralSourcesView>> response = profileMasterDataController.getAllReferralSource();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        ReferralSourcesView result = response.getBody().get(0);
        assertThat(result.getName()).isEqualTo("Referral A");
    }

    @Test
    void testGetAllReferralSource_WhenEmpty() {
        given(profileMasterDataService.getAllReferralSources()).willReturn(List.of());

        ResponseEntity<List<ReferralSourcesView>> response = profileMasterDataController.getAllReferralSource();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void testGetAllEducationLevels() {
        EducationLevelsView mockView = mock(EducationLevelsView.class);
        given(mockView.getName()).willReturn("Bachelor's");

        given(profileMasterDataService.getAllEducationLevels()).willReturn(List.of(mockView));

        ResponseEntity<List<EducationLevelsView>> response = profileMasterDataController.getAllEducationLevels();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        EducationLevelsView result = response.getBody().get(0);
        assertThat(result.getName()).isEqualTo("Bachelor's");
    }

    @Test
    void testGetAllEducationLevels_WhenEmpty() {
        given(profileMasterDataService.getAllEducationLevels()).willReturn(List.of());

        ResponseEntity<List<EducationLevelsView>> response = profileMasterDataController.getAllEducationLevels();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isEmpty();
    }
}
