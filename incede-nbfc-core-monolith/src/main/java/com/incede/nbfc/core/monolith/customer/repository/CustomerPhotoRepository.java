package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerPhotoRepository extends JpaRepository<CustomerPhoto,Integer> {
    List<CustomerPhoto> findByCustomerIdentityAndIsDelFalseOrderByCaptureTimeDesc(UUID identity);
}

