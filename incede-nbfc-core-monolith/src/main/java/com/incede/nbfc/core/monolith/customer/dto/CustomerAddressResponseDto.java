package com.incede.nbfc.core.monolith.customer.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAddressResponseDto {

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
        private Integer addressTypeId;
        private String doorNumber;
        private String addressLine1;
        private String addressLine2;
        private String landmark;
        private String placeName;
        private Integer cityId;
        private Integer districtId;
        private Integer stateId;
        private Integer countryId;
        private Integer pincode;
        private Integer postOfficeId;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private BigDecimal geoAccuracy;
        private Integer addressProofType;
        private Boolean isActive;
        @ToString.Exclude
        private String digipin;
    }
}