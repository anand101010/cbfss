package com.incede.nbfc.core.monolith.customer.domain.entity;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing customer liabilities such as loans or credit obligations.
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customer_liabilities", schema = "customers")
public class CustomerLiability implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liability_id")
    private Integer liabilityId;

    @ManyToOne(fetch= FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name="customer_Id",referencedColumnName = "customer_id")
    @NotNull(message="CustomerId must not be null")
    private Customer customer;

    @Column(name = "liability_type_id", nullable = false)
    @NotNull(message = "Liability type ID is required")
    @Min(value = 1, message = "Liability type id must be a positive integer")
    private Integer liabilityTypeId;

    @Column(name = "institution_name", length = 120)
    @Size(max = 120, message = "Institution name must not exceed 120 characters")
    @NotBlank(message = "Institution name must not be blank")
    private String institutionName;

    @Column(name = "outstanding_amount", precision = 15, scale = 2)
    @Digits(integer = 15, fraction = 2)
    @DecimalMin(value = "0.00", inclusive = true, message = "Outstanding amount must be non-negative")
    private BigDecimal outstandingAmount;

    @Column(name = "emi_amount", precision = 15, scale = 2)
    @Digits(integer = 15, fraction = 2)
    @DecimalMin(value = "0.00", inclusive = true, message = "EMI amount must be non-negative")
    private BigDecimal emiAmount;

    @Column(name = "since_date")
    @PastOrPresent(message = "Since date must be in the past or present")
    private LocalDate sinceDate;

    @Column(name = "document_ref_id")
    private Integer documentRefId;

}