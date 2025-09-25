package com.incede.nbfc.core.monolith.customer.dto;

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
public class BasicInformationResponseDto {


    private UUID identity;


    private String customerCode;

    private String status;


    private Basic basic;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Basic {


        private String firstName;

        private String lastName;

        private String aadharName;

        private LocalDate dob;

        private UUID gender;

        private UUID maritalStatus;


        private UUID taxCategory;


        private UUID salutation;

        private UUID branchId;

        private String middleName;

        private String crmReferenceId;

        private UUID occupation;

        private String employer;

        private BigDecimal annualIncome;

        private UUID customerListTypeId;

        private Boolean isBusiness;

        private Boolean isFirm;


        private UUID guardianCustomerId;

        private String spouseName;

        private String fatherName;

        private String motherName;

        private Boolean isMinor;

        private UUID customerStatus;

        private UUID visualScore;

        private String mobileNumber;

        private String aadharVaultId;

        private Boolean otpVerified;



    }
}