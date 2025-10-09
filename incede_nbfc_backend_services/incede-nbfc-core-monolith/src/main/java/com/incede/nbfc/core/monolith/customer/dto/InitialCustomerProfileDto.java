package com.incede.nbfc.core.monolith.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitialCustomerProfileDto {
    private Integer tenantId;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String branchCode;
    private String customerType;

}
