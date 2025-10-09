package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Purpose;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.ReferralSources;
import com.incede.nbfc.core.monolith.masterdata.dto.PurposeView;
import com.incede.nbfc.core.monolith.masterdata.dto.ReferralSourcesView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReferralSourceRepository extends JpaRepository<ReferralSources, Integer> {

    List<ReferralSourcesView> findByIsDelFalseAndIsActiveTrue();

    Optional<ReferralSources> findByIdentity( UUID referralSourceId);

}
