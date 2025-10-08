package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerContact;
import com.incede.nbfc.core.monolith.customer.dto.BasicInformationRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.BasicInformationResponseDto;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.ContactTypes;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

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
        Objects.requireNonNull(basicInformationRequestDto, "BasicInformationRequestDto must not be null");

        Customer customer = new Customer();
        customer.setFirstName(basicInformationRequestDto.getFirstName());
        customer.setMiddleName(basicInformationRequestDto.getMiddleName());
        customer.setLastName(basicInformationRequestDto.getLastName());
        customer.setDisplayName(basicInformationRequestDto.getAadharName());
        customer.setDob(basicInformationRequestDto.getDob());
        customer.setIsBusiness(basicInformationRequestDto.getIsBusiness());
        customer.setIsFirm(basicInformationRequestDto.getIsFirm());
        customer.setCrmReferenceId(basicInformationRequestDto.getCrmReferenceId());
        customer.setEmployer(basicInformationRequestDto.getEmployer());
        customer.setAnnualIncome(basicInformationRequestDto.getAnnualIncome());
        customer.setFatherName(basicInformationRequestDto.getFatherName());
        customer.setMotherName(basicInformationRequestDto.getMotherName());
        customer.setSpouseName(basicInformationRequestDto.getSpouseName());
        customer.setMobileNumber(basicInformationRequestDto.getMobileNumber());
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
        Objects.requireNonNull(customer, "Customer must not be null");
        Objects.requireNonNull(basicInformation, "BasicInformationRequestDto must not be null");

        customer.setFirstName(basicInformation.getFirstName());
        customer.setMiddleName(basicInformation.getMiddleName());
        customer.setLastName(basicInformation.getLastName());
        customer.setDob(basicInformation.getDob());
        customer.setIsBusiness(basicInformation.getIsBusiness());
        customer.setIsFirm(basicInformation.getIsFirm());
        customer.setCrmReferenceId(basicInformation.getCrmReferenceId());
        customer.setEmployer(basicInformation.getEmployer());
        customer.setAnnualIncome(basicInformation.getAnnualIncome());
        customer.setFatherName(basicInformation.getFatherName());
        customer.setMotherName(basicInformation.getMotherName());
        customer.setSpouseName(basicInformation.getSpouseName() != null ? basicInformation.getSpouseName() : "");
        customer.setIsMinor(basicInformation.getIsMinor() != null ? basicInformation.getIsMinor() : false);
        customer.setDisplayName(basicInformation.getAadharName());
        customer.setMobileNumber(basicInformation.getMobileNumber());
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
        Objects.requireNonNull(customer, "Customer must not be null");

        BasicInformationResponseDto.Basic basic = BasicInformationResponseDto.Basic.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dob(customer.getDob())
                .gender(customer.getGender().getIdentity())
                .maritalStatus(customer.getMaritalStatus().getIdentity())
                .taxCategory(customer.getTaxCategory().getIdentity())
                .salutation(customer.getSalutation().getIdentity())
                .branchId(customer.getBranchId().getIdentity())
                .middleName(customer.getMiddleName())
                .crmReferenceId(customer.getCrmReferenceId())
                .occupation(customer.getOccupation().getIdentity())
                .employer(customer.getEmployer())
                .annualIncome(customer.getAnnualIncome())
                .isBusiness(customer.getIsBusiness())
                .isFirm(customer.getIsFirm())
                .mobileNumber(customer.getMobileNumber())
                .otpVerified(customer.getOtpIsVerified())
                .spouseName(customer.getSpouseName())
                .fatherName(customer.getFatherName())
                .motherName(customer.getMotherName())
                .isMinor(customer.getIsMinor())
                .guardianCustomerId(customer.getGuardianCustomer() != null
                        ? customer.getGuardianCustomer().getIdentity()
                        : null)
                .customerStatus(customer.getCustomerStatus().getIdentity())
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
    /**
     * Maps mobile number to CustomerContact entity
     *
     * @param customer Customer entity
     * @param mobileNumber Mobile number
     * @param contactType ContactTypes entity for MOBILE
     * @return CustomerContact entity
     */
    public CustomerContact toCustomerContact(Customer customer, String mobileNumber, ContactTypes contactType,Boolean isVerified) {
        if (customer == null || mobileNumber == null || contactType == null||isVerified==null) {
            return null;
        }

        CustomerContact contact = new CustomerContact();
        contact.setCustomer(customer);
        contact.setContactType(contactType);
        contact.setContactValue(mobileNumber);
        contact.setIsPrimary(true);
        contact.setIsActive(true);
        contact.setIsVerified(isVerified);
        contact.setIsPromotionalOptOut(false);
        contact.setCreatedAt(LocalDateTime.now());
        contact.setCreatedBy(getCreatedBy());

        return contact;
    }

    /**
     * Updates existing CustomerContact entity with new mobile number
     *
     * @param contact Existing CustomerContact entity
     * @param mobileNumber New mobile number
     */
    public void updateCustomerContact(CustomerContact contact, String mobileNumber,Boolean isVerified) {
        if (contact != null && mobileNumber != null) {
            contact.setContactValue(mobileNumber);
            contact.setUpdatedAt(LocalDateTime.now());
            contact.setUpdatedBy(getUpdatedBy());
            contact.setIsVerified(isVerified);
            contact.setIsPrimary(true);
            contact.setIsVerified(true);
        }
    }
    public Integer getCreatedBy() {
        return CommonConstants.CREATED_BY;
    }

    public Integer getUpdatedBy() {
        return CommonConstants.UPDATED_BY;
    }
}
