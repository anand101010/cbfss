package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByTenantAndFirstNameAndLastName(Tenant tenant, String firstName, String lastName);

    Optional<Customer> findByIdentity(UUID identity);

    boolean existsByTenantAndFirstNameAndLastName(Tenant tenant, String firstName, String lastName);

    Optional<Customer> findByIdentityAndIsDelFalse(UUID identity);

    @Query(value = "SELECT nextval('customer_code_seq')", nativeQuery = true)
    Long getNextCustomerCodeSeq();

    boolean existsByTenantAndAadharVaultId(Tenant tenant, String aadharVault);

    Optional<Customer> findByTenantAndAadharVaultId(Tenant tenant, String aadharVaultId);

    Customer findTopByOrderByCustomerIdDesc();

    Optional<Customer> findByCustomerCode(String customerCode);
}
