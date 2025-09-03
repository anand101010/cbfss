//package com.incede.nbfc.core.monolith.masterdata.repository;
//
//import com.incede.nbfc.core.monolith.masterdata.domain.entity.DocumentType;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
///**
// * Repository interface for Document Type operations.
// * Provides data access methods for document types.
// *
// * @author Incede NBFC Development Team
// * @version 1.0.0
// */
//@Repository
//public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
//
//    /**
//     * Find document type by document type code.
//     *
//     * @param documentTypeCode the document type code to search for
//     * @return Optional containing the document type if found
//     */
//    Optional<DocumentType> findByDocumentTypeCode(String documentTypeCode);
//
//    /**
//     * Find all active document types.
//     *
//     * @return List of active document types
//     */
//    List<DocumentType> findByIsActiveTrue();
//
//    /**
//     * Find all mandatory document types.
//     *
//     * @return List of mandatory document types
//     */
//    List<DocumentType> findByIsMandatoryTrue();
//
//    /**
//     * Find document types by name containing the given text (case-insensitive).
//     *
//     * @param name the name text to search for
//     * @return List of matching document types
//     */
//    @Query("SELECT dt FROM DocumentType dt WHERE LOWER(dt.documentTypeName) LIKE LOWER(CONCAT('%', :name, '%'))")
//    List<DocumentType> findByDocumentTypeNameContainingIgnoreCase(@Param("name") String name);
//
//    /**
//     * Find document types by file extension.
//     *
//     * @param extension the file extension to search for
//     * @return List of document types supporting the extension
//     */
//    @Query("SELECT dt FROM DocumentType dt WHERE :extension IN (SELECT ext FROM dt.allowedExtensions ext)")
//    List<DocumentType> findByFileExtension(@Param("extension") String extension);
//
//    /**
//     * Check if a document type exists by document type code.
//     *
//     * @param documentTypeCode the document type code to check
//     * @return true if exists, false otherwise
//     */
//    boolean existsByDocumentTypeCode(String documentTypeCode);
//}