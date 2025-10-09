package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NomineeDto {
    private UUID nomineeIdentity;
    private String fullName;
    private UUID  relationship;
    private LocalDate dob;
    private String contactNumber;
    private Boolean isSameAddress;
    private BigDecimal percentageShare;
    private Boolean isMinor;
    private String guardianName;
    private LocalDate guardianDob;
    private String guardianEmail;
    private String guardianContactNumber;
    private UUID  addressTypeId;
    private String doorNumber;
    private String addressLine1;
    private String landmark;
    private String placeName;
    private String  city;
    private String  district;
    private String  state;
    private String  country;
    private String  pincode;
    private  UUID postOfficeId;
    @ToString.Exclude
    private BigDecimal latitude;
    @ToString.Exclude
    private BigDecimal longitude;
    @ToString.Exclude
    private String digipin;
}
