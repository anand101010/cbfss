package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Relationships;
import com.incede.nbfc.core.monolith.masterdata.dto.RelationshipsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipsRepository extends JpaRepository<Relationships, Integer> {

        List<RelationshipsView> findByIsDelFalseAndIsActiveTrue();
    }


