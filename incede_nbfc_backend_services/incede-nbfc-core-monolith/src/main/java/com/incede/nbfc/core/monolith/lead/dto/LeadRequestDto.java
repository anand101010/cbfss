package com.incede.nbfc.core.monolith.lead.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadRequestDto {

    @NotNull(message = "Tenant ID is required")
    private Integer tenantId;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Gender is required")
    private UUID gender;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Lead source is required")
    private UUID leadSourceIdentity;

    @NotNull(message = "Lead stage is required")
    private UUID leadStageIdentity;

    @NotNull(message = "Lead status is required")
    private UUID leadStatusIdentity;

    @NotNull(message = "Assign to user is required")
    private UUID assignTo;

    private String remarks;

    @NotNull(message = "Interested product/service is required")
    private UUID interestedProductIdentity;
    private List<AddressDto> addresses;
    private List<DynamicReferenceDto> dynamicReferences;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressDto {
        @NotNull(message = "Address type is required")
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DynamicReferenceDto {
        @NotBlank(message = "Reference config identity is required")
        private String referenceConfigIdentity;
        @NotBlank(message = "Reference field value is required")
        private String referenceFieldValue;
    }
}
