package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.*;
import com.incede.nbfc.core.monolith.customer.dto.*;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.ContactTypes;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

    //CustomerDetails GET

    /**
     *
     *
     * @param customer
     * @param addresses
     * @param customerPhotos
     * @param nominees
     * @param bankAccounts
     * @param contacts
     * @param additionalInfo
     * @return get all details of a custoemr
     */
    /**
     *
     *
     * @param customer
     * @param addresses
     * @param customerPhotos
     * @param nominees
     * @param bankAccounts
     * @param contacts
     * @param additionalInfo
     * @return get all details of a custoemr
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

    private List<CustomerPhotoResponseDto.PhotoDetail> mapPhotoDetails(List<CustomerPhoto> photos) {
        if (photos == null || photos.isEmpty()) return List.of();
        return photos.stream()
                .map(photo -> CustomerPhotoResponseDto.PhotoDetail.builder()
                        .firstname(photo.getCustomer() != null ? photo.getCustomer().getFirstName() : null)
                        .photoId(photo.getPhotoId())
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

    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) return "****";
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }

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
