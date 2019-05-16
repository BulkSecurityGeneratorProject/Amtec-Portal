package com.amtrak.application.repository;

import com.amtrak.application.domain.TeamEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TeamEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamEventRepository extends JpaRepository<TeamEvent, Long>, JpaSpecificationExecutor<TeamEvent> {

}
