package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Cities;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Districts;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Pincodes;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.States;
import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.mapper.CitiesMapper;
import com.incede.nbfc.core.monolith.masterdata.mapper.DistrictMapper;
import com.incede.nbfc.core.monolith.masterdata.mapper.PincodeMapper;
import com.incede.nbfc.core.monolith.masterdata.mapper.StatesMapper;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
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

    private final PincodesRepository pincodeRepository;
    private final StatesRepository statesRepository;
    private final DistrictRepository districtRepository;
    private final CitiesRepository citiesRepository;

    private final PincodeMapper pincodeMapper;
    private final StatesMapper statesMapper;
    private final DistrictMapper districtMapper;
    private final CitiesMapper citiesMapper;
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
     * Retrieves all Pincodes
     *
     */

    public Page<PincodeDto> getAllPincodes(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Pincodes> pincodesPage = pincodesRepository.findByIsDelFalse(pageable);

        if (pincodesPage.isEmpty()) {
            log.warn("No pincodes found");
            return Page.empty();
        }

        Set<Integer> stateIds = pincodesPage.stream()
                .map(Pincodes::getStateId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Integer> districtIds = pincodesPage.stream()
                .map(Pincodes::getDistrictId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Integer> cityIds = pincodesPage.stream()
                .map(Pincodes::getCityId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Integer, States> stateMap = statesRepository.findByStateIdIn(stateIds).stream()
                .collect(Collectors.toMap(States::getStateId, Function.identity()));

        Map<Integer, Districts> districtMap = districtRepository.findBydistrictIdIn(districtIds).stream()
                .collect(Collectors.toMap(Districts::getDistrictId, Function.identity()));

        Map<Integer, Cities> citiesMap = citiesRepository.findByCityIdIn(cityIds).stream()
                .collect(Collectors.toMap(Cities::getCityId, Function.identity()));


        Page<PincodeDto> dtoPage = pincodesPage.map(pincode -> {
            PincodeDto dto = pincodeMapper.convertToDto(pincode);

            if (pincode.getStateId() != null) {
                States state = stateMap.get(pincode.getStateId());
                if (state != null) dto.setStateDto(statesMapper.convertToDto(state));
            }

            if (pincode.getDistrictId() != null) {
                Districts district = districtMap.get(pincode.getDistrictId());
                if (district != null) dto.setDistrictDto(districtMapper.convertToDto(district));
            }

            if (pincode.getCityId() != null) {
                Cities city = citiesMap.get(pincode.getCityId());
                if (city != null) dto.setCitiesDto(citiesMapper.convertToDto(city));
            }

            return dto;
        });

        log.info("Fetched {} pincodes with related entities", dtoPage.getNumberOfElements());
        return dtoPage;
    }


    /**
     * Get list of city, District, State for a Pincode
     */
    @Transactional(readOnly = true)
    public List<PincodeDto> getPincodeDetails(Integer pincode) {
        log.info("Fetching details for pincode {}", pincode);

        List<Pincodes> entities = pincodesRepository.findByDetailsThroughPincode(pincode);

        if (entities.isEmpty()) {
            log.warn("No records found for pincode {}", pincode);
            return Collections.emptyList();
        }

        Set<Integer> stateIds = entities.stream()
                .map(Pincodes::getStateId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Integer> districtIds = entities.stream()
                .map(Pincodes::getDistrictId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Integer> cityIds = entities.stream()
                .map(Pincodes::getCityId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());


        Map<Integer, States> stateMap = statesRepository.findByStateIdIn(stateIds).stream()
                .collect(Collectors.toMap(States::getStateId, Function.identity()));

        Map<Integer, Districts> districtMap = districtRepository.findBydistrictIdIn(districtIds).stream()
                .collect(Collectors.toMap(Districts::getDistrictId, Function.identity()));

        Map<Integer, Cities> citiesMap = citiesRepository.findByCityIdIn(cityIds).stream()
                .collect(Collectors.toMap(Cities::getCityId, Function.identity()));

        List<PincodeDto> pincodeDtos = entities.stream()
                .map(pincodeEntity -> {
                    PincodeDto dto = pincodeMapper.convertToDto(pincodeEntity);

                    if (pincodeEntity.getStateId() != null) {
                        States state = stateMap.get(pincodeEntity.getStateId());
                        if (state != null) dto.setStateDto(statesMapper.convertToDto(state));
                    }

                    if (pincodeEntity.getDistrictId() != null) {
                        Districts district = districtMap.get(pincodeEntity.getDistrictId());
                        if (district != null) dto.setDistrictDto(districtMapper.convertToDto(district));
                    }

                    if (pincodeEntity.getCityId() != null) {
                        Cities city = citiesMap.get(pincodeEntity.getCityId());
                        if (city != null) dto.setCitiesDto(citiesMapper.convertToDto(city));
                    }

                    return dto;
                })
                .toList();

        log.info("Fetched {} pincodes with related entities", entities.size());
        return Collections.unmodifiableList(pincodeDtos);
    }
}
