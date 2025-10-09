
package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.DocumentType;
import com.incede.nbfc.core.monolith.masterdata.dto.DocumentTypeView;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Document Type operations.
 * Provides data access methods for document types.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {

    List<DocumentTypeView> findByIsDelFalseAndIsActiveTrue();

    Optional<DocumentType> findByIdentity(UUID idType);

}