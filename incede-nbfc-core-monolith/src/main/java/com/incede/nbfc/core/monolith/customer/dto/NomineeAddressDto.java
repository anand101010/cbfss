package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NomineeAddressDto {

    @NotNull(message = "Address type must not be null")
    private Integer addressTypeId;

    @NotBlank(message = "Door number must not be blank")
    private String doorNumber;

    @NotBlank(message = "Address line1 must not be blank")
    private String addressLine1;

    private String landmark;

    @NotBlank(message = "Place name must not be blank")
    private String placeName;

    @NotNull(message = "City ID must not be null")
    private Integer cityId;

    @NotNull(message = "District ID must not be null")
    private Integer districtId;

    @NotNull(message = "State ID must not be null")
    private Integer stateId;

    @NotNull(message = "Country ID must not be null")
    private Integer countryId;

    @NotNull(message = "Pincode must not be null")
    private Integer pincode;

    @NotNull(message = "Post office ID must not be null")
    private Integer postOfficeId;

    @ToString.Exclude
    private BigDecimal latitude;

    @ToString.Exclude
    private BigDecimal longitude;

    @ToString.Exclude
    private String digipin;
}
