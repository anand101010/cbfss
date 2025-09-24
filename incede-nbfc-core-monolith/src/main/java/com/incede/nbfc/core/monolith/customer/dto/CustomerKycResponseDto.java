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
public class CustomerKycResponseDto {

    private UUID identity;
    private Integer customerId;
    private String customerCode;
    private String status;

    private UUID idType;
    private String idNumber;
    private String placeOfIssue;
    private String issuingAuthority;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Integer documentRefId;
    private InitialCustomerProfileDto initialProfile;
}
