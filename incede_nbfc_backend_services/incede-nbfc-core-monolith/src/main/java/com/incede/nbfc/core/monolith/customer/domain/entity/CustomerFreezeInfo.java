package com.incede.nbfc.core.monolith.customer.domain.entity;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity to log freeze/unfreeze actions on customers due to compliance or risk triggers.
 *
 * @version 1.0.0
 */
@Entity
@Table(name = "customer_freeze_info", schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFreezeInfo extends CustomerBaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "freeze_id")
    private Integer freezeId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id")
    @NotNull(message = "Customer reference must not be null")
    private Customer customer;

    @Column(name = "freeze_type", nullable = false, length = 20)
    @NotBlank(message = "Freeze type must not be blank")
    @Size(max = 20, message = "Freeze type must not exceed 20 characters")
    private String freezeType;

    @Column(name = "reason", columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "Reason must not be blank")
    @Size(max = 1000, message = "Reason must not exceed 1000 characters") // Optional upper bound
    private String reason;

    @Column(name = "effective_from", nullable = false)
    @NotNull(message = "Effective from date is required")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    @Future(message = "Effective to date must be in the future") // Optional: if applicable
    private LocalDate effectiveTo;

    @Column(name = "status", length = 20)
    @NotBlank(message = "Status must not be blank")
    @Size(max = 20, message = "Status must not exceed 20 characters")
    private String status = "ACTIVE";


}
