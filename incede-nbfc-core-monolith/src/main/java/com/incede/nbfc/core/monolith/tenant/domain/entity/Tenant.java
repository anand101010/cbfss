package com.incede.nbfc.core.monolith.tenant.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenants", schema = "tenant")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tenant  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_id")
    private Integer tenantId;

    @Column(name = "tenant_code", nullable = false, unique = true, length = 50)
    private String tenantCode;

    @Column(name = "tenant_name", nullable = false, length = 100)
    private String tenantName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive ;


    @Column(name = "created_by", nullable = false)
    @NotNull(message = "Created by is required")
    private Integer createdBy;

    @Column(name = "created_at")
    @PastOrPresent(message = "Created at must be in the past or present")
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_at")
    @PastOrPresent(message = "Updated at must be in the past or present")
    private LocalDateTime updatedAt;

    @Column(name = "is_del", nullable = false)
    @NotNull(message = "Deletion flag must be set")
    private Boolean isDel = false;

    @Column(name = "identity", nullable = false, unique = true)
    @NotNull(message = "Identity UUID is required")
    private UUID identity;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.identity == null) {
            this.identity = UUID.randomUUID();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

