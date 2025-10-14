package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAddress;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressDetailDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CustomerAddressMapper {

    private static final Logger log = LoggerFactory.getLogger(CustomerAddressMapper.class);

    /**
     * Maps DTO to entity for creating a new CustomerAddress.
     * Validates that createdBy is not null.
     */
    public CustomerAddress toEntity(Customer customer, CustomerAddressRequestDto customerAddressRequestDto) {
        log.info("Mapping CustomerAddressRequestDto to CustomerAddress entity for customer: {}", customer.getCustomerCode());
        java.util.Objects.requireNonNull(customerAddressRequestDto, "CustomerAddressRequestDto must not be null");

        CustomerAddress address = new CustomerAddress();
        address.setCustomer(customer);
        address.setIsSameAsPermanent(customerAddressRequestDto.getIsSameAsPermanent());
        address.setDoorNumber(customerAddressRequestDto.getDoorNumber());
        address.setAddressLine1(customerAddressRequestDto.getAddressLine1());
        address.setAddressLine2(customerAddressRequestDto.getAddressLine2());
        address.setLandmark(customerAddressRequestDto.getLandmark());
        address.setPlaceName(customerAddressRequestDto.getPlaceName());
        address.setCity(customerAddressRequestDto.getCity());
        address.setDistrict(customerAddressRequestDto.getDistrict());
        address.setState(customerAddressRequestDto.getState());
        address.setCountry(customerAddressRequestDto.getCountry());
        address.setPincode(customerAddressRequestDto.getPincode());
        address.setLatitude(customerAddressRequestDto.getLatitude());
        address.setLongitude(customerAddressRequestDto.getLongitude());
        address.setGeoAccuracy(customerAddressRequestDto.getGeoAccuracy());
        address.setIsActive(customerAddressRequestDto.getIsActive() != null ? customerAddressRequestDto.getIsActive() : true);
        address.setDigipin(customerAddressRequestDto.getDigipin());
        address.setIdentity(UUID.randomUUID());
        address.setCreatedBy(getCreatedBy());
        address.setDocumentRefId(customerAddressRequestDto.getDocumentRefId());
        address.setFilePath(customerAddressRequestDto.getFilePath());

        log.debug("Created CustomerAddress entity: {}", address);
        return address;
    }

    /**
     * Updates an existing CustomerAddress entity with DTO values.
     * Validates that updatedBy is not null.
     */
    public void updateEntity(CustomerAddress address, CustomerAddressRequestDto customerAddressRequestDto) {
        log.info("Updating CustomerAddress entity: {} with DTO", address.getIdentity());

        address.setDoorNumber(customerAddressRequestDto.getDoorNumber());
        address.setAddressLine1(customerAddressRequestDto.getAddressLine1());
        address.setAddressLine2(customerAddressRequestDto.getAddressLine2());
        address.setLandmark(customerAddressRequestDto.getLandmark());
        address.setPlaceName(customerAddressRequestDto.getPlaceName());
        address.setCity(customerAddressRequestDto.getCity());
        address.setDistrict(customerAddressRequestDto.getDistrict());
        address.setState(customerAddressRequestDto.getState());
        address.setCountry(customerAddressRequestDto.getCountry());
        address.setPincode(customerAddressRequestDto.getPincode());
        address.setLatitude(customerAddressRequestDto.getLatitude());
        address.setLongitude(customerAddressRequestDto.getLongitude());
        address.setGeoAccuracy(customerAddressRequestDto.getGeoAccuracy());
        address.setIsActive(customerAddressRequestDto.getIsActive() != null ? customerAddressRequestDto.getIsActive() : true);
        address.setDigipin(customerAddressRequestDto.getDigipin());
        address.setUpdatedBy(getUpdatedBy());
        address.setDocumentRefId(customerAddressRequestDto.getDocumentRefId());
        address.setFilePath(customerAddressRequestDto.getFilePath());

        log.debug("Updated CustomerAddress entity: {}", address);
    }

    public CustomerAddressResponseDto.AddressDetail toAddressDetail(CustomerAddress address) {
        log.debug("Mapping CustomerAddress entity {} to AddressDetail DTO", address.getIdentity());

        return CustomerAddressResponseDto.AddressDetail.builder()
                .addressIdentity(address.getIdentity())
                .addressProofType(address.getAddressProofType().getIdentity())
                .postOffice(address.getPostOffice().getIdentity())
                .addressType(address.getAddressType().getIdentity())
                .doorNumber(address.getDoorNumber())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .landmark(address.getLandmark())
                .placeName(address.getPlaceName())
                .city(address.getCity())
                .district(address.getDistrict())
                .state(address.getState())
                .country(address.getCountry())
                .pincode(address.getPincode())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .geoAccuracy(address.getGeoAccuracy())
                .isActive(address.getIsActive())
                .digipin(address.getDigipin())
                .documentRefId(address.getDocumentRefId())
                .filePath(address.getFilePath())
                .build();
    }

    public CustomerAddressResponseDto toResponse(Customer customer, String status, List<CustomerAddressResponseDto.AddressDetail> addressDetails) {
        log.info("Mapping Customer {} and {} addresses to CustomerAddressResponseDto", customer.getCustomerCode(), addressDetails.size());

        return CustomerAddressResponseDto.builder()
                .identity(customer.getIdentity())
                .customerCode(customer.getCustomerCode())
                .status(status)
                .addresses(addressDetails)
                .build();
    }

    public Integer getCreatedBy() {
        log.debug("Returning createdBy constant: {}", CommonConstants.CREATED_BY);
        return CommonConstants.CREATED_BY;
    }

    public Integer getUpdatedBy() {
        log.debug("Returning updatedBy constant: {}", CommonConstants.UPDATED_BY);
        return CommonConstants.UPDATED_BY;
    }

    public CustomerAddressDetailDto mapToCustomerAddressDetailDto(Customer customer, CustomerAddress address) {
        return new CustomerAddressDetailDto(
                customer.getIdentity(),
                customer.getCustomerCode(),
//                customer.getStatus(),
                address.getIdentity(),
                address.getAddressType().getIdentity(),
                address.getDoorNumber(),
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getLandmark(),
                address.getPlaceName(),
                address.getCity(),
                address.getDistrict(),
                address.getState(),
                address.getCountry(),
                address.getPincode(),
                address.getPostOffice() != null ? address.getPostOffice().getIdentity() : null,
                address.getLatitude(),
                address.getLongitude(),
                address.getGeoAccuracy(),
                address.getAddressProofType() != null ? address.getAddressProofType().getIdentity() : null,
                address.getIsActive(),
                address.getDigipin()
        );
    }
}
