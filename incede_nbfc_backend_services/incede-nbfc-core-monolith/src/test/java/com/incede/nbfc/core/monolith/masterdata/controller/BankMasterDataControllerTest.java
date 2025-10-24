package com.incede.nbfc.core.monolith.masterdata.controller;

import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.service.BankMasterDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BankMasterDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    @Test
    void testGetAllCustomerGroups() {
        CustomerGroupMasterView mockView = mock(CustomerGroupMasterView.class);
        when(mockView.getCustomerGroup()).thenReturn("Retail");
        when(mockView.getCode()).thenReturn("RET");
        when(mockView.getIdentity()).thenReturn(UUID.randomUUID());

        List<CustomerGroupMasterView> mockList = List.of(mockView);
        when(bankMasterDataService.getAllCustomerGroups()).thenReturn(mockList);

        ResponseEntity<List<CustomerGroupMasterView>> response = bankMasterDataController.getAllCustomerGroups();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getCustomerGroup()).isEqualTo("Retail");
        assertThat(response.getBody().get(0).getCode()).isEqualTo("RET");
    }

    @Test
    void testGetAllRiskCategories() {
        RiskCategoryView mockView = mock(RiskCategoryView.class);
        when(mockView.getCategory()).thenReturn("High Risk");
        when(mockView.getCode()).thenReturn("HRISK");
        when(mockView.getIdentity()).thenReturn(UUID.randomUUID());

        List<RiskCategoryView> mockList = List.of(mockView);
        when(bankMasterDataService.getAllRiskCategories()).thenReturn(mockList);

        ResponseEntity<List<RiskCategoryView>> response = bankMasterDataController.getAllRiskCategories();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getCategory()).isEqualTo("High Risk");
        assertThat(response.getBody().get(0).getCode()).isEqualTo("HRISK");
    }

    @Test
    void testGetIfscCodeDetails_success() {

        IfscCodesDto mockDto = mock(IfscCodesDto.class);
        when(mockDto.getIfscCode()).thenReturn("SBIN0001234");
        when(mockDto.getBankName()).thenReturn("State Bank of India");
        when(mockDto.getBranchName()).thenReturn("MG Road");
        when(mockDto.getPincodes()).thenReturn(682016);
        when(mockDto.getIdentity()).thenReturn(UUID.randomUUID());

        when(bankMasterDataService.getIfscCodeDetails("SBIN0001234")).thenReturn(mockDto);

        ResponseEntity<IfscCodesDto> response = bankMasterDataController.getIfscCodeDetails("SBIN0001234");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getIfscCode()).isEqualTo("SBIN0001234");
        assertThat(response.getBody().getBankName()).isEqualTo("State Bank of India");
        assertThat(response.getBody().getBranchName()).isEqualTo("MG Road");
        assertThat(response.getBody().getPincodes()).isEqualTo(682016);
    }

    @Test
    void testGetAllIfscCodes_WithPagination() {

        IfscCodesDto mockDto = new IfscCodesDto();
        mockDto.setIfscCode("SBIN0016400");
        mockDto.setBankName("State Bank of India");
        mockDto.setBranchName("Main Branch");
        mockDto.setBranchPlace("Kochi");
        mockDto.setPincodes(682031);
        mockDto.setRbiFlag(true);
        mockDto.setIsActive(true);
        mockDto.setIdentity(UUID.randomUUID());

        Page<IfscCodesDto> mockPage = new PageImpl<>(List.of(mockDto), PageRequest.of(0, 10), 1);

        when(bankMasterDataService.getAllIfscCodes(PageRequest.of(0, 10))).thenReturn(mockPage);

        ResponseEntity<Page<IfscCodesDto>> response = bankMasterDataController.getAllIfscCodes(0, 10);

        assertNotNull(response);
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals("SBIN0016400", response.getBody().getContent().get(0).getIfscCode());
        assertEquals("State Bank of India", response.getBody().getContent().get(0).getBankName());
    }


}
