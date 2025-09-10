package com.incede.nbfc.core.monolith.client.dto;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaceMatchingResponseDto {

    @ToString.Exclude
    private String decentroTxnId;
    private String status;
    private String responseCode;
    private String message;
    private DataDto data;
    @ToString.Exclude
    private String responseKey;

    @Data
    public static class DataDto {
        private String status;
        private String match;
    }
}
