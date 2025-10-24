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
public class LeadMasterDataService {

    private final AdditionalReferenceConfigRepository additionalReferenceConfigRepository;
    private final LeadSourceRepository leadSourceRepository;
    private final LeadStageRepository leadStageRepository;
    private final FollowUpTypeRepository followUpTypeRepository;
    private final LeadStatusRepository leadStatusRepository;
    private final ProductServiceRepository productServiceRepository;

    /**
     * Retrieves all active and non-deleted additional reference configurations.
     *
     * @return an unmodifiable list of {@link AdditionalReferenceConfigView}; empty if none found
     */
    @Transactional(readOnly = true)
    public List<AdditionalReferenceConfigView> getAllAdditionalReferenceConfigs() {
        List<AdditionalReferenceConfigView> configs = additionalReferenceConfigRepository.findByIsDelFalseAndIsActiveTrue();
        if (configs.isEmpty()) {
            log.warn("No additional reference configs found");
            return configs;
        }
        log.info("Fetched {} additional reference configs", configs.size());
        return Collections.unmodifiableList(configs);
    }

    /**
     * Retrieves all active and non-deleted lead sources.
     *
     * @return an unmodifiable list of {@link LeadSourceView}; empty if none found
     */
    @Transactional(readOnly = true)
    public List<LeadSourceView> getAllLeadSources() {
        List<LeadSourceView> sources = leadSourceRepository.findByIsDelFalseAndIsActiveTrue();
        if (sources.isEmpty()) {
            log.warn("No lead sources found");
            return sources;
        }
        log.info("Fetched {} lead sources", sources.size());
        return Collections.unmodifiableList(sources);
    }

    /**
     * Retrieves all active and non-deleted lead stages.
     *
     * @return an unmodifiable list of {@link LeadStageView}; empty if none found
     */
    @Transactional(readOnly = true)
    public List<LeadStageView> getAllLeadStages() {
        List<LeadStageView> stages = leadStageRepository.findByIsDelFalseAndIsActiveTrue();
        if (stages.isEmpty()) {
            log.warn("No lead stages found");
            return stages;
        }
        log.info("Fetched {} lead stages", stages.size());
        return Collections.unmodifiableList(stages);
    }

    /**
     * Retrieves all active and non-deleted follow-up types.
     *
     * @return an unmodifiable list of {@link FollowUpTypeView}; empty if none found
     */
    @Transactional(readOnly = true)
    public List<FollowUpTypeView> getAllFollowUpTypes() {
        List<FollowUpTypeView> types = followUpTypeRepository.findByIsDelFalseAndIsActiveTrue();
        if (types.isEmpty()) {
            log.warn("No follow-up types found");
            return types;
        }
        log.info("Fetched {} follow-up types", types.size());
        return Collections.unmodifiableList(types);
    }

    /**
     * Retrieves all active and non-deleted lead statuses.
     *
     * @return an unmodifiable list of {@link LeadStatusView}; empty if none found
     */
    @Transactional(readOnly = true)
    public List<LeadStatusView> getAllLeadStatuses() {
        List<LeadStatusView> statuses = leadStatusRepository.findByIsDelFalseAndIsActiveTrue();
        if (statuses.isEmpty()) {
            log.warn("No lead statuses found");
            return statuses;
        }
        log.info("Fetched {} lead statuses", statuses.size());
        return Collections.unmodifiableList(statuses);
    }

    /**
     * Retrieves all active and non-deleted product services.
     *
     * @return an unmodifiable list of {@link ProductServiceView}; empty if none found
     */
    @Transactional(readOnly = true)
    public List<ProductServiceView> getAllProductServices() {

        List<ProductServiceView> statuses = productServiceRepository.findByIsDelFalseAndIsActiveTrue();
        if (statuses.isEmpty()) {
            log.warn("No product service found");
            return statuses;
        }
        log.info("Fetched {} product service", statuses.size());
        return Collections.unmodifiableList(statuses);

    }
}
