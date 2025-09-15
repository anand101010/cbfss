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

        private Integer gender;

        private Integer maritalStatus;


        private Integer taxCategory;


        private Integer salutation;

        private Integer branchId;

        private String middleName;

        private String crmReferenceId;

        private Integer occupation;

        private String employer;

        private BigDecimal annualIncome;

        private Integer customerListTypeId;

        private Boolean isBusiness;

        private Boolean isFirm;


        private Integer guardianCustomerId;

        private String spouseName;

        private String fatherName;

        private String motherName;

        private Boolean isMinor;

        private Integer customerStatus;

        private Integer visualScore;

        private String mobileNumber;

        private Integer aadharVaultId;

        private Boolean otpVerified;



    }
}