package com.incede.nbfc.core.monolith.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AadhaarMaskingRequestDto
{
    @NotBlank(message = "image is mandatory")
    @ToString.Exclude
    @JsonProperty("aadhar_image")
    private String aadhaarImage;
    @JsonProperty("image_format")
    private String imageFormat;
    @JsonProperty("image_uuid")
    private String imageUuid;
    @JsonProperty("pdf_out")
    private boolean pdfOut;
    @JsonProperty("detection_based_masking")
    private boolean detectionBasedMasking;


}
