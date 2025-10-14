package com.incede.nbfc.core.monolith.customer.domain.entity;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "address_type", nullable = false, referencedColumnName = "address_type_id")
    @NotNull(message = "Address type must not be null")
    private AddressType addressType;



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

    @Column(name = "city")
    @NotNull(message = "city must not be null")
    private String city;

    @Column(name = "district")
    @NotNull(message = "district must not be null")
    private String district;

    @Column(name = "state")
    @NotNull(message = "state must not be null")
    private String state;

    @Column(name = "country")
    @NotNull(message = "country must not be null")
    private String country;

    @Column(name = "pincode", length = 6)
    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be exactly 6 digits")
    private String pincode;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "post_office_id", nullable = false, referencedColumnName = "post_office_id")
    private PostOffices postOffice;


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
    @ToString.Exclude
    private String digipin;

    @ManyToOne
    @JoinColumn(name = "address_proof_type", nullable = false, referencedColumnName = "address_proof_type_id")
    private AddressProofType addressProofType;

    @Column(name = "is_sameAs_permanent")
    private Boolean isSameAsPermanent = false;

    @Column(name = "document_ref_id")
    @NotNull(message = "Document reference ID is required")
    private String documentRefId;

    @Column(name = "file_path")
    @NotNull(message = "File path is required")
    private String filePath;

    @Column(name = "is_active")
    private Boolean isActive = true;

}
