//package com.incede.nbfc.core.monolith.masterdata.repository;
//
//import com.incede.nbfc.core.monolith.masterdata.domain.entity.ProductCategory;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
///**
// * Repository interface for Product Category operations.
// * Provides data access methods for product categories.
// *
// * @author Incede NBFC Development Team
// * @version 1.0.0
// */
//@Repository
//public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
//
//    /**
//     * Find product category by category code.
//     *
//     * @param categoryCode the category code to search for
//     * @return Optional containing the product category if found
//     */
//    Optional<ProductCategory> findByCategoryCode(String categoryCode);
//
//    /**
//     * Find all active product categories.
//     *
//     * @return List of active product categories
//     */
//    List<ProductCategory> findByIsActiveTrue();
//
//    /**
//     * Find product categories by name containing the given text (case-insensitive).
//     *
//     * @param name the name text to search for
//     * @return List of matching product categories
//     */
//    @Query("SELECT pc FROM ProductCategory pc WHERE LOWER(pc.categoryName) LIKE LOWER(CONCAT('%', :name, '%'))")
//    List<ProductCategory> findByCategoryNameContainingIgnoreCase(@Param("name") String name);
//
//    /**
//     * Check if a product category exists by category code.
//     *
//     * @param categoryCode the category code to check
//     * @return true if exists, false otherwise
//     */
//    boolean existsByCategoryCode(String categoryCode);
//}