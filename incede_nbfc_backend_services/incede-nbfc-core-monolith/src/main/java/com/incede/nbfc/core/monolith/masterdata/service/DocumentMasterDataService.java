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
public class DocumentMasterDataService {

    private final KycTypesRepository kycTypesRepository;
    private final DocumentMasterRepository documentMasterRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final CanvassedTypesRepository canvassedTypesRepository;
    private final AssetTypesRepository assetTypesRepository;
    private final TenantRepository tenantRepository;


    /**
     * find tenant from Tenant Identity
     *
     */
    public Integer getTenantId(UUID tenantIdentity){
        Tenant tenant = tenantRepository.findByIdentity(tenantIdentity).orElseThrow(
                ()-> new BusinessException(CommonConstants.TENANT_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));
        return tenant.getTenantId();

    }

    /**
     * Retrieves all active Kyc types .
     *
     */
    @Transactional(readOnly = true)
    public List<KycTypesView> getAllKycTypes(UUID tenantIdentity) {
        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all active Kyc types by tenant Id={}",tenantId);


        List<KycTypesView> kycTypes = kycTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId);
        if (kycTypes.isEmpty()) {
            log.warn("No kyc types found");
            return kycTypes;
        }
        log.info("Fetched {} kyc types", kycTypes.size());
        return Collections.unmodifiableList(kycTypes);
    }

    /**
     * Retrieves all active document master .
     *
     */
    @Transactional(readOnly = true)
    public List<DocumentMasterView> getAllDocumentMasters(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all active document master by tenant Id={}",tenantId);

        List<DocumentMasterView> documentMaster = documentMasterRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId);

        if (documentMaster.isEmpty()) {
            log.warn("No document found");
            return documentMaster;
        }

        log.info("Fetched {} document", documentMaster.size());
        return Collections.unmodifiableList(documentMaster);
    }

    /**
     * Retrieves all active document types .
     *
     */
    @Transactional(readOnly = true)
    public List<DocumentTypeView> getAllDocumentType(UUID tenantIdentity) {
        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all active document types  by tenant Id={}",tenantId);
        List<DocumentTypeView> documentType = documentTypeRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId);
        if (documentType.isEmpty()) {
            log.warn("No document types found");
            return documentType;
        }
        log.info("Fetched {} document types", documentType.size());
        return Collections.unmodifiableList(documentType);
    }

    /**
     * Retrieves all active canvassed types .
     *
     */
    @Transactional(readOnly = true)
    public List<CanvassedTypesView> getAllCanvassedTypes(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all active canvassed types  by tenant Id={}",tenantId);
        List<CanvassedTypesView> canvassedTypes = canvassedTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId);
        if (canvassedTypes.isEmpty()) {
            log.warn("No canvassed types found");
            return canvassedTypes;
        }
        log.info("Fetched {} canvassed types", canvassedTypes.size());
        return Collections.unmodifiableList(canvassedTypes);
    }

    /**
     * Retrieves all active asset types .
     *
     */
    @Transactional(readOnly = true)
    public List<AssetTypesView> getAllAssetTypes(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all active asset types by tenant Id={}",tenantId);

        List<AssetTypesView> assetTypes = assetTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId);

        if (assetTypes.isEmpty()) {
            log.warn("No asset type types  found");
            return assetTypes;
        }

        log.info("Fetched {} asset type", assetTypes.size());
        return Collections.unmodifiableList(assetTypes);
    }


}
