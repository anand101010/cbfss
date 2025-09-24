package com.incede.nbfc.core.monolith.customer.domain.entity;

import com.incede.nbfc.core.monolith.customer.enums.PdStatus;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AccountStatuses;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AccountTypeMaster;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;



@Entity
@Table(name = "customer_bank_accounts", schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBankAccount extends CustomerBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_account_id")
    private Integer bankAccountId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull(message = "Customer reference is required")
    private Customer customer;

    @Column(name = "bank_name", length = 100, nullable = false)
    @NotBlank(message = "Bank name must not be blank")
    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    private String bankName;

    @Column(name = "ifsc_code", length = 20, nullable = false)
    @NotBlank(message = "IFSC code must not be blank")
    @Size(max = 20, message = "IFSC code must not exceed 20 characters")
    private String ifscCode;

    @Column(name = "account_number", length = 30, nullable = false, unique = true)
    @NotBlank(message = "Account number must not be blank")
    @Size(max = 30, message = "Account number must not exceed 30 characters")
    private String accountNumber;

    @Column(name = "upi_id", length = 50, unique = true)
    @Size(max = 50, message = "UPI ID must not exceed 50 characters")
    private String upiId;

    @NotNull(message = "Account Type  is required")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "account_type", nullable = false, referencedColumnName = "account_type_id")
    private AccountTypeMaster accountType;


    @NotNull(message = "Account status  is required")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "account_status", nullable = false, referencedColumnName = "account_status_id")
    private AccountStatuses accountStatus;

    @Column(name = "is_active")
    @NotNull(message = "Active status must be specified")
    private Boolean isActive;

    @Column(name = "is_primary", nullable = false)
    @NotNull(message = "Primary account flag must be specified")
    private Boolean isPrimary = true;

    @Column(name = "account_holder_name", length = 100, nullable = false)
    @NotBlank(message = "Account holder name must not be blank")
    @Size(max = 100, message = "Account holder name must not exceed 100 characters")
    private String accountHolderName;

    @Column(name = "branch_name", length = 100)
    @Size(max = 100, message = "Branch name must not exceed 100 characters")
    private String branchName;

    @Column(name = "bank_proof_document_ref_id")
    @NotNull(message = "Bank Proof id must not be null")
    private Integer bankProofDocumentRefId;

    @NotBlank(message="pd_status cannot be blank ")
    @Column(name = "pd_status", length = 20)
    private String pdStatus ;

    @Column(name = "pd_txn_id", length = 64)
    @Size(max = 64, message = "PD transaction ID must not exceed 64 characters")
    private String pdTxnId;

    @Column(name = "pd_name_match_score", precision = 5, scale = 2)
    @Digits(integer = 5, fraction = 2, message = "PD name match score must be a valid decimal")
    private BigDecimal pdNameMatchScore;

    @Column(name = "pd_verified_at")
    @PastOrPresent(message = "PD verification timestamp must be in the past or present")
    private LocalDateTime pdVerifiedAt;

    @Column(name = "upi_verified")
    @NotNull(message = "UPI verification flag must be specified")
    private Boolean upiVerified = false;

    @Column(name = "upi_verified_at")
    @PastOrPresent(message = "UPI verification timestamp must be in the past or present")
    private LocalDateTime upiVerifiedAt;
}