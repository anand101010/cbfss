package com.incede.nbfc.core.monolith.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoLivenessRequestDto
{


    @JsonProperty("reference_id")
    private String referenceId;
    private Boolean consent;
    private String purpose;

    @NotNull(message = "Image is required")
    @JsonProperty("image")
    @ToString.Exclude
    String base64EncodedImage;
}
