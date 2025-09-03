package com.incede.nbfc.core.monolith.customer.repository;


import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerPep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerPepRepository extends JpaRepository<CustomerPep,Integer> {
}