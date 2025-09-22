package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Pincodes;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PincodesRepository extends JpaRepository<Pincodes, Integer> {

    Page<Pincodes> findByIsDelFalse(Pageable pageable);

    @Query("SELECT p FROM Pincodes p WHERE p.pincode = :pincode")
    List<Pincodes> findByDetailsThroughPincode(@Param("pincode") Integer pincode);
}
