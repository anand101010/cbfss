package com.incede.nbfc.notification.repository;

import com.incede.nbfc.notification.domain.OtpRequest;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OtpRequestRepository extends JpaRepository<OtpRequest, Long> {

    Optional<OtpRequest> findFirstByTenantIdAndBranchCodeAndIdempotencyKey(Integer tenantId, String branchCode, String idempotencyKey);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from OtpRequest r where r.identity = :id")
    Optional<OtpRequest> findByIdentityForUpdate(@Param("id") UUID identity);
} 