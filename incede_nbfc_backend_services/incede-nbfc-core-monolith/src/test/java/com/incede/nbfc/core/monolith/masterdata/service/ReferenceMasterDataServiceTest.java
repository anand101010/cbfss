package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.mapper.CitiesMapper;
import com.incede.nbfc.core.monolith.masterdata.mapper.DistrictMapper;
import com.incede.nbfc.core.monolith.masterdata.mapper.PincodeMapper;
import com.incede.nbfc.core.monolith.masterdata.mapper.StatesMapper;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReferenceMasterDataServiceTest {

    @Mock
    AddressTypeRepository addressTypeRepository;
    @Mock
    AddressProofTypeRepository addressProofTypeRepository;
    @Mock
    ResidentialStatusesRepository residentialStatusesRepository;
    @Mock
    ContactTypesRepository contactTypesRepository;
    @Mock
    private PostOfficesRepository postOfficesRepository;

    @Mock
    PincodesRepository pincodesRepository;
    @Mock
    PincodeMapper pincodeMapper;
    @InjectMocks
    ReferenceMasterDataService referenceMasterDataService;


    @Test
    void testGetAllAddressTypes_WhenDataExists() {
        UUID id = UUID.randomUUID();

        AddressTypeView mockView = mock(AddressTypeView.class);
        when(mockView.getAddressTypeName()).thenReturn("Office");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(addressTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<AddressTypeView> result = referenceMasterDataService.getAllAddressTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Office", result.get(0).getAddressTypeName());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllAddressTypes_WhenDataEmpty() {
        lenient().when(addressTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<AddressTypeView> result = referenceMasterDataService.getAllAddressTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllAddressTypes_WhenRepositoryThrowsException() {
        when(addressTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> referenceMasterDataService.getAllAddressTypes());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllAddressProofTypes_WhenDataExists() {
        UUID id = UUID.randomUUID();

        AddressProofTypeView mockView = mock(AddressProofTypeView.class);
        when(mockView.getCode()).thenReturn("PASSPORT");
        when(mockView.getName()).thenReturn("Passport");
        when(mockView.getConciseDescription()).thenReturn("Passport Document");
        when(mockView.getIdentity()).thenReturn(id);

        when(addressProofTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<AddressProofTypeView> result = referenceMasterDataService.getAllAddressProofTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PASSPORT", result.get(0).getCode());
        assertEquals("Passport", result.get(0).getName());
        assertEquals("Passport Document", result.get(0).getConciseDescription());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllAddressProofTypes_WhenDataEmpty() {
        when(addressProofTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<AddressProofTypeView> result = referenceMasterDataService.getAllAddressProofTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllAddressProofTypes_WhenRepositoryThrowsException() {
        when(addressProofTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> referenceMasterDataService.getAllAddressProofTypes());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllResidentialStatuses_WhenDataExists() {
        UUID id = UUID.randomUUID();

        ResidentialStatusesView mockView = mock(ResidentialStatusesView.class);
        when(mockView.getName()).thenReturn("Owned");
        when(mockView.getCode()).thenReturn("OWN");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(residentialStatusesRepository.findByIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<ResidentialStatusesView> result = referenceMasterDataService.getAllResidentialStatuses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Owned", result.get(0).getName());
        assertEquals("OWN", result.get(0).getCode());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllResidentialStatuses_WhenDataEmpty() {
        when(residentialStatusesRepository.findByIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<ResidentialStatusesView> result = referenceMasterDataService.getAllResidentialStatuses();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllResidentialStatuses_WhenRepositoryThrowsException() {
        when(residentialStatusesRepository.findByIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> referenceMasterDataService.getAllResidentialStatuses());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllContactTypes_WhenDataExists() {
        UUID id = UUID.randomUUID();

        ContactTypesView mockView = mock(ContactTypesView.class);
        when(mockView.getContactType()).thenReturn("Mobile");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(contactTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<ContactTypesView> result = referenceMasterDataService.getAllContactTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mobile", result.get(0).getContactType());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllContactTypes_WhenDataEmpty() {
        when(contactTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<ContactTypesView> result = referenceMasterDataService.getAllContactTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllContactTypes_WhenRepositoryThrowsException() {
        when(contactTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> referenceMasterDataService.getAllContactTypes());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetPincodeDetails_withData() {

        Pincodes pincode = new Pincodes();
        pincode.setPincodeId(1);
        pincode.setPincode("682030");

        Cities city = new Cities(); city.setCity("Kochi");
        States state = new States(); state.setState("Kerala");
        Districts district = new Districts(); district.setDistrict("Ernakulam");

        pincode.setCities(city);
        pincode.setStates(state);
        pincode.setDistricts(district);
        pincode.setIdentity(UUID.randomUUID());

        PostOffices po = new PostOffices();
        po.setOfficeName("Ernakulam");
        po.setIdentity(UUID.randomUUID());
        po.setPincode(pincode);

        PincodeDto dto = new PincodeDto();
        dto.setPincode("682030");
        dto.setCityName("Kochi");
        dto.setStateName("Kerala");
        dto.setDistrictName("Ernakulam");
        dto.setIdentity(pincode.getIdentity());

        PostOfficesResponseDto poDto = new PostOfficesResponseDto(po.getOfficeName(), po.getIdentity());
        dto.setPostOffices(List.of(poDto));

        when(pincodesRepository.findByPincodeWithDetails("682030")).thenReturn(List.of(pincode));
        when(postOfficesRepository.findByPincode_PincodeIdIn(List.of(1))).thenReturn(List.of(po));
        when(pincodeMapper.convertToDto(pincode)).thenReturn(dto);

        List<PincodeDto> result = referenceMasterDataService.getPincodeDetails("682030");

        assertThat(result).hasSize(1);
        PincodeDto resultDto = result.get(0);
        assertThat(resultDto.getPincode()).isEqualTo("682030");
        assertThat(resultDto.getCityName()).isEqualTo("Kochi");
        assertThat(resultDto.getStateName()).isEqualTo("Kerala");
        assertThat(resultDto.getDistrictName()).isEqualTo("Ernakulam");

        assertThat(resultDto.getPostOffices()).hasSize(1);
        PostOfficesResponseDto resultPo = resultDto.getPostOffices().get(0);
        assertThat(resultPo.getOfficeName()).isEqualTo("Ernakulam");
        assertThat(resultPo.getIdentity()).isEqualTo(po.getIdentity());
    }

    @Test
    void testGetPincodeDetails_empty() {
        when(pincodesRepository.findByPincodeWithDetails("999999")).thenReturn(List.of());

        List<PincodeDto> result = referenceMasterDataService.getPincodeDetails("999999");

        assertThat(result).isEmpty();
    }


    @Test
    void testGetAllPincodes_withData() {
        Pincodes pincode = new Pincodes();
        pincode.setPincode("682030");

        Cities city = new Cities(); city.setCity("Kochi");
        States state = new States(); state.setState("Kerala");
        Districts district = new Districts(); district.setDistrict("Ernakulam");

        pincode.setCities(city);
        pincode.setStates(state);
        pincode.setDistricts(district);

        PincodeDto dto = new PincodeDto();
        dto.setPincode("682030");

        Page<Pincodes> mockPage = new PageImpl<>(List.of(pincode));
        when(pincodesRepository.findByIsDelFalse(any())).thenReturn(mockPage);
        when(pincodeMapper.convertToDto(pincode)).thenReturn(dto);

        Page<PincodeDto> result = referenceMasterDataService.getAllPincodes(0, 10);

        assertThat(result.getContent()).hasSize(1);
        PincodeDto resultDto = result.getContent().get(0);
        assertThat(resultDto.getPincode()).isEqualTo("682030");
        assertThat(resultDto.getCityName()).isEqualTo("Kochi");
        assertThat(resultDto.getStateName()).isEqualTo("Kerala");
        assertThat(resultDto.getDistrictName()).isEqualTo("Ernakulam");
    }
}
