package com.incede.nbfc.core.monolith.lead.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadAddressResponseDto {

    private UUID identity;
    private String customerCode;
    private String status;
    private List<AddressDetail> addresses;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddressDetail {
        private UUID addressIdentity;
        private UUID addressType;
        private String doorNumber ;
        private String streetName;
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
        private UUID addressProofType;
        private Boolean isActive;
        @ToString.Exclude
        private String digipin;
    }
}