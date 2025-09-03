package com.incede.nbfc.core.monolith.customer.domain.entity;



import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entity representing customer flags such as blacklist or fraud indicators.
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customer_flags", schema = "customers")
public class CustomerFlag extends CustomerBaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flag_id")
    private Integer flagId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id")
    @NotNull(message = "Customer reference must not be null")
    private Customer customer;

    @Column(name = "list_type_id", nullable = false)
    @NotNull(message = "List type ID is required")
    private Integer listTypeId;

    @Column(name = "reason", columnDefinition = "TEXT")
    @Size(max = 1000, message = "Reason must not exceed 1000 characters") // Optional upper bound
    private String reason;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    @FutureOrPresent(message = "Effective to date must be today or in the future") // Optional if applicable
    private LocalDate effectiveTo;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

}
