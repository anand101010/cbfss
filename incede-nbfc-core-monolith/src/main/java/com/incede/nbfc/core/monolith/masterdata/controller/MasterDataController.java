//package com.incede.nbfc.core.monolith.masterdata.controller;
//
//import com.incede.nbfc.core.monolith.masterdata.domain.entity.DocumentType;
//import com.incede.nbfc.core.monolith.masterdata.domain.entity.LoanType;
//import com.incede.nbfc.core.monolith.masterdata.domain.entity.ProductCategory;
//import com.incede.nbfc.core.monolith.masterdata.repository.DocumentTypeRepository;
//import com.incede.nbfc.core.monolith.masterdata.repository.LoanTypeRepository;
//import com.incede.nbfc.core.monolith.masterdata.repository.ProductCategoryRepository;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//
///**
// * Master Data Controller for Incede NBFC Core Monolith Service.
// * Provides REST endpoints for accessing master data including product categories,
// * loan types, and document types.
// *
// * @author Incede NBFC Development Team
// * @version 1.0.0
// */
//@RestController
//@RequestMapping("/api/v1/masterdata")
//@RequiredArgsConstructor
//@Slf4j
//@Tag(name = "Master Data", description = "Master Data Management APIs")
//@CrossOrigin(origins = "*")
//public class MasterDataController {
//
//    private final ProductCategoryRepository productCategoryRepository;
//    private final LoanTypeRepository loanTypeRepository;
//    private final DocumentTypeRepository documentTypeRepository;
//
//    // ========================================
//    // PRODUCT CATEGORY ENDPOINTS
//    // ========================================
//
//    /**
//     * Get all product categories.
//     *
//     * @return List of all product categories
//     */
//    @GetMapping("/product-categories")
//    @Operation(summary = "Get all product categories", description = "Retrieves all product categories from the system")
//    public ResponseEntity<List<ProductCategory>> getAllProductCategories() {
//        log.info("Fetching all product categories");
//        List<ProductCategory> categories = productCategoryRepository.findAll();
//        log.info("Found {} product categories", categories.size());
//        return ResponseEntity.ok(categories);
//    }
//
//    /**
//     * Get all active product categories.
//     *
//     * @return List of active product categories
//     */
//    @GetMapping("/product-categories/active")
//    @Operation(summary = "Get active product categories", description = "Retrieves only active product categories")
//    public ResponseEntity<List<ProductCategory>> getActiveProductCategories() {
//        log.info("Fetching active product categories");
//        List<ProductCategory> categories = productCategoryRepository.findByIsActiveTrue();
//        log.info("Found {} active product categories", categories.size());
//        return ResponseEntity.ok(categories);
//    }
//
//    /**
//     * Get product category by ID.
//     *
//     * @param id the product category ID
//     * @return Product category if found
//     */
//    @GetMapping("/product-categories/{id}")
//    @Operation(summary = "Get product category by ID", description = "Retrieves a specific product category by its ID")
//    public ResponseEntity<ProductCategory> getProductCategoryById(
//            @Parameter(description = "Product category ID") @PathVariable Long id) {
//        log.info("Fetching product category with ID: {}", id);
//        Optional<ProductCategory> category = productCategoryRepository.findById(id);
//        return category.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    /**
//     * Get product category by category code.
//     *
//     * @param categoryCode the category code
//     * @return Product category if found
//     */
//    @GetMapping("/product-categories/code/{categoryCode}")
//    @Operation(summary = "Get product category by code", description = "Retrieves a product category by its category code")
//    public ResponseEntity<ProductCategory> getProductCategoryByCode(
//            @Parameter(description = "Category code") @PathVariable String categoryCode) {
//        log.info("Fetching product category with code: {}", categoryCode);
//        Optional<ProductCategory> category = productCategoryRepository.findByCategoryCode(categoryCode);
//        return category.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    /**
//     * Search product categories by name.
//     *
//     * @param name the name to search for
//     * @return List of matching product categories
//     */
//    @GetMapping("/product-categories/search")
//    @Operation(summary = "Search product categories by name", description = "Searches product categories by name (case-insensitive)")
//    public ResponseEntity<List<ProductCategory>> searchProductCategoriesByName(
//            @Parameter(description = "Name to search for") @RequestParam String name) {
//        log.info("Searching product categories with name containing: {}", name);
//        List<ProductCategory> categories = productCategoryRepository.findByCategoryNameContainingIgnoreCase(name);
//        log.info("Found {} matching product categories", categories.size());
//        return ResponseEntity.ok(categories);
//    }
//
//    // ========================================
//    // LOAN TYPE ENDPOINTS
//    // ========================================
//
//    /**
//     * Get all loan types.
//     *
//     * @return List of all loan types
//     */
//    @GetMapping("/loan-types")
//    @Operation(summary = "Get all loan types", description = "Retrieves all loan types from the system")
//    public ResponseEntity<List<LoanType>> getAllLoanTypes() {
//        log.info("Fetching all loan types");
//        List<LoanType> loanTypes = loanTypeRepository.findAll();
//        log.info("Found {} loan types", loanTypes.size());
//        return ResponseEntity.ok(loanTypes);
//    }
//
//    /**
//     * Get all active loan types.
//     *
//     * @return List of active loan types
//     */
//    @GetMapping("/loan-types/active")
//    @Operation(summary = "Get active loan types", description = "Retrieves only active loan types")
//    public ResponseEntity<List<LoanType>> getActiveLoanTypes() {
//        log.info("Fetching active loan types");
//        List<LoanType> loanTypes = loanTypeRepository.findByIsActiveTrue();
//        log.info("Found {} active loan types", loanTypes.size());
//        return ResponseEntity.ok(loanTypes);
//    }
//
//    /**
//     * Get loan type by ID.
//     *
//     * @param id the loan type ID
//     * @return Loan type if found
//     */
//    @GetMapping("/loan-types/{id}")
//    @Operation(summary = "Get loan type by ID", description = "Retrieves a specific loan type by its ID")
//    public ResponseEntity<LoanType> getLoanTypeById(
//            @Parameter(description = "Loan type ID") @PathVariable Long id) {
//        log.info("Fetching loan type with ID: {}", id);
//        Optional<LoanType> loanType = loanTypeRepository.findById(id);
//        return loanType.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    /**
//     * Get loan type by loan type code.
//     *
//     * @param loanTypeCode the loan type code
//     * @return Loan type if found
//     */
//    @GetMapping("/loan-types/code/{loanTypeCode}")
//    @Operation(summary = "Get loan type by code", description = "Retrieves a loan type by its loan type code")
//    public ResponseEntity<LoanType> getLoanTypeByCode(
//            @Parameter(description = "Loan type code") @PathVariable String loanTypeCode) {
//        log.info("Fetching loan type with code: {}", loanTypeCode);
//        Optional<LoanType> loanType = loanTypeRepository.findByLoanTypeCode(loanTypeCode);
//        return loanType.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    /**
//     * Search loan types by name.
//     *
//     * @param name the name to search for
//     * @return List of matching loan types
//     */
//    @GetMapping("/loan-types/search")
//    @Operation(summary = "Search loan types by name", description = "Searches loan types by name (case-insensitive)")
//    public ResponseEntity<List<LoanType>> searchLoanTypesByName(
//            @Parameter(description = "Name to search for") @RequestParam String name) {
//        log.info("Searching loan types with name containing: {}", name);
//        List<LoanType> loanTypes = loanTypeRepository.findByLoanTypeNameContainingIgnoreCase(name);
//        log.info("Found {} matching loan types", loanTypes.size());
//        return ResponseEntity.ok(loanTypes);
//    }
//
//    /**
//     * Find loan types by amount range.
//     *
//     * @param amount the loan amount
//     * @return List of loan types suitable for the amount
//     */
//    @GetMapping("/loan-types/amount-range")
//    @Operation(summary = "Find loan types by amount", description = "Finds loan types suitable for a specific loan amount")
//    public ResponseEntity<List<LoanType>> findLoanTypesByAmount(
//            @Parameter(description = "Loan amount") @RequestParam BigDecimal amount) {
//        log.info("Finding loan types suitable for amount: {}", amount);
//        List<LoanType> loanTypes = loanTypeRepository.findByAmountRange(amount);
//        log.info("Found {} suitable loan types", loanTypes.size());
//        return ResponseEntity.ok(loanTypes);
//    }
//
//    /**
//     * Find loan types by tenure range.
//     *
//     * @param tenureMonths the tenure in months
//     * @return List of loan types suitable for the tenure
//     */
//    @GetMapping("/loan-types/tenure-range")
//    @Operation(summary = "Find loan types by tenure", description = "Finds loan types suitable for a specific tenure")
//    public ResponseEntity<List<LoanType>> findLoanTypesByTenure(
//            @Parameter(description = "Tenure in months") @RequestParam Integer tenureMonths) {
//        log.info("Finding loan types suitable for tenure: {} months", tenureMonths);
//        List<LoanType> loanTypes = loanTypeRepository.findByTenureRange(tenureMonths);
//        log.info("Found {} suitable loan types", loanTypes.size());
//        return ResponseEntity.ok(loanTypes);
//    }
//
//    // ========================================
//    // DOCUMENT TYPE ENDPOINTS
//    // ========================================
//
//    /**
//     * Get all document types.
//     *
//     * @return List of all document types
//     */
//    @GetMapping("/document-types")
//    @Operation(summary = "Get all document types", description = "Retrieves all document types from the system")
//    public ResponseEntity<List<DocumentType>> getAllDocumentTypes() {
//        log.info("Fetching all document types");
//        List<DocumentType> documentTypes = documentTypeRepository.findAll();
//        log.info("Found {} document types", documentTypes.size());
//        return ResponseEntity.ok(documentTypes);
//    }
//
//    /**
//     * Get all active document types.
//     *
//     * @return List of active document types
//     */
//    @GetMapping("/document-types/active")
//    @Operation(summary = "Get active document types", description = "Retrieves only active document types")
//    public ResponseEntity<List<DocumentType>> getActiveDocumentTypes() {
//        log.info("Fetching active document types");
//        List<DocumentType> documentTypes = documentTypeRepository.findByIsActiveTrue();
//        log.info("Found {} active document types", documentTypes.size());
//        return ResponseEntity.ok(documentTypes);
//    }
//
//    /**
//     * Get all mandatory document types.
//     *
//     * @return List of mandatory document types
//     */
//    @GetMapping("/document-types/mandatory")
//    @Operation(summary = "Get mandatory document types", description = "Retrieves only mandatory document types")
//    public ResponseEntity<List<DocumentType>> getMandatoryDocumentTypes() {
//        log.info("Fetching mandatory document types");
//        List<DocumentType> documentTypes = documentTypeRepository.findByIsMandatoryTrue();
//        log.info("Found {} mandatory document types", documentTypes.size());
//        return ResponseEntity.ok(documentTypes);
//    }
//
//    /**
//     * Get document type by ID.
//     *
//     * @param id the document type ID
//     * @return Document type if found
//     */
//    @GetMapping("/document-types/{id}")
//    @Operation(summary = "Get document type by ID", description = "Retrieves a specific document type by its ID")
//    public ResponseEntity<DocumentType> getDocumentTypeById(
//            @Parameter(description = "Document type ID") @PathVariable Long id) {
//        log.info("Fetching document type with ID: {}", id);
//        Optional<DocumentType> documentType = documentTypeRepository.findById(id);
//        return documentType.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    /**
//     * Get document type by document type code.
//     *
//     * @param documentTypeCode the document type code
//     * @return Document type if found
//     */
//    @GetMapping("/document-types/code/{documentTypeCode}")
//    @Operation(summary = "Get document type by code", description = "Retrieves a document type by its document type code")
//    public ResponseEntity<DocumentType> getDocumentTypeByCode(
//            @Parameter(description = "Document type code") @PathVariable String documentTypeCode) {
//        log.info("Fetching document type with code: {}", documentTypeCode);
//        Optional<DocumentType> documentType = documentTypeRepository.findByDocumentTypeCode(documentTypeCode);
//        return documentType.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    /**
//     * Search document types by name.
//     *
//     * @param name the name to search for
//     * @return List of matching document types
//     */
//    @GetMapping("/document-types/search")
//    @Operation(summary = "Search document types by name", description = "Searches document types by name (case-insensitive)")
//    public ResponseEntity<List<DocumentType>> searchDocumentTypesByName(
//            @Parameter(description = "Name to search for") @RequestParam String name) {
//        log.info("Searching document types with name containing: {}", name);
//        List<DocumentType> documentTypes = documentTypeRepository.findByDocumentTypeNameContainingIgnoreCase(name);
//        log.info("Found {} matching document types", documentTypes.size());
//        return ResponseEntity.ok(documentTypes);
//    }
//
//    /**
//     * Find document types by file extension.
//     *
//     * @param extension the file extension
//     * @return List of document types supporting the extension
//     */
//    @GetMapping("/document-types/extension/{extension}")
//    @Operation(summary = "Find document types by extension", description = "Finds document types that support a specific file extension")
//    public ResponseEntity<List<DocumentType>> findDocumentTypesByExtension(
//            @Parameter(description = "File extension") @PathVariable String extension) {
//        log.info("Finding document types supporting extension: {}", extension);
//        List<DocumentType> documentTypes = documentTypeRepository.findByFileExtension(extension);
//        log.info("Found {} supporting document types", documentTypes.size());
//        return ResponseEntity.ok(documentTypes);
//    }
//
//    // ========================================
//    // SUMMARY ENDPOINTS
//    // ========================================
//
//    /**
//     * Get master data summary.
//     *
//     * @return Summary of all master data counts
//     */
//    @GetMapping("/summary")
//    @Operation(summary = "Get master data summary", description = "Retrieves a summary of all master data counts")
//    public ResponseEntity<MasterDataSummary> getMasterDataSummary() {
//        log.info("Fetching master data summary");
//
//        long productCategoryCount = productCategoryRepository.count();
//        long loanTypeCount = loanTypeRepository.count();
//        long documentTypeCount = documentTypeRepository.count();
//
//        long activeProductCategoryCount = productCategoryRepository.findByIsActiveTrue().size();
//        long activeLoanTypeCount = loanTypeRepository.findByIsActiveTrue().size();
//        long activeDocumentTypeCount = documentTypeRepository.findByIsActiveTrue().size();
//
//        MasterDataSummary summary = MasterDataSummary.builder()
//                .totalProductCategories(productCategoryCount)
//                .totalLoanTypes(loanTypeCount)
//                .totalDocumentTypes(documentTypeCount)
//                .activeProductCategories(activeProductCategoryCount)
//                .activeLoanTypes(activeLoanTypeCount)
//                .activeDocumentTypes(activeDocumentTypeCount)
//                .build();
//
//        log.info("Master data summary: {}", summary);
//        return ResponseEntity.ok(summary);
//    }
//
//    /**
//     * Master Data Summary DTO.
//     */
//    public static class MasterDataSummary {
//        private long totalProductCategories;
//        private long totalLoanTypes;
//        private long totalDocumentTypes;
//        private long activeProductCategories;
//        private long activeLoanTypes;
//        private long activeDocumentTypes;
//
//        // Getters and Setters
//        public long getTotalProductCategories() { return totalProductCategories; }
//        public void setTotalProductCategories(long totalProductCategories) { this.totalProductCategories = totalProductCategories; }
//
//        public long getTotalLoanTypes() { return totalLoanTypes; }
//        public void setTotalLoanTypes(long totalLoanTypes) { this.totalLoanTypes = totalLoanTypes; }
//
//        public long getTotalDocumentTypes() { return totalDocumentTypes; }
//        public void setTotalDocumentTypes(long totalDocumentTypes) { this.totalDocumentTypes = totalDocumentTypes; }
//
//        public long getActiveProductCategories() { return activeProductCategories; }
//        public void setActiveProductCategories(long activeProductCategories) { this.activeProductCategories = activeProductCategories; }
//
//        public long getActiveLoanTypes() { return activeLoanTypes; }
//        public void setActiveLoanTypes(long activeLoanTypes) { this.activeLoanTypes = activeLoanTypes; }
//
//        public long getActiveDocumentTypes() { return activeDocumentTypes; }
//        public void setActiveDocumentTypes(long activeDocumentTypes) { this.activeDocumentTypes = activeDocumentTypes; }
//
//        // Builder pattern
//        public static MasterDataSummaryBuilder builder() {
//            return new MasterDataSummaryBuilder();
//        }
//
//        public static class MasterDataSummaryBuilder {
//            private MasterDataSummary summary = new MasterDataSummary();
//
//            public MasterDataSummaryBuilder totalProductCategories(long count) {
//                summary.totalProductCategories = count;
//                return this;
//            }
//
//            public MasterDataSummaryBuilder totalLoanTypes(long count) {
//                summary.totalLoanTypes = count;
//                return this;
//            }
//
//            public MasterDataSummaryBuilder totalDocumentTypes(long count) {
//                summary.totalDocumentTypes = count;
//                return this;
//            }
//
//            public MasterDataSummaryBuilder activeProductCategories(long count) {
//                summary.activeProductCategories = count;
//                return this;
//            }
//
//            public MasterDataSummaryBuilder activeLoanTypes(long count) {
//                summary.activeLoanTypes = count;
//                return this;
//            }
//
//            public MasterDataSummaryBuilder activeDocumentTypes(long count) {
//                summary.activeDocumentTypes = count;
//                return this;
//            }
//
//            public MasterDataSummary build() {
//                return summary;
//            }
//        }
//    }
//}