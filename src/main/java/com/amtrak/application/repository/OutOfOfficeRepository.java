package com.amtrak.application.repository;

import com.amtrak.application.domain.OutOfOffice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the OutOfOffice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OutOfOfficeRepository extends JpaRepository<OutOfOffice, Long>, JpaSpecificationExecutor<OutOfOffice> {

    @Query("select outOfOffice from OutOfOffice outOfOffice where outOfOffice.user.login = ?#{principal.username}")
    List<OutOfOffice> findByUserIsCurrentUser();

}
