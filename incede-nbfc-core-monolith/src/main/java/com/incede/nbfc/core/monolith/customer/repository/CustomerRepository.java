package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    Optional<Customer> findByTenantIdAndFirstNameAndLastName(@NotNull(message = "Tenant ID is required") Integer tenantId, @NotBlank(message = "First name is required") String firstName, @NotBlank(message = "Last name is required") String lastName);

    Optional<Customer> findByIdentity(UUID identity);

    boolean existsByTenantIdAndFirstNameAndLastName(@NotNull(message = "Tenant ID is required") Integer tenantId, @NotBlank(message = "First name is required") String firstName, @NotBlank(message = "Last name is required") String lastName);


}


