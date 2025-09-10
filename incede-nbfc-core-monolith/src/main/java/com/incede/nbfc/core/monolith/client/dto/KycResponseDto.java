package com.incede.nbfc.core.monolith.client.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KycResponseDto{

    private String kycStatus;
    private String status;
    private String message;
    private KycResult kycResult;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("error")
    private KycErrorDto Error;
    @ToString.Exclude
    private String responseKey;
    private String responseCode;
    private String requestTimestamp;
    private String responseTimestamp;
    private String decentroTxnId;
}
