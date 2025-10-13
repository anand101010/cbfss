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


    private UUID addressType;

    private String houseNo;

    @Size(max = 100, message = "Address line 1 must not exceed 100 characters")
    private String streetName;

    @Size(max = 100, message = "Landmark must not exceed 100 characters")
    private String landmark;

    @Size(max = 100, message = "Place name must not exceed 100 characters")
    private String placeName;

    private String city;

    private String district;

    private String state;

    private String country;

    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be exactly 6 digits")
    private String pincode;

    private UUID postOfficeId;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private UUID addressProofType;

    private Boolean isActive;

    private Boolean isDel;

    @ToString.Exclude
    @Size(max = 30, message = "Digipin must not exceed 30 characters")
    private String digipin;

}
