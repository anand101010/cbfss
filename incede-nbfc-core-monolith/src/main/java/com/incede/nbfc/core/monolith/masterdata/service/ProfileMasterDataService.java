package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileMasterDataService {


    private final NationalityRepository nationalityRepository;
    private final OccupationRepository occupationRepository;
    private final RelationshipsRepository relationshipsRepository;
    private final LanguagesRepository languagesRepository;
    private final GendersRepository gendersRepository;
    private final MaritalStatusRepository maritalStatusRepository;
    private final DesignationsRepository designationsRepository;
    private final PurposeRepository purposeRepository;
    private final SourceOfIncomeTypeRepository sourceOfIncomeTypeRepository;
    private final TaxCategoryRepository taxCategoryRepository;
    private final SalutationTypesRepository salutationTypesRepository;
    /**
     * Retrieves all active nationalities
     *
     */
    @Transactional(readOnly = true)
    public List<NationalityView> getAllNationalities() {
        List<NationalityView> nationalities = nationalityRepository.findByIsDelFalseAndIsActiveTrue();

        if (nationalities.isEmpty()) {
            log.warn("No nationalities found");
            return nationalities;
        }

        log.info("Fetched {} nationalities", nationalities.size());
        return Collections.unmodifiableList(nationalities);
    }

    /**
     * Retrieves all active occupations
     *
     */
    @Transactional(readOnly = true)
    public List<OccupationView> getAllOccupations() {

        List<OccupationView> occupation = occupationRepository.findByIsDelFalseAndIsActiveTrue();
        if (occupation.isEmpty()) {
            log.warn("No occupations found");
            return occupation;
        }

        log.info("Fetched {} occupations", occupation.size());
        return Collections.unmodifiableList(occupation);
    }

    /**
     * Retrieves all active relationships
     *
     */
    @Transactional(readOnly = true)
    public List<RelationshipsView> getAllRelationships() {

        log.info("Fetching relationships from repository");
        List<RelationshipsView> relationships = relationshipsRepository.findByIsDelFalseAndIsActiveTrue();

        if (relationships.isEmpty()) {
            log.warn("No relationships found");
            return relationships;
        }

        log.info("Fetched {} relations", relationships.size());
        return Collections.unmodifiableList(relationships);
    }
    /**
     * Retrieves all active Languages
     *
     */
    @Transactional(readOnly = true)
    public List<LanguagesView> getAllLanguages() {

        log.info("Fetching Languages from repository");
        List<LanguagesView> languages = languagesRepository.findByIsDelFalseAndIsActiveTrue();

        if (languages.isEmpty()) {
            log.warn("No Languages found");
            return languages;
        }

        log.info("Fetched {} Languages", languages.size());
        return Collections.unmodifiableList(languages);
    }


    /**
     * Retrieves all active genders
     *
     */
    @Transactional(readOnly = true)
    public List<GendersView> getAllGenders() {
        List<GendersView> genders = gendersRepository.findByIsDelFalseAndIsActiveTrue();

        if (genders.isEmpty()) {
            log.warn("No genders found");
            return genders;
        }

        log.info("Fetched {} genders", genders.size());
        return Collections.unmodifiableList(genders);
    }

    /**
     * Retrieves all active marital statuses
     *
     */
    @Transactional(readOnly = true)
    public List<MaritalStatusView> getAllMaritalStatus() {
        List<MaritalStatusView> maritalStatus = maritalStatusRepository.findByIsDelFalseAndIsActiveTrue();


        if (maritalStatus.isEmpty()) {
            log.warn("No marital statuses found");
            return maritalStatus;
        }

        log.info("Fetched {} marital statuses", maritalStatus.size());
        return Collections.unmodifiableList(maritalStatus);
    }

    /**
     * Retrieves all active  designation
     *
     */
    @Transactional(readOnly = true)
    public List<DesignationsView> getAllDesignations() {
        List<DesignationsView> designations = designationsRepository.findByIsActiveTrue();
        if (designations.isEmpty()) {
            log.warn("No designations found");
            return designations;
        }
        log.info("Fetched {} designations", designations.size());
        return Collections.unmodifiableList(designations);
    }

    /**
     * Retrieves all active purpose
     *
     */
    @Transactional(readOnly = true)
    public List<PurposeView> getAllPurpose() {
        List<PurposeView> purpose = purposeRepository.findByIsDelFalseAndIsActiveTrue();
        if (purpose.isEmpty()) {
            log.warn("No purpose found");
            return purpose;
        }
        log.info("Fetched {} purpose", purpose.size());
        return Collections.unmodifiableList(purpose);
    }
    /**
     * Retrieves all active source of income
     *
     */
    @Transactional(readOnly = true)
    public List<SourceOfIncomeTypeView> getAllSourceOfIncomeType() {
        List<SourceOfIncomeTypeView> sourceOfIncomeType = sourceOfIncomeTypeRepository.findByIsDelFalseAndIsActiveTrue();
        if (sourceOfIncomeType.isEmpty()) {
            log.warn("No source of income types found");
            return sourceOfIncomeType;
        }
        log.info("Fetched {} source of income type", sourceOfIncomeType.size());
        return Collections.unmodifiableList(sourceOfIncomeType);
    }
    /**
     * Retrieves all active tax categories
     *
     */
    @Transactional(readOnly = true)
    public List<TaxCategoryView> getAllTaxCategories() {
        List<TaxCategoryView> taxCategory = taxCategoryRepository.findByIsDelFalseAndIsActiveTrue();

        if (taxCategory.isEmpty()) {
            log.warn("No active tax found");
            return taxCategory;
        }

        log.info("Fetched {} active tax", taxCategory.size());
        return Collections.unmodifiableList(taxCategory);
    }
    /**
     * Retrieves all active salutation types
     *
     */
    @Transactional(readOnly = true)
    public List<SalutationTypesView> getAllSalutationTypes() {
        List<SalutationTypesView> salutationTypes = salutationTypesRepository.findByIsDelFalseAndIsActiveTrue();


        if (salutationTypes.isEmpty()) {
            log.warn("No salutation types found");
            return salutationTypes;
        }

        log.info("Fetched {} salutation types", salutationTypes.size());
        return Collections.unmodifiableList(salutationTypes);
    }

}
