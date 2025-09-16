package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
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
     * @param basicInformationRequestDto The request DTO
     * @return Customer entity
     */
    public Customer toEntity(BasicInformationRequestDto basicInformationRequestDto) {
        java.util.Objects.requireNonNull(basicInformationRequestDto, "BasicInformationRequestDto must not be null");


        Customer customer = new Customer();
        customer.setTenantId(basicInformationRequestDto.getTenantId());
        customer.setBranchId(basicInformationRequestDto.getBranchId());
        customer.setFirstName(basicInformationRequestDto.getFirstName());
        customer.setMiddleName(basicInformationRequestDto.getMiddleName());
        customer.setLastName(basicInformationRequestDto.getLastName());
        customer.setDisplayName(basicInformationRequestDto.getAadharName());
        customer.setGender(basicInformationRequestDto.getGender());
        customer.setDob(basicInformationRequestDto.getDob());
        customer.setSalutation(basicInformationRequestDto.getSalutation());
        customer.setMaritalStatus(basicInformationRequestDto.getMaritalStatus());
        customer.setTaxCategory(basicInformationRequestDto.getTaxCategory());
        customer.setOccupation(basicInformationRequestDto.getOccupation());
        customer.setIsBusiness(basicInformationRequestDto.getIsBusiness());
        customer.setIsFirm(basicInformationRequestDto.getIsFirm());
        customer.setCrmReferenceId(basicInformationRequestDto.getCrmReferenceId());
        customer.setCustomerStatus(basicInformationRequestDto.getCustomerStatus());
        customer.setEmployer(basicInformationRequestDto.getEmployer());
        customer.setAnnualIncome(basicInformationRequestDto.getAnnualIncome());
        customer.setFatherName(basicInformationRequestDto.getFatherName());
        customer.setMotherName(basicInformationRequestDto.getMotherName());
        customer.setSpouseName(basicInformationRequestDto.getSpouseName());
        customer.setMobileNumber(basicInformationRequestDto.getMobileNumber());
        customer.setGuardianCustomerId(basicInformationRequestDto.getGuardianCustomerId());
        customer.setOtpIsVerified(basicInformationRequestDto.getOtpVerified());
        customer.setAadharVaultId(basicInformationRequestDto.getAadharVault());
        customer.setIsMinor(basicInformationRequestDto.getIsMinor());
        customer.setCreatedBy(getCreatedBy());

        return customer;
    }



    /**
     * Update an existing Customer entity with values from the DTO.
     *
     * @param customer Existing Customer entity
     * @param basicInformation DTO with updated values
     */
    public void updateEntityFromDto(Customer customer, BasicInformationRequestDto basicInformation) {
        java.util.Objects.requireNonNull(basicInformation, "BasicInformationRequestDto must not be null");

        customer.setTenantId(basicInformation.getTenantId());
        customer.setBranchId(basicInformation.getBranchId());
        customer.setFirstName(basicInformation.getFirstName());
        customer.setMiddleName(basicInformation.getMiddleName());
        customer.setLastName(basicInformation.getLastName());
        customer.setGender(basicInformation.getGender());
        customer.setDob(basicInformation.getDob());
        customer.setSalutation(basicInformation.getSalutation());
        customer.setMaritalStatus(basicInformation.getMaritalStatus());
        customer.setTaxCategory(basicInformation.getTaxCategory());
        customer.setOccupation(basicInformation.getOccupation());
        customer.setIsBusiness(basicInformation.getIsBusiness());
        customer.setIsFirm(basicInformation.getIsFirm());
        customer.setCrmReferenceId(basicInformation.getCrmReferenceId());
        customer.setCustomerStatus(basicInformation.getCustomerStatus());
        customer.setEmployer(basicInformation.getEmployer());
        customer.setAnnualIncome(basicInformation.getAnnualIncome());
        customer.setFatherName(basicInformation.getFatherName());
        customer.setMotherName(basicInformation.getMotherName());
        customer.setSpouseName(basicInformation.getSpouseName() != null ? basicInformation.getSpouseName() : ""); // avoid null
        customer.setIsMinor(basicInformation.getIsMinor() != null ? basicInformation.getIsMinor() : false);
        customer.setDisplayName(basicInformation.getAadharName());

        customer.setMobileNumber(basicInformation.getMobileNumber());

        customer.setGuardianCustomerId(basicInformation.getGuardianCustomerId());

        customer.setOtpIsVerified(basicInformation.getOtpVerified());

        customer.setAadharVaultId(basicInformation.getAadharVault());
        customer.setUpdatedBy(getUpdatedBy());


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
                .salutation(customer.getSalutation())
                .branchId(customer.getBranchId())
                .middleName(customer.getMiddleName())
                .crmReferenceId(customer.getCrmReferenceId())
                .occupation(customer.getOccupation())
                .employer(customer.getEmployer())
                .annualIncome(customer.getAnnualIncome())
                .isBusiness(customer.getIsBusiness())
                .isFirm(customer.getIsFirm())
                .mobileNumber(customer.getMobileNumber())
                .otpVerified(customer.getOtpIsVerified())
                .spouseName(customer.getSpouseName())
                .fatherName(customer.getFatherName())
                .spouseName(customer.getSpouseName())
                .motherName(customer.getMotherName())
                .isMinor(customer.getIsMinor())
                .guardianCustomerId(customer.getGuardianCustomerId())
                .customerStatus(customer.getCustomerStatus())
                .mobileNumber(customer.getMobileNumber())
                .otpVerified(customer.getOtpIsVerified())
                .aadharVaultId(customer.getAadharVaultId())
                .aadharName(customer.getDisplayName())


                .build();

        return BasicInformationResponseDto.builder()
                .identity(customer.getIdentity())
                .customerCode(customer.getCustomerCode())
                .status(customer.getOnboardingStatus())
                .basic(basic)
                .build();
    }

    public  Integer getCreatedBy(){
        return CommonConstants.CREATED_BY;

    }
    public  Integer getUpdatedBy(){
        return CommonConstants.UPDATED_BY;

    }


}
