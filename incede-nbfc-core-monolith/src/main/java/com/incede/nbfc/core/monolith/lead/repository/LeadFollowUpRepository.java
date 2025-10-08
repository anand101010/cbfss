package com.incede.nbfc.core.monolith.lead.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadFollowUp;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadFollowUpHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeadFollowUpRepository extends JpaRepository<LeadFollowUp, Integer> {

    Optional<LeadFollowUp> findByIdentity(UUID identity);

    Optional<LeadFollowUp> findByIdentityAndIsDelFalseAndIsActiveTrue(UUID identity);

    Page<LeadFollowUp> findAllByIsDelFalseAndIsActiveTrue(Pageable pageable);

    @Query(value = """
        SELECT * 
        FROM lead.lead_follow_ups lfu
        WHERE lfu.is_active = true
          AND lfu.is_del = false
          AND (:leadIdentity IS NULL OR lfu.lead_id = (SELECT lead_id FROM lead.leads WHERE identity = :leadIdentity))
          AND (:staffId IS NULL OR lfu.staff_id = :staffId)
          AND (:followUpTypeIdentity IS NULL OR lfu.follow_up_type_id = (SELECT follow_up_type_id FROM master_data.follow_up_types WHERE identity = :followUpTypeIdentity))
          AND lfu.follow_up_date >= COALESCE(CAST(:leadDateFrom AS DATE), lfu.follow_up_date)
          AND lfu.follow_up_date <= COALESCE(CAST(:leadDateTo   AS DATE), lfu.follow_up_date)
        """,
            countQuery = """
        SELECT count(*) 
        FROM lead.lead_follow_ups lfu
        WHERE lfu.is_active = true
          AND lfu.is_del = false
          AND (:leadIdentity IS NULL OR lfu.lead_id = (SELECT lead_id FROM lead.leads WHERE identity = :leadIdentity))
          AND (:staffId IS NULL OR lfu.staff_id = :staffId)
          AND (:followUpTypeIdentity IS NULL OR lfu.follow_up_type_id = (SELECT follow_up_type_id FROM master_data.follow_up_types WHERE identity = :followUpTypeIdentity))
          AND lfu.follow_up_date >= COALESCE(CAST(:leadDateFrom AS DATE), lfu.follow_up_date)
          AND lfu.follow_up_date <= COALESCE(CAST(:leadDateTo   AS DATE), lfu.follow_up_date)
        """,
            nativeQuery = true)
    Page<LeadFollowUp> searchFollowUps(
            @Param("leadIdentity") UUID leadIdentity,
            @Param("staffId") Integer staffId,
            @Param("followUpTypeIdentity") UUID followUpTypeIdentity,
            @Param("leadDateFrom") LocalDate leadDateFrom,
            @Param("leadDateTo") LocalDate leadDateTo,
            Pageable pageable
    );
}
