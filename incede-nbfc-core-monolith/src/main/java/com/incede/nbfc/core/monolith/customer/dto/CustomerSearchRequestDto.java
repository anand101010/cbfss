package com.incede.nbfc.core.monolith.customer.dto;




import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomerSearchRequestDto {
    @NotNull
    private String branchCode;
    private Integer branchId;
    private Integer mobileNumber;
    private String emailId;
    private String panCard;
    private String aadhaarNumber;
    private String voterId;
    private String passportNumber;
    private String customerName;
}


