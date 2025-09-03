package com.incede.nbfc.core.monolith.customer.domain.entity;

import com.incede.nbfc.core.monolith.customer.enums.PepStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_pep" , schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPep extends CustomerBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pep_id")
    private Integer pepId;

    @OneToOne(fetch=FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id")
    @NotNull(message = "Customer reference must not be Null")
    private Customer customerId;


    @Column(name = "status", length = 10, nullable = false)
    @NotBlank(message = "YES','NO','PENDING")
    private String status;

    @Column(name = "category_id")
    @Min(value = 1, message = "Category id must be a positive integer")
    private Integer categoryId;

    @Column(name = "relationship_id")
    @Min(value = 1, message = "Relationship id must be a positive integer")
    private Integer relationshipId;

    @Column(name = "verification_source_id")
    @Min(value = 1, message = "Verification source id must be a positive integer")
    private Integer verificationSourceId;

    @Column(name = "verified_at")
    @PastOrPresent(message = "Verified at timestamp must be in the past or present")
    private LocalDateTime verifiedAt;

    @Column(name = "document_ref_id")
    @Min(value = 1, message = "Document reference id must be a positive integer")
    private Integer documentRefId;


}