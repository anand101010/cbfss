package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NomineeDetailsRequestDto {

    private Integer nomineeId;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Relationship is required")
    private Integer relationship;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @Size(max = 10, message = "Contact number must not exceed 10 characters")
    private String contactNumber;

    @DecimalMin(value = "0.0", inclusive = false, message = "Percentage share must be greater than 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Percentage share must not exceed 100")
    private BigDecimal percentageShare = BigDecimal.valueOf(100.00);

    private Boolean isMinor = false;

    private String guardianName;

    @Past(message = "Guardian DOB must be in the past")
    private LocalDate guardianDob;

    @Email(message = "Guardian email must be valid")
    private String guardianEmail;

    @Size(max = 15, message = "Guardian contact number must not exceed 15 characters")
    private String guardianContactNumber;

    private Boolean isSameAddress = false;



    private Integer addressTypeId;

    private String doorNumber;

    private String addressLine1;

    private String landmark;


    private String placeName;

    private Integer cityId;

    private Integer districtId;


    private Integer stateId;


    private Integer countryId;


    private Integer pincode;


    private Integer postOfficeId;

    @ToString.Exclude
    private BigDecimal latitude;

    @ToString.Exclude
    private BigDecimal longitude;

    @ToString.Exclude
    private String digipin;
}
