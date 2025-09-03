package com.incede.nbfc.core.monolith.customer.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * Entity for mapping customers to different account types.
 * Maintains relationship between a customer and their associated accounts.

 * Examples include savings account, current account, or loan account types.
 *
 * @version 1.0.0
 */

@Entity
@Table(name = "customer_account_mapping", schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccountMapping extends CustomerBaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_acnt_map_id")
    private Integer customerAccountMappingId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "cust_id", nullable = false, referencedColumnName = "customer_id")
    @NotNull(message = "Customer reference must not be null")
    private Customer customer;

    @Column(name = "acnt_type", nullable = false)
    @NotNull(message = "Account type must not be null")
    @Min(value = 1, message = "Account type must be a positive integer")
    private Integer accountType;


}
