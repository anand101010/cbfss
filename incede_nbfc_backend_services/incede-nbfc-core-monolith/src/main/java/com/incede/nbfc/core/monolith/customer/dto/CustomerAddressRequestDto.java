package com.incede.nbfc.core.monolith.customer.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAddressRequestDto {

    @NotNull(message = "Address typeId is required")
    private UUID addressType;

    @NotBlank(message = "Door number must not be blank")
    @Size(max = 50, message = "Door number must not exceed 50 characters")
    private String doorNumber;

    @NotBlank(message = "Address line 1 must not be blank")
    @Size(max = 100, message = "Address line 1 must not exceed 100 characters")
    private String addressLine1;


    @NotNull(message = "Document Reference ID is required")
    private String documentRefId;

    @Size(max = 100, message = "Address line 2 must not exceed 100 characters")
    private String addressLine2;

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

    private Boolean isSameAsPermanent;

    @Digits(integer = 3, fraction = 6, message = "Longitude must be valid")
    private BigDecimal longitude;

    @Digits(integer = 5, fraction = 2, message = "Geo accuracy must be valid")
    private BigDecimal geoAccuracy;

    @NotNull(message = "Address proof type is required")
    private UUID addressProofType;

    private Boolean isActive;

    private Boolean isDel;

    @ToString.Exclude
    @Size(max = 30, message = "Digipin must not exceed 30 characters")
    private String digipin;

    @ToString.Exclude
    private String customerCode;

    private String filePath;
}
