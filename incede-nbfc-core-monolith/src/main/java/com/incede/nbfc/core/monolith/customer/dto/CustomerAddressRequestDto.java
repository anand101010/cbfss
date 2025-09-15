package com.incede.nbfc.core.monolith.customer.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAddressRequestDto {

    @NotNull(message = "Address typeId is required")
    private Integer addressTypeId;

    @Size(max = 50, message = "Door number must not exceed 50 characters")
    private String doorNumber;

    @Size(max = 100, message = "Address line 1 must not exceed 100 characters")
    private String addressLine1;

    @Size(max = 100, message = "Address line 2 must not exceed 100 characters")
    private String addressLine2;

    @Size(max = 100, message = "Landmark must not exceed 100 characters")
    private String landmark;

    @Size(max = 100, message = "Place name must not exceed 100 characters")
    private String placeName;

    @Min(value = 1, message = "City ID must be a positive number")
    private Integer cityId;

    @Min(value = 1, message = "District ID must be a positive number")
    private Integer districtId;

    @Min(value = 1, message = "State ID must be a positive number")
    private Integer stateId;

    @Min(value = 1, message = "Country ID must be a positive number")
    private Integer countryId;

    @Min(value = 100000, message = "Pincode must be 6 digits")
    @Max(value = 999999, message = "Pincode must be 6 digits")
    private Integer pincode;

    private Integer postOfficeId;

    @Digits(integer = 3, fraction = 6, message = "Latitude must be valid")
    private BigDecimal latitude;

    @Digits(integer = 3, fraction = 6, message = "Longitude must be valid")
    private BigDecimal longitude;

    @Digits(integer = 5, fraction = 2, message = "Geo accuracy must be valid")
    private BigDecimal geoAccuracy;

    private Integer addressProofType;

    private Boolean isActive;

    private Integer createdBy;

    private Integer updatedBy;

    private Boolean isDel;

    @ToString.Exclude
    private String digipin;

    @ToString.Exclude
    private String customerCode;
}
