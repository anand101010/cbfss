package com.incede.nbfc.core.monolith.masterdata.service;

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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProfileMasterDataServiceTest {

    @Mock
    NationalityRepository nationalityRepository;
    @Mock
    OccupationRepository occupationRepository;
    @Mock
    RelationshipsRepository relationshipsRepository;
    @Mock
    LanguagesRepository languagesRepository;
    @Mock
    GendersRepository gendersRepository;
    @Mock
    MaritalStatusRepository maritalStatusRepository;
    @Mock
    DesignationsRepository designationsRepository;
    @Mock
    PurposeRepository purposeRepository;
    @Mock
    SourceOfIncomeTypeRepository sourceOfIncomeTypeRepository;
    @Mock
    TaxCategoryRepository taxCategoryRepository;
    @Mock
    SalutationTypesRepository salutationTypesRepository;
    @Mock
    ReferralSourceRepository referralSourceRepository;
    @Mock
    EducationLevelsRepository educationLevelsRepository;
    @Mock
    TenantRepository tenantRepository;

    @InjectMocks
    ProfileMasterDataService profileMasterDataService;

    UUID tenantIdentity;
    Tenant tenant;

    @BeforeEach
    void setup() {
        tenantIdentity = UUID.randomUUID();
        tenant = new Tenant();
        tenant.setTenantId(1);
        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.of(tenant));
    }

    @Test
    void testGetAllNationalities_WhenDataExists() {
        NationalityView mockView = mock(NationalityView.class);
        when(nationalityRepository.findAllByTenantIdOrAll(1)).thenReturn(List.of(mockView));

        List<NationalityView> result = profileMasterDataService.getAllNationalities(tenantIdentity);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllNationalities_WhenEmpty() {
        when(nationalityRepository.findAllByTenantIdOrAll(1)).thenReturn(Collections.emptyList());
        List<NationalityView> result = profileMasterDataService.getAllNationalities(tenantIdentity);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllOccupations_WhenDataExists() {
        OccupationView mockView = mock(OccupationView.class);
        when(occupationRepository.findAllByTenantIdOrAll(1)).thenReturn(List.of(mockView));

        List<OccupationView> result = profileMasterDataService.getAllOccupations(tenantIdentity);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllOccupations_WhenEmpty() {
        when(occupationRepository.findAllByTenantIdOrAll(1)).thenReturn(Collections.emptyList());
        List<OccupationView> result = profileMasterDataService.getAllOccupations(tenantIdentity);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllRelationships_WhenDataExists() {
        RelationshipsView mockView = mock(RelationshipsView.class);
        when(relationshipsRepository.findAllByTenantIdOrAll(1)).thenReturn(List.of(mockView));

        List<RelationshipsView> result = profileMasterDataService.getAllRelationships(tenantIdentity);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllRelationships_WhenEmpty() {
        when(relationshipsRepository.findAllByTenantIdOrAll(1)).thenReturn(Collections.emptyList());
        List<RelationshipsView> result = profileMasterDataService.getAllRelationships(tenantIdentity);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllLanguages_WhenDataExists() {
        LanguagesView mockView = mock(LanguagesView.class);
        when(languagesRepository.findAllByTenantIdOrAll(1)).thenReturn(List.of(mockView));

        List<LanguagesView> result = profileMasterDataService.getAllLanguages(tenantIdentity);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllGenders_WhenDataExists() {
        GendersView mockView = mock(GendersView.class);
        when(gendersRepository.findAllByTenantIdOrAll(1)).thenReturn(List.of(mockView));

        List<GendersView> result = profileMasterDataService.getAllGenders(tenantIdentity);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllMaritalStatus_WhenDataExists() {
        MaritalStatusView mockView = mock(MaritalStatusView.class);
        when(maritalStatusRepository.findAllByTenantIdOrAll(1)).thenReturn(List.of(mockView));

        List<MaritalStatusView> result = profileMasterDataService.getAllMaritalStatus(tenantIdentity);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllDesignations_WhenDataExists() {
        DesignationsView mockView = mock(DesignationsView.class);
        when(designationsRepository.findAllByTenantIdOrAll(1)).thenReturn(List.of(mockView));

        List<DesignationsView> result = profileMasterDataService.getAllDesignations(tenantIdentity);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllPurpose_WhenDataExists() {
        PurposeView mockView = mock(PurposeView.class);
        when(purposeRepository.findAllByTenantIdOrAll(1)).thenReturn(List.of(mockView));

        List<PurposeView> result = profileMasterDataService.getAllPurpose(tenantIdentity);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllSourceOfIncomeType_WhenDataExists() {
        SourceOfIncomeTypeView mockView = mock(SourceOfIncomeTypeView.class);
        when(sourceOfIncomeTypeRepository.findAllByTenantIdOrAll(1)).thenReturn(List.of(mockView));

        List<SourceOfIncomeTypeView> result = profileMasterDataService.getAllSourceOfIncomeType(tenantIdentity);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllTaxCategories_WhenDataExists() {
        TaxCategoryView mockView = mock(TaxCategoryView.class);
        when(taxCategoryRepository.findAllByTenantIdOrAll(1)).thenReturn(List.of(mockView));

        List<TaxCategoryView> result = profileMasterDataService.getAllTaxCategories(tenantIdentity);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllSalutationTypes_WhenDataExists() {
        SalutationTypesView mockView = mock(SalutationTypesView.class);
        when(salutationTypesRepository.findAllByTenantIdOrAll(1)).thenReturn(List.of(mockView));

        List<SalutationTypesView> result = profileMasterDataService.getAllSalutationTypes(tenantIdentity);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllReferralSources_WhenDataExists() {
        ReferralSourcesView mockView = mock(ReferralSourcesView.class);
        when(referralSourceRepository.findAllByTenantIdOrAll(1)).thenReturn(List.of(mockView));

        List<ReferralSourcesView> result = profileMasterDataService.getAllReferralSources(tenantIdentity);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllEducationLevels_WhenDataExists() {
        EducationLevelsView mockView = mock(EducationLevelsView.class);
        when(educationLevelsRepository.findAllByTenantIdOrAll(1)).thenReturn(List.of(mockView));

        List<EducationLevelsView> result = profileMasterDataService.getAllEducationLevels(tenantIdentity);
        assertEquals(1, result.size());
    }
}
