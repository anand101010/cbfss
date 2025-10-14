package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerKycUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerKycUploadRepository extends JpaRepository<CustomerKycUpload,Integer> {

   Optional<List<CustomerKycUpload>> findByCustomer(Customer conflictCustomer);
}
