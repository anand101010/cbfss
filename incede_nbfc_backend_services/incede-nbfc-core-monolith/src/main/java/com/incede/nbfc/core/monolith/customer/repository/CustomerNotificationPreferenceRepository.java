package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerNotificationPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerNotificationPreferenceRepository extends JpaRepository<CustomerNotificationPreference,Integer> {
    Optional<CustomerNotificationPreference> findByCustomer(Customer customer);
}
