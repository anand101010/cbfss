package com.incede.nbfc.core.monolith.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinaVaultResponseDto {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("UidForDisplay")
    private String uidForDisplay;

    @ToString.Exclude
    @JsonProperty("Uid")
    private String uid;

    @JsonProperty("UidReferenceKey")
    private String uidReferenceKey;

    @JsonProperty("TokenizeErrorCode")
    private String tokenizeErrorCode;

    @JsonProperty("ErrorCode")
    private String errorCode;

    private String uidTokenHash;

    private String message;
}
