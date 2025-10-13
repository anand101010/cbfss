package com.incede.nbfc.core.monolith.lead.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadAddress;
import com.incede.nbfc.core.monolith.lead.dto.LeadAddressRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadAddressResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LeadAddressMapper {

    private static final Logger log = LoggerFactory.getLogger(LeadAddressMapper.class);

    public LeadAddress toEntity(Lead lead, LeadAddressRequestDto dto) {
        log.info("Mapping LeadAddressRequestDto to LeadAddress entity for lead: {}", lead.getLeadCode());
        java.util.Objects.requireNonNull(dto, "LeadAddressRequestDto must not be null");

        LeadAddress address = new LeadAddress();
        address.setLead(lead);
        address.setHouseNo(dto.getHouseNo());
        address.setStreetName(dto.getStreetName());
        address.setPlaceName(dto.getPlaceName());
        address.setLandmark(dto.getLandmark());
        address.setPincode(dto.getPincode());
        address.setCountry(dto.getCountry());
        address.setState(dto.getState());
        address.setDistrict(dto.getDistrict());
        address.setCity(dto.getCity());
        address.setLatitude(dto.getLatitude());
        address.setLongitude(dto.getLongitude());
        address.setDigipin(dto.getDigipin());
        address.setIdentity(UUID.randomUUID());
        address.setCreatedBy(getCreatedBy());

        log.debug("Created LeadAddress entity: {}", address);
        return address;
    }

    public void updateEntityFromDto(LeadAddress address, LeadRequestDto.AddressDto dto) {
        log.info("Updating LeadAddress entity: {} with DTO", address.getIdentity());

        address.setHouseNo(dto.getHouseNo());
        address.setStreetName(dto.getStreetName());
        address.setPlaceName(dto.getPlaceName());
        address.setLandmark(dto.getLandmark());
        address.setPincode(dto.getPincode());
        address.setCountry(dto.getCountry());
        address.setState(dto.getState());
        address.setDistrict(dto.getDistrict());
        address.setCity(dto.getCity());
        address.setLatitude(dto.getLatitude());
        address.setLongitude(dto.getLongitude());
        address.setDigipin(dto.getDigipin());
        address.setUpdatedBy(getUpdatedBy());

        log.debug("Updated LeadAddress entity: {}", address);
    }

    public LeadAddressResponseDto.AddressDetail toAddressDetail(LeadAddress address) {
        log.debug("Mapping LeadAddress entity {} to AddressDetail DTO", address.getIdentity());

        return LeadAddressResponseDto.AddressDetail.builder()
                .addressIdentity(address.getIdentity())
                .addressType(address.getAddressType().getIdentity())
                .houseNumber(address.getHouseNo())
                .streetName(address.getStreetName())
                .placeName(address.getPlaceName())
                .landmark(address.getLandmark())
                .pincode(address.getPincode())
                .country(address.getCountry())
                .state(address.getState())
                .district(address.getDistrict())
                .postOffice(address.getPostOfficeId().getIdentity())
                .city(address.getCity())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .addressProofType(address.getAddressProofType().getIdentity())
                .digipin(address.getDigipin())
                .build();
    }

    public LeadAddressResponseDto toResponse(Lead lead, String status, LeadAddressResponseDto.AddressDetail addressDetail) {
        log.info("Mapping Lead {} and address {} to LeadAddressResponseDto", lead.getLeadCode(), addressDetail.getAddressIdentity());

        return LeadAddressResponseDto.builder()
                .identity(lead.getIdentity())
                .leadCode(lead.getLeadCode())
                .status(status)
                .address(addressDetail)
                .build();
    }

    public Integer getCreatedBy() {
        return CommonConstants.CREATED_BY;
    }

    public Integer getUpdatedBy() {
        return CommonConstants.UPDATED_BY;
    }

}


