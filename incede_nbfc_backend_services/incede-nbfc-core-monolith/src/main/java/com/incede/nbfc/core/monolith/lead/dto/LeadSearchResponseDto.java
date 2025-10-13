package com.incede.nbfc.core.monolith.lead.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadSearchResponseDto {

    private String leadIdentity;
    private String leadCode;
    private String fullName;
    private UUID gender;
    private String contactNumber;
    private String email;
    private UUID interestedProduct;
    private UUID leadSource;
    private UUID leadStage;
    private UUID leadStatus;
    private String remarks;
    private List<Address> addresses;
    private List<DynamicReference> dynamicReferences;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private UUID addressTypeIdentity;
        private String houseNo;
        private String streetName;
        private String placeName;
        private String pincode;
        private String country;
        private String state;
        private String district;
        private UUID postOfficeIdentity;
        private String city;
        private String landmark;
        private Double latitude;
        private Double longitude;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DynamicReference {
        private UUID referenceConfigIdentity;
        private String referenceFieldValue;
    }
}
