package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerForm60;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface CustomerForm60Repository extends JpaRepository<CustomerForm60, Integer> {

    @Query("SELECT c FROM CustomerForm60 c WHERE c.form60Id = :form60Id AND c.customerId.customerId = :customerId")
    Optional<CustomerForm60> findByForm60IdAndCustomerId(@Param("form60Id") Integer form60Id, @Param("customerId") Integer customerId);
}
