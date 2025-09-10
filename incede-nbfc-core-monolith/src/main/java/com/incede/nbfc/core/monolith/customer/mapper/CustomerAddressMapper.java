package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAddress;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressRequestDTO;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CustomerAddressMapper {

    /**
     * Maps DTO to entity for creating a new CustomerAddress.
     * Validates that createdBy is not null.
     */
    public CustomerAddress toEntity(Customer customer, CustomerAddressRequestDTO customerAddressRequestDTO) {
        java.util.Objects.requireNonNull(customerAddressRequestDTO, "CustomerAddressRequestDTO must not be null");
        if (customerAddressRequestDTO.getCreatedBy() == null) {
            throw new IllegalArgumentException("createdBy must not be null for creation");
        }

        CustomerAddress address = new CustomerAddress();
        address.setCustomer(customer);
        address.setAddressTypeId(customerAddressRequestDTO.getAddressTypeId());
        address.setDoorNumber(customerAddressRequestDTO.getDoorNumber());
        address.setAddressLine1(customerAddressRequestDTO.getAddressLine1());
        address.setAddressLine2(customerAddressRequestDTO.getAddressLine2());
        address.setLandmark(customerAddressRequestDTO.getLandmark());
        address.setPlaceName(customerAddressRequestDTO.getPlaceName());
        address.setCityId(customerAddressRequestDTO.getCityId());
        address.setDistrictId(customerAddressRequestDTO.getDistrictId());
        address.setStateId(customerAddressRequestDTO.getStateId());
        address.setCountryId(customerAddressRequestDTO.getCountryId());
        address.setPincode(customerAddressRequestDTO.getPincode());
        address.setPostOfficeId(customerAddressRequestDTO.getPostOfficeId());
        address.setLatitude(customerAddressRequestDTO.getLatitude());
        address.setLongitude(customerAddressRequestDTO.getLongitude());
        address.setGeoAccuracy(customerAddressRequestDTO.getGeoAccuracy());
        address.setAddressProofTypes(customerAddressRequestDTO.getAddressProofType());
        address.setIsActive(customerAddressRequestDTO.getIsActive() != null ? customerAddressRequestDTO.getIsActive() : true);
        address.setDigipin(customerAddressRequestDTO.getDigipin());
        address.setIdentity(UUID.randomUUID());
        address.setCreatedBy(customerAddressRequestDTO.getCreatedBy());

        return address;
    }

    /**
     * Updates an existing CustomerAddress entity with DTO values.
     * Validates that updatedBy is not null.
     */
    public void updateEntity(CustomerAddress address, CustomerAddressRequestDTO customerAddressRequestDTO) {
        if (customerAddressRequestDTO.getUpdatedBy() == null) {
            throw new IllegalArgumentException("updatedBy must not be null for update");
        }

        address.setAddressTypeId(customerAddressRequestDTO.getAddressTypeId());
        address.setDoorNumber(customerAddressRequestDTO.getDoorNumber());
        address.setAddressLine1(customerAddressRequestDTO.getAddressLine1());
        address.setAddressLine2(customerAddressRequestDTO.getAddressLine2());
        address.setLandmark(customerAddressRequestDTO.getLandmark());
        address.setPlaceName(customerAddressRequestDTO.getPlaceName());
        address.setCityId(customerAddressRequestDTO.getCityId());
        address.setDistrictId(customerAddressRequestDTO.getDistrictId());
        address.setStateId(customerAddressRequestDTO.getStateId());
        address.setCountryId(customerAddressRequestDTO.getCountryId());
        address.setPincode(customerAddressRequestDTO.getPincode());
        address.setPostOfficeId(customerAddressRequestDTO.getPostOfficeId());
        address.setLatitude(customerAddressRequestDTO.getLatitude());
        address.setLongitude(customerAddressRequestDTO.getLongitude());
        address.setGeoAccuracy(customerAddressRequestDTO.getGeoAccuracy());
        address.setAddressProofTypes(customerAddressRequestDTO.getAddressProofType());
        address.setIsActive(customerAddressRequestDTO.getIsActive() != null ? customerAddressRequestDTO.getIsActive() : true);
        address.setDigipin(customerAddressRequestDTO.getDigipin());
        address.setUpdatedBy(customerAddressRequestDTO.getUpdatedBy());
    }

    public CustomerAddressResponseDTO.AddressDetail toAddressDetail(CustomerAddress address) {
        return CustomerAddressResponseDTO.AddressDetail.builder()
                .addressId(address.getAddressId())
                .addressTypeId(address.getAddressTypeId())
                .doorNumber(address.getDoorNumber())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .landmark(address.getLandmark())
                .placeName(address.getPlaceName())
                .cityId(address.getCityId())
                .districtId(address.getDistrictId())
                .stateId(address.getStateId())
                .countryId(address.getCountryId())
                .pincode(address.getPincode())
                .postOfficeId(address.getPostOfficeId())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .geoAccuracy(address.getGeoAccuracy())
                .addressProofType(address.getAddressProofTypes())
                .isActive(address.getIsActive())
                .digipin(address.getDigipin())
                .build();
    }

    public CustomerAddressResponseDTO toResponse(Customer customer, String status, List<CustomerAddressResponseDTO.AddressDetail> addressDetails) {
        return CustomerAddressResponseDTO.builder()
                .identity(customer.getIdentity())
                .customerCode(customer.getCustomerCode())
                .status(status)
                .addresses(addressDetails)
                .build();
    }
}
