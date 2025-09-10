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
public class BankMasterDataService {


    private final BranchContactsRepository branchContactsRepository;
    private final BranchWeekScheduleRepository branchWeekScheduleRepository;
    private final BanksRepository banksRepository;
    private final BranchesRepository branchesRepository;
    private final AccountStatusesRepository accountStatusesRepository;
    private final AccountTypeMasterRepository accountTypeMasterRepository;
    private final CustomerStatusRepository customerStatusRepository;


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
    public List<BranchesView> getAllBranches() {
        List<BranchesView> branches = branchesRepository.findByIsDelFalse();

        if (branches.isEmpty()) {
            log.warn("No branches found");
            return branches;
        }

        log.info("Fetched {} branches", branches.size());
        return Collections.unmodifiableList(branches);
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

}
