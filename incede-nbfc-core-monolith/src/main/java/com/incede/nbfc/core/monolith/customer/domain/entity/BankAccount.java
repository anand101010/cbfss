package com.incede.nbfc.core.monolith.customer.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer_bank_accounts", schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount extends CustomerBaseEntity implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_account_id")
    private Integer bankAccountId;


    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "bank_name", length = 100, nullable = false)
    private String bankName;

    @Column(name = "ifsc_code", length = 20, nullable = false)
    private String ifscCode;

    @Column(name = "account_number", length = 30, nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "upi_id", length = 50, unique = true)
    private String upiId;

    @Column(name = "account_type", nullable = false)
    private Integer accountType;

    @Column(name = "account_status")
    private String accountStatus;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = true;

    @Column(name = "account_holder_name", length = 100, nullable = false)
    private String accountHolderName;

    @Column(name = "branch_name", length = 100)
    private String branchName;

    @Column(name = "bank_proof_document_ref_id")
    private Integer bankProofDocumentRefId;

    @Column(name = "pd_status", length = 20)
    private String pdStatus = "PENDING";

    @Column(name = "pd_txn_id", length = 64)
    private String pdTxnId;

    @Column(name = "pd_name_match_score", precision = 5, scale = 2)
    private BigDecimal pdNameMatchScore;

    @Column(name = "pd_verified_at")
    private LocalDateTime pdVerifiedAt;

    @Column(name = "upi_verified", nullable = false)
    private Boolean upiVerified = false;

    @Column(name = "upi_verified_at")
    private LocalDateTime upiVerifiedAt;


}