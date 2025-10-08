package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSearchRequestDto {

    private String branchCode;
    private Integer branchId;
    private String mobileNumber;
    private String emailId;
    private String panCard;
    private String aadhaarNumber;
    private String voterId;
    private String passportNumber;
    private String customerName;
}