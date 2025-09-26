package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAdditionalReferenceName;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAdditionalReferenceValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerAdditionalReferenceValueRepository extends JpaRepository<CustomerAdditionalReferenceValue, Integer> {
    Optional<CustomerAdditionalReferenceValue> findByCustomer(Customer customer);

    Optional<CustomerAdditionalReferenceValue> findByCustomerAndCustomerAdditionalReferenceName(Customer customer, CustomerAdditionalReferenceName customerAdditionalReferenceName);

    List<CustomerAdditionalReferenceValue> findAllByCustomer(Customer customer);

    Optional<CustomerAdditionalReferenceValue> findByIdentity(UUID referenceValueIdentity);

}
