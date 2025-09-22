package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankMasterDataServiceTest {

    @Mock
    BranchContactsRepository branchContactsRepository;
    @Mock
    BranchWeekScheduleRepository branchWeekScheduleRepository;
    @Mock
    BanksRepository banksRepository;
    @Mock
    BranchesRepository branchesRepository;
    @Mock
    AccountStatusesRepository accountStatusesRepository;
    @Mock
    AccountTypeMasterRepository accountTypeMasterRepository;
    @Mock
    CustomerStatusRepository customerStatusRepository;

    @InjectMocks
    private BankMasterDataService bankMasterDataService;


    @Test
    void testGetAllBranchContacts_WhenDataExists() {
        UUID id = UUID.randomUUID();

        BranchContactView mockView = mock(BranchContactView.class);
        when(mockView.getValue()).thenReturn("1234567890");
        when(mockView.getRemarks()).thenReturn("Main contact");
        when(mockView.getIdentity()).thenReturn(id);

        when(branchContactsRepository.findByIsDelFalse())
                .thenReturn(List.of(mockView));

        List<BranchContactView> result = bankMasterDataService.getAllBranchContact();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1234567890", result.get(0).getValue());
        assertEquals("Main contact", result.get(0).getRemarks());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllBranchContacts_WhenDataEmpty() {
        when(branchContactsRepository.findByIsDelFalse())
                .thenReturn(Collections.emptyList());

        List<BranchContactView> result = bankMasterDataService.getAllBranchContact();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllBranchContacts_WhenRepositoryThrowsException() {
        when(branchContactsRepository.findByIsDelFalse())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bankMasterDataService.getAllBranchContact());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllBranchWeekSchedules_WhenDataExists() {
        UUID id = UUID.randomUUID();

        BranchWeekScheduleView mockView = mock(BranchWeekScheduleView.class);
        when(mockView.getDayOfWeek()).thenReturn((short) 1);
        when(mockView.getIdentity()).thenReturn(id);

        when(branchWeekScheduleRepository.findByIsDelFalse())
                .thenReturn(List.of(mockView));

        List<BranchWeekScheduleView> result = bankMasterDataService.getAllBranchWeekSchedule();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals((short) 1, result.get(0).getDayOfWeek());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllBranchWeekSchedules_WhenDataEmpty() {
        when(branchWeekScheduleRepository.findByIsDelFalse())
                .thenReturn(Collections.emptyList());

        List<BranchWeekScheduleView> result = bankMasterDataService.getAllBranchWeekSchedule();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllBranchWeekSchedules_WhenRepositoryThrowsException() {
        when(branchWeekScheduleRepository.findByIsDelFalse())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bankMasterDataService.getAllBranchWeekSchedule());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllBanks_WhenDataExists() {
        UUID id = UUID.randomUUID();

        BanksView mockView = mock(BanksView.class);
        when(mockView.getName()).thenReturn("State Bank of India");
        when(mockView.getCode()).thenReturn("SBI");
        when(mockView.getSwiftBic()).thenReturn("SBININBB");
        when(mockView.getIdentity()).thenReturn(id);

        when(banksRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<BanksView> result = bankMasterDataService.getAllBanks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("State Bank of India", result.get(0).getName());
        assertEquals("SBI", result.get(0).getCode());
        assertEquals("SBININBB", result.get(0).getSwiftBic());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllBanks_WhenDataEmpty() {
        when(banksRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<BanksView> result = bankMasterDataService.getAllBanks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllBanks_WhenRepositoryThrowsException() {
        when(banksRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bankMasterDataService.getAllBanks());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllBranches_WhenDataExists() {
        UUID id = UUID.randomUUID();

        Branches mockParentBranch = mock(Branches.class);
        Branches mockLinkServiceMainBranch = mock(Branches.class);
        PostOffices mockPostOffice = mock(PostOffices.class);
        Cities mockCity = mock(Cities.class);
        Districts mockDistrict = mock(Districts.class);
        States mockState = mock(States.class);
        Countries mockCountry = mock(Countries.class);
        BranchTypes mockBranchType = mock(BranchTypes.class);

        // Mock BranchesView with all fields
        BranchesView mockView = mock(BranchesView.class);

        when(mockView.getBranchCode()).thenReturn("BR001");
        when(mockView.getBranchName()).thenReturn("Main Branch");
        when(mockView.getBranchShortName()).thenReturn("MAIN");
        when(mockView.getBranchType()).thenReturn(mockBranchType);
        when(mockView.getStatus()).thenReturn(1);
        when(mockView.getParentBranch()).thenReturn(mockParentBranch);
        when(mockView.getAdminUnitType()).thenReturn("ZONE");
        when(mockView.getParentAdminCode()).thenReturn("P001");
        when(mockView.getLocationCode()).thenReturn("LOC001");
        when(mockView.getOpeningDate()).thenReturn(LocalDate.of(2020, 1, 1));
        when(mockView.getClosingDate()).thenReturn(LocalDate.of(2030, 1, 1));
        when(mockView.getDateOfShift()).thenReturn(LocalDate.of(2022, 5, 10));
        when(mockView.getNumExtensionCounters()).thenReturn(2);
        when(mockView.getIsMainBranchInLocation()).thenReturn(true);
        when(mockView.getLinkServiceMainBranch()).thenReturn(mockLinkServiceMainBranch);
        when(mockView.getNumSplitPremises()).thenReturn((short) 1);
        when(mockView.getLocalClearingMember()).thenReturn(true);
        when(mockView.getNationalClearingMember()).thenReturn(false);
        when(mockView.getHighValueClearingMember()).thenReturn(true);
        when(mockView.getNumOfficersAvailable()).thenReturn((short) 5);
        when(mockView.getMicrCode()).thenReturn("MICR123");
        when(mockView.getIfscCode()).thenReturn("IFSC0001");
        when(mockView.getSwiftBicCode()).thenReturn("SWBIC001");
        when(mockView.getBsrCode()).thenReturn("BSR001");
        when(mockView.getClearingBasedOnMicr()).thenReturn(true);
        when(mockView.getCashMgmtBranch()).thenReturn(false);
        when(mockView.getRtgsDepEnabled()).thenReturn(true);
        when(mockView.getAuthDealForex()).thenReturn(false);
        when(mockView.getAuthForeignCurrencyDeposit()).thenReturn(true);
        when(mockView.getDdIssueAllowed()).thenReturn(true);
        when(mockView.getTtIssueAllowed()).thenReturn(false);
        when(mockView.getBaseCurrencyCode()).thenReturn('I');
        when(mockView.getAuthDealerCode()).thenReturn("AD001");
        when(mockView.getTbaMainKey()).thenReturn("TBA001");
        when(mockView.getRegDirectoryCode()).thenReturn("REG001");
        when(mockView.getDedicatedIssueOperations()).thenReturn("DIO001");
        when(mockView.getDoorNumber()).thenReturn("12A");
        when(mockView.getAddressLine1()).thenReturn("Street 1");
        when(mockView.getAddressLine2()).thenReturn("Street 2");
        when(mockView.getLandmark()).thenReturn("Near Park");
        when(mockView.getPlaceName()).thenReturn("City Center");
        when(mockView.getPostOffice()).thenReturn(mockPostOffice);
        when(mockView.getCity()).thenReturn(mockCity);
        when(mockView.getDistrict()).thenReturn(mockDistrict);
        when(mockView.getState()).thenReturn(mockState);
        when(mockView.getCountry()).thenReturn(mockCountry);
        when(mockView.getPincode()).thenReturn(682001);
        when(mockView.getLatitude()).thenReturn(BigDecimal.valueOf(9.9312));
        when(mockView.getLongitude()).thenReturn(BigDecimal.valueOf(76.2673));
        when(mockView.getTimezone()).thenReturn("Asia/Kolkata");
        when(mockView.getIdentity()).thenReturn(id);

        when(branchesRepository.findByIsDelFalse())
                .thenReturn(List.of(mockView));

        List<BranchesView> result = bankMasterDataService.getAllBranches();

        assertNotNull(result);
        assertEquals(1, result.size());
        BranchesView branch = result.get(0);

        // Verify every field
        assertEquals("BR001", branch.getBranchCode());
        assertEquals("Main Branch", branch.getBranchName());
        assertEquals("MAIN", branch.getBranchShortName());
        assertEquals(mockBranchType, branch.getBranchType());
        assertEquals(1, branch.getStatus());
        assertEquals(mockParentBranch, branch.getParentBranch());
        assertEquals("ZONE", branch.getAdminUnitType());
        assertEquals("P001", branch.getParentAdminCode());
        assertEquals("LOC001", branch.getLocationCode());
        assertEquals(LocalDate.of(2020, 1, 1), branch.getOpeningDate());
        assertEquals(LocalDate.of(2030, 1, 1), branch.getClosingDate());
        assertEquals(LocalDate.of(2022, 5, 10), branch.getDateOfShift());
        assertEquals(2, branch.getNumExtensionCounters());
        assertTrue(branch.getIsMainBranchInLocation());
        assertEquals(mockLinkServiceMainBranch, branch.getLinkServiceMainBranch());
        assertEquals((short) 1, branch.getNumSplitPremises());
        assertTrue(branch.getLocalClearingMember());
        assertFalse(branch.getNationalClearingMember());
        assertTrue(branch.getHighValueClearingMember());
        assertEquals((short) 5, branch.getNumOfficersAvailable());
        assertEquals("MICR123", branch.getMicrCode());
        assertEquals("IFSC0001", branch.getIfscCode());
        assertEquals("SWBIC001", branch.getSwiftBicCode());
        assertEquals("BSR001", branch.getBsrCode());
        assertTrue(branch.getClearingBasedOnMicr());
        assertFalse(branch.getCashMgmtBranch());
        assertTrue(branch.getRtgsDepEnabled());
        assertFalse(branch.getAuthDealForex());
        assertTrue(branch.getAuthForeignCurrencyDeposit());
        assertTrue(branch.getDdIssueAllowed());
        assertFalse(branch.getTtIssueAllowed());
        assertEquals('I', branch.getBaseCurrencyCode());
        assertEquals("AD001", branch.getAuthDealerCode());
        assertEquals("TBA001", branch.getTbaMainKey());
        assertEquals("REG001", branch.getRegDirectoryCode());
        assertEquals("DIO001", branch.getDedicatedIssueOperations());
        assertEquals("12A", branch.getDoorNumber());
        assertEquals("Street 1", branch.getAddressLine1());
        assertEquals("Street 2", branch.getAddressLine2());
        assertEquals("Near Park", branch.getLandmark());
        assertEquals("City Center", branch.getPlaceName());
        assertEquals(mockPostOffice, branch.getPostOffice());
        assertEquals(mockCity, branch.getCity());
        assertEquals(mockDistrict, branch.getDistrict());
        assertEquals(mockState, branch.getState());
        assertEquals(mockCountry, branch.getCountry());
        assertEquals(682001, branch.getPincode());
        assertEquals(BigDecimal.valueOf(9.9312), branch.getLatitude());
        assertEquals(BigDecimal.valueOf(76.2673), branch.getLongitude());
        assertEquals("Asia/Kolkata", branch.getTimezone());
        assertEquals(id, branch.getIdentity());
    }

    @Test
    void testGetAllBranches_WhenDataEmpty() {
        when(branchesRepository.findByIsDelFalse())
                .thenReturn(Collections.emptyList());

        List<BranchesView> result = bankMasterDataService.getAllBranches();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllBranches_WhenRepositoryThrowsException() {
        when(branchesRepository.findByIsDelFalse())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bankMasterDataService.getAllBranches());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllAccountStatuses_WhenDataExists() {
        UUID id = UUID.randomUUID();

        AccountStatusesView mockView = mock(AccountStatusesView.class);
        when(mockView.getName()).thenReturn("Active");
        when(mockView.getCode()).thenReturn("ACT");
        when(mockView.getIdentity()).thenReturn(id);

        when(accountStatusesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<AccountStatusesView> result = bankMasterDataService.getAllAccountStatuses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Active", result.get(0).getName());
        assertEquals("ACT", result.get(0).getCode());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllAccountStatuses_WhenDataEmpty() {
        when(accountStatusesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<AccountStatusesView> result = bankMasterDataService.getAllAccountStatuses();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllAccountStatuses_WhenRepositoryThrowsException() {
        when(accountStatusesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bankMasterDataService.getAllAccountStatuses());

        assertEquals("DB error", ex.getMessage());
    }
    @Test
    void testGetAllAccountTypes_WhenDataExists() {
        UUID id = UUID.randomUUID();
        AccountTypeMasterView mockView = mock(AccountTypeMasterView.class);
        when(mockView.getAccountType()).thenReturn("Savings");
        when(mockView.getIdentity()).thenReturn(id);

        when(accountTypeMasterRepository.findByIsDelFalse())
                .thenReturn(List.of(mockView));

        List<AccountTypeMasterView> result = bankMasterDataService.getAllAccountTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Savings", result.get(0).getAccountType());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllAccountTypes_WhenDataEmpty() {
        when(accountTypeMasterRepository.findByIsDelFalse())
                .thenReturn(Collections.emptyList());

        List<AccountTypeMasterView> result = bankMasterDataService.getAllAccountTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllAccountTypes_WhenRepositoryThrowsException() {
        when(accountTypeMasterRepository.findByIsDelFalse())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bankMasterDataService.getAllAccountTypes());

        assertEquals("DB error", ex.getMessage());
    }
    @Test
    void testGetAllCustomerStatuses_WhenDataExists() {
        UUID id = UUID.randomUUID();

        CustomerStatusView mockView = mock(CustomerStatusView.class);
        when(mockView.getStatusName()).thenReturn("Active");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(customerStatusRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<CustomerStatusView> result = bankMasterDataService.getAllCustomerStatuses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Active", result.get(0).getStatusName());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllCustomerStatuses_WhenDataEmpty() {
        when(customerStatusRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<CustomerStatusView> result = bankMasterDataService.getAllCustomerStatuses();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllCustomerStatuses_WhenRepositoryThrowsException() {
        when(customerStatusRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bankMasterDataService.getAllCustomerStatuses());

        assertEquals("DB error", ex.getMessage());
    }


}
