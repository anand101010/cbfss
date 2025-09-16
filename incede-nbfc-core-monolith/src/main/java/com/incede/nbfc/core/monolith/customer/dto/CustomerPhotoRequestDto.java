package com.incede.nbfc.core.monolith.customer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.incede.nbfc.core.monolith.customer.enums.PhotoStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPhotoRequestDto {

    @ToString.Exclude
    @NotNull(message = "Photo reference ID is required")
    private Integer photoRefId;

    @NotNull(message = "CapturedBy is required")
    @Min(value = 1, message = "CapturedBy must be a positive integer")
    private Integer capturedBy;

    @ToString.Exclude
    @Digits(integer = 9, fraction = 6, message = "Latitude must have up to 9 digits and 6 decimals")
    @NotNull(message = "Latitude is required")
    private BigDecimal latitude;

    @ToString.Exclude
    @Digits(integer = 9, fraction = 6, message = "Longitude must have up to 9 digits and 6 decimals")
    @NotNull(message = "Longitude is required")
    private BigDecimal longitude;

    @NotNull(message="faceMatchScore should not be null")
    private Integer photo;

    @ToString.Exclude
    @Digits(integer = 5, fraction = 2, message = "Accuracy must have up to 5 digits and 2 decimals")
    @NotNull(message = "Accuracy is required")
    private BigDecimal accuracy;

    @NotBlank(message = "Capture device is required")
    @Size(max = 50, message = "Capture device must not exceed 50 characters")
    private String captureDevice;

    @ToString.Exclude
    @NotBlank(message = "Location description is required")
    @Size(max = 200, message = "Location description must not exceed 200 characters")
    private String locationDescription;

    @NotNull(message = "Capture time is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @PastOrPresent(message = "Capture time must be in the past or present")
    private String captureTime;





}
