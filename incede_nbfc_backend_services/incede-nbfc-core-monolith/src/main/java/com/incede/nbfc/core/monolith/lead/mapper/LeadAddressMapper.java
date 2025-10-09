package com.incede.nbfc.core.monolith.lead.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAddress;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressResponseDto;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadAddress;
import com.incede.nbfc.core.monolith.lead.dto.LeadAddressRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadAddressResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class LeadAddressMapper {

    private static final Logger log = LoggerFactory.getLogger(LeadAddressMapper.class);

    /**
     * Maps DTO to entity for creating a new CustomerAddress.
     * Validates that createdBy is not null.
     */
    public LeadAddress toEntity(Lead lead, LeadAddressRequestDto leadAddressRequestDto) {
     //   log.info("Mapping CustomerAddressRequestDto to CustomerAddress entity for customer: {}", lead.getCustomerCode());
        java.util.Objects.requireNonNull(leadAddressRequestDto, "LeadAddressRequestDto must not be null");

        LeadAddress address = new LeadAddress();
        address.setLead(lead);
        address.setHouseNo(leadAddressRequestDto.getDoorNumber());
        address.setStreetName(leadAddressRequestDto.getStreetName());
        address.setLandmark(leadAddressRequestDto.getLandmark());
        address.setPlaceName(leadAddressRequestDto.getPlaceName());
        address.setCity(leadAddressRequestDto.getCity());
        address.setDistrict(leadAddressRequestDto.getDistrict());
        address.setState(leadAddressRequestDto.getState());
        address.setCountry(leadAddressRequestDto.getCountry());
        address.setPincode(leadAddressRequestDto.getPincode());
        address.setLatitude(leadAddressRequestDto.getLatitude());
        address.setLongitude(leadAddressRequestDto.getLongitude());
     //   address.setIsActive(leadAddressRequestDto.getIsActive() != null ? leadAddressRequestDto.getIsActive() : true);
        address.setDigipin(leadAddressRequestDto.getDigipin());
        address.setIdentity(UUID.randomUUID());
        address.setCreatedBy(getCreatedBy());

        log.debug("Created CustomerAddress entity: {}", address);
        return address;
    }

    /**
     * Updates an existing CustomerAddress entity with DTO values.
     * Validates that updatedBy is not null.
     */
    public void updateEntity(LeadAddress address, LeadAddressRequestDto leadAddressRequestDto) {
        log.info("Updating CustomerAddress entity: {} with DTO", address.getIdentity());

        address.setHouseNo(leadAddressRequestDto.getDoorNumber());
        address.setStreetName(leadAddressRequestDto.getStreetName());
        address.setLandmark(leadAddressRequestDto.getLandmark());
        address.setPlaceName(leadAddressRequestDto.getPlaceName());
        address.setCity(leadAddressRequestDto.getCity());
        address.setDistrict(leadAddressRequestDto.getDistrict());
        address.setState(leadAddressRequestDto.getState());
        address.setCountry(leadAddressRequestDto.getCountry());
        address.setPincode(leadAddressRequestDto.getPincode());
        address.setLatitude(leadAddressRequestDto.getLatitude());
        address.setLongitude(leadAddressRequestDto.getLongitude());
    //    address.setIsActive(leadAddressRequestDto.getIsActive() != null ? customerAddressRequestDto.getIsActive() : true);
        address.setDigipin(leadAddressRequestDto.getDigipin());
        address.setUpdatedBy(getUpdatedBy());

        log.debug("Updated CustomerAddress entity: {}", address);
    }

    public LeadAddressResponseDto.AddressDetail toAddressDetail(LeadAddress address) {
        log.debug("Mapping CustomerAddress entity {} to AddressDetail DTO", address.getIdentity());

        return LeadAddressResponseDto.AddressDetail.builder()
                .addressIdentity(address.getIdentity())
                .addressProofType(address.getAddressProofType().getIdentity())
                .postOffice(address.getPostOfficeId().getIdentity())
                .addressType(address.getAddressType().getIdentity())
                .doorNumber(address.getHouseNo())
                .streetName(address.getStreetName())
                .landmark(address.getLandmark())
                .placeName(address.getPlaceName())
                .city(address.getCity())
                .district(address.getDistrict())
                .state(address.getState())
                .country(address.getCountry())
                .pincode(address.getPincode())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
       //         .isActive(address.getIsActive())
                .digipin(address.getDigipin())
                .build();
    }

//    public LeadAddressResponseDto toResponse(Lead lead, String status, List<LeadAddressResponseDto.AddressDetail> addressDetails) {
//    //    log.info("Mapping Customer {} and {} addresses to CustomerAddressResponseDto", customer.getCustomerCode(), addressDetails.size());
//
//        return CustomerAddressResponseDto.builder()
//                .identity(customer.getIdentity())
//                .customerCode(customer.getCustomerCode())
//                .status(status)
//                .addresses(addressDetails)
//                .build();
//    }

    public Integer getCreatedBy() {
        log.debug("Returning createdBy constant: {}", CommonConstants.CREATED_BY);
        return CommonConstants.CREATED_BY;
    }

    public Integer getUpdatedBy() {
        log.debug("Returning updatedBy constant: {}", CommonConstants.UPDATED_BY);
        return CommonConstants.UPDATED_BY;
    }
}
