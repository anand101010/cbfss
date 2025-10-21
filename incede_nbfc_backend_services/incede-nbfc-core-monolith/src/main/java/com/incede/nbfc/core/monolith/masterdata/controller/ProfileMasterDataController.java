package com.incede.nbfc.core.monolith.masterdata.controller;

import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.service.ProfileMasterDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/master")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Master Data", description = "Master Data Management APIs")
public class ProfileMasterDataController
{
    private final ProfileMasterDataService profileMasterDataService;
    /**
     * Get all salutation-types.
     *
     * @return List of all salutation-types
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/salutation-types")
    @Operation(summary = "Get all salutation types", description = "Retrieves all salutation types from the system")
    public ResponseEntity<List<SalutationTypesView>> getAllSalutationTypes(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity) {
        log.info("Fetching all salutation types by tenantIdentity={}\",tenantIdentity");
        List<SalutationTypesView> salutationTypes = profileMasterDataService.getAllSalutationTypes(tenantIdentity);
        log.info("Found {} salutation types", salutationTypes.size());
        return ResponseEntity.ok(salutationTypes);
    }
    /**
     * Get all customer-statuses.
     *
     * @return List of all customer-statuses
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/nationalities")
    @Operation(summary = "Get all natinalities", description = "Retrieves all natinalities from the system")
    public ResponseEntity<List<NationalityView>> getAllNationalities(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all nationalities by tenantIdentity={}\",tenantIdentity");
        List<NationalityView> nationalities = profileMasterDataService.getAllNationalities(tenantIdentity);
        log.info("Found {} nationalities", nationalities.size());
        return ResponseEntity.ok(nationalities);
    }


    /**
     * Get all occupations.
     *
     * @return List of all occupations
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/occupations")
    @Operation(summary = "Get all occupations", description = "Retrieves all occupations from the system")
    public ResponseEntity<List<OccupationView>> getAllOccupations(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all occupations by tenantIdentity={}\",tenantIdentity");
        List<OccupationView> occupations = profileMasterDataService.getAllOccupations(tenantIdentity);
        log.info("Found {} occupations", occupations.size());
        return ResponseEntity.ok(occupations);
    }


    /**
     * Get all relationships.
     *
     * @return List of all relationships
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/relationships")
    @Operation(summary = "Get all relationships", description = "Retrieves all relationships from the system")
    public ResponseEntity<List<RelationshipsView>> getAllRelationships(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity) {
        log.info("Fetching all relationships by tenantIdentity={}\",tenantIdentity");
        List<RelationshipsView> relationships = profileMasterDataService.getAllRelationships(tenantIdentity);
        log.info("Found {} relationships", relationships.size());
        return ResponseEntity.ok(relationships);
    }

    /**
     * Get all languages.
     *
     * @return List of all languages
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/languages")
    @Operation(summary = "Get all languages", description = "Retrieves all languages from the system")
    public ResponseEntity<List<LanguagesView>> getAllLanguages(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity) {
        log.info("Fetching all languagesby tenantIdentity={}\",tenantIdentity");
        List<LanguagesView> languages = profileMasterDataService.getAllLanguages(tenantIdentity);
        log.info("Found {} languages", languages.size());
        return ResponseEntity.ok(languages);
    }


    /**
     * Get all genders.
     *
     * @return List of all genders
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/genders")
    @Operation(summary = "Get all genders", description = "Retrieves all genders from the system")
    public ResponseEntity<List<GendersView>> getAllGenders(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all genders by tenantIdentity={}\",tenantIdentity");
        List<GendersView> genders = profileMasterDataService.getAllGenders(tenantIdentity);
        log.info("Found {} genders", genders.size());
        return ResponseEntity.ok(genders);
    }

    /**
     * Get all marital-status.
     *
     * @return List of all marital-status
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/marital-status")
    @Operation(summary = "Get all marital status", description = "Retrieves all marital status from the system")
    public ResponseEntity<List<MaritalStatusView>> getAllMaritalStatus(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all marital status by tenantIdentity={}\",tenantIdentity");
        List<MaritalStatusView> maritalStatus = profileMasterDataService.getAllMaritalStatus(tenantIdentity);
        log.info("Found {} marital status", maritalStatus.size());
        return ResponseEntity.ok(maritalStatus);
    }
    /**
     * Get all Designations   .
     *
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/designations")
    @Operation(summary = "Get all designations", description = "Retrieves all designations from the system")
    public ResponseEntity<List<DesignationsView>> getAllDesignations(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all designations by tenantIdentity={}\",tenantIdentity");
        List<DesignationsView> designationsView =profileMasterDataService.getAllDesignations(tenantIdentity);
        log.info("Found {} designations", designationsView.size());
        return ResponseEntity.ok(designationsView);
    }

    /**
     * Get all tax-category.
     *
     * @return List of all tax-category
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/tax-category")
    @Operation(summary = "Get all tax category", description = "Retrieves all tax category from the system")
    public ResponseEntity<List<TaxCategoryView>> getAllTaxCategories(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all tax categories by tenantIdentity={}\",tenantIdentity");
        List<TaxCategoryView> taxCategories = profileMasterDataService.getAllTaxCategories(tenantIdentity);
        log.info("Found {} tax categories", taxCategories.size());
        return ResponseEntity.ok(taxCategories);
    }

    /**
     * Get all Purpose .
     *
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/purpose")
    @Operation(summary = "Get all purpose", description = "Retrieves all purpose from the system")
    public ResponseEntity<List<PurposeView>> getAllPurpose(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all purpose by tenantIdentity={}\",tenantIdentity");
        List<PurposeView> purposeView =profileMasterDataService.getAllPurpose(tenantIdentity);
        log.info("Found {} purpose", purposeView.size());
        return ResponseEntity.ok(purposeView);
    }

    /**
     * Get all Source Of Income Type .
     *
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/source-of-income")
    @Operation(summary = "Get all Source Of Income Type", description = "Retrieves all Source Of Income Type from the system")
    public ResponseEntity<List<SourceOfIncomeTypeView>> getAllSourceOfIncomeType(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all Source Of Income Type by tenantIdentity={}\",tenantIdentity");
        List<SourceOfIncomeTypeView> sourceOfIncomeTypeView =profileMasterDataService.getAllSourceOfIncomeType(tenantIdentity);
        log.info("Found {} Source Of Income Type", sourceOfIncomeTypeView.size());
        return ResponseEntity.ok(sourceOfIncomeTypeView);
    }
    /**
     * Get all referral sources.
     *
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/referral_source")
    @Operation(summary = "Get all referral source", description = "Retrieves all referral source from the system")
    public ResponseEntity<List<ReferralSourcesView>> getAllReferralSource(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all referral source by tenantIdentity={}\",tenantIdentity");
        List<ReferralSourcesView> referralSourcesView =profileMasterDataService.getAllReferralSources(tenantIdentity);
        log.info("Found {} referral source", referralSourcesView.size());
        return ResponseEntity.ok(referralSourcesView);
    }
    /**
     * Get all educational level.
     *
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/education-level")
    @Operation(summary = "Get all education level", description = "Retrieves all education level from the system")
    public ResponseEntity<List<EducationLevelsView>> getAllEducationLevels(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all education level by tenantIdentity={}\",tenantIdentity");
        List<EducationLevelsView> educationLevelsView =profileMasterDataService.getAllEducationLevels(tenantIdentity);
        log.info("Found {}  education level", educationLevelsView.size());
        return ResponseEntity.ok(educationLevelsView);
    }



}
