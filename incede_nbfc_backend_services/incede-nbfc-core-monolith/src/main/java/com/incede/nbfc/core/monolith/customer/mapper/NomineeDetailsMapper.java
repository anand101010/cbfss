package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAddress;
import com.incede.nbfc.core.monolith.customer.domain.entity.Nominee;
import com.incede.nbfc.core.monolith.customer.dto.NomineeAddressDto;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDetailsResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NomineeDetailsMapper {

    public NomineeDto toNomineeDto(Nominee nominee) {
        return NomineeDto.builder()
                .fullName(nominee.getFullName())
                .nomineeIdentity(nominee.getIdentity())
                .relationship(nominee.getRelationship().getIdentity())
                .dob(nominee.getDob())
                .contactNumber(nominee.getContactNumber())
                .percentageShare(nominee.getPercentageShare())
                .isMinor(nominee.getIsMinor())
                .guardianName(nominee.getGuardianName())
                .guardianDob(nominee.getGuardianDob())
                .guardianEmail(nominee.getGuardianEmail())
                .guardianContactNumber(nominee.getGuardianContactNumber())
                .isSameAddress(nominee.getIsSameAddress())
                .addressTypeId(nominee.getAddressTypeId().getIdentity())
                .doorNumber(nominee.getHouseNumber())
                .addressLine1(nominee.getStreet())
                .landmark(nominee.getLandmark())
                .placeName(nominee.getPlaceName())
                .city(nominee.getCity())
                .district(nominee.getDistrict())
                .state(nominee.getState())
                .country(nominee.getCountry())
                .pincode(nominee.getPincode())
                .postOfficeId(nominee.getPostOfficeId().getIdentity())
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
                        .city(n.getCity())
                        .district(n.getDistrict())
                        .state(n.getState())
                        .country(n.getCountry())
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
                .addressTypeId(addr.getAddressType() != null ? addr.getAddressType().getIdentity() : null)
                .doorNumber(addr.getDoorNumber())
                .addressLine1(addr.getAddressLine1())
                .landmark(addr.getLandmark())
                .placeName(addr.getPlaceName())
                .city(addr.getCity())
                .district(addr.getDistrict())
                .state(addr.getState())
                .country(addr.getCountry())
                .pincode(addr.getPincode())
                .postOfficeId(addr.getPostOffice() != null ? addr.getPostOffice().getIdentity() : null)
                .latitude(addr.getLatitude())
                .longitude(addr.getLongitude())
                .digipin(addr.getDigipin())
                .build();
    }

    public Integer getCreatedBy() {
        return CommonConstants.CREATED_BY;
    }

    public Integer getUpdatedBy() {
        return CommonConstants.UPDATED_BY;
    }
}