package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerBankAccountRequestDto {

    @NotBlank(message = "Bank name must not be blank")
    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    private String bankName;

    @NotBlank(message = "IFSC code must not be blank")
    @Size(max = 20, message = "IFSC code must not exceed 20 characters")
    private String ifscCode;

    @NotBlank(message = "Account number must not be blank")
    @Size(max = 30, message = "Account number must not exceed 30 characters")
    private String accountNumber;

    @Size(max = 50, message = "UPI ID must not exceed 50 characters")
    private String upiId;

    @NotNull(message = "Account type is required")
    private UUID accountType;

    @NotNull(message = "accountStatus type is required")

    private UUID accountStatus;

    @NotBlank(message = "Account holder name must not be blank")
    @Size(max = 100, message = "Account holder name must not exceed 100 characters")
    private String accountHolderName;

    @Size(max = 100, message = "Branch name must not exceed 100 characters")
    private String branchName;

    @Min(value = 1, message = "Bank proof document reference ID must be positive")
    private String  bankProofDocumentRefId;

    @NotNull(message = "Document File path cannot be null")
    private String bankProofFilePath;

    @NotNull(message = "Active status must be specified")
    private Boolean isActive;

    @NotNull(message = "Primary account flag must be specified")
    private Boolean isPrimary;

    @NotNull(message = "UPI verification flag must be specified")
    private Boolean upiVerified;

    @NotBlank(message="pdStatus cannot be blank")
    private String pdStatus;

    @Size(max = 64, message = "PD transaction ID must not exceed 64 characters")
    private String pdTxnId;

    @ToString.Exclude
    private String customerCode;

}