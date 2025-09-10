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
public class DocumentMasterDataService {

    private final KycTypesRepository kycTypesRepository;
    private final DocumentMasterRepository documentMasterRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final CanvassedTypesRepository canvassedTypesRepository;
    private final AssetTypesRepository assetTypesRepository;


    /**
     * Retrieves all active Kyc types .
     *
     */
    @Transactional(readOnly = true)
    public List<KycTypesView> getAllKycTypes() {
        List<KycTypesView> kycTypes = kycTypesRepository.findByIsDelFalseAndIsActiveTrue();
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
    public List<DocumentMasterView> getAllDocumentMasters() {
        List<DocumentMasterView> documentMaster = documentMasterRepository.findByIsDelFalseAndIsActiveTrue();

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
    public List<DocumentTypeView> getAllDocumentType() {
        List<DocumentTypeView> documentType = documentTypeRepository.findByIsDelFalseAndIsActiveTrue();
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
    public List<CanvassedTypesView> getAllCanvassedTypes() {
        List<CanvassedTypesView> canvassedTypes = canvassedTypesRepository.findByIsDelFalseAndIsActiveTrue();
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
    public List<AssetTypesView> getAllAssetTypes() {

        List<AssetTypesView> assetTypes = assetTypesRepository.findByIsDelFalseAndIsActiveTrue();

        if (assetTypes.isEmpty()) {
            log.warn("No asset type types  found");
            return assetTypes;
        }

        log.info("Fetched {} asset type", assetTypes.size());
        return Collections.unmodifiableList(assetTypes);
    }


}
