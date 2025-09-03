//package com.incede.nbfc.core.monolith.masterdata.repository;
//
//import com.incede.nbfc.core.monolith.masterdata.domain.entity.LoanType;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//
///**
// * Repository interface for Loan Type operations.
// * Provides data access methods for loan types.
// *
// * @author Incede NBFC Development Team
// * @version 1.0.0
// */
//@Repository
//public interface LoanTypeRepository extends JpaRepository<LoanType, Long> {
//
//    /**
//     * Find loan type by loan type code.
//     *
//     * @param loanTypeCode the loan type code to search for
//     * @return Optional containing the loan type if found
//     */
//    Optional<LoanType> findByLoanTypeCode(String loanTypeCode);
//
//    /**
//     * Find all active loan types.
//     *
//     * @return List of active loan types
//     */
//    List<LoanType> findByIsActiveTrue();
//
//    /**
//     * Find loan types by name containing the given text (case-insensitive).
//     *
//     * @param name the name text to search for
//     * @return List of matching loan types
//     */
//    @Query("SELECT lt FROM LoanType lt WHERE LOWER(lt.loanTypeName) LIKE LOWER(CONCAT('%', :name, '%'))")
//    List<LoanType> findByLoanTypeNameContainingIgnoreCase(@Param("name") String name);
//
//    /**
//     * Find loan types within a specific amount range.
//     *
//     * @param minAmount the minimum amount
//     * @param maxAmount the maximum amount
//     * @return List of loan types within the amount range
//     */
//    @Query("SELECT lt FROM LoanType lt WHERE lt.minAmount <= :amount AND lt.maxAmount >= :amount AND lt.isActive = true")
//    List<LoanType> findByAmountRange(@Param("amount") BigDecimal amount);
//
//    /**
//     * Find loan types by tenure range.
//     *
//     * @param tenureMonths the tenure in months
//     * @return List of loan types matching the tenure
//     */
//    @Query("SELECT lt FROM LoanType lt WHERE lt.tenureMinMonths <= :tenureMonths AND lt.tenureMaxMonths >= :tenureMonths AND lt.isActive = true")
//    List<LoanType> findByTenureRange(@Param("tenureMonths") Integer tenureMonths);
//
//    /**
//     * Check if a loan type exists by loan type code.
//     *
//     * @param loanTypeCode the loan type code to check
//     * @return true if exists, false otherwise
//     */
//    boolean existsByLoanTypeCode(String loanTypeCode);
//}