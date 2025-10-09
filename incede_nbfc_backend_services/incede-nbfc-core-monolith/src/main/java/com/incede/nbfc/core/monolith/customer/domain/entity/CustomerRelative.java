package com.incede.nbfc.core.monolith.customer.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "customer_relatives" , schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRelative extends CustomerBaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relative_id")
    private Integer relativeId;

    @NotNull(message = "Customer Id must Not be null")
    @ManyToOne(fetch= FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name="customer_id",referencedColumnName="customer_id")
    private Customer customer;


    @Column(name = "relation_type_id", nullable = false)
    @NotNull(message = "RelationshipId must not be null ")
    @Min(value = 1, message = "Relationship id must be a positive integer")
    private Integer relationTypeId;

    @Size(max = 100,message = "Full Name must be at most 100 characters")
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;


    @ManyToOne(fetch= FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name="guardian_customer_id",referencedColumnName="customer_id")
    private Customer guardianCustomer;

    @Column(name = "email", length = 100)
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;


    @Column(name = "phone", length = 15)
    @Size(max = 15, message = "Phone must not exceed 15 characters")
    private String phone;
}