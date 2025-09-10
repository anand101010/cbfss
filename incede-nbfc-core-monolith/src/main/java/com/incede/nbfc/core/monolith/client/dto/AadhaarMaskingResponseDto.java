package com.incede.nbfc.core.monolith.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AadhaarMaskingResponseDto
{

    @JsonProperty("image_uuid")
    private String imageUuid;
    @JsonProperty("msg")
    private String message;

    @JsonProperty("aadhaar_detected")
    private boolean aadhaarDetected;

    @JsonProperty("aadhaar_masked")
    private boolean aadhaarMasked;

    @JsonProperty("number_of_pages")
    private int numberOfPages;

    @JsonProperty("utc_time_stamp")
    private String utcTimeStamp;

    @ToString.Exclude
    @JsonProperty("response_image")
    private String responseImage;
}
