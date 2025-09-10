package com.incede.nbfc.core.monolith.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

        private LocalDate dob;

        private Integer gender;

        private Integer maritalStatus;


        private Integer taxCategory;






    }
}