package com.incede.nbfc.core.monolith.client.dto;


import lombok.Data;
import lombok.ToString;

@Data
public class AadhaarOtpResponse
{
    private String decentroTxnId;
    private String status;
    private String responseCode;
    private String message;
    @ToString.Exclude
    private String responseKey;
}
