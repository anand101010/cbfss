package com.incede.nbfc.core.monolith.customer.domain.entity;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CKYC Upload tracking entity for NBFC system.
 *
 *
 * @version 1.0.0
 */
@Entity
@Table(name = "customer_kyc_ckyc_uploads", schema = "customers",
        uniqueConstraints = @UniqueConstraint(columnNames = "ckyc_reference_no"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerKycCkycUpload implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ckyc_upload_id")
    private Integer ckycUploadId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id")
    @NotNull(message = "Customer reference must not be null")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "kyc_document_id", referencedColumnName = "kycId")
    private CustomerKyc kycDocument;

    @Column(name = "upload_status", nullable = false, length = 20)
    @NotBlank(message = "Upload status must not be blank")
    @Size(max = 20, message = "Upload status must not exceed 20 characters")
    @Pattern(regexp = "PENDING|SUCCESS|FAILED", message = "Upload status must be PENDING, SUCCESS, or FAILED")
    private String uploadStatus;

    @Column(name = "ckyc_reference_no", length = 100, unique = true)
    @Size(max = 100, message = "CKYC reference number must not exceed 100 characters")
    private String ckycReferenceNo;

    @Column(name = "upload_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @PastOrPresent(message = "Upload date must be in the past or present")
    private LocalDateTime uploadDate = LocalDateTime.now();


    @Column(name = "response_payload")
    @Size(max = 5000, message = "Response payload must not exceed 5000 characters")
    private String responsePayload;


}
