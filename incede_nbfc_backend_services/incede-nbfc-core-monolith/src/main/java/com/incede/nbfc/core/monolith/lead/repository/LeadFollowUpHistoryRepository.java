package com.incede.nbfc.core.monolith.lead.repository;

import com.incede.nbfc.core.monolith.lead.domain.entity.LeadFollowUpHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeadFollowUpHistoryRepository extends JpaRepository<LeadFollowUpHistory, Integer>{

    List<LeadFollowUpHistory> findByLeadIdentityAndIsDelFalseOrderByFollowUpDateDesc(UUID leadIdentity);
    Optional<LeadFollowUpHistory> findByLeadIdentityAndIsDelFalse(UUID leadIdentity);
    Optional<LeadFollowUpHistory> findByIdentityAndIsDelFalse(UUID leadIdentity);
    @Query(
            value = """
        SELECT * 
        FROM lead.lead_follow_up_history lfh
        WHERE
           lfh.is_del = false
          AND (:leadIdentity IS NULL OR lfh.lead_id = (SELECT lead_id FROM lead.leads WHERE identity = :leadIdentity))
          AND (:staffIdentity IS NULL OR lfh.staff_id = :staffIdentity)
          AND (:followUpTypeIdentity IS NULL OR lfh.follow_up_type_id = (SELECT follow_up_type_id FROM master_data.follow_up_types WHERE identity = :followUpTypeIdentity))
         AND (:leadStageIdentity IS NULL OR lfh.lead_stage_id = (SELECT lead_stage_id FROM master_data.lead_stages WHERE identity = :leadStageIdentity))
          AND lfh.follow_up_date >= COALESCE(CAST(:leadDateFrom AS DATE), lfh.follow_up_date)
          AND lfh.follow_up_date <= COALESCE(CAST(:leadDateTo   AS DATE), lfh.follow_up_date)
        ORDER BY lfh.follow_up_date DESC
        """,
            countQuery = """
        SELECT count(*) 
        FROM lead.lead_follow_up_history lfh
        WHERE
           lfh.is_del = false
          AND (:leadIdentity IS NULL OR lfh.lead_id = (SELECT lead_id FROM lead.leads WHERE identity = :leadIdentity))
          AND (:staffIdentity IS NULL OR lfh.staff_id = :staffIdentity)
          AND (:followUpTypeIdentity IS NULL OR lfh.follow_up_type_id = (SELECT follow_up_type_id FROM master_data.follow_up_types WHERE identity = :followUpTypeIdentity))
          AND (:leadStageIdentity IS NULL OR lfh.lead_stage_id = (SELECT lead_stage_id FROM master_data.lead_stages WHERE identity = :leadStageIdentity))
          AND lfh.follow_up_date >= COALESCE(CAST(:leadDateFrom AS DATE), lfh.follow_up_date)
          AND lfh.follow_up_date <= COALESCE(CAST(:leadDateTo   AS DATE), lfh.follow_up_date)
        """,
            nativeQuery = true
    )
    Page<LeadFollowUpHistory> searchFollowUpHistory(
            @Param("leadIdentity") UUID leadIdentity,
            @Param("staffIdentity") Integer staffIdentity,
            @Param("followUpTypeIdentity") UUID followUpTypeIdentity,
            @Param("leadStageIdentity") UUID  leadStageIdentity,
            @Param("leadDateFrom") LocalDate leadDateFrom,
            @Param("leadDateTo") LocalDate leadDateTo,
            Pageable pageable
    );

    @Query("""
    SELECT h 
    FROM LeadFollowUpHistory h 
    WHERE h.lead.identity = :leadIdentity 
      AND h.isDel = false
""")
    List<LeadFollowUpHistory> findByLeadIdentity(@Param("leadIdentity") UUID leadIdentity);



    List<LeadFollowUpHistory> findByLead_IdentityAndIsDelFalseOrderByFollowUpDateDesc(UUID leadIdentity);

}
