package com.incede.nbfc.core.monolith.lead.repository;

import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Integer> {

    @Query(value = "SELECT * FROM lead.leads l WHERE " +
            "(:mobileNumber IS NULL OR l.contact_number = :mobileNumber) AND " +
            "(:email IS NULL OR l.email ILIKE CAST(:email AS text)) AND " +
            "(:fullName IS NULL OR l.full_name ILIKE '%' || CAST(:fullName AS text) || '%')",
            nativeQuery = true)
    List<Lead> searchLeads(
            @Param("mobileNumber") String mobileNumber,
            @Param("email") String email,
            @Param("fullName") String fullName
    );
}