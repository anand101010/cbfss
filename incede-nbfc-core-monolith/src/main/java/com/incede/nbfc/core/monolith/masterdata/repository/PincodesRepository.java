package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Pincodes;
import com.incede.nbfc.core.monolith.masterdata.dto.PincodesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PincodesRepository extends JpaRepository<Pincodes, Integer> {

    List<PincodesView> findByIsDelFalse();
}
