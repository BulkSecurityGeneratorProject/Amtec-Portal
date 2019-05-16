package com.amtrak.application.repository;

import com.amtrak.application.domain.Release;
import com.amtrak.application.domain.enumeration.Territory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;


/**
 * Spring Data  repository for the Release entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReleaseRepository extends JpaRepository<Release, Long>, JpaSpecificationExecutor<Release> {
    List<Release> findAllByCurrentIsTrue();
    List<Release> findAllByDateIsGreaterThanEqual(Instant now);
    List<Release> findAllByDateIsGreaterThanAndCurrent(Instant now, Boolean current);
    List<Release> findAllByTerritoryAndCurrentIsTrue(Territory territory);
}
