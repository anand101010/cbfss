package com.incede.nbfc.core.monolith.customer.domain.entity;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.DocumentType;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * KycDocument entity for storing customer KYC details.
 * Supports multiple document types like Aadhar, PAN, etc.
 *
 *
 * @version 1.0.0
 */
@Entity
@Table(name = "customer_kyc", schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerKyc extends CustomerBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kycId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id")
    @NotNull(message = "Customer reference must not be null")
    private Customer customer;

    @NotNull(message = "ID type is required")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_type", nullable = false, referencedColumnName = "document_type_id")
    private DocumentType idType;

    @Column(name = "id_number", nullable = false, length = 50)
    @NotBlank(message = "ID number must not be blank")
    @Size(max = 50, message = "ID number must not exceed 50 characters")
    private String idNumber;

    @Column(name = "place_of_issue", length = 100)
    @Size(max = 100, message = "Place of issue must not exceed 100 characters")
    private String placeOfIssue;

    @Column(name = "issuing_authority", length = 100)
    @Size(max = 100, message = "Issuing authority must not exceed 100 characters")
    private String issuingAuthority;

    @Column(name = "valid_from")
    @PastOrPresent(message = "Valid from date cannot be in the future")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    @FutureOrPresent(message = "Valid to date must be today or in the future")
    private LocalDate validTo;

    @Column(name = "document_ref_id", nullable = false)
    @NotNull(message = "Document reference ID is required")
    private Integer documentRefId;

    @Column(name = "is_verified")
    @NotNull(message = "Verification status must be specified")
    private Boolean isVerified = false;

    @Column(name = "is_active")
    @NotNull(message = "Active status must be specified")
    private Boolean isActive = true;



}