package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAddress;
import com.incede.nbfc.core.monolith.customer.domain.entity.Nominee;
import com.incede.nbfc.core.monolith.customer.dto.NomineeAddressDto;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDetailsRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDetailsResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class NomineeDetailsMapper {

    public Nominee toEntity(Customer customer, NomineeDetailsRequestDto nomineeDetailsRequestDto, NomineeAddressDto nomineeAddressDto) {
        Objects.requireNonNull(nomineeDetailsRequestDto, "NomineeDetailsRequestDto must not be null");

        Nominee nominee = new Nominee();
        nominee.setIdentity(UUID.randomUUID());
        nominee.setCustomer(customer);
        nominee.setNomineeId(nomineeDetailsRequestDto.getNomineeId());
        nominee.setFullName(nomineeDetailsRequestDto.getFullName());
        nominee.setRelationship(nomineeDetailsRequestDto.getRelationship());
        nominee.setDob(nomineeDetailsRequestDto.getDob());
        nominee.setContactNumber(nomineeDetailsRequestDto.getContactNumber());
        nominee.setPercentageShare(nomineeDetailsRequestDto.getPercentageShare());
        nominee.setIsMinor(nomineeDetailsRequestDto.getIsMinor());
        nominee.setGuardianName(nomineeDetailsRequestDto.getGuardianName());
        nominee.setGuardianDob(nomineeDetailsRequestDto.getGuardianDob());
        nominee.setGuardianEmail(nomineeDetailsRequestDto.getGuardianEmail());
        nominee.setGuardianContactNumber(nomineeDetailsRequestDto.getGuardianContactNumber());
        nominee.setIsSameAddress(nomineeDetailsRequestDto.getIsSameAddress());
        nominee.setCreatedBy(getCreatedBy());


        if (nomineeAddressDto != null) {
            mapAddressToNominee(nominee, nomineeAddressDto);
        }

        return nominee;
    }
    public void updateEntity(Nominee nominee, NomineeDetailsRequestDto nomineeDetailsRequestDto, NomineeAddressDto nomineeAddressDto) {
        nominee.setFullName(nomineeDetailsRequestDto.getFullName());
        nominee.setRelationship(nomineeDetailsRequestDto.getRelationship());
        nominee.setDob(nomineeDetailsRequestDto.getDob());
        nominee.setContactNumber(nomineeDetailsRequestDto.getContactNumber());
        nominee.setPercentageShare(nomineeDetailsRequestDto.getPercentageShare());
        nominee.setIsMinor(nomineeDetailsRequestDto.getIsMinor());
        nominee.setGuardianName(nomineeDetailsRequestDto.getGuardianName());
        nominee.setGuardianDob(nomineeDetailsRequestDto.getGuardianDob());
        nominee.setGuardianEmail(nomineeDetailsRequestDto.getGuardianEmail());
        nominee.setGuardianContactNumber(nomineeDetailsRequestDto.getGuardianContactNumber());
        nominee.setIsSameAddress(nomineeDetailsRequestDto.getIsSameAddress());
        nominee.setUpdatedBy(getUpdatedBy());

        if (nomineeAddressDto != null) {
            mapAddressToNominee(nominee, nomineeAddressDto);
        }
    }

    private void mapAddressToNominee(Nominee nominee, NomineeAddressDto address) {
        nominee.setHouseNumber(address.getDoorNumber());
        nominee.setStreet(address.getAddressLine1());
        nominee.setLandmark(address.getLandmark());
        nominee.setPlaceName(address.getPlaceName());
        nominee.setCity(address.getCityId());
        nominee.setDistrict(address.getDistrictId());
        nominee.setStateId(address.getStateId());
        nominee.setCountry(address.getCountryId());
        nominee.setPincode(address.getPincode());
        nominee.setPostOfficeId(address.getPostOfficeId());
        nominee.setLatitude(address.getLatitude());
        nominee.setLongitude(address.getLongitude());
        nominee.setDigipin(address.getDigipin());
        nominee.setAddressTypeId(address.getAddressTypeId());
    }

    public NomineeDto toNomineeDto(Nominee nominee) {
        return NomineeDto.builder()
                .fullName(nominee.getFullName())
                .nomineeIdentity(nominee.getIdentity())
                .relationship(nominee.getRelationship())
                .dob(nominee.getDob())
                .contactNumber(nominee.getContactNumber())
                .percentageShare(nominee.getPercentageShare())
                .isMinor(nominee.getIsMinor())
                .guardianName(nominee.getGuardianName())
                .guardianDob(nominee.getGuardianDob())
                .guardianEmail(nominee.getGuardianEmail())
                .guardianContactNumber(nominee.getGuardianContactNumber())
                .isSameAddress(nominee.getIsSameAddress())
                .addressTypeId(nominee.getAddressTypeId())
                .doorNumber(nominee.getHouseNumber())
                .addressLine1(nominee.getStreet())
                .landmark(nominee.getLandmark())
                .placeName(nominee.getPlaceName())
                .cityId(nominee.getCity())
                .districtId(nominee.getDistrict())
                .stateId(nominee.getStateId())
                .countryId(nominee.getCountry())
                .pincode(nominee.getPincode())
                .postOfficeId(nominee.getPostOfficeId())
                .latitude(nominee.getLatitude())
                .longitude(nominee.getLongitude())
                .digipin(nominee.getDigipin())
                .build();
    }

    public NomineeDetailsResponseDto toResponse(Customer customer, String status, List<NomineeDto> nomineeDtos) {
        List<NomineeDetailsResponseDto.NomineeResponseDto> responseList = nomineeDtos.stream()
                .map(n -> NomineeDetailsResponseDto.NomineeResponseDto.builder()
                        .nomineeIdentity(n.getNomineeIdentity())
                        .fullName(n.getFullName())
                        .relationship(n.getRelationship())
                        .dob(n.getDob())
                        .contactNumber(n.getContactNumber())
                        .isSameAddress(n.getIsSameAddress())
                        .percentageShare(n.getPercentageShare())
                        .isMinor(n.getIsMinor())
                        .guardianName(n.getGuardianName())
                        .guardianDob(n.getGuardianDob())
                        .guardianEmail(n.getGuardianEmail())
                        .guardianContactNumber(n.getGuardianContactNumber())
                        .addressTypeId(n.getAddressTypeId())
                        .doorNumber(n.getDoorNumber())
                        .addressLine1(n.getAddressLine1())
                        .landmark(n.getLandmark())
                        .placeName(n.getPlaceName())
                        .cityId(n.getCityId())
                        .districtId(n.getDistrictId())
                        .stateId(n.getStateId())
                        .countryId(n.getCountryId())
                        .pincode(n.getPincode())
                        .postOfficeId(n.getPostOfficeId())
                        .latitude(n.getLatitude())
                        .longitude(n.getLongitude())
                        .digipin(n.getDigipin())
                        .build())
                .toList();

        return NomineeDetailsResponseDto.builder()
                .identity(customer.getIdentity())
                .customerCode(customer.getCustomerCode())
                .status(status)
                .nominees(responseList)
                .build();
    }

    public NomineeDetailsResponseDto toResponse(Customer customer, String status, NomineeDto nomineeDto) {
        return toResponse(customer, status, List.of(nomineeDto));
    }
    public NomineeAddressDto toNomineeAddressDtoFromCustomerAddress(CustomerAddress addr) {
        if (addr == null) return null;
        return NomineeAddressDto.builder()
                .addressTypeId(addr.getAddressTypeId())
                .doorNumber(addr.getDoorNumber())
                .addressLine1(addr.getAddressLine1())
                .landmark(addr.getLandmark())
                .placeName(addr.getPlaceName())
                .cityId(addr.getCityId())
                .districtId(addr.getDistrictId())
                .stateId(addr.getStateId())
                .countryId(addr.getCountryId())
                .pincode(addr.getPincode())
                .postOfficeId(addr.getPostOfficeId())
                .latitude(addr.getLatitude())
                .longitude(addr.getLongitude())
                .digipin(addr.getDigipin())
                .build();
    }


    public  Integer getCreatedBy(){
        return CommonConstants.CREATED_BY;

    }
    public  Integer getUpdatedBy(){
        return CommonConstants.UPDATED_BY;

    }
}
