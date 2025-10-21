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
public class LeadMasterDataService {

    private final AdditionalReferenceConfigRepository additionalReferenceConfigRepository;
    private final LeadSourceRepository leadSourceRepository;
    private final LeadStageRepository leadStageRepository;
    private final FollowUpTypeRepository followUpTypeRepository;
    private final LeadStatusRepository leadStatusRepository;
    private final ProductServiceRepository productServiceRepository;
    private final TenantRepository tenantRepository;


    public Integer getTenantId(UUID tenantIdentity){
        Tenant tenant = tenantRepository.findByIdentity(tenantIdentity).orElseThrow(
                ()-> new BusinessException(CommonConstants.TENANT_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));
        return tenant.getTenantId();

    }

    /**
     * Retrieves all additional reference configurations.
     *
     * @return an unmodifiable list of {@link AdditionalReferenceConfigView}; empty if none found
     */
    @Transactional(readOnly = true)
    public List<AdditionalReferenceConfigView> getAllAdditionalReferenceConfigs(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all additional reference config by tenant Id={}",tenantId);
        List<AdditionalReferenceConfigView> configs = additionalReferenceConfigRepository.findAllByTenantIdOrAll(tenantId);
        if (configs.isEmpty()) {
            log.warn("No additional reference configs found");
            return configs;
        }
        log.info("Fetched {} additional reference configs", configs.size());
        return Collections.unmodifiableList(configs);
    }

    /**
     * Retrieves all lead sources.
     *
     * @return an unmodifiable list of {@link LeadSourceView}; empty if none found
     */
    @Transactional(readOnly = true)
    public List<LeadSourceView> getAllLeadSources(UUID tenantIdentity) {
        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all Lead sources by tenant Id={}",tenantId);
        List<LeadSourceView> sources = leadSourceRepository.findAllByTenantIdOrAll(tenantId);
        if (sources.isEmpty()) {
            log.warn("No lead sources found");
            return sources;
        }
        log.info("Fetched {} lead sources", sources.size());
        return Collections.unmodifiableList(sources);
    }

    /**
     * Retrieves all lead stages.
     *
     * @return an unmodifiable list of {@link LeadStageView}; empty if none found
     */
    @Transactional(readOnly = true)
    public List<LeadStageView> getAllLeadStages(UUID tenantIdentity) {
        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all Lead stages by tenant Id={}",tenantId);
        List<LeadStageView> stages = leadStageRepository.findAllByTenantIdOrAll(tenantId);
        if (stages.isEmpty()) {
            log.warn("No lead stages found");
            return stages;
        }
        log.info("Fetched {} lead stages", stages.size());
        return Collections.unmodifiableList(stages);
    }

    /**
     * Retrieves all follow-up types.
     *
     * @return an unmodifiable list of {@link FollowUpTypeView}; empty if none found
     */
    @Transactional(readOnly = true)
    public List<FollowUpTypeView> getAllFollowUpTypes(UUID tenantIdentity) {
        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all follow up type by tenant Id={}",tenantId);
        List<FollowUpTypeView> types = followUpTypeRepository.findAllByTenantIdOrAll(tenantId);
        if (types.isEmpty()) {
            log.warn("No follow-up types found");
            return types;
        }
        log.info("Fetched {} follow-up types", types.size());
        return Collections.unmodifiableList(types);
    }
    /**
     * Retrieves all lead statuses.
     *
     * @return an unmodifiable list of {@link LeadStatusView}; empty if none found
     */
    @Transactional(readOnly = true)
    public List<LeadStatusView> getAllLeadStatuses(UUID tenantIdentity) {
        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all Lead statuses by tenant Id={}",tenantId);
        List<LeadStatusView> statuses = leadStatusRepository.findAllByTenantIdOrAll(tenantId);
        if (statuses.isEmpty()) {
            log.warn("No lead statuses found");
            return statuses;
        }
        log.info("Fetched {} lead statuses", statuses.size());
        return Collections.unmodifiableList(statuses);
    }

    /**
     * Retrieves all product services.
     *
     * @return an unmodifiable list of {@link ProductServiceView}; empty if none found
     */
    @Transactional(readOnly = true)
    public List<ProductServiceView> getAllProductServices(UUID tenantIdentity) {
        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all product and services by tenant Id={}",tenantId);
        List<ProductServiceView> statuses = productServiceRepository.findAllByTenantIdOrAll(tenantId);
        if (statuses.isEmpty()) {
            log.warn("No product service found");
            return statuses;
        }
        log.info("Fetched {} product service", statuses.size());
        return Collections.unmodifiableList(statuses);

    }
}
