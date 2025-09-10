package com.incede.nbfc.core.monolith.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class  FaceMatchingRequestDto
{
    @JsonProperty("reference_id")
    private String referenceId;

    private String consent;

    @JsonProperty("consent_purpose")
    private String consentPurpose;
    @ToString.Exclude
    @NotNull(message = "Image 1 is required")
    private MultipartFile image1;
    @ToString.Exclude
    @NotNull(message = "Image 2 is required")
    private MultipartFile image2;

}
