package com.incede.nbfc.core.monolith.customer.dto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerSearchResponseDto {
    private UUID customerIdentity;
    private Boolean isCustomerExist;
    private Boolean isLeadExist;
    private String branchCode;
    private String displayName;
    private String customerCode;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fatherName;
    private String houseName;
    private String mobile;
    private String city;

}
