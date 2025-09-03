package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotBlank(message = "First name is required")
    private String firstName;


    private String middleName;


    @NotBlank(message = "DisplayName is required")
    private String displayName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Gender is required")
    private Integer gender;

    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    @NotNull(message = "Marital status is required")
    private Integer maritalStatus;



    @NotNull(message = "Tax category is required")
    private Integer taxCategory;

    @NotBlank(message = "CRM Reference ID is required")
    private String crmReferenceId;






    @NotNull(message = "Occupation is required")
    private Integer occupation;

    private String employer;
    private BigDecimal annualIncome;

    private Integer customerListTypeId;

    @NotNull(message = "IsBusiness cannot be null")
    private Boolean isBusiness;

    @NotNull(message = "IsFirm cannot be null")
    private Boolean isFirm;

    @NotNull(message = "Created by is required")
    private Integer createdBy;


    private String guardian;

    private String spouseName;

    private String fatherName;

    private String motherName;


    private Boolean isMinor;

    @NotNull(message = "Customer status is required")
    private Integer customerStatus;

    @NotNull(message = "Locality is required")
    private Integer locality;

    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    private Integer visualScore;



}