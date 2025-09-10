package com.incede.nbfc.core.monolith.customer.repository;


import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerPep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CustomerPepRepository extends JpaRepository<CustomerPep,Integer> {
    Optional<CustomerPep> findByCustomer(Customer customer);
}