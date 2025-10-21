package com.incede.nbfc.core.monolith.masterdata.controller;

import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.service.BankMasterDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BankMasterDataControllerTest {

    @Mock
    private BankMasterDataService bankMasterDataService;

    @InjectMocks
    private BankMasterDataController bankMasterDataController;

    private UUID tenantIdentity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tenantIdentity = UUID.randomUUID();
    }

    @Test
    void testGetAllAccountTypes() {
        AccountTypeMasterView mockAccountType = mock(AccountTypeMasterView.class);
        when(mockAccountType.getAccountType()).thenReturn("Savings");
        when(mockAccountType.getIdentity()).thenReturn(UUID.randomUUID());

        when(bankMasterDataService.getAllAccountTypes(tenantIdentity)).thenReturn(List.of(mockAccountType));

        ResponseEntity<List<AccountTypeMasterView>> response = bankMasterDataController.getAllAccountTypes(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getAccountType()).isEqualTo("Savings");
    }

    @Test
    void testGetAllBranches() {
        BranchesDto mockBranch = new BranchesDto();
        mockBranch.setBranchName("Main Branch");

        when(bankMasterDataService.getAllBranches(tenantIdentity)).thenReturn(List.of(mockBranch));

        ResponseEntity<List<BranchesDto>> response = bankMasterDataController.getAllBranches(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getBranchName()).isEqualTo("Main Branch");
    }

    @Test
    void testGetAllCustomerStatuses() {
        CustomerStatusView mockStatus = mock(CustomerStatusView.class);
        UUID id = UUID.randomUUID();

        when(mockStatus.getStatusName()).thenReturn("Active");
        when(mockStatus.getIsActive()).thenReturn(true);
        when(mockStatus.getIdentity()).thenReturn(id);

        when(bankMasterDataService.getAllCustomerStatuses(tenantIdentity)).thenReturn(List.of(mockStatus));

        ResponseEntity<List<CustomerStatusView>> response = bankMasterDataController.getAllCustomerStatuses(tenantIdentity);

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

        when(mockAccountStatus.getName()).thenReturn("Active");
        when(mockAccountStatus.getCode()).thenReturn("ACT");
        when(mockAccountStatus.getIdentity()).thenReturn(id);

        when(bankMasterDataService.getAllAccountStatuses(tenantIdentity)).thenReturn(List.of(mockAccountStatus));

        ResponseEntity<List<AccountStatusesView>> response = bankMasterDataController.getAllAccountStatuses(tenantIdentity);

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

        when(mockBank.getName()).thenReturn("State Bank");
        when(mockBank.getCode()).thenReturn("SBI");
        when(mockBank.getSwiftBic()).thenReturn("SBININBB");
        when(mockBank.getIdentity()).thenReturn(id);

        when(bankMasterDataService.getAllBanks(tenantIdentity)).thenReturn(List.of(mockBank));

        ResponseEntity<List<BanksView>> response = bankMasterDataController.getAllBanks(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        BanksView result = response.getBody().get(0);
        assertThat(result.getName()).isEqualTo("State Bank");
        assertThat(result.getCode()).isEqualTo("SBI");
        assertThat(result.getSwiftBic()).isEqualTo("SBININBB");
    }

    @Test
    void testGetAllBranchContact() {
        BranchContactView mockContact = mock(BranchContactView.class);
        UUID id = UUID.randomUUID();

        when(mockContact.getValue()).thenReturn("9876543210");
        when(mockContact.getRemarks()).thenReturn("Primary contact");
        when(mockContact.getIdentity()).thenReturn(id);

        when(bankMasterDataService.getAllBranchContact(tenantIdentity)).thenReturn(List.of(mockContact));

        ResponseEntity<List<BranchContactView>> response = bankMasterDataController.getAllBranchContact(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        BranchContactView result = response.getBody().get(0);
        assertThat(result.getValue()).isEqualTo("9876543210");
        assertThat(result.getRemarks()).isEqualTo("Primary contact");
    }

    @Test
    void testGetAllBranchWeekSchedule() {
        BranchWeekScheduleView mockSchedule = mock(BranchWeekScheduleView.class);
        UUID id = UUID.randomUUID();

        when(mockSchedule.getDayOfWeek()).thenReturn((short) 1);
        when(mockSchedule.getIdentity()).thenReturn(id);

        when(bankMasterDataService.getAllBranchWeekSchedule(tenantIdentity)).thenReturn(List.of(mockSchedule));

        ResponseEntity<List<BranchWeekScheduleView>> response = bankMasterDataController.getAllBranchWeekSchedule(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getDayOfWeek()).isEqualTo((short) 1);
    }

    @Test
    void testGetAllCustomerGroups() {
        CustomerGroupMasterView mockView = mock(CustomerGroupMasterView.class);
        when(mockView.getCustomerGroup()).thenReturn("Retail");
        when(mockView.getCode()).thenReturn("RET");

        when(bankMasterDataService.getAllCustomerGroups(tenantIdentity)).thenReturn(List.of(mockView));

        ResponseEntity<List<CustomerGroupMasterView>> response = bankMasterDataController.getAllCustomerGroups(tenantIdentity);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getCustomerGroup()).isEqualTo("Retail");
    }

    @Test
    void testGetAllRiskCategories() {
        RiskCategoryView mockView = mock(RiskCategoryView.class);
        when(mockView.getCategory()).thenReturn("High Risk");
        when(mockView.getCode()).thenReturn("HRISK");

        when(bankMasterDataService.getAllRiskCategories(tenantIdentity)).thenReturn(List.of(mockView));

        ResponseEntity<List<RiskCategoryView>> response = bankMasterDataController.getAllRiskCategories(tenantIdentity);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getCategory()).isEqualTo("High Risk");
    }

    @Test
    void testGetIfscCodeDetails_success() {
        IfscCodesDto mockDto = mock(IfscCodesDto.class);
        when(mockDto.getIfscCode()).thenReturn("SBIN0001234");
        when(mockDto.getBankName()).thenReturn("State Bank of India");

        when(bankMasterDataService.getIfscCodeDetails("SBIN0001234")).thenReturn(mockDto);

        ResponseEntity<IfscCodesDto> response = bankMasterDataController.getIfscCodeDetails("SBIN0001234");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getIfscCode()).isEqualTo("SBIN0001234");
    }

    @Test
    void testGetAllIfscCodes_WithPagination() {
        IfscCodesDto mockDto = new IfscCodesDto();
        mockDto.setIfscCode("SBIN0016400");
        mockDto.setBankName("State Bank of India");

        Page<IfscCodesDto> mockPage = new PageImpl<>(List.of(mockDto), PageRequest.of(0, 10), 1);
        when(bankMasterDataService.getAllIfscCodes(PageRequest.of(0, 10))).thenReturn(mockPage);

        ResponseEntity<Page<IfscCodesDto>> response = bankMasterDataController.getAllIfscCodes(0, 10);

        assertNotNull(response);
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals("SBIN0016400", response.getBody().getContent().get(0).getIfscCode());
    }
}
