package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicInformationRequestDto {

    @NotNull(message = "Tenant ID is required")
    private UUID tenantId;

    @NotNull(message = "Salutation is required")
    private UUID salutation;

    @NotNull(message = "BranchId is required")
    private UUID branchId;

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
    private UUID gender;

    @ToString.Exclude
    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    @NotNull(message = "Marital status is required")
    private UUID maritalStatus;

    @NotNull(message = "Tax category is required")
    private UUID taxCategory;

    @NotNull(message = " aadharVaultId is required")
    @ToString.Exclude
    private String aadharVault;

    @ToString.Exclude
    private String crmReferenceId;

    @NotNull(message = "Occupation is required")
    private UUID occupation;

    @ToString.Exclude
    private String employer;

    @ToString.Exclude
    private BigDecimal annualIncome;

    private UUID customerListTypeId;

    @NotNull(message = "IsBusiness cannot be null")
    private Boolean isBusiness;

    @NotNull(message = "IsFirm cannot be null")
    private Boolean isFirm;

    private Boolean isVerified;
    @ToString.Exclude
    private String spouseName;

    @ToString.Exclude
    private String fatherName;

    @ToString.Exclude
    private String motherName;

    private Boolean isMinor;

    @NotNull(message = "Customer status is required")
    private UUID customerStatus;

    private Integer visualScore;

    @ToString.Exclude
    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;


    @NotNull(message = " otpVerified is required")
    private Boolean otpVerified;



    private UUID guardianCustomerId;




}
