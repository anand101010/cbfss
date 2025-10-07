package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerForm60;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;


public interface CustomerForm60Repository extends JpaRepository<CustomerForm60, Integer> {


    Optional<CustomerForm60> findByIdentity(UUID form60Identity);

}
