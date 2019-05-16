package com.amtrak.application.repository;

import com.amtrak.application.domain.Spr;
import com.amtrak.application.domain.User;
import com.amtrak.application.domain.enumeration.Resolution;
import com.amtrak.application.domain.enumeration.Territory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Spr entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SprRepository extends JpaRepository<Spr, Long>, JpaSpecificationExecutor<Spr> {

    @Query("select spr from Spr spr where spr.user.login = ?#{principal.username}")
    List<Spr> findByUserIsCurrentUser();

    List<Spr> findAllByResolution(Resolution resolution);

    List<Spr> findAllByTerritory(Territory territory);

    List<Spr> findAllByUser(User user);

    List<Spr> findAllByUserAndResolution(User user, Resolution resolution);
}
