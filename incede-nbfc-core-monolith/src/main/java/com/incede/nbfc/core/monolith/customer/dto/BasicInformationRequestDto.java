package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicInformationRequestDto {

    @NotNull(message = "Tenant ID is required")
    private Integer tenantId;

    @NotNull(message = "Salutation is required")
    private Integer salutation;

    @NotNull(message = "BranchId is required")
    private Integer branchId;

    @ToString.Exclude
    @NotBlank(message = "First name is required")
    private String firstName;

    @ToString.Exclude
    private String middleName;

    @NotBlank(message = "aadharName is required")
    @ToString.Exclude
    private String aadharName;

    @NotBlank(message = "Last name is required")
    @ToString.Exclude
    private String lastName;

    @NotNull(message = "Gender is required")
    private Integer gender;

    @ToString.Exclude
    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    @NotNull(message = "Marital status is required")
    private Integer maritalStatus;

    @NotNull(message = "Tax category is required")
    private Integer taxCategory;

    @ToString.Exclude
    @NotBlank(message = "CRM Reference ID is required")
    private String crmReferenceId;

    @NotNull(message = "Occupation is required")
    private Integer occupation;

    @ToString.Exclude
    private String employer;

    @ToString.Exclude
    private BigDecimal annualIncome;

    private Integer customerListTypeId;

    @NotNull(message = "IsBusiness cannot be null")
    private Boolean isBusiness;

    @NotNull(message = "IsFirm cannot be null")
    private Boolean isFirm;


    @ToString.Exclude
    private String spouseName;

    @ToString.Exclude
    private String fatherName;

    @ToString.Exclude
    private String motherName;

    private Boolean isMinor;

    @NotNull(message = "Customer status is required")
    private Integer customerStatus;

    private Integer visualScore;

    @ToString.Exclude
    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;


    private Boolean otpVerified;

    private Integer guardianCustomerId;

    private Integer aadharVaultId;


    private Integer createdBy;

    private Integer updatedBy;


}
