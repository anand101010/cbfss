package com.incede.nbfc.core.monolith.customer.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "customer_relationships", schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRelationship extends CustomerBaseEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relationship_id")
    private Integer relationshipId;

    @NotNull(message = "Customer reference must Not be null")
    @ManyToOne(fetch= FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name="customer_id",referencedColumnName="customer_id")
    private Customer customer;

    @Column(name = "staff_id", nullable = false)
    @NotNull(message = "Staff reference must not be null")
    @Min(value = 1, message = "Staff must be a positive integer")
    private Integer staff;

    @Column(name = "assigned_from", nullable = false)
    @NotNull(message = "Assigned From must not be Null")
    @PastOrPresent(message = "Assigned from timestamp must be in the past or present")
    private LocalDate assignedFrom;

    @Column(name = "assigned_to")
    @FutureOrPresent(message = "Assigned to timestamp must be in the future or present")
    private LocalDate assignedTo;

    @Column(name = "last_contacted")
    @PastOrPresent(message = "Last contacted timestamp must be in the past or present")
    private LocalDate lastContacted;

    @NotNull(message = "isActive must not be null ")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

}