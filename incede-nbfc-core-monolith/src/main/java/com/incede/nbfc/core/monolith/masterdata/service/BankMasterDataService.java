package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.mapper.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import com.incede.nbfc.core.monolith.masterdata.repository.StatesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankMasterDataService {


    private final BranchContactsRepository branchContactsRepository;
    private final BranchWeekScheduleRepository branchWeekScheduleRepository;
    private final BanksRepository banksRepository;
    private final BranchesRepository branchesRepository;
    private final AccountStatusesRepository accountStatusesRepository;
    private final AccountTypeMasterRepository accountTypeMasterRepository;
    private final CustomerStatusRepository customerStatusRepository;
    private final BranchesMapper branchesMapper;
    private final StatesRepository statesRepository;
    private final StatesMapper statesMapper;
    private final CountryMapper countryMapper;
    private final PincodeMapper pincodeMapper;
    private final BranchTypeRepository branchTypeRepository;
    private final BranchTypeMapper branchTypeMapper;
    private final PostOfficesRepository postOfficesRepository;
    private final PostOfficeMapper postOfficeMapper;
    private final CitiesRepository citiesRepository;
    private final CitiesMapper citiesMapper;
    private final DistrictRepository districtRepository;
    private final DistrictMapper districtMapper;
    private final CustomerCategoryRepository customerCategoryRepository;
    private final CountryRepository countryRepository;
    private final PincodesRepository pincodesRepository;

    /**
     * Retrieves all active branch contact
     *
     */
    @Transactional(readOnly = true)
    public List<BranchContactView> getAllBranchContact() {
        List<BranchContactView> branchContact = branchContactsRepository.findByIsDelFalse();
        if (branchContact.isEmpty()) {
            log.warn("No branch contacts found");
            return branchContact;
        }
        log.info("Fetched {} branch contacts", branchContact.size());
        return Collections.unmodifiableList(branchContact);
    }

    /**
     * Retrieves all active branch week schedule
     *
     */
    @Transactional(readOnly = true)
    public List<BranchWeekScheduleView> getAllBranchWeekSchedule() {
        List<BranchWeekScheduleView> branchWeekSchedule = branchWeekScheduleRepository.findByIsDelFalse();
        if (branchWeekSchedule.isEmpty()) {
            log.warn("No branch week schedules found");
            return branchWeekSchedule;
        }
        log.info("Fetched {} branch week schedules", branchWeekSchedule.size());
        return Collections.unmodifiableList(branchWeekSchedule);
    }

    /**
     * Retrieves all active banks
     *
     */
    @Transactional(readOnly = true)
    public List<BanksView> getAllBanks() {

        List<BanksView> banks = banksRepository.findByIsDelFalseAndIsActiveTrue();
        if (banks.isEmpty()) {
            log.warn("No banks found");
            return banks;
        }
        log.info("Fetched {} banks", banks.size());

        return Collections.unmodifiableList(banks);
    }


    /**
     * Retrieves all active branches
     *
     */
    @Transactional(readOnly = true)
    public List<BranchesDto> getAllBranches() {
        List<Branches> branches = branchesRepository.findAllBranchesByIsDelFalse();

        if (branches.isEmpty()) {
            log.warn("No branches found ");
            return Collections.emptyList();
        }

        /**
         * Retrieves all branches foreign key IDs
         *
         */
        Set<Integer> stateIds = branches.stream()
                .map(Branches::getStateId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Integer> branchTypeIds = branches.stream()
                .map(Branches::getBranchTypeId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Integer> postOfficesIds = branches.stream()
                .map(Branches::getPostOfficeId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Integer> cityIds = branches.stream()
                .map(Branches::getCityId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Integer> districtIds = branches.stream()
                .map(Branches::getDistrictId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Integer> countryIds = branches.stream()
                .map(Branches::getCountryId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        /**
         * Bulk fetch entities from repositories
         *
         */
        Map<Integer, States> stateMap = statesRepository.findByStateIdIn(stateIds).stream()
                .collect(Collectors.toMap(States::getStateId, Function.identity()));

        Map<Integer, BranchTypes> branchTypeMap = branchTypeRepository.findByBranchTypeIdIn(branchTypeIds).stream()
                .collect(Collectors.toMap(BranchTypes::getBranchTypeId, Function.identity()));

        Map<Integer, PostOffices> postOfficesMap = postOfficesRepository.findByPostOfficeIdIn(postOfficesIds).stream()
                .collect(Collectors.toMap(PostOffices::getPostOfficeId, Function.identity()));

        Map<Integer, Cities> citiesMap = citiesRepository.findByCityIdIn(postOfficesIds).stream()
                .collect(Collectors.toMap(Cities::getCityId, Function.identity()));

        Map<Integer, Districts> districtMap = districtRepository.findBydistrictIdIn(districtIds).stream()
                .collect(Collectors.toMap(Districts::getDistrictId, Function.identity()));

        Map<Integer, Countries> countryMap = countryRepository.findByCountryIdIn(countryIds).stream()
                .collect(Collectors.toMap(Countries::getCountryId, Function.identity()));

        /**
         * Convert to branches DTOs
         *
         */
        List<BranchesDto> branchDtos = branches.stream()
                .map(branch -> {
                    BranchesDto dto = branchesMapper.convertToDto(branch);

                    if (branch.getStateId() != null) {
                        States states = stateMap.get(branch.getStateId());
                        if (states != null) {
                            dto.setStateDto(statesMapper.convertToDto(states));
                        }
                    }

                    if(branch.getBranchTypeId() != null){
                        BranchTypes branchTypes = branchTypeMap.get(branch.getBranchTypeId());
                        if(branchTypes!=null){
                            dto.setBranchTypeDto(branchTypeMapper.convertToDto(branchTypes));
                        }
                    }

                    if(branch.getPostOfficeId() != null){
                        PostOffices postOffices = postOfficesMap.get(branch.getPostOfficeId());
                        if(postOffices!=null){
                            dto.setPostOfficesDto(postOfficeMapper.convertToDto(postOffices));
                        }
                    }
                    if(branch.getCityId() != null){
                        Cities cties = citiesMap.get(branch.getCityId());
                        if(cties!=null){
                            dto.setCitiesDto(citiesMapper.convertToDto(cties));
                        }
                    }

                    if(branch.getDistrictId() != null){
                        Districts districts = districtMap.get(branch.getDistrictId());
                        if(districts!=null){
                            dto.setDistrictDto(districtMapper.convertToDto(districts));
                        }
                    }

                    if(branch.getCountryId() != null){
                        Countries countries = countryMap.get(branch.getCountryId());
                        if(countries != null){
                            dto.setCountryDto(countryMapper.convertToDto(countries));
                        }
                    }


                    return dto;
                })
                .toList();

        log.info("Fetched {} branches with related entities", branches.size());
        return Collections.unmodifiableList(branchDtos);
    }

    /**
     * Retrieves all active account Types
     *
     */
    @Transactional(readOnly = true)
    public List<AccountTypeMasterView> getAllAccountTypes() {

        log.info("Fetching account types from repository");
        List<AccountTypeMasterView> accountTypeMaster = accountTypeMasterRepository.findByIsDelFalse();
        if (accountTypeMaster.isEmpty()) {
            log.warn("No account types found");
            return accountTypeMaster;
        }
        log.info("Fetched {} account types", accountTypeMaster.size());
        return Collections.unmodifiableList(accountTypeMaster);
    }

    /**
     * Retrieves all active Account statuses.
     *
     * @return List of Account statuses
     */
    @Transactional(readOnly = true)
    public List<AccountStatusesView> getAllAccountStatuses() {

        List<AccountStatusesView> accountStatuses = accountStatusesRepository.findByIsDelFalseAndIsActiveTrue();

        if (accountStatuses.isEmpty()) {
            log.warn("No Account statuses found");
            return accountStatuses;
        }

        log.info("Fetched {} Account statuses", accountStatuses.size());
        return Collections.unmodifiableList(accountStatuses);
    }

    /**
     * Retrieves all active customer statuses
     *
     */
    @Transactional(readOnly = true)
    public List<CustomerStatusView> getAllCustomerStatuses() {

        List<CustomerStatusView> customerStatuses = customerStatusRepository.findByIsDelFalseAndIsActiveTrue();

        if (customerStatuses.isEmpty()) {
            log.warn("No customer statuses found");
            return customerStatuses;
        }

        log.info("Fetched {} customer statuses", customerStatuses.size());
        return Collections.unmodifiableList(customerStatuses);
    }

    @Transactional(readOnly = true)
    public List<CustomerCategoryView> getCustomerCategoryView() {
        List<CustomerCategoryView> customerCategory = customerCategoryRepository.findByIsDelFalseAndIsActiveTrue();

        if (customerCategory.isEmpty()) {
            log.warn("No customer category found");
            return customerCategory;
        }

        log.info("Fetched {} customer category", customerCategory.size());
        return Collections.unmodifiableList(customerCategory);
    }
}
