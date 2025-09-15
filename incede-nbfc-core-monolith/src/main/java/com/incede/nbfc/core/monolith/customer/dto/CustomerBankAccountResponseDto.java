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
    private String customerCode;
    private String status;
    private List<BankAccount> bankAccounts;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BankAccount{

        private Integer bankAccountId;
        private String bankName;
        private String branchName;
        private String ifscCode;
        private String upiId;
        private String accountNumber;
        private String maskedAccountNumber;
        private String accountHolderName;
        private Integer accountType;
        private String accountStatus;
        private Boolean isPrimary;
        private String pdStatus;
        private Boolean upiVerified;
        private Boolean isActive;
        private Integer bankProofDocumentRefId;
    }

}
