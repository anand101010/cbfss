package com.incede.nbfc.core.monolith.customer.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * CustomerAddress entity for managing multiple address types.
 */
@Entity
@Table(name = "customer_addresses", schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddress extends CustomerBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="address_id")
    private Integer addressId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id")
    @NotNull(message = "Customer reference is required")
    private Customer customer;

    @Column(name = "address_type", nullable = false, length = 20)
    @NotBlank(message = "Address type must not be blank")
    @Size(max = 20, message = "Address type must not exceed 20 characters")
    private String addressType;

    @Column(name = "door_number", length = 50)
    @Size(max = 50, message = "Door number must not exceed 50 characters")
    private String doorNumber;

    @Column(name = "address_line1", length = 100)
    @Size(max = 100, message = "Address line 1 must not exceed 100 characters")
    private String addressLine1;

    @Column(name = "address_line2", length = 100)
    @Size(max = 100, message = "Address line 2 must not exceed 100 characters")
    private String addressLine2;

    @Column(name = "landmark", length = 100)
    @Size(max = 100, message = "Landmark must not exceed 100 characters")
    private String landmark;

    @Column(name = "place_name", length = 100)
    @Size(max = 100, message = "Place name must not exceed 100 characters")
    private String placeName;

    @Column(name = "city_id")
    @Min(value = 1,message = "city Id must be a positive Number")
    private Integer cityId;

    @Column(name = "district_id")
    @Min(value = 1,message = " District Id must be a positive Number")
    private Integer districtId;

    @Column(name = "state_id")
    @Min(value = 1,message = "State Id must be a positive Number")
    private Integer stateId;

    @Column(name = "country_id")
    @Min(value = 1,message = "Country Id must be a positive Number")
    private Integer countryId;

    @Column(name = "pincode")
    @Min(value = 100000, message = "Pincode must be at least 6 digits")
    @Max(value = 999999, message = "Pincode must be at most 6 digits")
    private Integer pincode;

    @Column(name = "identity", nullable = false, unique = true)
    @NotNull(message = "Identity UUID is required")
    private UUID identity;

    @Column(name = "post_office_id")
    private Integer postOfficeId;

    @Column(name = "latitude", precision = 9, scale = 6)
    @Digits(integer = 3, fraction = 6, message = "Latitude must be a valid coordinate")
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6)
    @Digits(integer = 9, fraction = 6, message = "Longitude must be a valid coordinate")
    private BigDecimal longitude;

    @Column(name = "geo_accuracy", precision = 5, scale = 2)
    @Digits(integer = 5, fraction = 2, message = "Geo accuracy must be a valid decimal")
    private BigDecimal geoAccuracy;

    @Column(name = "digipin", length = 30, unique = true)
    @Size(max = 30, message = "Digipin must not exceed 30 characters")
    private String digipin;

    @Column(name = "address_proof_type")
    private Integer addressProofTypes;

    @Column(name = "is_active")
    private Boolean isActive = true;
}