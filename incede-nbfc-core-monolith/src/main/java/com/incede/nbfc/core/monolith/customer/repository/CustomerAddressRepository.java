package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAddress;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Integer> {

    Optional<CustomerAddress> findByIdentity(UUID identity);

    List<CustomerAddress> findByCustomerAndIsDelFalse(Customer customer);


    List<CustomerAddress> findByCustomerAndAddressTypeAndIsActiveTrueAndIsDelFalse(Customer customer, AddressType permanentAddressType);
}
