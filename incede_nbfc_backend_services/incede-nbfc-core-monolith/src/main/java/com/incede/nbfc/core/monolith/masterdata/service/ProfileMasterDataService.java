package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import com.incede.nbfc.core.monolith.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
    private final ReferralSourceRepository referralSourceRepository;
    private  final EducationLevelsRepository educationLevelsRepository;
    private final TenantRepository tenantRepository;

    public Integer getTenantId(UUID tenantIdentity){
        Tenant tenant = tenantRepository.findByIdentity(tenantIdentity).orElseThrow(
                ()-> new BusinessException(CommonConstants.TENANT_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));
        return tenant.getTenantId();

    }


    /**
     * Retrieves all nationalities
     *
     */
    @Transactional(readOnly = true)
    public List<NationalityView> getAllNationalities(UUID tenantIdentity) {
        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all nationalities by tenant Id={}",tenantId);
        List<NationalityView> nationalities = nationalityRepository.findAllByTenantIdOrAll(tenantId);

        if (nationalities.isEmpty()) {
            log.warn("No nationalities found");
            return nationalities;
        }

        log.info("Fetched {} nationalities", nationalities.size());
        return Collections.unmodifiableList(nationalities);
    }

    /**
     * Retrieves all occupations
     *
     */
    @Transactional(readOnly = true)
    public List<OccupationView> getAllOccupations(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all occupations by tenant Id={}",tenantId);
        List<OccupationView> occupation = occupationRepository.findAllByTenantIdOrAll(tenantId);
        if (occupation.isEmpty()) {
            log.warn("No occupations found");
            return occupation;
        }

        log.info("Fetched {} occupations", occupation.size());
        return Collections.unmodifiableList(occupation);
    }

    /**
     * Retrieves all relationships
     *
     */
    @Transactional(readOnly = true)
    public List<RelationshipsView> getAllRelationships(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all relationships by tenant Id={}",tenantId);
        List<RelationshipsView> relationships = relationshipsRepository.findAllByTenantIdOrAll(tenantId);

        if (relationships.isEmpty()) {
            log.warn("No relationships found");
            return relationships;
        }

        log.info("Fetched {} relations", relationships.size());
        return Collections.unmodifiableList(relationships);
    }
    /**
     * Retrieves all Languages
     *
     */
    @Transactional(readOnly = true)
    public List<LanguagesView> getAllLanguages(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all languages by tenant Id={}",tenantId);
        List<LanguagesView> languages = languagesRepository.findAllByTenantIdOrAll(tenantId);

        if (languages.isEmpty()) {
            log.warn("No Languages found");
            return languages;
        }

        log.info("Fetched {} Languages", languages.size());
        return Collections.unmodifiableList(languages);
    }


    /**
     * Retrieves all genders
     *
     */
    @Transactional(readOnly = true)
    public List<GendersView> getAllGenders(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all genders by tenant Id={}",tenantId);
        List<GendersView> genders = gendersRepository.findAllByTenantIdOrAll(tenantId);

        if (genders.isEmpty()) {
            log.warn("No genders found");
            return genders;
        }

        log.info("Fetched {} genders", genders.size());
        return Collections.unmodifiableList(genders);
    }

    /**
     * Retrieves all marital statuses
     *
     */
    @Transactional(readOnly = true)
    public List<MaritalStatusView> getAllMaritalStatus(UUID tenantIdentity) {
        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all marital statuses by tenant Id={}",tenantId);
        List<MaritalStatusView> maritalStatus = maritalStatusRepository.findAllByTenantIdOrAll(tenantId);


        if (maritalStatus.isEmpty()) {
            log.warn("No marital statuses found");
            return maritalStatus;
        }

        log.info("Fetched {} marital statuses", maritalStatus.size());
        return Collections.unmodifiableList(maritalStatus);
    }

    /**
     * Retrieves all designations
     *
     */
    @Transactional(readOnly = true)
    public List<DesignationsView> getAllDesignations(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all designations by tenant Id={}",tenantId);
        List<DesignationsView> designations = designationsRepository.findAllByTenantIdOrAll(tenantId);
        if (designations.isEmpty()) {
            log.warn("No designations found");
            return designations;
        }
        log.info("Fetched {} designations", designations.size());
        return Collections.unmodifiableList(designations);
    }

    /**
     * Retrieves all purposes
     *
     */
    @Transactional(readOnly = true)
    public List<PurposeView> getAllPurpose(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all purpose by tenant Id={}",tenantId);
        List<PurposeView> purpose = purposeRepository.findAllByTenantIdOrAll(tenantId);
        if (purpose.isEmpty()) {
            log.warn("No purpose found");
            return purpose;
        }
        log.info("Fetched {} purpose", purpose.size());
        return Collections.unmodifiableList(purpose);
    }
    /**
     * Retrieves all source of income
     *
     */
    @Transactional(readOnly = true)
    public List<SourceOfIncomeTypeView> getAllSourceOfIncomeType(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all source of income by tenant Id={}",tenantId);
        List<SourceOfIncomeTypeView> sourceOfIncomeType = sourceOfIncomeTypeRepository.findAllByTenantIdOrAll(tenantId);
        if (sourceOfIncomeType.isEmpty()) {
            log.warn("No source of income types found");
            return sourceOfIncomeType;
        }
        log.info("Fetched {} source of income type", sourceOfIncomeType.size());
        return Collections.unmodifiableList(sourceOfIncomeType);
    }
    /**
     * Retrieves all tax categories
     *
     */
    @Transactional(readOnly = true)
    public List<TaxCategoryView> getAllTaxCategories(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all tax categories by tenant Id={}",tenantId);
        List<TaxCategoryView> taxCategory = taxCategoryRepository.findAllByTenantIdOrAll(tenantId);

        if (taxCategory.isEmpty()) {
            log.warn("No active tax found");
            return taxCategory;
        }

        log.info("Fetched {} active tax", taxCategory.size());
        return Collections.unmodifiableList(taxCategory);
    }
    /**
     * Retrieves all salutation types
     *
     */
    @Transactional(readOnly = true)
    public List<SalutationTypesView> getAllSalutationTypes(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all salutation types by tenant Id={}",tenantId);
        List<SalutationTypesView> salutationTypes = salutationTypesRepository.findAllByTenantIdOrAll(tenantId);

        if (salutationTypes.isEmpty()) {
            log.warn("No salutation types found");
            return salutationTypes;
        }

        log.info("Fetched {} salutation types", salutationTypes.size());
        return Collections.unmodifiableList(salutationTypes);
    }

    /**
     * Retrieves all referral sources
     *
     */
    @Transactional(readOnly = true)
    public List<ReferralSourcesView> getAllReferralSources(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all referral sources by tenant Id={}",tenantId);
        List<ReferralSourcesView> referralSource = referralSourceRepository.findAllByTenantIdOrAll(tenantId);

        if (referralSource.isEmpty()) {
            log.warn("No referral source found");
            return referralSource;
        }

        log.info("Fetched {}  referral source", referralSource.size());
        return Collections.unmodifiableList(referralSource);
    }

    /**
     * Retrieves all education levels
     *
     */
    @Transactional(readOnly = true)
    public List<EducationLevelsView> getAllEducationLevels(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all education levels by tenant Id={}",tenantId);
        List<EducationLevelsView> educationLevel = educationLevelsRepository.findAllByTenantIdOrAll(tenantId);

        if (educationLevel.isEmpty()) {
            log.warn("No education level found");
            return educationLevel;
        }

        log.info("Fetched {} education level", educationLevel.size());
        return Collections.unmodifiableList(educationLevel);
    }
}
