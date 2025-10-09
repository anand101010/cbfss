package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Pincodes;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PincodesRepository extends JpaRepository<Pincodes, Integer> {

    List<Pincodes> findByPincodeIdIn(Set<Integer> pincodeIds);

    @Query("""
        SELECT p FROM Pincodes p
        JOIN FETCH p.cities
        JOIN FETCH p.states
        JOIN FETCH p.districts
        WHERE p.pincode = :pincode
    """)
    List<Pincodes> findByPincodeWithDetails(@Param("pincode") String pincode);

    Page<Pincodes> findByIsDelFalse(Pageable pageable);
}
