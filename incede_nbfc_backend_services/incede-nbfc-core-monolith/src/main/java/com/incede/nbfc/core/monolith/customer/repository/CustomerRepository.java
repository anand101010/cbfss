package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import jakarta.validation.constraints.NotBlank;
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

    @Query(value = """
    SELECT DISTINCT c.*
    FROM customers.customer c
    LEFT JOIN master_data.branches b ON b.branch_id = c.branch_id
    LEFT JOIN customers.customer_contacts cc ON cc.customer_id = c.customer_id
    LEFT JOIN master_data.contact_types ct ON ct.contact_type_id = cc.contact_type
    WHERE
        (:branchCode IS NULL OR b.branch_code = :branchCode)
        AND (:branchId IS NULL OR c.branch_id = :branchId)
        AND (:mobileNumber IS NULL OR (
            c.mobile_number = :mobileNumber 
            OR (
                cc.contact_value = :mobileNumber 
                AND ct.contact_type = 'Mobile'
                AND cc.is_active = true
            )
        ))
        AND (:emailId IS NULL OR (
            cc.contact_value = :emailId
            AND ct.contact_type = 'Email'
            AND cc.is_active = true
        ))
        AND (:panCard IS NULL OR EXISTS (
            SELECT 1 FROM customers.customer_kyc k 
            WHERE k.customer_id = c.customer_id 
              AND LOWER(k.id_number) = LOWER(:panCard)
        ))
        AND (:aadhaarNumber IS NULL OR c.aadhar_vault_id = :aadhaarNumber)
        AND (:voterId IS NULL OR EXISTS (
            SELECT 1 FROM customers.customer_kyc k 
            WHERE k.customer_id = c.customer_id 
              AND LOWER(k.id_number) = LOWER(:voterId)
        ))
        AND (:passportNumber IS NULL OR EXISTS (
            SELECT 1 FROM customers.customer_kyc k 
            WHERE k.customer_id = c.customer_id 
              AND LOWER(k.id_number) = LOWER(:passportNumber)
        ))
        AND (:customerName IS NULL OR 
            LOWER(c.first_name::text) LIKE LOWER(CONCAT('%', :customerName, '%')) OR 
            LOWER(c.last_name::text) LIKE LOWER(CONCAT('%', :customerName, '%'))
        )
    """, nativeQuery = true)
    List<Customer> searchCustomersFlexible(
            @Param("branchCode") String branchCode,
            @Param("branchId") Integer branchId,
            @Param("mobileNumber") String mobileNumber,
            @Param("emailId") String emailId,
            @Param("panCard") String panCard,
            @Param("aadhaarNumber") String aadhaarNumber,
            @Param("voterId") String voterId,
            @Param("passportNumber") String passportNumber,
            @Param("customerName") String customerName
    );
    boolean existsByTenantAndMobileNumber(Tenant tenant,String mobileNumber);
}
