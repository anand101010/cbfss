package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    Optional<Customer> findByTenantIdAndFirstNameAndLastName( Integer tenantId,  String firstName, String lastName);

    Optional<Customer> findByIdentity(UUID identity);

    boolean existsByTenantIdAndFirstNameAndLastName( Integer tenantId, String firstName, String lastName);
    Optional<Customer> findByIdentityAndIsDelFalse(UUID identity);

    @Query(value = "SELECT nextval('customer_code_seq')", nativeQuery = true)
    Long getNextCustomerCodeSeq();


    boolean existsByTenantIdAndAadharVaultId( Integer tenantId,String aadharVault);

    Optional<Customer> findByTenantIdAndAadharVaultId(Integer tenantId, String aadharVault);
}


