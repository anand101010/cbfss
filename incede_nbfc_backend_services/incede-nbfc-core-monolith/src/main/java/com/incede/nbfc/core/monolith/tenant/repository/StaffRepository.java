package com.incede.nbfc.core.monolith.tenant.repository;

import com.incede.nbfc.core.monolith.tenant.domain.entity.Staff;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {


    List<Staff> findAllByStaffName(String canvasserName);

    List<Staff> findAllByStaffNameStartingWithIgnoreCase(String canvasserName);

    Optional<Staff> findByIdentity(UUID canvasserIdentity);
}