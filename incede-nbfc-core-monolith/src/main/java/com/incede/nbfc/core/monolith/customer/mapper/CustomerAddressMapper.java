package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAddress;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Component
public class CustomerAddressMapper {

    /**
     * Maps DTO to entity for creating a new CustomerAddress.
     * Validates that createdBy is not null.
     */
    public CustomerAddress toEntity(Customer customer, CustomerAddressRequestDto customerAddressRequestDto) {
        java.util.Objects.requireNonNull(customerAddressRequestDto, "CustomerAddressRequestDto must not be null");

        CustomerAddress address = new CustomerAddress();
        address.setCustomer(customer);
        address.setIsSameAsPermanent(customerAddressRequestDto.getIsSameAsPermanent());
        address.setAddressTypeId(customerAddressRequestDto.getAddressTypeId());
        address.setDoorNumber(customerAddressRequestDto.getDoorNumber());
        address.setAddressLine1(customerAddressRequestDto.getAddressLine1());
        address.setAddressLine2(customerAddressRequestDto.getAddressLine2());
        address.setLandmark(customerAddressRequestDto.getLandmark());
        address.setPlaceName(customerAddressRequestDto.getPlaceName());
        address.setCityId(customerAddressRequestDto.getCityId());
        address.setDistrictId(customerAddressRequestDto.getDistrictId());
        address.setStateId(customerAddressRequestDto.getStateId());
        address.setCountryId(customerAddressRequestDto.getCountryId());
        address.setPincode(customerAddressRequestDto.getPincode());
        address.setPostOfficeId(customerAddressRequestDto.getPostOfficeId());
        address.setLatitude(customerAddressRequestDto.getLatitude());
        address.setLongitude(customerAddressRequestDto.getLongitude());
        address.setGeoAccuracy(customerAddressRequestDto.getGeoAccuracy());
        address.setAddressProofTypes(customerAddressRequestDto.getAddressProofType());
        address.setIsActive(customerAddressRequestDto.getIsActive() != null ? customerAddressRequestDto.getIsActive() : true);
        address.setDigipin(customerAddressRequestDto.getDigipin());
        address.setIdentity(UUID.randomUUID());
        address.setCreatedBy(getCreatedBy());

        return address;
    }

    /**
     * Updates an existing CustomerAddress entity with DTO values.
     * Validates that updatedBy is not null.
     */
    public void updateEntity(CustomerAddress address, CustomerAddressRequestDto customerAddressRequestDto) {


        address.setAddressTypeId(customerAddressRequestDto.getAddressTypeId());
        address.setDoorNumber(customerAddressRequestDto.getDoorNumber());
        address.setAddressLine1(customerAddressRequestDto.getAddressLine1());
        address.setAddressLine2(customerAddressRequestDto.getAddressLine2());
        address.setLandmark(customerAddressRequestDto.getLandmark());
        address.setPlaceName(customerAddressRequestDto.getPlaceName());
        address.setCityId(customerAddressRequestDto.getCityId());
        address.setDistrictId(customerAddressRequestDto.getDistrictId());
        address.setStateId(customerAddressRequestDto.getStateId());
        address.setCountryId(customerAddressRequestDto.getCountryId());
        address.setPincode(customerAddressRequestDto.getPincode());
        address.setPostOfficeId(customerAddressRequestDto.getPostOfficeId());
        address.setLatitude(customerAddressRequestDto.getLatitude());
        address.setLongitude(customerAddressRequestDto.getLongitude());
        address.setGeoAccuracy(customerAddressRequestDto.getGeoAccuracy());
        address.setAddressProofTypes(customerAddressRequestDto.getAddressProofType());
        address.setIsActive(customerAddressRequestDto.getIsActive() != null ? customerAddressRequestDto.getIsActive() : true);
        address.setDigipin(customerAddressRequestDto.getDigipin());
        address.setUpdatedBy(getUpdatedBy());
    }

    public CustomerAddressResponseDto.AddressDetail toAddressDetail(CustomerAddress address) {
        return CustomerAddressResponseDto.AddressDetail.builder()
                .addressIdentity(address.getIdentity())
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

    public CustomerAddressResponseDto toResponse(Customer customer, String status, List<CustomerAddressResponseDto.AddressDetail> addressDetails) {
        return CustomerAddressResponseDto.builder()
                .identity(customer.getIdentity())
                .customerCode(customer.getCustomerCode())
                .status(status)
                .addresses(addressDetails)
                .build();
    }

    public  Integer getCreatedBy(){
        return CommonConstants.CREATED_BY;

    }
    public  Integer getUpdatedBy(){
        return CommonConstants.UPDATED_BY;

    }
}
