package com.incede.nbfc.core.monolith.lead.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadResponseDto {

    private UUID leadIdentity;
    private String leadCode;
    private String status;
    private LeadDetails leadDetails;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LeadDetails {

        private String fullName;
        private UUID gender;
        private String contactNumber;
        private String email;
        private UUID leadSourceIdentity;
        private UUID leadStageIdentity;
        private UUID leadStatusIdentity;
        private UUID assignTo;
        private String remarks;
        private UUID interestedProductIdentity;
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
}
