package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PersonDetails;
import com.mycompany.myapp.repository.PersonDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PersonDetails}.
 */
@Service
@Transactional
public class PersonDetailsService {

    private final Logger log = LoggerFactory.getLogger(PersonDetailsService.class);

    private final PersonDetailsRepository personDetailsRepository;

    public PersonDetailsService(PersonDetailsRepository personDetailsRepository) {
        this.personDetailsRepository = personDetailsRepository;
    }

    /**
     * Save a personDetails.
     *
     * @param personDetails the entity to save.
     * @return the persisted entity.
     */
    public PersonDetails save(PersonDetails personDetails) {
        log.debug("Request to save PersonDetails : {}", personDetails);
        return personDetailsRepository.save(personDetails);
    }

    /**
     * Get all the personDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonDetails> findAll(Pageable pageable) {
        log.debug("Request to get all PersonDetails");
        return personDetailsRepository.findAll(pageable);
    }


    /**
     * Get one personDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonDetails> findOne(Long id) {
        log.debug("Request to get PersonDetails : {}", id);
        return personDetailsRepository.findById(id);
    }

    /**
     * Delete the personDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PersonDetails : {}", id);
        personDetailsRepository.deleteById(id);
    }
}
