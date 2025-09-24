package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerContact;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.ContactTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.*;


@Repository
public interface CustomerContactRepository extends JpaRepository<CustomerContact, Integer> {
    Optional<CustomerContact> findByCustomerAndContactType(Customer customer, ContactTypes contactType);

    List<CustomerContact> findByCustomer(Customer customer);

    Optional<CustomerContact> findByIdentityAndCustomer(UUID contactIdentity, Customer customer);

    List<CustomerContact> findByCustomerAndContactTypeAndIsActiveTrue(Customer customer, ContactTypes contactType);

   List< CustomerContact> findByCustomerAndIsActiveTrue(Customer customer);

    boolean existsByContactValueAndIdentityNot( String contactDetails, UUID contactId);

    boolean existsByContactValue(String contactDetails);

    boolean existsByContactValueAndIsPrimaryTrueAndIsActiveTrue( String contactDetails);

    boolean existsByContactValueAndIdentityNotAndIsPrimaryTrueAndIsActiveTrue( String contactDetails, UUID contactId);
}

