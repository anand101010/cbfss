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
public class ReferenceMasterDataService {

    private final AddressTypeRepository addressTypeRepository;
    private final AddressProofTypeRepository addressProofTypeRepository;
    private final ResidentialStatusesRepository residentialStatusesRepository;
    private final ContactTypesRepository contactTypesRepository;

    /**
     * Retrieves all active address types
     *
     */
    @Transactional(readOnly = true)
    public List<AddressTypeView> getAllAddressTypes() {

        List<AddressTypeView> addressType = addressTypeRepository.findByIsDelFalseAndIsActiveTrue();

        if (addressType.isEmpty()) {
            log.warn("No address types found");
            return addressType;
        }
        log.info("Fetched {} address types", addressType.size());
        return Collections.unmodifiableList(addressType);
    }

    /**
     * Retrieves all active address proof Types
     *
     */
    @Transactional(readOnly = true)
    public List<AddressProofTypeView> getAllAddressProofTypes() {

        List<AddressProofTypeView> addressProofType = addressProofTypeRepository.findByIsDelFalseAndIsActiveTrue();

        if (addressProofType.isEmpty()) {
            log.warn("No Address proof types  found");
            return addressProofType;
        }

        log.info("Fetched {} Address proof types", addressProofType.size());
        return Collections.unmodifiableList(addressProofType);

    }

    /**
     * Retrieves all active Residential statuses
     *
     */
    @Transactional(readOnly = true)
    public List<ResidentialStatusesView> getAllResidentialStatuses() {

        List<ResidentialStatusesView> residentialStatuses = residentialStatusesRepository.findByIsActiveTrue();
        if (residentialStatuses.isEmpty()) {
            log.warn("No residential statuses found");
            return residentialStatuses;
        }
        log.info("Fetched {} residential statuses", residentialStatuses.size());
        return Collections.unmodifiableList(residentialStatuses);
    }

    /**
     * Retrieves all active contact Types
     *
     */
    @Transactional(readOnly = true)
    public List<ContactTypesView> getAllContactTypes() {

        log.info("Fetching contact types from repository");
        List<ContactTypesView> contactTypes = contactTypesRepository.findByIsDelFalseAndIsActiveTrue();

        if (contactTypes.isEmpty()) {
            log.warn("No contact types found");
            return contactTypes;
        }
        log.info("Fetched {} contact types", contactTypes.size());
        return Collections.unmodifiableList(contactTypes);
    }

}
