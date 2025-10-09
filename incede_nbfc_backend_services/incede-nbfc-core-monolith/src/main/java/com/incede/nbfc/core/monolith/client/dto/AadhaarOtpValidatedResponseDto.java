package com.incede.nbfc.core.monolith.client.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class AadhaarOtpValidatedResponseDto {

    private String decentroTxnId;
    private String status;
    private String responseCode;
    private String message;
    private DataDto data;
    @ToString.Exclude
    private String responseKey;

    @Data
    public static class DataDto {
        private String aadhaarReferenceNumber;
        private ProofOfIdentityDto proofOfIdentity;
        private ProofOfAddressDto proofOfAddress;
        @ToString.Exclude
        private String image;
    }

    @Data
    public static class ProofOfIdentityDto {
        private String dob;
        private String hashedEmail;
        private String gender;
        private String name;
        @ToString.Exclude
        private String mobileNumber;
    }

    @Data
    public static class ProofOfAddressDto {
        private String careOf;
        private String country;
        private String district;
        private String house;
        private String landmark;
        private String locality;
        private String pincode;
        private String postOffice;
        private String state;
        private String street;
        private String subDistrict;
        private String vtc;
    }
}
