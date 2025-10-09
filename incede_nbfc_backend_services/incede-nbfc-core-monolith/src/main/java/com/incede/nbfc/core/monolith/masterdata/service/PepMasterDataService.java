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
public class PepMasterDataService {

    private final PepCategoriesRepository pepCategoriesRepository;
    private final  PepRelationshipRepository pepRelationshipRepository;
    private final PepVerificationSourceRepository  pepVerificationSourceRepository;


    /**
     * Retrieves all active pep categories .
     *
     */
    @Transactional(readOnly = true)
    public List<PepCategoriesView> getAllPepCategories() {
        List<PepCategoriesView> pepCategories = pepCategoriesRepository.findByIsActiveTrue();
        if (pepCategories.isEmpty()) {
            log.warn("No pep categories found");
            return pepCategories;
        }
        log.info("Fetched {} pep categories", pepCategories.size());
        return Collections.unmodifiableList(pepCategories);
    }


    /**
     * Retrieves all active pep relationships .
     *
     */
    @Transactional(readOnly = true)
    public List<PepRelationshipsView> getAllPepRelationships() {
        List<PepRelationshipsView> pepRelationships = pepRelationshipRepository.findByIsActiveTrue();
        if (pepRelationships.isEmpty()) {
            log.warn("No pep relationships found");
            return pepRelationships;
        }
        log.info("Fetched {} pep relationships", pepRelationships.size());
        return Collections.unmodifiableList(pepRelationships);
    }

    /**
     * Retrieves all active pep verification source .
     *
     */
    @Transactional(readOnly = true)
    public List<PepVerificationSourceView> getAllPepVerificationSource() {
        List<PepVerificationSourceView> pepVerificationSource = pepVerificationSourceRepository.findByIsActiveTrue();
        if (pepVerificationSource.isEmpty()) {
            log.warn("No pep verification source found");
            return pepVerificationSource;
        }
        log.info("Fetched {} pep verification source", pepVerificationSource.size());
        return Collections.unmodifiableList(pepVerificationSource);
    }
}
