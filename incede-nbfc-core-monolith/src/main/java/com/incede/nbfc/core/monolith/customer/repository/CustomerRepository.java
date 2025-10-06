package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query("""
        SELECT DISTINCT c FROM Customer c
        LEFT JOIN FETCH c.branchId b
        WHERE (:branchCode IS NULL OR b.branchCode = :branchCode)
          AND (:branchId IS NULL OR b.branchId = :branchId)
          AND (:mobileNumber IS NULL OR c.mobileNumber = :mobileNumber)
          AND (:emailId IS NULL OR c.crmReferenceId = :emailId)
          AND (:panCard IS NULL OR c.customerCode = :panCard)
          AND (:aadhaarNumber IS NULL OR c.aadharVaultId = :aadhaarNumber)
          AND (:voterId IS NULL OR c.crmReferenceId = :voterId)
          AND (:passportNumber IS NULL OR c.crmReferenceId = :passportNumber)
          AND (:customerName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :customerName, '%'))
                                  OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :customerName, '%')))
        """)
    List<Customer> searchCustomers(
            @Param("branchCode") String branchCode,
            @Param("branchId") Integer branchId,
            @Param("mobileNumber") Integer mobileNumber,
            @Param("emailId") String emailId,
            @Param("panCard") String panCard,
            @Param("aadhaarNumber") String aadhaarNumber,
            @Param("voterId") String voterId,
            @Param("passportNumber") String passportNumber,
            @Param("customerName") String customerName
    );
    }


