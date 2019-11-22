package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PersonDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PersonDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonDetailsRepository extends JpaRepository<PersonDetails, Long>, JpaSpecificationExecutor<PersonDetails> {

}
