package com.incede.nbfc.core.monolith.domain.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameMatchRequestDto
{
    @JsonProperty("reference_id")
    private String referenceId;
    private String name1;
    private String name2;
    private MatchThreshold matchThreshold;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MatchThreshold
    {
        private String textual;
        private String phonetic;


    }
}
