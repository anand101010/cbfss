package com.incede.nbfc.core.monolith.masterdata.controller;

import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.service.BankMasterDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class BankMasterDataControllerTest {

    @Mock
    private BankMasterDataService bankMasterDataService;

    @InjectMocks
    private BankMasterDataController bankMasterDataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetAllAccountTypes() {
        AccountTypeMasterView mockAccountType = mock(AccountTypeMasterView.class);
        given(mockAccountType.getAccountType()).willReturn("Savings");
        given(mockAccountType.getIdentity()).willReturn(UUID.randomUUID());
        given(bankMasterDataService.getAllAccountTypes()).willReturn(List.of(mockAccountType));

        ResponseEntity<List<AccountTypeMasterView>> response = bankMasterDataController.getAllAccountTypes();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getAccountType()).isEqualTo("Savings");
    }

    @Test
    void testGetAllBranches() {

        BranchesDto mockBranch = new BranchesDto();
        mockBranch.setBranchName("Main Branch");

        given(bankMasterDataService.getAllBranches()).willReturn(List.of(mockBranch));

        ResponseEntity<List<BranchesDto>> response = bankMasterDataController.getAllBranches();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getBranchName()).isEqualTo("Main Branch");
    }


    @Test
    void testGetAllCustomerStatuses() {
        CustomerStatusView mockStatus = mock(CustomerStatusView.class);
        UUID id = UUID.randomUUID();
        given(mockStatus.getStatusName()).willReturn("Active");
        given(mockStatus.getIsActive()).willReturn(true);
        given(mockStatus.getIdentity()).willReturn(id);

        given(bankMasterDataService.getAllCustomerStatuses()).willReturn(List.of(mockStatus));

        ResponseEntity<List<CustomerStatusView>> response = bankMasterDataController.getAllCustomerStatuses();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        CustomerStatusView result = response.getBody().get(0);
        assertThat(result.getStatusName()).isEqualTo("Active");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllAccountStatuses() {
        AccountStatusesView mockAccountStatus = mock(AccountStatusesView.class);
        UUID id = UUID.randomUUID();
        given(mockAccountStatus.getName()).willReturn("Active");
        given(mockAccountStatus.getCode()).willReturn("ACT");
        given(mockAccountStatus.getIdentity()).willReturn(id);

        given(bankMasterDataService.getAllAccountStatuses()).willReturn(List.of(mockAccountStatus));

        ResponseEntity<List<AccountStatusesView>> response = bankMasterDataController.getAllAccountStatuses();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        AccountStatusesView result = response.getBody().get(0);
        assertThat(result.getName()).isEqualTo("Active");
        assertThat(result.getCode()).isEqualTo("ACT");
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllBanks() {

        BanksView mockBank = mock(BanksView.class);
        UUID id = UUID.randomUUID();

        given(mockBank.getName()).willReturn("State Bank");
        given(mockBank.getCode()).willReturn("SBI");
        given(mockBank.getSwiftBic()).willReturn("SBININBB");
        given(mockBank.getIdentity()).willReturn(id);

        given(bankMasterDataService.getAllBanks()).willReturn(List.of(mockBank));

        ResponseEntity<List<BanksView>> response = bankMasterDataController.getAllBanks();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        BanksView result = response.getBody().get(0);
        assertThat(result.getName()).isEqualTo("State Bank");
        assertThat(result.getCode()).isEqualTo("SBI");
        assertThat(result.getSwiftBic()).isEqualTo("SBININBB");
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllBranchContact() {

        BranchContactView mockContact = mock(BranchContactView.class);
        UUID id = UUID.randomUUID();

        given(mockContact.getValue()).willReturn("9876543210");
        given(mockContact.getRemarks()).willReturn("Primary contact");
        given(mockContact.getIdentity()).willReturn(id);

        given(bankMasterDataService.getAllBranchContact()).willReturn(List.of(mockContact));

        ResponseEntity<List<BranchContactView>> response = bankMasterDataController.getAllBranchContact();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        BranchContactView result = response.getBody().get(0);
        assertThat(result.getValue()).isEqualTo("9876543210");
        assertThat(result.getRemarks()).isEqualTo("Primary contact");
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllBranchWeekSchedule() {

        BranchWeekScheduleView mockSchedule = mock(BranchWeekScheduleView.class);
        UUID id = UUID.randomUUID();

        given(mockSchedule.getDayOfWeek()).willReturn((short) 1);
        given(mockSchedule.getIdentity()).willReturn(id);


        given(bankMasterDataService.getAllBranchWeekSchedule()).willReturn(List.of(mockSchedule));


        ResponseEntity<List<BranchWeekScheduleView>> response = bankMasterDataController.getAllBranchWeekSchedule();


        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        BranchWeekScheduleView result = response.getBody().get(0);
        assertThat(result.getDayOfWeek()).isEqualTo((short) 1);
        assertThat(result.getIdentity()).isEqualTo(id);
    }
}
