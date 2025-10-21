package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.mapper.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import com.incede.nbfc.core.monolith.tenant.repository.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BankMasterDataServiceTest {

    @Mock private BranchContactsRepository branchContactsRepository;
    @Mock private BranchWeekScheduleRepository branchWeekScheduleRepository;
    @Mock private BanksRepository banksRepository;
    @Mock private BranchesRepository branchesRepository;
    @Mock private AccountStatusesRepository accountStatusesRepository;
    @Mock private AccountTypeMasterRepository accountTypeMasterRepository;
    @Mock private CustomerStatusRepository customerStatusRepository;
    @Mock private BranchesMapper branchesMapper;
    @Mock private StatesRepository statesRepository;
    @Mock private StatesMapper statesMapper;
    @Mock private CountryMapper countryMapper;
    @Mock private BranchTypeRepository branchTypeRepository;
    @Mock private BranchTypeMapper branchTypeMapper;
    @Mock private PostOfficesRepository postOfficesRepository;
    @Mock private PostOfficeMapper postOfficeMapper;
    @Mock private CitiesRepository citiesRepository;
    @Mock private CitiesMapper citiesMapper;
    @Mock private DistrictRepository districtRepository;
    @Mock private DistrictMapper districtMapper;
    @Mock private IfscCodesRepository ifscCodesRepository;
    @Mock private IfscCodeMapper ifscCodeMapper;
    @Mock private CustomerCategoryRepository customerCategoryRepository;
    @Mock private CustomerGroupMasterRepository customerGroupMasterRepository;
    @Mock private RiskCategoryRepository riskCategoryRepository;
    @Mock private CountryRepository countryRepository;
    @Mock private PincodesRepository pincodesRepository;
    @Mock private TenantRepository tenantRepository;

    @InjectMocks private BankMasterDataService bankMasterDataService;

    private UUID tenantIdentity;
    private Integer tenantId;

    @BeforeEach
    void setUp() {
        tenantIdentity = UUID.randomUUID();
        tenantId = 1;

        Tenant tenant = new Tenant();
        tenant.setTenantId(tenantId);
        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.of(tenant));
    }

    // ------------------- Branch Contact Tests -------------------
    @Test
    void testGetAllBranchContact_WithData() {
        BranchContactView contact = mock(BranchContactView.class);
        when(branchContactsRepository.findByIsDelFalseByTenataId(tenantId)).thenReturn(List.of(contact));

        List<BranchContactView> result = bankMasterDataService.getAllBranchContact(tenantIdentity);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllBranchContact_Empty() {
        when(branchContactsRepository.findByIsDelFalseByTenataId(tenantId)).thenReturn(Collections.emptyList());

        List<BranchContactView> result = bankMasterDataService.getAllBranchContact(tenantIdentity);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllBranchContact_ThrowsException() {
        when(branchContactsRepository.findByIsDelFalseByTenataId(tenantId))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bankMasterDataService.getAllBranchContact(tenantIdentity));
        assertEquals("DB error", ex.getMessage());
    }

    // ------------------- Branch Week Schedule Tests -------------------
    @Test
    void testGetAllBranchWeekSchedule_WithData() {
        BranchWeekScheduleView schedule = mock(BranchWeekScheduleView.class);
        when(branchWeekScheduleRepository.findByIsDelFalseByTenantId(tenantId)).thenReturn(List.of(schedule));

        List<BranchWeekScheduleView> result = bankMasterDataService.getAllBranchWeekSchedule(tenantIdentity);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllBranchWeekSchedule_Empty() {
        when(branchWeekScheduleRepository.findByIsDelFalseByTenantId(tenantId)).thenReturn(Collections.emptyList());

        List<BranchWeekScheduleView> result = bankMasterDataService.getAllBranchWeekSchedule(tenantIdentity);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllBranchWeekSchedule_ThrowsException() {
        when(branchWeekScheduleRepository.findByIsDelFalseByTenantId(tenantId))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bankMasterDataService.getAllBranchWeekSchedule(tenantIdentity));
        assertEquals("DB error", ex.getMessage());
    }

    // ------------------- Banks Tests -------------------
    @Test
    void testGetAllBanks_WithData() {
        BanksView bank = mock(BanksView.class);
        when(banksRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId)).thenReturn(List.of(bank));

        List<BanksView> result = bankMasterDataService.getAllBanks(tenantIdentity);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllBanks_Empty() {
        when(banksRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId)).thenReturn(Collections.emptyList());

        List<BanksView> result = bankMasterDataService.getAllBanks(tenantIdentity);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllBanks_ThrowsException() {
        when(banksRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bankMasterDataService.getAllBanks(tenantIdentity));
        assertEquals("DB error", ex.getMessage());
    }

    // ------------------- Branches Tests -------------------
    @Test
    void testGetAllBranches_WithData() {
        Branches branch = new Branches();
        branch.setBranchId(1);
        branch.setStateId(10);
        branch.setBranchTypeId(20);
        branch.setPostOfficeId(30);
        branch.setCityId(40);
        branch.setDistrictId(50);
        branch.setCountryId(60);

        when(branchesRepository.findAllBranchesByIsDelFalseByTenantId(tenantId))
                .thenReturn(List.of(branch));

        // Mock related entities
        States state = new States(); state.setStateId(10);
        BranchTypes branchType = new BranchTypes(); branchType.setBranchTypeId(20);
        PostOffices postOffice = new PostOffices(); postOffice.setPostOfficeId(30);
        Cities city = new Cities(); city.setCityId(40);
        Districts district = new Districts(); district.setDistrictId(50);
        Countries country = new Countries(); country.setCountryId(60);

        when(statesRepository.findByStateIdIn(Set.of(10))).thenReturn(List.of(state));
        when(branchTypeRepository.findByBranchTypeIdIn(Set.of(20))).thenReturn(List.of(branchType));
        when(postOfficesRepository.findByPostOfficeIdIn(Set.of(30))).thenReturn(List.of(postOffice));
        when(citiesRepository.findByCityIdIn(Set.of(40))).thenReturn(List.of(city));
        when(districtRepository.findBydistrictIdIn(Set.of(50))).thenReturn(List.of(district));
        when(countryRepository.findByCountryIdIn(Set.of(60))).thenReturn(List.of(country));

        // DTO mappings
        BranchesDto branchDto = new BranchesDto(); StatesDto stateDto = new StatesDto();
        BranchTypeDto branchTypeDto = new BranchTypeDto(); PostOfficesDto postOfficeDto = new PostOfficesDto();
        CitiesDto cityDto = new CitiesDto(); DistrictDto districtDto = new DistrictDto(); CountryDto countryDto = new CountryDto();

        when(branchesMapper.convertToDto(branch)).thenReturn(branchDto);
        when(statesMapper.convertToDto(state)).thenReturn(stateDto);
        when(branchTypeMapper.convertToDto(branchType)).thenReturn(branchTypeDto);
        when(postOfficeMapper.convertToDto(postOffice)).thenReturn(postOfficeDto);
        when(citiesMapper.convertToDto(city)).thenReturn(cityDto);
        when(districtMapper.convertToDto(district)).thenReturn(districtDto);
        when(countryMapper.convertToDto(country)).thenReturn(countryDto);

        List<BranchesDto> result = bankMasterDataService.getAllBranches(tenantIdentity);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStateDto()).isSameAs(stateDto);
        assertThat(result.get(0).getBranchTypeDto()).isSameAs(branchTypeDto);
        assertThat(result.get(0).getPostOfficesDto()).isSameAs(postOfficeDto);
        assertThat(result.get(0).getCitiesDto()).isSameAs(cityDto);
        assertThat(result.get(0).getDistrictDto()).isSameAs(districtDto);
        assertThat(result.get(0).getCountryDto()).isSameAs(countryDto);
    }

    @Test
    void testGetAllBranches_Empty() {
        when(branchesRepository.findAllBranchesByIsDelFalseByTenantId(tenantId))
                .thenReturn(Collections.emptyList());

        List<BranchesDto> result = bankMasterDataService.getAllBranches(tenantIdentity);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllBranches_ThrowsException() {
        when(branchesRepository.findAllBranchesByIsDelFalseByTenantId(tenantId))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bankMasterDataService.getAllBranches(tenantIdentity));
        assertEquals("DB error", ex.getMessage());
    }

    // ------------------- IFSC Code Tests -------------------
    @Test
    void testGetAllIfscCodes_WithData() {
        IfscCodes entity = new IfscCodes(); entity.setIfscCode("IFSC123"); entity.setPincodeId(1); entity.setIdentity(UUID.randomUUID());
        IfscCodesDto dto = new IfscCodesDto(); dto.setIfscCode("IFSC123"); dto.setPincodes(123456);
        Pincodes pincode = new Pincodes(); pincode.setPincodeId(1); pincode.setPincode("123456");

        Page<IfscCodes> page = new PageImpl<>(List.of(entity), PageRequest.of(0, 10), 1);
        when(ifscCodesRepository.findByIsDelFalse(PageRequest.of(0, 10))).thenReturn(page);
        when(pincodesRepository.findByPincodeIdIn(Set.of(1))).thenReturn(List.of(pincode));
        when(ifscCodeMapper.convertToDto(entity)).thenReturn(dto);

        Page<IfscCodesDto> result = bankMasterDataService.getAllIfscCodes(PageRequest.of(0, 10));
        assertThat(result.getContent().get(0).getPincodes()).isEqualTo(123456);
    }

    @Test
    void testGetAllIfscCodes_Empty() {
        Page<IfscCodes> page = new PageImpl<>(Collections.emptyList());
        when(ifscCodesRepository.findByIsDelFalse(PageRequest.of(0, 10))).thenReturn(page);

        Page<IfscCodesDto> result = bankMasterDataService.getAllIfscCodes(PageRequest.of(0, 10));
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void testGetIfscCodeDetails_Found() {
        IfscCodes entity = new IfscCodes(); entity.setIfscCode("IFSC123"); entity.setPincodeId(1);
        Banks bank = new Banks(); bank.setName("Bank"); entity.setBank(bank);
        Pincodes pincode = new Pincodes(); pincode.setPincodeId(1); pincode.setPincode("123456");
        IfscCodesDto dto = new IfscCodesDto(); dto.setIfscCode("IFSC123");

        when(ifscCodesRepository.findByIfscCodeAndIsDelFalse("IFSC123")).thenReturn(Optional.of(entity));
        when(pincodesRepository.findById(1)).thenReturn(Optional.of(pincode));
        when(ifscCodeMapper.convertToDto(entity)).thenReturn(dto);

        IfscCodesDto result = bankMasterDataService.getIfscCodeDetails("IFSC123");
        assertThat(result.getBankName()).isEqualTo("Bank");
        assertThat(result.getPincodes()).isEqualTo(123456);
    }

    @Test
    void testGetIfscCodeDetails_NotFound() {
        when(ifscCodesRepository.findByIfscCodeAndIsDelFalse("IFSC123")).thenReturn(Optional.empty());
        assertNull(bankMasterDataService.getIfscCodeDetails("IFSC123"));
    }

    // ------------------- Account Types -------------------
    @Test
    void testGetAllAccountTypes_WithData() {
        AccountTypeMasterView view = mock(AccountTypeMasterView.class);
        when(accountTypeMasterRepository.findAllByTenantIdOrAll(tenantId)).thenReturn(List.of(view));

        List<AccountTypeMasterView> result = bankMasterDataService.getAllAccountTypes(tenantIdentity);
        assertThat(result).hasSize(1);
    }

    @Test
    void testGetAllAccountTypes_Empty() {
        when(accountTypeMasterRepository.findAllByTenantIdOrAll(tenantId)).thenReturn(Collections.emptyList());

        List<AccountTypeMasterView> result = bankMasterDataService.getAllAccountTypes(tenantIdentity);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllAccountTypes_ThrowsException() {
        when(accountTypeMasterRepository.findAllByTenantIdOrAll(tenantId))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bankMasterDataService.getAllAccountTypes(tenantIdentity));
        assertEquals("DB error", ex.getMessage());
    }

    // ------------------- Account Status -------------------
    @Test
    void testGetAllAccountStatuses_WithData() {
        AccountStatusesView view = mock(AccountStatusesView.class);
        when(accountStatusesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId)).thenReturn(List.of(view));

        List<AccountStatusesView> result = bankMasterDataService.getAllAccountStatuses(tenantIdentity);
        assertThat(result).hasSize(1);
    }

    @Test
    void testGetAllAccountStatuses_Empty() {
        when(accountStatusesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId)).thenReturn(Collections.emptyList());
        List<AccountStatusesView> result = bankMasterDataService.getAllAccountStatuses(tenantIdentity);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllAccountStatuses_ThrowsException() {
        when(accountStatusesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId))
                .thenThrow(new RuntimeException("DB error"));
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bankMasterDataService.getAllAccountStatuses(tenantIdentity));
        assertEquals("DB error", ex.getMessage());
    }

    // ------------------- Customer Status -------------------
    @Test
    void testGetAllCustomerStatuses_WithData() {
        CustomerStatusView view = mock(CustomerStatusView.class);
        when(customerStatusRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId)).thenReturn(List.of(view));

        List<CustomerStatusView> result = bankMasterDataService.getAllCustomerStatuses(tenantIdentity);
        assertThat(result).hasSize(1);
    }

    @Test
    void testGetAllCustomerStatuses_Empty() {
        when(customerStatusRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId)).thenReturn(Collections.emptyList());
        List<CustomerStatusView> result = bankMasterDataService.getAllCustomerStatuses(tenantIdentity);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllCustomerStatuses_ThrowsException() {
        when(customerStatusRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId))
                .thenThrow(new RuntimeException("DB error"));
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bankMasterDataService.getAllCustomerStatuses(tenantIdentity));
        assertEquals("DB error", ex.getMessage());
    }

    // ------------------- Customer Categories -------------------
    @Test
    void testGetCustomerCategoryView_WithData() {
        CustomerCategoryView view = mock(CustomerCategoryView.class);
        when(customerCategoryRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId)).thenReturn(List.of(view));

        List<CustomerCategoryView> result = bankMasterDataService.getCustomerCategoryView(tenantIdentity);
        assertThat(result).hasSize(1);
    }

    @Test
    void testGetCustomerCategoryView_Empty() {
        when(customerCategoryRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId)).thenReturn(Collections.emptyList());

        List<CustomerCategoryView> result = bankMasterDataService.getCustomerCategoryView(tenantIdentity);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetCustomerCategoryView_ThrowsException() {
        when(customerCategoryRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bankMasterDataService.getCustomerCategoryView(tenantIdentity));
        assertEquals("DB error", ex.getMessage());
    }

    // ------------------- Customer Groups -------------------
    @Test
    void testGetAllCustomerGroups_WithData() {
        CustomerGroupMasterView view = mock(CustomerGroupMasterView.class);
        when(customerGroupMasterRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId)).thenReturn(List.of(view));

        List<CustomerGroupMasterView> result = bankMasterDataService.getAllCustomerGroups(tenantIdentity);
        assertThat(result).hasSize(1);
    }

    @Test
    void testGetAllCustomerGroups_Empty() {
        when(customerGroupMasterRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId)).thenReturn(Collections.emptyList());

        List<CustomerGroupMasterView> result = bankMasterDataService.getAllCustomerGroups(tenantIdentity);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllCustomerGroups_ThrowsException() {
        when(customerGroupMasterRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bankMasterDataService.getAllCustomerGroups(tenantIdentity));
        assertEquals("DB error", ex.getMessage());
    }

    // ------------------- Risk Categories -------------------
    @Test
    void testGetAllRiskCategories_WithData() {
        RiskCategoryView view = mock(RiskCategoryView.class);
        when(riskCategoryRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId)).thenReturn(List.of(view));

        List<RiskCategoryView> result = bankMasterDataService.getAllRiskCategories(tenantIdentity);
        assertThat(result).hasSize(1);
    }

    @Test
    void testGetAllRiskCategories_Empty() {
        when(riskCategoryRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId)).thenReturn(Collections.emptyList());

        List<RiskCategoryView> result = bankMasterDataService.getAllRiskCategories(tenantIdentity);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllRiskCategories_ThrowsException() {
        when(riskCategoryRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId))
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bankMasterDataService.getAllRiskCategories(tenantIdentity));
        assertEquals("DB error", ex.getMessage());
    }
}
