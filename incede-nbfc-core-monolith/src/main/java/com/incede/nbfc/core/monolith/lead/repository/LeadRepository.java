package com.incede.nbfc.core.monolith.lead.repository;

import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Integer> {

    @Query("SELECT l FROM Lead l WHERE " +
            "(:mobileNumber IS NULL OR l.contactNumber = :mobileNumber) AND " +
            "(:email IS NULL OR LOWER(CAST(l.email AS string)) = LOWER(:email)) AND " +
            "(:fullName IS NULL OR LOWER(CAST(l.fullName AS string)) LIKE LOWER(CONCAT('%', :fullName, '%')))")
    List<Lead> searchLeads(
            @Param("mobileNumber") String mobileNumber,
            @Param("email") String email,
            @Param("fullName") String fullName
    );
}