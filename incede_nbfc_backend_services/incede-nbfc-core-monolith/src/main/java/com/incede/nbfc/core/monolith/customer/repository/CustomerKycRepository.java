package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerKyc;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.DocumentType;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.KycTypes;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerKycRepository extends JpaRepository<CustomerKyc, Integer> {
    boolean existsByIdTypeAndCustomer(KycTypes idType, Customer existingCustomer);

    List<CustomerKyc> findByCustomer(Customer savedCustomer);

    Optional<CustomerKyc> findByIdTypeAndIdNumber(KycTypes documentType, @NotBlank(message = "ID number must not be blank") @Size(max = 50, message = "ID number must not exceed 50 characters") String idNumber);

}
