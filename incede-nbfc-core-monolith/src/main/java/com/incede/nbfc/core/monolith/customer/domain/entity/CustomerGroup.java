package com.incede.nbfc.core.monolith.customer.domain.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity for logical grouping of customers (e.g., SME Group, High-Value Clients)
 * used for segmentation, operational, or marketing purposes.
 *
 * @version 1.0.0
 */
@Entity
@Table(name = "customer_groups", schema = "customers",
        uniqueConstraints = @UniqueConstraint(columnNames = "identity"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerGroup extends CustomerBaseEntity implements  Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "tenant_id", nullable = false)
    @NotNull(message = "Tenant ID is required")
    @Min(value = 1, message = "Tenant id must be a positive integer")
    private Integer tenantId;

    @Column(name = "group_name", nullable = false, unique = true, length = 100)
    @NotBlank(message = "Group name must not be blank")
    @Size(max = 100, message = "Group name must not exceed 100 characters")
    private String groupName;

    @Column(name = "description", columnDefinition = "TEXT")
    @Size(max = 1000, message = "Description must not exceed 1000 characters") // Optional upper bound
    private String description;

    @Column(name = "is_active")
    @NotNull(message = "Active flag must be specified")
    private Boolean isActive = true;

}
