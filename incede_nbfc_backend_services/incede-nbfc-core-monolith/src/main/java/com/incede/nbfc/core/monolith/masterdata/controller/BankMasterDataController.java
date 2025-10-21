package com.incede.nbfc.core.monolith.masterdata.controller;

import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.service.BankMasterDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/master")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Master Data", description = "Master Data Management APIs")

public class BankMasterDataController
{
    /**
     * Get all account-types.
     *
     * @return List of all account-types
     */

    private final BankMasterDataService bankMasterDataService;

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/account-types")
    @Operation(summary = "Get all account types", description = "Retrieves all account types from the system")
    public ResponseEntity<List<AccountTypeMasterView>> getAllAccountTypes(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all account types by tenantIdentity={}",tenantIdentity);
        List<AccountTypeMasterView> accountTypes = bankMasterDataService.getAllAccountTypes(tenantIdentity);
        log.info("Found {} account types ", accountTypes.size());
        return ResponseEntity.ok(accountTypes);
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/branches")
    @Operation(summary = "Get all branches", description = "Retrieves all branches from the system")
    public ResponseEntity<List<BranchesDto>> getAllBranches(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity) {
        log.info("Fetching all branches with tenant Id : {}",tenantIdentity);
        List<BranchesDto> branches = bankMasterDataService.getAllBranches(tenantIdentity);
        log.info("Found {} branches", branches.size());
        return ResponseEntity.ok(branches);
    }

    /**
     * Get all customer-statuses.
     *
     * @return List of all customer-statuses
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/customer-statuses")
    @Operation(summary = "Get all customer statuses", description = "Retrieves all customer statuses from the system")
    public ResponseEntity<List<CustomerStatusView>> getAllCustomerStatuses(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity) {
        log.info("Fetching all customer statuses with tenant Identity = {}",tenantIdentity);
        List<CustomerStatusView> customerStatuses = bankMasterDataService.getAllCustomerStatuses(tenantIdentity);
        log.info("Found {} customer statuses", customerStatuses.size());
        return ResponseEntity.ok(customerStatuses);
    }





    /**
     * Get all Account Statuses .
     *
     * @return List of all Account Statuses
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/account-statuses")
    @Operation(summary = "Get all Account Statuses", description = "Retrieves all Account Statuses from the system")
    public ResponseEntity<List<AccountStatusesView>> getAllAccountStatuses(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all Account Statuses with tenant Identity = {}",tenantIdentity);
        List<AccountStatusesView> accountStatusesView =bankMasterDataService.getAllAccountStatuses(tenantIdentity);
        log.info("Found {} Account Statuses", accountStatusesView.size());
        return ResponseEntity.ok(accountStatusesView);
    }



    /**
     * Get all banks .
     *
     * @return List of all banks
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/banks")
    @Operation(summary = "Get all banks", description = "Retrieves all banks from the system")
    public ResponseEntity<List<BanksView>> getAllBanks(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all banks with tenantId = {}",tenantIdentity);
        List<BanksView> banksView =bankMasterDataService.getAllBanks(tenantIdentity);
        log.info("Found {} banks", banksView.size());
        return ResponseEntity.ok(banksView);
    }

    /**
     * Get all banks .
     *
     * @return List of all banks
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/branch-contact")
    @Operation(summary = "Get all branch contacts", description = "Retrieves all branch contacts from the system")
    public ResponseEntity<List<BranchContactView>> getAllBranchContact(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all branch contacts with tenantId = {}",tenantIdentity);
        List<BranchContactView> branchContactView =bankMasterDataService.getAllBranchContact(tenantIdentity);
        log.info("Found {} branch contacts", branchContactView.size());
        return ResponseEntity.ok(branchContactView);
    }

    /**
     * Get all branch week schedule .
     *
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/branch-week-schedule")
    @Operation(summary = "Get all branch week schedule", description = "Retrieves all branch week schedule from the system")
    public ResponseEntity<List<BranchWeekScheduleView>> getAllBranchWeekSchedule(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all branch week schedule with tenantId = {}",tenantIdentity);
        List<BranchWeekScheduleView> branchWeekScheduleView =bankMasterDataService.getAllBranchWeekSchedule(tenantIdentity);
        log.info("Found {} branch week schedule", branchWeekScheduleView.size());
        return ResponseEntity.ok(branchWeekScheduleView);
    }


    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/ifsc-codes")
    @Operation(summary = "Get IFSC codes with pagination", description = "Retrieves paginated IFSC codes from the system")
    public ResponseEntity<Page<IfscCodesDto>> getAllIfscCodes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Fetching IFSC codes - page: {}, size: {} " , page, size);
        Page<IfscCodesDto> ifscCodes = bankMasterDataService.getAllIfscCodes(PageRequest.of(page, size));
        log.info("Found {} IFSC codes", ifscCodes.getNumberOfElements());

        return ResponseEntity.ok(ifscCodes);
    }


    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/ifsc-codes/{ifscCode}")
    @Operation(summary = "Get IFSC code details", description = "Retrieves details for a specific IFSC code")
    public ResponseEntity<IfscCodesDto> getIfscCodeDetails(
            @PathVariable String ifscCode) {
        log.info("Fetching details for IFSC code: {} ", ifscCode);
        IfscCodesDto dto = bankMasterDataService.getIfscCodeDetails(ifscCode);

        if (dto == null) {
            log.warn("No details found for IFSC code: {}", ifscCode);
            return ResponseEntity.notFound().build();
        }

        log.info("Found details for IFSC code: {}", ifscCode);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/customer-category")
    @Operation(summary = "Get all  customer category", description = "Retrieves all customer category from the system")
    public ResponseEntity<List<CustomerCategoryView>> getAllCustomerCategory(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity){
        log.info("Fetching all customer category with tenantId = {}",tenantIdentity);
        List<CustomerCategoryView> customerCategoryView =bankMasterDataService.getCustomerCategoryView(tenantIdentity);
        log.info("Found {} customer category", customerCategoryView.size());
        return ResponseEntity.ok(customerCategoryView);
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/customer-group")
    @Operation(summary = "Get all customer groups", description = "Retrieves all customer groups from the system")
    public ResponseEntity<List<CustomerGroupMasterView>> getAllCustomerGroups(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity) {
        log.info("Fetching all customer groups with tenantId = {}",tenantIdentity);
        List<CustomerGroupMasterView> customerGroups = bankMasterDataService.getAllCustomerGroups(tenantIdentity);
        log.info("Found {} customer groups", customerGroups.size());
        return ResponseEntity.ok(customerGroups);
    }


    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/risk-category")
    @Operation(summary = "Get all risk categories", description = "Retrieves all active risk categories from the system")
    public ResponseEntity<List<RiskCategoryView>> getAllRiskCategories(@RequestParam(value = "tenantIdentity", required = false) UUID tenantIdentity) {
        log.info("Fetching all risk categories with tenantId = {}",tenantIdentity);
        List<RiskCategoryView> categories = bankMasterDataService.getAllRiskCategories(tenantIdentity);
        log.info("Found {} risk categories", categories.size());
        return ResponseEntity.ok(categories);
    }

}
