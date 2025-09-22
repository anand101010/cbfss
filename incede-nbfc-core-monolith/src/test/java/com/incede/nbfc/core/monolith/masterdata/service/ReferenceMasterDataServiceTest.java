package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Cities;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Districts;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Pincodes;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.States;
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
import org.springframework.data.domain.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
    PincodesRepository pincodesRepository;
    @Mock
    StatesRepository statesRepository;
    @Mock
    DistrictRepository districtRepository;
    @Mock
    CitiesRepository citiesRepository;

    @Mock
    PincodeMapper pincodeMapper;
    @Mock
    StatesMapper statesMapper;
    @Mock
    DistrictMapper districtMapper;
    @Mock
    CitiesMapper citiesMapper;

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
    void testGetAllPincodes() {
        Pincodes entity = new Pincodes();
        entity.setPincode(673528);
        entity.setStateId(1);
        entity.setDistrictId(1);
        entity.setCityId(1);

        Page<Pincodes> page = new PageImpl<>(List.of(entity));

        States state = new States();
        state.setStateId(1);
        state.setState("Kerala");

        Districts district = new Districts();
        district.setDistrictId(1);
        district.setDistrict("Kozhikode");

        Cities city = new Cities();
        city.setCityId(1);
        city.setCity("Peruvannamuzhi");

        PincodeDto dto = new PincodeDto();
        dto.setPincode(673528);
        dto.setStateDto(new StatesDto());
        dto.setDistrictDto(new DistrictDto());
        dto.setCitiesDto(new CitiesDto());

        when(pincodesRepository.findByIsDelFalse(any(Pageable.class))).thenReturn(page);
        when(statesRepository.findByStateIdIn(any(Set.class))).thenReturn(List.of(state));
        when(districtRepository.findBydistrictIdIn(any(Set.class))).thenReturn(List.of(district));
        when(citiesRepository.findByCityIdIn(any(Set.class))).thenReturn(List.of(city));
        when(pincodeMapper.convertToDto(entity)).thenReturn(dto);
        when(statesMapper.convertToDto(state)).thenReturn(new StatesDto(){{
            setState("Kerala");
        }});
        when(districtMapper.convertToDto(district)).thenReturn(new DistrictDto(){{
            setDistrict("Kozhikode");
        }});
        when(citiesMapper.convertToDto(city)).thenReturn(new CitiesDto(){{
            setCity("Peruvannamuzhi");
        }});

        Page<PincodeDto> result = referenceMasterDataService.getAllPincodes(0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        PincodeDto resultDto = result.getContent().get(0);
        assertEquals(673528, resultDto.getPincode());
        assertEquals("Kerala", resultDto.getStateDto().getState());
        assertEquals("Kozhikode", resultDto.getDistrictDto().getDistrict());
        assertEquals("Peruvannamuzhi", resultDto.getCitiesDto().getCity());
    }

    @Test
    void testGetPincodeDetails() {
        Pincodes entity = new Pincodes();
        entity.setPincode(673528);
        entity.setStateId(1);
        entity.setDistrictId(1);
        entity.setCityId(1);

        States state = new States();
        state.setStateId(1);
        state.setState("Kerala");

        Districts district = new Districts();
        district.setDistrictId(1);
        district.setDistrict("Kozhikode");

        Cities city = new Cities();
        city.setCityId(1);
        city.setCity("Peruvannamuzhi");

        PincodeDto dto = new PincodeDto();
        dto.setPincode(673528);

        when(pincodesRepository.findByDetailsThroughPincode(673528)).thenReturn(List.of(entity));
        when(statesRepository.findByStateIdIn(any(Set.class))).thenReturn(List.of(state));
        when(districtRepository.findBydistrictIdIn(any(Set.class))).thenReturn(List.of(district));
        when(citiesRepository.findByCityIdIn(any(Set.class))).thenReturn(List.of(city));
        when(pincodeMapper.convertToDto(entity)).thenReturn(dto);
        when(statesMapper.convertToDto(state)).thenReturn(new StatesDto(){{
            setState("Kerala");
        }});
        when(districtMapper.convertToDto(district)).thenReturn(new DistrictDto(){{
            setDistrict("Kozhikode");
        }});
        when(citiesMapper.convertToDto(city)).thenReturn(new CitiesDto(){{
            setCity("Peruvannamuzhi");
        }});

        List<PincodeDto> result = referenceMasterDataService.getPincodeDetails(673528);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        PincodeDto resultDto = result.get(0);
        assertEquals(673528, resultDto.getPincode());
        assertEquals("Kerala", resultDto.getStateDto().getState());
        assertEquals("Kozhikode", resultDto.getDistrictDto().getDistrict());
        assertEquals("Peruvannamuzhi", resultDto.getCitiesDto().getCity());
    }

}
