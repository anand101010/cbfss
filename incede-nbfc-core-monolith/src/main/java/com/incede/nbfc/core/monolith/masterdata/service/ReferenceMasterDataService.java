package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Pincodes;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.PostOffices;
import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.mapper.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReferenceMasterDataService {

    private final AddressTypeRepository addressTypeRepository;
    private final AddressProofTypeRepository addressProofTypeRepository;
    private final ResidentialStatusesRepository residentialStatusesRepository;
    private final ContactTypesRepository contactTypesRepository;
    private final PincodesRepository pincodesRepository;
    private final PostOfficesRepository postOfficesRepository;

    private final StatesRepository statesRepository;
    private final DistrictRepository districtRepository;
    private final CitiesRepository citiesRepository;

    private final PincodeMapper pincodeMapper;
    private final StatesMapper statesMapper;
    private final DistrictMapper districtMapper;
    private final CitiesMapper citiesMapper;
    private final PostOfficeMapper postOfficeMapper;

    /**
     * Retrieves all active address p
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

    /**
     * Get details for a specific pincode including city, state, district, and post office names
     */
    @Transactional(readOnly = true)
    public List<PincodeDto> getPincodeDetails(String pincodeNumber) {
        List<Pincodes> entities = pincodesRepository.findByPincodeWithDetails(pincodeNumber);

        if (entities.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> pincodeIds = entities.stream()
                .map(Pincodes::getPincodeId)
                .toList();

        List<PostOffices> postOffices = postOfficesRepository.findByPincode_PincodeIdIn(pincodeIds);

        Map<Integer, List<String>> postOfficeMap = postOffices.stream()
                .collect(Collectors.groupingBy(
                        po -> po.getPincode().getPincodeId(),
                        Collectors.mapping(PostOffices::getOfficeName, Collectors.toList())
                ));

        return entities.stream()
                .map(pincode -> {
                    PincodeDto dto = pincodeMapper.convertToDto(pincode);
                    dto.setCityName(pincode.getCities().getCity());
                    dto.setStateName(pincode.getStates().getState());
                    dto.setDistrictName(pincode.getDistricts().getDistrict());

                    List<String> officeNames = postOfficeMap.getOrDefault(pincode.getPincodeId(), List.of());
                    dto.setPostOfficeNames(officeNames);

                    return dto;
                })
                .toList();
    }

        /**
         * Retrieves all pincdes
         *
         */
        @Transactional(readOnly = true)
        public Page<PincodeDto> getAllPincodes ( int page, int size){
            Pageable pageable = PageRequest.of(page, size);

            Page<Pincodes> pincodesPage = pincodesRepository.findByIsDelFalse(pageable);

            return pincodesPage.map(pincode -> {
                PincodeDto dto = pincodeMapper.convertToDto(pincode);

                if (pincode.getCities() != null) {
                    dto.setCityName(pincode.getCities().getCity());
                }
                if (pincode.getStates() != null) {
                    dto.setStateName(pincode.getStates().getState());
                }
                if (pincode.getDistricts() != null) {
                    dto.setDistrictName(pincode.getDistricts().getDistrict());
                }
                return dto;
            });
        }

    }
