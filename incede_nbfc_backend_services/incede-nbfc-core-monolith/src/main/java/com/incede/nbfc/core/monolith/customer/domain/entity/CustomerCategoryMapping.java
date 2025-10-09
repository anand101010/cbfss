package com.incede.nbfc.core.monolith.customer.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Table(name = "customer_category_mappings", schema = "customers")
@Data
@NoArgsConstructor
public class CustomerCategoryMapping extends CustomerBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_map_id")
    private Integer categoryMapId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id")
    @NotNull(message = "Customer reference is required")
    private Customer customer;

    @Column(name = "category_id", nullable = false)
    @NotNull(message = "Category ID is required")
    private Integer categoryId;

    @Column(name = "assigned_date", nullable = false)
    @NotNull(message = "Assigned date is required")
    @PastOrPresent(message = "Assigned date must be in the past or present")
    private LocalDate assignedDate;

    @Column(name = "is_active")
    @NotNull(message = "Active status must be specified")
    private Boolean isActive = true;

}