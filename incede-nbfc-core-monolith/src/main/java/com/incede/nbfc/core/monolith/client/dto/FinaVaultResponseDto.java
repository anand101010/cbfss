package com.incede.nbfc.core.monolith.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinaVaultResponseDto {
    private String status;
    private String uidForDisplay;
    @ToString.Exclude
    private String uid;
    private String uidReferenceKey;
    private String tokenizeErrorCode;
    private String errorCode;
    private String uidTokenHash;
    private String message;

}
