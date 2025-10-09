package com.incede.nbfc.core.monolith.customer.domain.entity;


import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;



/**
 * Entity for maintaining Nominee Details .
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "nominees", schema = "customers")
public class Nominee extends CustomerBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nominee_id")
    private Integer nomineeId;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id")
    @NotNull(message = "Customer reference must not be null")
    private Customer customer;

    @Column(name = "full_name", nullable = false, length = 100)
    @NotBlank(message = "Full name must not be blank")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "relationship", nullable = false, referencedColumnName = "relationship_id")
    @NotNull(message = "Relationship must not be null")
    private Relationships relationship;

    @Column(name = "dob")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @Column(name = "contact_number", length = 15)
    private String contactNumber;

    @Column(name = "is_same_address")
    private Boolean isSameAddress = false;

    @Column(name = "house_number", length = 50)
    @Size(max = 50, message = "House number must not exceed 50 characters")
    private String houseNumber;

    @Column(name = "street", length = 100)
    @Size(max = 100, message = "Street must not exceed 100 characters")
    private String street;

    @Column(name = "landmark", length = 100)
    @Size(max = 100, message = "Landmark must not exceed 100 characters")
    private String landmark;

    @Column(name = "place_name", length = 50, nullable = false)
    @NotBlank(message = "Place name must not be blank")
    @Size(max = 50, message = "Place name must not exceed 50 characters")
    private String placeName;


    @Column(name = "city", nullable = false)
    @NotNull(message = "City must not be null")
    private String city;

    @Column(name = "district", nullable = false)
    @NotNull(message = "District must not be null")
    private String district;

    @Column(name = "state", nullable = false)
    @NotNull(message = "State must not be null")
    private String state;


    @Column(name = "country", nullable = false)
    @NotNull(message = "Country must not be null")
    private String country;



    @Column(name = "pincode", nullable = false)

    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be exactly 6 digits")
    private String pincode;



    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "address_type_id", nullable = false, referencedColumnName = "address_type_id")
    @NotNull(message = "Address Type must not be null")
    private AddressType addressTypeId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "post_office_id", nullable = false, referencedColumnName = "post_office_id")
    @NotNull(message = "Post Office must not be null")
    private PostOffices postOfficeId;

    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "digipin", length = 100)
    private String digipin;

    @Digits(integer = 5, fraction = 2)
    @Column(name = "percentage_share", precision = 5, scale = 2)
    private BigDecimal percentageShare = new BigDecimal("100.00");

    @Column(name = "is_minor")
    private Boolean isMinor = false;

    @Column(name = "guardian_name", length = 100)
    @Size(max = 100, message = "Guardian name must not exceed 100 characters")
    private String guardianName;

    @Column(name = "guardian_email", length = 100)
    @Size(max = 100, message = "Guardian email must not exceed 100 characters")
    private String guardianEmail;

    @Column(name = "guardian_dob")
    @Past(message = "Guardian's date of birth must be in the past")
    private LocalDate guardianDob;

    @Column(name = "guardian_contact_number", length = 10)
    @Size(max = 10, message = "Guardian contact number must not exceed 10 characters")
    private String guardianContactNumber;
}

