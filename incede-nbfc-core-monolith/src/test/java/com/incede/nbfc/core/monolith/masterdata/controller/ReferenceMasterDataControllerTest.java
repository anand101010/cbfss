package com.incede.nbfc.core.monolith.masterdata.controller;


import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.service.ReferenceMasterDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
public class ReferenceMasterDataControllerTest {

    @Mock
    private ReferenceMasterDataService referenceMasterDataService;

    @InjectMocks
    private ReferenceMasterDataController referenceMasterDataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllContactTypes() {
        ContactTypesView mockContactType = mock(ContactTypesView.class);
        given(mockContactType.getContactType()).willReturn("Email");
        given(referenceMasterDataService.getAllContactTypes()).willReturn(List.of(mockContactType));

        ResponseEntity<List<ContactTypesView>> response = referenceMasterDataController.getAllContactTypes();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getContactType()).isEqualTo("Email");
    }

    @Test
    void testGetAllAddressTypes() {

        AddressTypeView mockAddressType = mock(AddressTypeView.class);
        UUID id = UUID.randomUUID();
        given(mockAddressType.getAddressTypeName()).willReturn("Permanent");
        given(mockAddressType.getIsActive()).willReturn(true);
        given(mockAddressType.getIdentity()).willReturn(id);

        given(referenceMasterDataService.getAllAddressTypes()).willReturn(List.of(mockAddressType));

        ResponseEntity<List<AddressTypeView>> response = referenceMasterDataController.getAllAddressTypes();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        AddressTypeView result = response.getBody().get(0);
        assertThat(result.getAddressTypeName()).isEqualTo("Permanent");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllAddressProofTypes() {
        AddressProofTypeView mockProof = mock(AddressProofTypeView.class);
        UUID id = UUID.randomUUID();
        given(mockProof.getCode()).willReturn("AAD");
        given(mockProof.getName()).willReturn("Aadhar");
        given(mockProof.getConciseDescription()).willReturn("Aadhar Card");
        given(mockProof.getIdentity()).willReturn(id);

        given(referenceMasterDataService.getAllAddressProofTypes()).willReturn(List.of(mockProof));

        ResponseEntity<List<AddressProofTypeView>> response = referenceMasterDataController.getAllAddressProofTypes();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        AddressProofTypeView result = response.getBody().get(0);
        assertThat(result.getCode()).isEqualTo("AAD");
        assertThat(result.getName()).isEqualTo("Aadhar");
        assertThat(result.getConciseDescription()).isEqualTo("Aadhar Card");
        assertThat(result.getIdentity()).isEqualTo(id);
    }


    @Test
    void testGetAllResidentialStatuses() {

        ResidentialStatusesView mockStatus = mock(ResidentialStatusesView.class);
        UUID id = UUID.randomUUID();

        given(mockStatus.getName()).willReturn("Owned");
        given(mockStatus.getCode()).willReturn("OWN");
        given(mockStatus.getIsActive()).willReturn(true);
        given(mockStatus.getIdentity()).willReturn(id);

        given(referenceMasterDataService.getAllResidentialStatuses()).willReturn(List.of(mockStatus));

        ResponseEntity<List<ResidentialStatusesView>> response = referenceMasterDataController.getAllResidentialStatuses();

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        ResidentialStatusesView result = response.getBody().get(0);
        assertThat(result.getName()).isEqualTo("Owned");
        assertThat(result.getCode()).isEqualTo("OWN");
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getIdentity()).isEqualTo(id);
    }

    @Test
    void testGetAllPincodes() {

        PincodeDto mockPincode = new PincodeDto();
        mockPincode.setPincode(673528);

        StatesDto stateDto = new StatesDto();
        stateDto.setState("Kerala");
        mockPincode.setStateDto(stateDto);

        DistrictDto districtDto = new DistrictDto();
        districtDto.setDistrict("Kozhikode");
        mockPincode.setDistrictDto(districtDto);

        CitiesDto citiesDto = new CitiesDto();
        citiesDto.setCity("Peruvannamuzhi");
        mockPincode.setCitiesDto(citiesDto);

        Page<PincodeDto> mockPage = new PageImpl<>(List.of(mockPincode));

        given(referenceMasterDataService.getAllPincodes(0, 10)).willReturn(mockPage);

        ResponseEntity<Page<PincodeDto>> response = referenceMasterDataController.getAllPincodes(0, 10);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(1);
        PincodeDto result = response.getBody().getContent().get(0);
        assertThat(result.getPincode()).isEqualTo(673528);
        assertThat(result.getStateDto().getState()).isEqualTo("Kerala");
        assertThat(result.getDistrictDto().getDistrict()).isEqualTo("Kozhikode");
        assertThat(result.getCitiesDto().getCity()).isEqualTo("Peruvannamuzhi");
    }

    @Test
    void testGetPincodeByNumber() {
        PincodeDto mockPincode = new PincodeDto();
        mockPincode.setPincode(673528);

        StatesDto stateDto = new StatesDto();
        stateDto.setState("Kerala");
        mockPincode.setStateDto(stateDto);

        DistrictDto districtDto = new DistrictDto();
        districtDto.setDistrict("Kozhikode");
        mockPincode.setDistrictDto(districtDto);

        CitiesDto citiesDto = new CitiesDto();
        citiesDto.setCity("Peruvannamuzhi");
        mockPincode.setCitiesDto(citiesDto);

        List<PincodeDto> mockList = List.of(mockPincode);

        given(referenceMasterDataService.getPincodeDetails(673528)).willReturn(mockList);

        ResponseEntity<List<PincodeDto>> response = referenceMasterDataController.getPincodeByNumber(673528);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        PincodeDto result = response.getBody().get(0);
        assertThat(result.getPincode()).isEqualTo(673528);
        assertThat(result.getStateDto().getState()).isEqualTo("Kerala");
        assertThat(result.getDistrictDto().getDistrict()).isEqualTo("Kozhikode");
        assertThat(result.getCitiesDto().getCity()).isEqualTo("Peruvannamuzhi");
    }
}
