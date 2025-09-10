package com.incede.nbfc.core.monolith.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

/**
 * Request DTO for validating a bank account using the external Decentro API.
 * <p>
 * Contains reference details, transaction purpose, transfer amount,
 * and beneficiary details (account number and IFSC).
 */
@Data
public class BankValidationRequestDto {

    @JsonProperty("reference_id")
    private String referenceId;

    @JsonProperty("purpose_message")
    private String purposeMessage;

    @JsonProperty("transfer_amount")
    private String transferAmount;

    @JsonProperty("beneficiary_details")
    private BeneficiaryDetails beneficiaryDetails;

    /**
     * Inner class representing beneficiary details for account validation.
     */
    @Data
    public static class BeneficiaryDetails {

        @JsonProperty("account_number")
        @ToString.Exclude
        private String accountNumber;

        @JsonProperty("ifsc")
        @ToString.Exclude
        private String ifsc;
    }
}
