
package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.*;
import com.incede.nbfc.core.monolith.customer.dto.*;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.ContactTypes;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Customer entity and DTOs.
 *
 * Author: Incede NBFC Development Team
 * Version: 1.0.0
 */
@Component
public class BasicInformationMapper {

    /**
     * Converts a request DTO to a Customer entity.
     *
     * @param basicInformationRequestDto Request DTO containing customer details
     * @return Mapped Customer entity
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
     * Updates an existing Customer entity with values from a request DTO.
     *
     * @param customer Existing Customer entity to update
     * @param basicInformation Request DTO with updated customer details
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
     * Converts a Customer entity to a response DTO.
     *
     * @param customer Customer entity to map
     * @return BasicInformationResponseDto with customer details
     */
    public BasicInformationResponseDto toResponseDto(Customer customer) {
        Objects.requireNonNull(customer, "Customer must not be null");

        BasicInformationResponseDto.Basic basic = BasicInformationResponseDto.Basic.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dob(customer.getDob())
                .gender(customer.getGender() != null ? customer.getGender().getIdentity() : null)
                .maritalStatus(customer.getMaritalStatus() != null ? customer.getMaritalStatus().getIdentity() : null)
                .taxCategory(customer.getTaxCategory() != null ? customer.getTaxCategory().getIdentity() : null)
                .salutation(customer.getSalutation() != null ? customer.getSalutation().getIdentity() : null)
                .branchId(customer.getBranchId() != null ? customer.getBranchId().getIdentity() : null)
                .middleName(customer.getMiddleName())
                .crmReferenceId(customer.getCrmReferenceId())
                .occupation(customer.getOccupation() != null ? customer.getOccupation().getIdentity() : null)
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
                .customerStatus(customer.getCustomerStatus() != null ? customer.getCustomerStatus().getIdentity() : null)
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
     * Maps a mobile number to a CustomerContact entity.
     *
     * @param customer Customer entity
     * @param mobileNumber Mobile number to map
     * @param contactType ContactTypes entity for the contact
     * @param isVerified Verification status of the contact
     * @return Mapped CustomerContact entity
     */
    public CustomerContact toCustomerContact(Customer customer, String mobileNumber, ContactTypes contactType, Boolean isVerified) {
        if (customer == null || mobileNumber == null || contactType == null || isVerified == null) {
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
     * Updates an existing CustomerContact entity with a new mobile number and verification status.
     *
     * @param contact Existing CustomerContact entity
     * @param mobileNumber New mobile number
     * @param isVerified Verification status of the contact
     */
    public void updateCustomerContact(CustomerContact contact, String mobileNumber, Boolean isVerified) {
        if (contact != null && mobileNumber != null) {
            contact.setContactValue(mobileNumber);
            contact.setUpdatedAt(LocalDateTime.now());
            contact.setUpdatedBy(getUpdatedBy());
            contact.setIsVerified(isVerified);
            contact.setIsPrimary(true);
            contact.setIsVerified(true);
        }
    }

    /**
     * Converts a Customer entity and related entities to a detailed response DTO.
     *
     * @param customer Customer entity
     * @param addresses List of customer addresses
     * @param customerPhotos List of customer photos
     * @param nominees List of customer nominees
     * @param bankAccounts List of customer bank accounts
     * @param contacts List of customer contacts
     * @param additionalInfo Additional customer information
     * @return CustomerDetailResponseDto with all customer details
     */
    public CustomerDetailResponseDto toCustomerDetailResponse(
            Customer customer,
            List<CustomerAddress> addresses,
            List<CustomerPhoto> customerPhotos,
            List<Nominee> nominees,
            List<CustomerBankAccount> bankAccounts,
            List<CustomerContact> contacts,
            CustomerAdditionalInfoResponseDto additionalInfo) {

        return CustomerDetailResponseDto.builder()
                .customerIdentity(customer.getIdentity())
                .branchCode(customer.getBranchId().getBranchCode())
                .customerCode(customer.getCustomerCode())
                .firstName(customer.getFirstName())
                .fatherName(customer.getFatherName())
                .middleName(customer.getMiddleName())
                .lastName(customer.getLastName())
                .displayName(customer.getDisplayName())
                .dob(customer.getDob())
                .maritalStatus(customer.getMaritalStatus() != null ? customer.getMaritalStatus().getStatusName() : null)
                .nationality(customer.getNationality() != null ? customer.getNationality().getNationality() : null)
                .taxCategory(customer.getTaxCategory() != null ? customer.getTaxCategory().getTaxCatName() : null)
                .occupation(customer.getOccupation() != null ? customer.getOccupation().getOccupationName() : null)
                .employer(customer.getEmployer())
                .annualIncome(customer.getAnnualIncome())
                .crmReferenceId(customer.getCrmReferenceId())
                .preferredLanguage(customer.getPreferredLanguageId() != null ? customer.getPreferredLanguageId().getLanguageName() : null)
                .isBusiness(customer.getIsBusiness())
                .isFirm(customer.getIsFirm())
                .isMinor(customer.getIsMinor())
                .riskCategory(customer.getRiskCategory() != null ? customer.getRiskCategory().getCategory() : null)
                .otpIsVerified(customer.getOtpIsVerified())
                .onboardingStatus(customer.getOnboardingStatus())
                .customerCategory(customer.getCategoryId() != null ? customer.getCategoryId().getCategoryName() : null)
                .gender(customer.getGender() != null ? customer.getGender().getGender() : null)
                .branchName(customer.getBranchId() != null ? customer.getBranchId().getBranchName() : null)
                .mobileNumber(customer.getMobileNumber())
                .tenantCode(customer.getTenant() != null ? customer.getTenant().getTenantCode() : null)
                .addresses(mapAddressDetails(addresses))
                .customerPhotoResponseDtos(mapPhotoDetails(customerPhotos))
                .nomineeResponseDtos(mapNomineeDetails(nominees))
                .bankAccountResponseDtos(mapBankAccounts(bankAccounts))
                .contactResponseDtos(mapContacts(contacts))
                .additionalInfo(mapAdditionalInfo(additionalInfo))
                .build();
    }

    /**
     * Maps a list of CustomerAddress entities to AddressDetail DTOs.
     *
     * @param addresses List of customer address entities
     * @return List of AddressDetail DTOs
     */
    private List<CustomerAddressResponseDto.AddressDetail> mapAddressDetails(List<CustomerAddress> addresses) {
        if (addresses == null || addresses.isEmpty()) return List.of();
        return addresses.stream()
                .map(addr -> CustomerAddressResponseDto.AddressDetail.builder()
                        .addressIdentity(addr.getIdentity())
                        .addressType(addr.getAddressType() != null ? addr.getAddressType().getIdentity() : null)
                        .doorNumber(addr.getDoorNumber())
                        .addressLine1(addr.getAddressLine1())
                        .addressLine2(addr.getAddressLine2())
                        .landmark(addr.getLandmark())
                        .placeName(addr.getPlaceName())
                        .city(addr.getCity())
                        .district(addr.getDistrict())
                        .state(addr.getState())
                        .country(addr.getCountry())
                        .pincode(addr.getPincode())
                        .postOffice(addr.getPostOffice() != null ? addr.getPostOffice().getIdentity() : null)
                        .latitude(addr.getLatitude())
                        .longitude(addr.getLongitude())
                        .geoAccuracy(addr.getGeoAccuracy())
                        .addressProofType(addr.getAddressProofType() != null ? addr.getAddressProofType().getIdentity() : null)
                        .isActive(addr.getIsActive())
                        .digipin(addr.getDigipin())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Maps a list of CustomerPhoto entities to PhotoDetail DTOs.
     *
     * @param photos List of customer photo entities
     * @return List of PhotoDetail DTOs
     */
    private List<CustomerPhotoResponseDto.PhotoDetail> mapPhotoDetails(List<CustomerPhoto> photos) {
        if (photos == null || photos.isEmpty()) return List.of();
        return photos.stream()
                .map(photo -> CustomerPhotoResponseDto.PhotoDetail.builder()
                        .firstname(photo.getCustomer() != null ? photo.getCustomer().getFirstName() : null)
                        .photoId(UUID.randomUUID())
                        .photoRefId(photo.getPhotoRefId())
                        .capturedBy(photo.getCapturedBy().getIdentity())
                        .latitude(photo.getLatitude())
                        .longitude(photo.getLongitude())
                        .captureTime(photo.getCaptureTime())
                        .status(photo.getStatus())
                        .accuracy(photo.getAccuracy())
                        .captureDevice(photo.getCaptureDevice())
                        .locationDescription(photo.getLocationDescription())
                        .filePath(photo.getFilePath())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Maps a list of Nominee entities to NomineeResponseDto DTOs.
     *
     * @param nominees List of nominee entities
     * @return List of NomineeResponseDto DTOs
     */
    private List<NomineeDetailsResponseDto.NomineeResponseDto> mapNomineeDetails(List<Nominee> nominees) {
        if (nominees == null || nominees.isEmpty()) return List.of();
        return nominees.stream()
                .map(nominee -> NomineeDetailsResponseDto.NomineeResponseDto.builder()
                        .nomineeIdentity(nominee.getIdentity())
                        .fullName(nominee.getFullName())
                        .relationship(nominee.getRelationship() != null ? nominee.getRelationship().getIdentity() : null)
                        .dob(nominee.getDob())
                        .contactNumber(nominee.getContactNumber())
                        .isSameAddress(nominee.getIsSameAddress())
                        .percentageShare(nominee.getPercentageShare())
                        .isMinor(nominee.getIsMinor())
                        .guardianName(nominee.getGuardianName())
                        .guardianDob(nominee.getGuardianDob())
                        .guardianEmail(nominee.getGuardianEmail())
                        .guardianContactNumber(nominee.getGuardianContactNumber())
                        .addressTypeId(nominee.getAddressTypeId() != null ? nominee.getAddressTypeId().getIdentity() : null)
                        .doorNumber(nominee.getHouseNumber())
                        .landmark(nominee.getLandmark())
                        .placeName(nominee.getPlaceName())
                        .city(nominee.getCity())
                        .district(nominee.getDistrict())
                        .state(nominee.getState())
                        .country(nominee.getCountry())
                        .pincode(nominee.getPincode())
                        .postOfficeId(nominee.getPostOfficeId() != null ? nominee.getPostOfficeId().getIdentity() : null)
                        .latitude(nominee.getLatitude())
                        .longitude(nominee.getLongitude())
                        .digipin(nominee.getDigipin())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Maps a list of CustomerBankAccount entities to BankAccount DTOs.
     *
     * @param bankAccounts List of bank account entities
     * @return List of BankAccount DTOs
     */
    private List<CustomerBankAccountResponseDto.BankAccount> mapBankAccounts(List<CustomerBankAccount> bankAccounts) {
        if (bankAccounts == null || bankAccounts.isEmpty()) return List.of();
        return bankAccounts.stream()
                .map(account -> CustomerBankAccountResponseDto.BankAccount.builder()
                        .bankName(account.getBankName())
                        .branchName(account.getBranchName())
                        .ifscCode(account.getIfscCode())
                        .upiId(account.getUpiId())
                        .accountNumber(account.getAccountNumber())
                        .maskedAccountNumber(maskAccountNumber(account.getAccountNumber()))
                        .accountHolderName(account.getAccountHolderName())
                        .accountType(account.getAccountType() != null ? account.getAccountType().getIdentity() : null)
                        .accountStatus(account.getAccountStatus() != null ? account.getAccountStatus().getIdentity() : null)
                        .isPrimary(account.getIsPrimary())
                        .pdStatus(account.getPdStatus())
                        .upiVerified(account.getUpiVerified())
                        .isActive(account.getIsActive())
                        .bankProofDocumentRefId(account.getBankProofDocumentRefId())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Masks an account number, showing only the last four digits.
     *
     * @param accountNumber Account number to mask
     * @return Masked account number
     */
    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) return "****";
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }

    /**
     * Maps a list of CustomerContact entities to Contact DTOs.
     *
     * @param contacts List of contact entities
     * @return List of Contact DTOs
     */
    private List<CustomerContactResponseDto.Contact> mapContacts(List<CustomerContact> contacts) {
        if (contacts == null || contacts.isEmpty()) return List.of();
        return contacts.stream()
                .map(contact -> CustomerContactResponseDto.Contact.builder()
                        .contactType(contact.getContactType() != null ? contact.getContactType().getIdentity() : null)
                        .isPrimary(contact.getIsPrimary())
                        .contactDetails(contact.getContactValue())
                        .isActive(contact.getIsActive())
                        .isOptOutPromotionalNotification(contact.getIsPromotionalOptOut())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Maps additional customer information to an AdditionalInfo DTO.
     *
     * @param dto Additional customer information DTO
     * @return Mapped AdditionalInfo DTO
     */
    private CustomerDetailResponseDto.AdditionalInfo mapAdditionalInfo(CustomerAdditionalInfoResponseDto dto) {
        if (dto == null) return null;
        return CustomerDetailResponseDto.AdditionalInfo.builder()
                .employment(dto.getAdditional() != null ? dto.getAdditional().getEmployment() : null)
                .referrals(dto.getAdditional() != null ? dto.getAdditional().getReferrals() : null)
                .profileExtra(dto.getAdditional() != null ? dto.getAdditional().getProfileExtra() : null)
                .assets(dto.getAdditional() != null ? dto.getAdditional().getAssets() : null)
                .additionalReferenceValues(dto.getAdditional() != null ? dto.getAdditional().getAdditionalReferenceValueDto() : List.of())
                .build();
    }


    public Integer getCreatedBy() {
        return CommonConstants.CREATED_BY;
    }

    public Integer getUpdatedBy() {
        return CommonConstants.UPDATED_BY;
    }
}
