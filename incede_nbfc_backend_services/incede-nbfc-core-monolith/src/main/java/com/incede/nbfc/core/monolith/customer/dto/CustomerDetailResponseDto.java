package com.incede.nbfc.core.monolith.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailResponseDto {

    private UUID customerIdentity;
    private String customerCode;
    private String firstName;
    private String middleName;
    private String fatherName;

    private String lastName;
    private String displayName;
    private LocalDate dob;
    private String gender;
    private String maritalStatus;
    private String nationality;
    private String taxCategory;
    private String occupation;
    private String employer;
    private BigDecimal annualIncome;
    private String branchName;
    private String branchCode;
    private String crmReferenceId;
    private String preferredLanguage;
    private String mobileNumber;
    private Boolean otpIsVerified;
    private String onboardingStatus;
    private Boolean isFirm;
    private Boolean isBusiness;
    private Boolean isMinor;
    private String riskCategory;
    private String customerCategory;
    private String tenantCode;
    private AdditionalInfo additionalInfo;
    private List<CustomerAddressResponseDto.AddressDetail> addresses;
    private List<CustomerPhotoResponseDto.PhotoDetail> customerPhotoResponseDtos;
    private List<NomineeDetailsResponseDto.NomineeResponseDto> nomineeResponseDtos;
    private List<CustomerBankAccountResponseDto.BankAccount> bankAccountResponseDtos;
    private List<CustomerContactResponseDto.Contact> contactResponseDtos;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdditionalInfo {
        private CustomerEmploymentDto employment;
        private CustomerReferralDto referrals;
        private CustomerProfileExtraDto profileExtra;
        private CustomerAssetDto assets;
        private List<AdditionalReferenceValueDto> additionalReferenceValues;

    }
}