package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Banks;
import com.incede.nbfc.core.monolith.masterdata.dto.BanksView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BanksRepository extends JpaRepository<Banks, Integer> {

    List<BanksView> findByIsDelFalseAndIsActiveTrue();
}
