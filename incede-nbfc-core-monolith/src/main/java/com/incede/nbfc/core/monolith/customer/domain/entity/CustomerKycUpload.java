package com.incede.nbfc.core.monolith.customer.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customer_kyc_upload", schema = "customers")
public class CustomerKycUpload extends CustomerBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_id")
    private Long uploadId;

    @NotNull(message = "KYC is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kyc_id", nullable = false, referencedColumnName = "kyc_id",
            foreignKey = @ForeignKey(name = "fk_customer_kyc_upload_kyc"))
    private CustomerKyc kyc;

    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "customer_id",
            foreignKey = @ForeignKey(name = "fk_customer_kyc_upload_customer"))
    private Customer customer;

    @NotBlank(message = "Document reference must not be blank")
    @Size(max = 255, message = "Document reference must not exceed 255 characters")
    @Column(name = "document_reference", nullable = false, length = 255)
    private String documentReference;

    @Size(max = 255, message = "File name must not exceed 255 characters")
    @Column(name = "file_name", length = 255)
    private String fileName;

    @Size(max = 50, message = "File type must not exceed 50 characters")
    @Column(name = "file_type", length = 50)
    private String fileType;

    @NotNull(message = "Upload status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "upload_status", nullable = false, length = 20)
    private UploadStatus uploadStatus = UploadStatus.PENDING;

    @Builder.Default
    @Column(name = "version")
    private Integer version = 1;

    @Column(name = "upload_date", updatable = false, insertable = false)
    private Timestamp uploadDate;

    @Lob
    @Column(name = "response_payload")
    private String responsePayload;

    @NotNull(message = "Identity UUID is required")
    @Column(name = "identity", nullable = false, unique = true)
    private UUID identity;

    @PrePersist
    protected void onCreate() {
        if (this.identity == null) {
            this.identity = UUID.randomUUID();
        }
        if (this.uploadStatus == null) {
            this.uploadStatus = UploadStatus.PENDING;
        }
        if (this.version == null) {
            this.version = 1;
        }
    }


    public void markAsSuccess(String response) {
        this.uploadStatus = UploadStatus.SUCCESS;
        this.responsePayload = response;
    }

    public void markAsFailed(String errorResponse) {
        this.uploadStatus = UploadStatus.FAILED;
        this.responsePayload = errorResponse;
    }

    public void markAsExpired() {
        this.uploadStatus = UploadStatus.EXPIRED;
    }

    public boolean isSuccessful() {
        return UploadStatus.SUCCESS.equals(this.uploadStatus);
    }

    public enum UploadStatus {
        PENDING,
        SUCCESS,
        FAILED,
        EXPIRED
    }
}