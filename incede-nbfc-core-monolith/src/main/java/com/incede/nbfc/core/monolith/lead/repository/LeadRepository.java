package com.incede.nbfc.core.monolith.lead.repository;

import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {

    Optional<Lead> findByIdentity(UUID identity);
    Optional<Lead> findByIdentityAndIsDelFalse(UUID identity);

    boolean existsByEmail(String email);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT * FROM lead.leads l WHERE " +
            "(:fullName IS NULL OR LOWER(l.full_name) LIKE LOWER(CONCAT('%', CAST(:fullName AS text), '%'))) " +
            "AND (:contactNumber IS NULL OR l.contact_number LIKE CONCAT('%', CAST(:contactNumber AS text), '%')) " +
            "AND (:email IS NULL OR LOWER(l.email) LIKE LOWER(CONCAT('%', CAST(:email AS text), '%'))) " +
            "AND l.is_del = false ORDER BY l.created_at DESC",
            countQuery = "SELECT count(*) FROM lead.leads l WHERE " +
                    "(:fullName IS NULL OR LOWER(l.full_name) LIKE LOWER(CONCAT('%', CAST(:fullName AS text), '%'))) " +
                    "AND (:contactNumber IS NULL OR l.contact_number LIKE CONCAT('%', CAST(:contactNumber AS text), '%')) " +
                    "AND (:email IS NULL OR LOWER(l.email) LIKE LOWER(CONCAT('%', CAST(:email AS text), '%'))) " +
                    "AND l.is_del = false",
            nativeQuery = true)
    Page<Lead> searchLeads(
            @Param("fullName") String fullName,
            @Param("contactNumber") String contactNumber,
            @Param("email") String email,
            Pageable pageable
    );

    @Query(value = "SELECT * FROM lead.leads l WHERE " +
            "(:mobileNumber IS NULL OR l.contact_number = :mobileNumber) AND " +
            "(:email IS NULL OR l.email ILIKE CAST(:email AS text)) AND " +
            "(:fullName IS NULL OR l.full_name ILIKE '%' || CAST(:fullName AS text) || '%')",
            nativeQuery = true)
    List<Lead> searchLeadDetails(
            @Param("mobileNumber") String mobileNumber,
            @Param("email") String email,
            @Param("fullName") String fullName
    );

}
