package com.incede.nbfc.core.monolith.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * DTO representing the response
 * from the external bank account validation API.
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankValidationResponseDto {
    private String status;
    private String decentroTxnId;
    private String accountStatus;
    private String responseCode;
    private String message;
    private String beneficiaryName;
    @ToString.Exclude
    private String bankReferenceNumber;
    private String transactionStatus;
}
