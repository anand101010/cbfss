package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.dto.BasicInformationRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.BasicInformationResponseDto;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Customer entity and DTOs.
 *
 * Author: Incede NBFC Development Team
 * Version: 1.0.0
 */
@Component
public class BasicInformationMapper {

    /**
     * Convert a request DTO to a Customer entity.
     *
     * @param dto The request DTO
     * @return Customer entity
     */
    public Customer toEntity(BasicInformationRequestDto dto) {
        Customer customer = new Customer();
        customer.setTenantId(dto.getTenantId());
        customer.setBranchId(dto.getBranchId());
        customer.setFirstName(dto.getFirstName());
        customer.setMiddleName(dto.getMiddleName());
        customer.setLastName(dto.getLastName());
        customer.setDisplayName(dto.getDisplayName()); // required
        customer.setGender(dto.getGender());
        customer.setDob(dto.getDob());
        customer.setSalutation(dto.getSalutation());
        customer.setMaritalStatus(dto.getMaritalStatus());
        customer.setTaxCategory(dto.getTaxCategory());
        customer.setOccupation(dto.getOccupation());
        customer.setIsBusiness(dto.getIsBusiness());
        customer.setIsFirm(dto.getIsFirm());
        customer.setCrmReferenceId(dto.getCrmReferenceId());
        customer.setCustomerStatus(dto.getCustomerStatus());
        customer.setEmployer(dto.getEmployer());
        customer.setAnnualIncome(dto.getAnnualIncome());
        customer.setFatherName(dto.getFatherName());
        customer.setMotherName(dto.getMotherName());
        customer.setSpouseName(dto.getSpouseName() != null ? dto.getSpouseName() : ""); // avoid null
        customer.setIsMinor(dto.getIsMinor() != null ? dto.getIsMinor() : false);
        customer.setDisplayName(dto.getDisplayName());
        return customer;
    }


    /**
     * Update an existing Customer entity with values from the DTO.
     *
     * @param customer Existing Customer entity
     * @param dto DTO with updated values
     */
    public void updateEntityFromDto(Customer customer, BasicInformationRequestDto dto) {
        customer.setTenantId(dto.getTenantId());
        customer.setBranchId(dto.getBranchId());
        customer.setFirstName(dto.getFirstName());
        customer.setMiddleName(dto.getMiddleName());
        customer.setLastName(dto.getLastName());
        customer.setGender(dto.getGender());
        customer.setDob(dto.getDob());
        customer.setMaritalStatus(dto.getMaritalStatus());
        customer.setTaxCategory(dto.getTaxCategory());
        customer.setOccupation(dto.getOccupation());
        customer.setIsBusiness(dto.getIsBusiness());
        customer.setIsFirm(dto.getIsFirm());
        customer.setCrmReferenceId(dto.getCrmReferenceId());
        customer.setCustomerStatus(dto.getCustomerStatus());
        customer.setEmployer(dto.getEmployer());
        customer.setAnnualIncome(dto.getAnnualIncome());
        customer.setFatherName(dto.getFatherName());
        customer.setMotherName(dto.getMotherName());
        customer.setSpouseName(dto.getSpouseName() != null ? dto.getSpouseName() : ""); // avoid null
        customer.setIsMinor(dto.getIsMinor() != null ? dto.getIsMinor() : false);
        customer.setDisplayName(dto.getDisplayName());
    }

    /**
     * Convert a Customer entity to a response DTO.
     *
     * @param customer The customer entity
     * @return BasicInformationResponseDto
     */
    public BasicInformationResponseDto toResponseDto(Customer customer) {
        BasicInformationResponseDto.Basic basic = BasicInformationResponseDto.Basic.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dob(customer.getDob())
                .gender(customer.getGender())
                .maritalStatus(customer.getMaritalStatus())
                .taxCategory(customer.getTaxCategory())

                .preferredLanguage(customer.getPreferredLanguageId())
                .residentialStatus(customer.getResidentialStatusId())
                .build();

        return BasicInformationResponseDto.builder()
                .identity(customer.getIdentity())
                .customerCode(customer.getCustomerCode())
                .status(customer.getOnboardingStatus())
                .basic(basic)
                .build();
    }
}
