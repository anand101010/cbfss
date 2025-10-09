package com.incede.nbfc.core.monolith.customer.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_address_proofs",schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddressProof extends CustomerBaseEntity implements Serializable {



        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "address_proof_id")
        private Integer addressProofId;

        @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "address_id", nullable = false, referencedColumnName = "address_id")
        @NotNull(message = "Customer address must not be null")
        private CustomerAddress address;

        @Column(name = "proof_type_id", nullable = false)
        @NotNull(message = "Proof type ID is required")
        @Min(value = 1, message = "Proof type ID must be a positive integer")
        private Integer proofTypeId;

        @Column(name = "document_ref_id", nullable = false)
        @NotNull(message = "Document reference ID is required")
        @Min(value = 1, message = "Document reference ID must be a positive integer")
        private Integer documentRefId;

        @Column(name = "verified_by")
        @Min(value = 1, message = "Verifier ID must be a positive integer")
        private Integer verifiedBy;

        @Column(name = "verified_at")
        @PastOrPresent(message = "Verification timestamp must be in the past or present")
        private LocalDateTime verifiedAt;

        @Column(name = "is_verified")
        @NotNull(message = "Verification status must be specified")
        private Boolean isVerified = false;
    }
