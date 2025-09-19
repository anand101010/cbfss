package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerBankAccount;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerBankAccountRepository extends JpaRepository<CustomerBankAccount, Integer> {

        /**
         * Find all active bank accounts for a given customer.
         *
         * @param customer Customer entity
         * @return list of active bank accounts
         */
        List<CustomerBankAccount> findByCustomerAndIsActiveTrue(Customer customer);

        boolean existsByAccountNumberAndCustomer(String accountNumber, Customer customer);

    boolean existsByUpiIdAndIsDelFalse(String upiId);


    boolean existsByAccountNumberAndCustomerAndBankAccountIdNot( String accountNumber, Customer customer, Integer bankAccountId);

    boolean existsByUpiIdAndIsDelFalseAndBankAccountIdNot(String upiId, Integer bankAccountId);
}
