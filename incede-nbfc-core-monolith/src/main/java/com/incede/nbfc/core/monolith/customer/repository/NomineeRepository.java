package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.Nominee;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Relationships;
import feign.Param;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface NomineeRepository extends JpaRepository<Nominee, Integer> {

    List<Nominee> findByCustomerIdentity(UUID customerIdentity);
    Optional<Nominee> findByIdentityAndCustomer(UUID identity, Customer customer);
    @Query("SELECT n FROM Nominee n WHERE n.customer.identity = :customerIdentity AND n.isDel = false")
    List<Nominee> findByCustomerIdentityAndIsDelFalse(UUID customerIdentity);
    boolean existsByCustomerAndFullNameAndRelationshipAndIsDelFalse(Customer customer, String fullName, Relationships relationship);

    List<Nominee> findByCustomerCustomerIdAndIsDelFalse(Integer customerId);

}

