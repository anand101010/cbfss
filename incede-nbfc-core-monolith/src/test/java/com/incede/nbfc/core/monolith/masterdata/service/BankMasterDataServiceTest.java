package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.mapper.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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

    @Mock
    private StatesRepository statesRepository;
    @Mock
    private BranchTypeRepository branchTypeRepository;
    @Mock
    private PostOfficesRepository postOfficesRepository;
    @Mock
    private CitiesRepository citiesRepository;
    @Mock
    private DistrictRepository districtRepository;
    @Mock
    private PincodesRepository pincodesRepository;
    @Mock
    private IfscCodesRepository ifscCodesRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private CustomerGroupMasterRepository customerGroupMasterRepository;
    @Mock
    private RiskCategoryRepository riskCategoryRepository;
    @Mock
    private IfscCodeMapper ifscCodeMapper;
    @Mock
    private BranchesMapper branchesMapper;
    @Mock
    private StatesMapper statesMapper;
    @Mock
    private BranchTypeMapper branchTypeMapper;
    @Mock
    private PostOfficeMapper postOfficeMapper;
    @Mock
    private CitiesMapper citiesMapper;
    @Mock
    private DistrictMapper districtMapper;
    @Mock
    private CountryMapper countryMapper;

    private Branches branchEntity;

    private IfscCodes ifscEntity;
    private IfscCodesDto ifscDto;
    private Pincodes pincode;

    @BeforeEach
    void setUp() {

        branchEntity = new Branches();
        branchEntity.setBranchId(1);
        branchEntity.setStateId(10);
        branchEntity.setBranchTypeId(20);
        branchEntity.setPostOfficeId(30);
        branchEntity.setCityId(40);
        branchEntity.setDistrictId(50);

        ifscEntity = new IfscCodes();
        ifscEntity.setIfscCode("TEST0001234");
        ifscEntity.setBranchName("Test Branch");
        ifscEntity.setPincodeId(101);

        ifscDto = new IfscCodesDto();
        ifscDto.setIfscCode("TEST0001234");
        ifscDto.setBranchName("Test Branch");

        pincode = new Pincodes();
        pincode.setPincodeId(101);
        pincode.setPincode("673528");
        branchEntity.setCountryId(1);
    }


    @Test
    void testGetAllBranchContacts_WhenDataExists() {
        UUID id = UUID.randomUUID();

        BranchContactView mockView = mock(BranchContactView.class);
        when(mockView.getValue()).thenReturn("1234567890");
        when(mockView.getRemarks()).thenReturn("Main contact");
        when(mockView.getIdentity()).thenReturn(id);

        when(branchContactsRepository.findByIsDelFalse()).thenReturn(List.of(mockView));

        List<BranchContactView> result = bankMasterDataService.getAllBranchContact();

        assertNotNull(result);
        assertEquals(1, result.size(), "Expected exactly 1 branch contact"); // message will show in case of failure
        assertEquals("1234567890", result.get(0).getValue());
        assertEquals("Main contact", result.get(0).getRemarks());
        assertEquals(id, result.get(0).getIdentity());
    }


    @Test
    void testGetAllBranchContacts_WhenDataEmpty() {
        when(branchContactsRepository.findByIsDelFalse()).thenReturn(Collections.emptyList());

        List<BranchContactView> result = bankMasterDataService.getAllBranchContact();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllBranchContacts_WhenRepositoryThrowsException() {
        when(branchContactsRepository.findByIsDelFalse()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bankMasterDataService.getAllBranchContact());

        assertEquals("DB error", ex.getMessage());
    }


    @Test
    void testGetAllBranchWeekSchedules_WhenDataExists() {
        UUID id = UUID.randomUUID();

        BranchWeekScheduleView mockView = mock(BranchWeekScheduleView.class);
        when(mockView.getDayOfWeek()).thenReturn((short) 1);
        when(mockView.getIdentity()).thenReturn(id);

        when(branchWeekScheduleRepository.findByIsDelFalse()).thenReturn(List.of(mockView));

        List<BranchWeekScheduleView> result = bankMasterDataService.getAllBranchWeekSchedule();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals((short) 1, result.get(0).getDayOfWeek());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllBranchWeekSchedules_WhenDataEmpty() {
        when(branchWeekScheduleRepository.findByIsDelFalse()).thenReturn(Collections.emptyList());

        List<BranchWeekScheduleView> result = bankMasterDataService.getAllBranchWeekSchedule();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllBranchWeekSchedules_WhenRepositoryThrowsException() {
        when(branchWeekScheduleRepository.findByIsDelFalse()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bankMasterDataService.getAllBranchWeekSchedule());

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

        when(banksRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(List.of(mockView));

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
        when(banksRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(Collections.emptyList());

        List<BanksView> result = bankMasterDataService.getAllBanks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllBanks_WhenRepositoryThrowsException() {
        when(banksRepository.findByIsDelFalseAndIsActiveTrue()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bankMasterDataService.getAllBanks());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllBranches_WhenDataExists() {

        when(branchesRepository.findAllBranchesByIsDelFalse()).thenReturn(List.of(branchEntity));

        States state = new States();
        state.setStateId(10);
        when(statesRepository.findByStateIdIn(Set.of(10))).thenReturn(List.of(state));

        BranchTypes branchType = new BranchTypes();
        branchType.setBranchTypeId(20);
        when(branchTypeRepository.findByBranchTypeIdIn(Set.of(20))).thenReturn(List.of(branchType));

        PostOffices postOffice = new PostOffices();
        postOffice.setPostOfficeId(30);
        when(postOfficesRepository.findByPostOfficeIdIn(Set.of(30))).thenReturn(List.of(postOffice));

        Cities city = new Cities();
        city.setCityId(40);
        when(citiesRepository.findByCityIdIn(Set.of(40))).thenReturn(List.of(city));

        Countries country = new Countries();
        country.setCountryId(60);
        when(countryRepository.findByCountryIdIn(Set.of(60))).thenReturn(List.of(country));

        Districts district = new Districts();
        district.setDistrictId(50);
        when(districtRepository.findBydistrictIdIn(Set.of(50))).thenReturn(List.of(district));

        BranchesDto branchDto = new BranchesDto();
        when(branchesMapper.convertToDto(branchEntity)).thenReturn(branchDto);

        StatesDto stateDto = new StatesDto();
        when(statesMapper.convertToDto(state)).thenReturn(stateDto);

        BranchTypeDto branchTypeDto = new BranchTypeDto();
        when(branchTypeMapper.convertToDto(branchType)).thenReturn(branchTypeDto);

        PostOfficesDto postOfficeDto = new PostOfficesDto();
        when(postOfficeMapper.convertToDto(postOffice)).thenReturn(postOfficeDto);

        CitiesDto cityDto = new CitiesDto();
        when(citiesMapper.convertToDto(city)).thenReturn(cityDto);

        DistrictDto districtDto = new DistrictDto();
        when(districtMapper.convertToDto(district)).thenReturn(districtDto);

        CountryDto countryDto = new CountryDto();
        when(countryMapper.convertToDto(country)).thenReturn(countryDto);

        List<BranchesDto> result = bankMasterDataService.getAllBranches();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);

        BranchesDto dto = result.get(0);
        assertThat(dto).isSameAs(branchDto);
        assertThat(dto.getStateDto()).isSameAs(stateDto);
        assertThat(dto.getBranchTypeDto()).isSameAs(branchTypeDto);
        assertThat(dto.getPostOfficesDto()).isSameAs(postOfficeDto);
        assertThat(dto.getDistrictDto()).isSameAs(districtDto);

    }


    @Test
    void testGetAllBranches_WhenNoBranchesExist() {
        when(branchesRepository.findAllBranchesByIsDelFalse()).thenReturn(Collections.emptyList());

        List<BranchesDto> result = bankMasterDataService.getAllBranches();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    void testGetAllBranches_WhenRepositoryThrowsException() {
        when(branchesRepository.findAllBranchesByIsDelFalse()).thenThrow(new RuntimeException("DB error"));

        try {
            bankMasterDataService.getAllBranches();
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).isEqualTo("DB error");
        }
    }

    @Test
    void testGetAllAccountStatuses_WhenDataExists() {
        UUID id = UUID.randomUUID();

        AccountStatusesView mockView = mock(AccountStatusesView.class);
        when(mockView.getName()).thenReturn("Active");
        when(mockView.getCode()).thenReturn("ACT");
        when(mockView.getIdentity()).thenReturn(id);

        when(accountStatusesRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(List.of(mockView));

        List<AccountStatusesView> result = bankMasterDataService.getAllAccountStatuses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Active", result.get(0).getName());
        assertEquals("ACT", result.get(0).getCode());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllAccountStatuses_WhenDataEmpty() {
        when(accountStatusesRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(Collections.emptyList());

        List<AccountStatusesView> result = bankMasterDataService.getAllAccountStatuses();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllAccountStatuses_WhenRepositoryThrowsException() {
        when(accountStatusesRepository.findByIsDelFalseAndIsActiveTrue()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bankMasterDataService.getAllAccountStatuses());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllAccountTypes_WhenDataExists() {
        UUID id = UUID.randomUUID();
        AccountTypeMasterView mockView = mock(AccountTypeMasterView.class);
        when(mockView.getAccountType()).thenReturn("Savings");
        when(mockView.getIdentity()).thenReturn(id);

        when(accountTypeMasterRepository.findByIsDelFalse()).thenReturn(List.of(mockView));

        List<AccountTypeMasterView> result = bankMasterDataService.getAllAccountTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Savings", result.get(0).getAccountType());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllAccountTypes_WhenDataEmpty() {
        when(accountTypeMasterRepository.findByIsDelFalse()).thenReturn(Collections.emptyList());

        List<AccountTypeMasterView> result = bankMasterDataService.getAllAccountTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllAccountTypes_WhenRepositoryThrowsException() {
        when(accountTypeMasterRepository.findByIsDelFalse()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bankMasterDataService.getAllAccountTypes());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllCustomerStatuses_WhenDataExists() {
        UUID id = UUID.randomUUID();

        CustomerStatusView mockView = mock(CustomerStatusView.class);
        when(mockView.getStatusName()).thenReturn("Active");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(customerStatusRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(List.of(mockView));

        List<CustomerStatusView> result = bankMasterDataService.getAllCustomerStatuses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Active", result.get(0).getStatusName());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllCustomerStatuses_WhenDataEmpty() {
        when(customerStatusRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(Collections.emptyList());

        List<CustomerStatusView> result = bankMasterDataService.getAllCustomerStatuses();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllCustomerStatuses_WhenRepositoryThrowsException() {
        when(customerStatusRepository.findByIsDelFalseAndIsActiveTrue()).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bankMasterDataService.getAllCustomerStatuses());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllIfscCodes_withData() {

        IfscCodes ifsc = new IfscCodes();
        ifsc.setIfscCode("SBIN0001234");
        ifsc.setPincodeId(1);

        Pincodes pincode = new Pincodes();
        pincode.setPincodeId(1);
        pincode.setPincode("682030");

        IfscCodesDto dto = new IfscCodesDto();
        dto.setIfscCode("SBIN0001234");

        when(ifscCodesRepository.findAllIfscCodeByIsDelFalse()).thenReturn(List.of(ifsc));
        when(pincodesRepository.findByPincodeIdIn(Set.of(1))).thenReturn(List.of(pincode));
        when(ifscCodeMapper.convertToDto(ifsc)).thenReturn(dto);

        List<IfscCodesDto> result = bankMasterDataService.getAllIfscCodes();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIfscCode()).isEqualTo("SBIN0001234");
        assertThat(result.get(0).getPincodes()).isEqualTo(682030);
    }

    @Test
    void testGetAllIfscCodes_empty() {
        when(ifscCodesRepository.findAllIfscCodeByIsDelFalse()).thenReturn(List.of());

        List<IfscCodesDto> result = bankMasterDataService.getAllIfscCodes();

        assertThat(result).isEmpty();
    }

    @Test
    void testGetAllIfscCodes_missingPincodeMapping() {

        IfscCodes ifsc = new IfscCodes();
        ifsc.setIfscCode("SBIN0005678");
        ifsc.setPincodeId(99);

        IfscCodesDto dto = new IfscCodesDto();
        dto.setIfscCode("SBIN0005678");

        when(ifscCodesRepository.findAllIfscCodeByIsDelFalse()).thenReturn(List.of(ifsc));
        when(pincodesRepository.findByPincodeIdIn(Set.of(99))).thenReturn(List.of());
        when(ifscCodeMapper.convertToDto(ifsc)).thenReturn(dto);

        List<IfscCodesDto> result = bankMasterDataService.getAllIfscCodes();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIfscCode()).isEqualTo("SBIN0005678");
        assertThat(result.get(0).getPincodes()).isNull();
    }

    @Test
    void testGetAllCustomerGroups_withData() {
        CustomerGroupMasterView mockView = mock(CustomerGroupMasterView.class);
        when(mockView.getCustomerGroup()).thenReturn("Retail");
        when(mockView.getCode()).thenReturn("RET");
        when(mockView.getIdentity()).thenReturn(UUID.randomUUID());

        List<CustomerGroupMasterView> mockList = List.of(mockView);
        when(customerGroupMasterRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(mockList);

        List<CustomerGroupMasterView> result = bankMasterDataService.getAllCustomerGroups();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCustomerGroup()).isEqualTo("Retail");
        assertThat(result.get(0).getCode()).isEqualTo("RET");
    }

    @Test
    void testGetAllCustomerGroups_empty() {
        when(customerGroupMasterRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(List.of());

        List<CustomerGroupMasterView> result = bankMasterDataService.getAllCustomerGroups();

        assertThat(result).isEmpty();
    }

    @Test
    void testGetAllRiskCategories_withData() {
        RiskCategoryView mockView = mock(RiskCategoryView.class);
        when(mockView.getCategory()).thenReturn("High Risk");
        when(mockView.getCode()).thenReturn("HRISK");
        when(mockView.getIdentity()).thenReturn(UUID.randomUUID());

        List<RiskCategoryView> mockList = List.of(mockView);
        when(riskCategoryRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(mockList);

        List<RiskCategoryView> result = bankMasterDataService.getAllRiskCategories();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo("High Risk");
        assertThat(result.get(0).getCode()).isEqualTo("HRISK");
    }

    @Test
    void testGetAllRiskCategories_empty() {
        when(riskCategoryRepository.findByIsDelFalseAndIsActiveTrue()).thenReturn(List.of());

        List<RiskCategoryView> result = bankMasterDataService.getAllRiskCategories();

        assertThat(result).isEmpty();
    }

    @Test
    void testGetIfscCodeDetails_success() {
        String ifscCode = "SBIN0001234";

        Banks bank = new Banks();
        bank.setName("State Bank of India");

        IfscCodes entity = new IfscCodes();
        entity.setIfscCode(ifscCode);
        entity.setBank(bank);
        entity.setBranchName("MG Road");
        entity.setPincodeId(101);
        entity.setIdentity(UUID.randomUUID());

        Pincodes pincode = new Pincodes();
        pincode.setPincodeId(101);
        pincode.setPincode("682016");

        IfscCodesDto dto = IfscCodesDto.builder()
                .ifscCode(ifscCode)
                .bankName(bank.getName())
                .branchName(entity.getBranchName())
                .pincodes(Integer.valueOf(pincode.getPincode()))
                .identity(entity.getIdentity())
                .build();

        when(ifscCodesRepository.findByIfscCodeAndIsDelFalse(ifscCode)).thenReturn(Optional.of(entity));
        when(pincodesRepository.findById(101)).thenReturn(Optional.of(pincode));
        when(ifscCodeMapper.convertToDto(entity)).thenReturn(dto);

        IfscCodesDto result = bankMasterDataService.getIfscCodeDetails(ifscCode);

        assertThat(result).isNotNull();
        assertThat(result.getBankName()).isEqualTo("State Bank of India");
        assertThat(result.getPincodes()).isEqualTo(682016);
        verify(pincodesRepository).findById(101);
    }
}
