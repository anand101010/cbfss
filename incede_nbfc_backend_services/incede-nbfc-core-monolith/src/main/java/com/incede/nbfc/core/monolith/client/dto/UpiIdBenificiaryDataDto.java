package com.incede.nbfc.core.monolith.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public  class UpiIdBenificiaryDataDto {
    private String upiVpa;
    private String nameAsPerBank;
    @ToString.Exclude
    private String accountNumber;
    @ToString.Exclude
    private String ifsc;
    private String bankReferenceNumber;
    private String npciTransactionId;
}