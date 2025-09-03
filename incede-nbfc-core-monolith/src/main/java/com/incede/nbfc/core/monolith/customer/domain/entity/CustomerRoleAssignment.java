package com.incede.nbfc.core.monolith.customer.domain.entity;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * CustomerRoleAssignment entity for managing role-based access to digital services.
 *
 *
 * @version 1.0.0
 */
@Entity
@Table(name = "customer_role_assignments", schema = "customers",
        uniqueConstraints = @UniqueConstraint(columnNames = "identity"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRoleAssignment extends CustomerBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_assignment_id")
    private Integer roleAssignmentId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id")
    @NotNull(message = "Customer reference must not be null")
    private Customer customer;

    @Column(name = "role_id", nullable = false)
    @NotNull(message = "Role reference must not be null")
    @Min(value = 1, message = "Role ID must be a positive integer")
    private Integer roleId;

    @Column(name = "assigned_by", nullable = false)
    @NotNull(message = "Assigned by reference must not be null")
    @Min(value = 1, message = "Assigned by must be a positive integer")
    private Integer assignedBy;

    @Column(name = "assigned_date")
    @PastOrPresent(message = "Assigned date timestamp must be in the past or present")
    private LocalDate assignedDate = LocalDate.now();

    @Column(name = "expiration_date")
    @FutureOrPresent(message = "Expiration date timestamp must be in the future or present")
    private LocalDate expirationDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;


}
