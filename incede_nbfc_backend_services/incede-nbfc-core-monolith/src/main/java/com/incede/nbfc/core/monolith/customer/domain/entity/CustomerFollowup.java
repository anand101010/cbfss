package com.incede.nbfc.core.monolith.customer.domain.entity;

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
 * Entity for tracking customer follow-ups and interactions.
 *
 *
 * @version 1.0.0
 */
@Entity
@Table(name = "customer_followups", schema = "customers",
        uniqueConstraints = @UniqueConstraint(columnNames = "identity"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFollowup extends CustomerBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "followup_id")
    private Integer followupId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id")
    @NotNull(message = "Customer reference must not be null")
    private Customer customer;

    @Column(name = "staff_id", nullable = false)
    @NotNull(message = "Staff ID is required")
    private Integer staffId;

    @Column(name = "followup_date", nullable = false)
    @NotNull(message = "Follow-up date is required")
    @PastOrPresent(message = "Follow-up date cannot be in the future")
    private LocalDate followupDate;

    @Column(name = "followup_type", nullable = false)
    @NotNull(message = "Follow-up type is required")
    @Min(value = 1,message = "Follow up id must be a positive Integer")
    private Integer followupType;

    @Column(name = "followup_notes")
    @Size(max = 1000, message = "Follow-up notes must not exceed 1000 characters") // Optional upper bound
    private String followupNotes;

    @Column(name = "next_followup_date")
    @Future(message = "Next follow-up date must be in the future") // Optional: if scheduling ahead
    private LocalDate nextFollowupDate;

    @Column(name = "status", nullable = false)
    @NotNull( message = "Status is required")
    private Integer status;


}