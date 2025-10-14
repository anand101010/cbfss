package com.incede.nbfc.core.monolith.customer.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerBankAccountResponseDto {

    private UUID identity;
    @ToString.Exclude
    private String customerCode;
    private String status;
    private List<BankAccount> bankAccounts;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BankAccount{


        private String bankName;
        private String branchName;
        private String ifscCode;
        @ToString.Exclude
        private String upiId;
        @ToString.Exclude
        private String accountNumber;
        @ToString.Exclude
        private String maskedAccountNumber;
        private String accountHolderName;
        private UUID accountType;
        private UUID accountStatus;
        private Boolean isPrimary;
        private String pdStatus;
        private Boolean upiVerified;
        private Boolean isActive;
        @ToString.Exclude
        private String bankProofDocumentRefId;
        private String bankProofFilePath;
        private UUID bankAccountIdentity;
    }

}