package com.incede.nbfc.core.monolith.lead.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadAddressRequestDto {

    @NotNull(message = "Address typeId is required")
    private UUID addressType;

    @NotBlank(message = "Door number must not be blank")
    @Size(max = 50, message = "Door number must not exceed 50 characters")
    private String doorNumber;

    @NotBlank(message = "Address line 1 must not be blank")
    @Size(max = 100, message = "Address line 1 must not exceed 100 characters")
    private String streetName;

    @Size(max = 100, message = "Landmark must not exceed 100 characters")
    private String landmark;

    @NotBlank(message = "Place name must not be blank")
    @Size(max = 100, message = "Place name must not exceed 100 characters")
    private String placeName;

    @NotBlank(message = "City must not be blank")
    private String city;

    @NotBlank(message = "District must not be blank")
    private String district;

    @NotBlank(message = "State must not be blank")
    private String state;

    @NotBlank(message = "Country must not be blank")
    private String country;

    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be exactly 6 digits")
    private String pincode;

    @NotNull(message = "Post office ID is required")
    private UUID postOfficeId;

    @Digits(integer = 3, fraction = 6, message = "Latitude must be valid")
    private BigDecimal latitude;

    @Digits(integer = 3, fraction = 6, message = "Longitude must be valid")
    private BigDecimal longitude;

    @NotNull(message = "Address proof type is required")
    private UUID addressProofType;

    private Boolean isActive;

    private Boolean isDel;

    @ToString.Exclude
    @Size(max = 30, message = "Digipin must not exceed 30 characters")
    private String digipin;

}
