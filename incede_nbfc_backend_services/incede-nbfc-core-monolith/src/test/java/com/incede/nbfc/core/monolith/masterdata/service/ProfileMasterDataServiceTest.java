package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;
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
    private ReferralSourceRepository referralSourceRepository;
    @Mock
    private EducationLevelsRepository educationLevelsRepository;

    @InjectMocks
    ProfileMasterDataService profileMasterDataService;

    @Test
    void testGetAllNationalities_WhenDataExists() {
        UUID id = UUID.randomUUID();

        NationalityView mockView = mock(NationalityView.class);
        when(mockView.getNationality()).thenReturn("Indian");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(nationalityRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<NationalityView> result = profileMasterDataService.getAllNationalities();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Indian", result.get(0).getNationality());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllNationalities_WhenDataEmpty() {
        when(nationalityRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<NationalityView> result = profileMasterDataService.getAllNationalities();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllNationalities_WhenRepositoryThrowsException() {
        when(nationalityRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> profileMasterDataService.getAllNationalities());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllOccupations_WhenDataExists() {
        UUID id = UUID.randomUUID();

        OccupationView mockView = mock(OccupationView.class);
        when(mockView.getOccupationName()).thenReturn("Engineer");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(occupationRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<OccupationView> result = profileMasterDataService.getAllOccupations();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Engineer", result.get(0).getOccupationName());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllOccupations_WhenDataEmpty() {
        when(occupationRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<OccupationView> result = profileMasterDataService.getAllOccupations();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllOccupations_WhenRepositoryThrowsException() {
        when(occupationRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> profileMasterDataService.getAllOccupations());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllRelationships_WhenDataExists() {
        RelationshipsView mockView = mock(RelationshipsView.class);
        when(relationshipsRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<RelationshipsView> result = profileMasterDataService.getAllRelationships();

        assertEquals(1, result.size());
    }

    @Test
    void testGetAllRelationships_WhenDataEmpty() {
        when(relationshipsRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<RelationshipsView> result = profileMasterDataService.getAllRelationships();

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllRelationships_WhenRepositoryThrowsException() {
        when(relationshipsRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class,
                () -> profileMasterDataService.getAllRelationships());
    }

    @Test
    void testGetAllLanguages_WhenDataExists() {
        UUID id = UUID.randomUUID();
        LanguagesView mockView = mock(LanguagesView.class);
        when(mockView.getLanguageName()).thenReturn("English");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(languagesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<LanguagesView> result = profileMasterDataService.getAllLanguages();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("English", result.get(0).getLanguageName());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllLanguages_WhenDataEmpty() {
        when(languagesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<LanguagesView> result = profileMasterDataService.getAllLanguages();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllLanguages_WhenRepositoryThrowsException() {
        when(languagesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> profileMasterDataService.getAllLanguages());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllGenders_WhenDataExists() {
        UUID id = UUID.randomUUID();

        GendersView mockView = mock(GendersView.class);
        when(mockView.getGender()).thenReturn("Male");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(gendersRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<GendersView> result = profileMasterDataService.getAllGenders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Male", result.get(0).getGender());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllGenders_WhenDataEmpty() {
        when(gendersRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<GendersView> result = profileMasterDataService.getAllGenders();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllGenders_WhenRepositoryThrowsException() {
        when(gendersRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> profileMasterDataService.getAllGenders());

        assertEquals("DB error", ex.getMessage());
    }
    @Test
    void testGetAllMaritalStatus_WhenDataExists() {
        UUID id = UUID.randomUUID();

        MaritalStatusView mockView = mock(MaritalStatusView.class);
        when(mockView.getStatusName()).thenReturn("Single");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(maritalStatusRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<MaritalStatusView> result = profileMasterDataService.getAllMaritalStatus();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Single", result.get(0).getStatusName());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllMaritalStatus_WhenDataEmpty() {
        when(maritalStatusRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<MaritalStatusView> result = profileMasterDataService.getAllMaritalStatus();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    void testGetAllMaritalStatus_WhenRepositoryThrowsException() {
        when(maritalStatusRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> profileMasterDataService.getAllMaritalStatus());

        assertEquals("DB error", ex.getMessage());
    }


    @Test
    void testGetAllDesignations_WhenDataExists() {
        UUID id = UUID.randomUUID();

        DesignationsView mockView = mock(DesignationsView.class);
        when(mockView.getName()).thenReturn("Manager");
        when(mockView.getCode()).thenReturn("MGR");
        when(mockView.getDescription()).thenReturn("Branch Manager");
        when(mockView.getLevel()).thenReturn((short) 2);
        when(mockView.getIdentity()).thenReturn(id);

        when(designationsRepository.findByIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<DesignationsView> result = profileMasterDataService.getAllDesignations();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Manager", result.get(0).getName());
        assertEquals("MGR", result.get(0).getCode());
        assertEquals("Branch Manager", result.get(0).getDescription());
        assertEquals((short) 2, result.get(0).getLevel());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllDesignations_WhenDataEmpty() {
        when(designationsRepository.findByIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<DesignationsView> result = profileMasterDataService.getAllDesignations();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllDesignations_WhenRepositoryThrowsException() {
        when(designationsRepository.findByIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> profileMasterDataService.getAllDesignations());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllPurposes_WhenDataExists() {
        UUID id = UUID.randomUUID();

        PurposeView mockView = mock(PurposeView.class);
        when(mockView.getName()).thenReturn("Loan");
        when(mockView.getCode()).thenReturn("LN");
        when(mockView.getIdentity()).thenReturn(id);

        when(purposeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<PurposeView> result = profileMasterDataService.getAllPurpose();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Loan", result.get(0).getName());
        assertEquals("LN", result.get(0).getCode());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllPurposes_WhenDataEmpty() {
        when(purposeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<PurposeView> result = profileMasterDataService.getAllPurpose();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllPurposes_WhenRepositoryThrowsException() {
        when(purposeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> profileMasterDataService.getAllPurpose());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllSourceOfIncomeTypes_WhenDataExists() {
        UUID id = UUID.randomUUID();

        SourceOfIncomeTypeView mockView = mock(SourceOfIncomeTypeView.class);
        when(mockView.getName()).thenReturn("Salary");
        when(mockView.getCode()).thenReturn("SAL");
        when(mockView.getIdentity()).thenReturn(id);

        when(sourceOfIncomeTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<SourceOfIncomeTypeView> result = profileMasterDataService.getAllSourceOfIncomeType();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Salary", result.get(0).getName());
        assertEquals("SAL", result.get(0).getCode());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllSourceOfIncomeTypes_WhenDataEmpty() {
        when(sourceOfIncomeTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<SourceOfIncomeTypeView> result = profileMasterDataService.getAllSourceOfIncomeType();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllSourceOfIncomeTypes_WhenRepositoryThrowsException() {
        when(sourceOfIncomeTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> profileMasterDataService.getAllSourceOfIncomeType());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllTaxCategories_WhenDataExists() {
        UUID id = UUID.randomUUID();

        TaxCategoryView mockView = mock(TaxCategoryView.class);
        when(mockView.getTaxCatName()).thenReturn("GST");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(taxCategoryRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<TaxCategoryView> result = profileMasterDataService.getAllTaxCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("GST", result.get(0).getTaxCatName());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllTaxCategories_WhenDataEmpty() {
        lenient().when(taxCategoryRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<TaxCategoryView> result = profileMasterDataService.getAllTaxCategories();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    @Test
    void testGetAllTaxCategories_WhenRepositoryThrowsException() {
        when(taxCategoryRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> profileMasterDataService.getAllTaxCategories());

        assertEquals("DB error", ex.getMessage());
    }


    @Test
    void testGetAllSalutationTypes_WhenDataExists() {
        UUID id = UUID.randomUUID();

        SalutationTypesView mockView = mock(SalutationTypesView.class);
        when(mockView.getSalutation()).thenReturn("Mr.");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(salutationTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<SalutationTypesView> result = profileMasterDataService.getAllSalutationTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mr.", result.get(0).getSalutation());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllSalutationTypes_WhenDataEmpty() {
        // Use lenient stubbing to avoid UnnecessaryStubbingException
        lenient().when(salutationTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<SalutationTypesView> result = profileMasterDataService.getAllSalutationTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllSalutationTypes_WhenRepositoryThrowsException() {
        when(salutationTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> profileMasterDataService.getAllSalutationTypes());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllReferralSources_WhenDataExists() {
        ReferralSourcesView mockView = mock(ReferralSourcesView.class);
        when(mockView.getName()).thenReturn("Referral A");

        when(referralSourceRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<ReferralSourcesView> result = profileMasterDataService.getAllReferralSources();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Referral A", result.get(0).getName());
    }

    @Test
    void testGetAllReferralSources_WhenDataEmpty() {
        lenient().when(referralSourceRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<ReferralSourcesView> result = profileMasterDataService.getAllReferralSources();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllReferralSources_WhenRepositoryThrowsException() {
        when(referralSourceRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> profileMasterDataService.getAllReferralSources());

        assertEquals("DB error", ex.getMessage());
    }


    @Test
    void testGetAllEducationLevels_WhenDataExists() {
        EducationLevelsView mockView = mock(EducationLevelsView.class);
        when(mockView.getName()).thenReturn("Bachelor's");

        when(educationLevelsRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<EducationLevelsView> result = profileMasterDataService.getAllEducationLevels();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllEducationLevels_WhenDataEmpty() {
        lenient().when(educationLevelsRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<EducationLevelsView> result = profileMasterDataService.getAllEducationLevels();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllEducationLevels_WhenRepositoryThrowsException() {
        when(educationLevelsRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> profileMasterDataService.getAllEducationLevels());

        assertEquals("DB error", ex.getMessage());
    }

}
