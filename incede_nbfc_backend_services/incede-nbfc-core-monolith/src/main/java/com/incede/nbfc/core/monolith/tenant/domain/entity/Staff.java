package com.incede.nbfc.core.monolith.tenant.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "staff", schema = "tenant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Integer staffId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tenant_id", nullable = false, referencedColumnName = "tenant_id")
    @NotNull(message = "Tenant is mandatory")
    private Tenant tenantId;

    @Column(name = "staff_name", nullable = false, length = 50)
    @NotBlank(message = "Staff name is required")
    private String staffName;

    @Column(name = "staff_code", nullable = false, length = 10)
    @NotBlank(message = "Staff code is required")
    private String staffCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporting_to")
    private Staff reportingTo;

    @Column(name = "contact_address", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Contact address is required")
    private String contactAddress;

    @Column(name = "contact_phone", nullable = false, length = 50)
    @NotBlank(message = "Contact phone is required")
    private String contactPhone;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    @NotBlank(message = "Email is required")
    private String email;

    @Column(name = "is_app_user", nullable = false)
    @NotNull(message = "isAppUser is mandatory")
    private Boolean isAppUser;

    @Column(name = "app_user_ref_id", length = 50)
    private String appUserRefId;

    @Column(name = "is_del", nullable = false)
    @NotNull(message = "Deletion flag must be set")
    private Boolean isDel = false;

    @Column(name = "identity", nullable = false, unique = true)
    @NotNull(message = "Identity UUID is required")
    private UUID identity;

    @Column(name = "created_by", nullable = false)
    @NotNull(message = "Created by is required")
    private Integer createdBy;

    @Column(name = "created_at", updatable = false)
    @PastOrPresent(message = "Created at must be in the past or present")
    private Timestamp createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_at")
    @PastOrPresent(message = "Updated at must be in the past or present")
    private Timestamp updatedAt;
}

 