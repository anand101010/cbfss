package com.incede.nbfc.core.monolith.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddressDetailDto {
    // Customer-level details
    private UUID identity;
    private String customerCode;

    // Address-level details
    private UUID addressIdentity;
    private UUID addressType;
    private String doorNumber;
    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String placeName;
    private String city;
    private String district;
    private String state;
    private String country;
    private String pincode;
    private UUID postOffice;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal geoAccuracy;
    private UUID addressProofType;
    private Boolean isActive;
    private String digipin;
}
