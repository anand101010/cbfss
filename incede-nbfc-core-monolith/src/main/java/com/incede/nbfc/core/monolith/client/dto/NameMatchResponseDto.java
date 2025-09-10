package com.incede.nbfc.core.monolith.client.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NameMatchResponseDto {
    private String decentroTxnId;
    private String status;
    private String responseCode;
    private String message;
    private DataDto data;
    @ToString.Exclude
    private String responseKey;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataDto {
        private MatchResult overallScore;
        private MatchResult firstName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MatchResult {
        private String value1;
        private String value2;
        private PhoneticMatch phoneticMatch;
        private TextualMatch textualMatch;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhoneticMatch {
        private int matchScore;
        private boolean matchStatus;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextualMatch {
        private int matchScore;
        private boolean matchStatus;
    }
}
