package com.incede.nbfc.core.monolith.masterdata.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", length = 20, updatable = false)
    private Integer createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", length = 20)
    private Integer updatedBy;

    @Column(name = "is_del", nullable = false)
    private Boolean isDel = false;



    // Lifecycle methods
    @PrePersist
    protected void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
        if (isDel == null) {
            isDel = false;
        }

    }

    @PreUpdate
    protected void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Business methods
    public void softDelete() {
        this.isDel = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void restore() {
        this.isDel = false;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", deleted=" + isDel +
                '}';
    }
}
