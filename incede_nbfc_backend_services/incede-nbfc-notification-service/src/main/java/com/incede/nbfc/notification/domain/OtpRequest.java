package com.incede.nbfc.notification.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;

@Entity
@Table(name = "otp_requests")
public class OtpRequest
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long otpRequestId;

	@Column(nullable = false)
	private Integer tenantId;

	@Column(nullable = false)
	private String branchCode;
    @Column(name = "customer_identity")
	private Integer customerIdentity;

    @Column(name = "template_catalog_id",nullable = false, length = 100)
	private Integer templateCatalogId;

    @Column(name = "template_content_id",length = 100)
    private Integer templateContentId;

	@Column(nullable = false)
	private String msisdn;

	@Column(nullable = false, length = 64)
	private String activeSlotKey;

	@Column(length = 64)
	private String idempotencyKey;

	@Column(nullable = false, columnDefinition = "text")
	private String otpHash;

	@Column(nullable = false)
	private byte[] otpSalt;

	@Column(nullable = false)
	private short otpLength;

	@Column(nullable = false)
	private OffsetDateTime expiresAt;

	@Column(nullable = false)
	private short maxAttempts;

	@Column(nullable = false)
	private short attemptCount;

	@Column(nullable = false, length = 20)
	private String status;

	@Column(name = "provider_request_json",columnDefinition = "text")
	private String providerRequestJson;

    @Column(name = "provider_response_id",length = 100)
    private String providerResponseId;

    @Column(name = "created_by")
    private Integer createdBy;

	@Column(name = "created_at")
	private OffsetDateTime createdAt;

    @Column(name = "updated_by",nullable = true)
    private Integer updatedBy;

    @Column(name = "updated_at",nullable = true)
	private OffsetDateTime updatedAt;

    @Column(nullable = false, unique = true)
    private UUID identity;

    // -------------------- Getters and Setters --------------------

    public Long getOtpRequestId() {
        return otpRequestId;
    }

    public void setOtpRequestId(Long otpRequestId) {
        this.otpRequestId = otpRequestId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public Integer getCustomerIdentity() {
        return customerIdentity;
    }

    public void setCustomerIdentity(Integer customerIdentity) {
        this.customerIdentity = customerIdentity;
    }


    public Integer getTemplateCatalogId() {
        return templateCatalogId;
    }

    public void setTemplateCatalogId(Integer templateCatalogId) {
        this.templateCatalogId = templateCatalogId;
    }


    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getActiveSlotKey() {
        return activeSlotKey;
    }

    public void setActiveSlotKey(String activeSlotKey) {
        this.activeSlotKey = activeSlotKey;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public String getOtpHash() {
        return otpHash;
    }

    public void setOtpHash(String otpHash) {
        this.otpHash = otpHash;
    }

    public byte[] getOtpSalt() {
        return otpSalt != null ? otpSalt.clone() : null;
    }

    public void setOtpSalt(byte[] otpSalt) {
        this.otpSalt = otpSalt;
    }

    public short getOtpLength() {
        return otpLength;
    }

    public void setOtpLength(short otpLength) {
        this.otpLength = otpLength;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public short getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(short maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public short getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(short attemptCount) {
        this.attemptCount = attemptCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProviderResponseId() {
        return providerResponseId;
    }

    public void setProviderResponseId(String providerResponseId) {
        this.providerResponseId = providerResponseId;
    }

    public String getProviderRequestJson() {
        return providerRequestJson;
    }

    public void setProviderRequestJson(String metadataJson) {
        this.providerRequestJson = metadataJson;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public Integer getTemplateContentId() {
        return templateContentId;
    }
    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }


    public void setTemplateContentId(Integer templateContentId) {
        this.templateContentId = templateContentId;
    }
    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public UUID getIdentity() {
        return identity;
    }

    public void setIdentity(UUID identity) {
        this.identity = identity;
    }


} 