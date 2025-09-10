package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerAssetRepository extends JpaRepository<CustomerAsset, Integer> {
    void deleteByCustomer(Customer customer);

    CustomerAsset findByCustomer(Customer customer);
}
