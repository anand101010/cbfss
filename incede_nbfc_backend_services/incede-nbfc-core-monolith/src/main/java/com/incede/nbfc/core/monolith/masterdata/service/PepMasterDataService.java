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
public class PepMasterDataService {

    private final PepCategoriesRepository pepCategoriesRepository;
    private final  PepRelationshipRepository pepRelationshipRepository;
    private final PepVerificationSourceRepository  pepVerificationSourceRepository;
    private final TenantRepository tenantRepository;

    public Integer getTenantId(UUID tenantIdentity){
        Tenant tenant = tenantRepository.findByIdentity(tenantIdentity).orElseThrow(
                ()-> new BusinessException(CommonConstants.TENANT_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));
        return tenant.getTenantId();

    }
    /**
     * Retrieves all pep categories .
     *
     */
    @Transactional(readOnly = true)
    public List<PepCategoriesView> getAllPepCategories(UUID tenantIdentity) {
        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all pep categories by tenant Id={}",tenantId);
        List<PepCategoriesView> pepCategories = pepCategoriesRepository.findAllByTenantIdOrAll(tenantId);
        if (pepCategories.isEmpty()) {
            log.warn("No pep categories found");
            return pepCategories;
        }
        log.info("Fetched {} pep categories", pepCategories.size());
        return Collections.unmodifiableList(pepCategories);
    }


    /**
     * Retrieves all pep relationships .
     *
     */
    @Transactional(readOnly = true)
    public List<PepRelationshipsView> getAllPepRelationships(UUID tenantIdentity) {
        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all pep relationship by tenant Id={}",tenantId);
        List<PepRelationshipsView> pepRelationships = pepRelationshipRepository.findAllByTenantIdOrAll(tenantId);
        if (pepRelationships.isEmpty()) {
            log.warn("No pep relationships found");
            return pepRelationships;
        }
        log.info("Fetched {} pep relationships", pepRelationships.size());
        return Collections.unmodifiableList(pepRelationships);
    }

    /**
     * Retrieves all pep verification source .
     *
     */
    @Transactional(readOnly = true)
    public List<PepVerificationSourceView> getAllPepVerificationSource(UUID tenantIdentity) {
        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all pep verification source by tenant Id={}",tenantId);
        List<PepVerificationSourceView> pepVerificationSource = pepVerificationSourceRepository.findAllByTenantIdOrAll(tenantId);
        if (pepVerificationSource.isEmpty()) {
            log.warn("No pep verification source found");
            return pepVerificationSource;
        }
        log.info("Fetched {} pep verification source", pepVerificationSource.size());
        return Collections.unmodifiableList(pepVerificationSource);
    }
}
