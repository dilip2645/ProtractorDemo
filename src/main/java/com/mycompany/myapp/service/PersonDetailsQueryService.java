package com.mycompany.myapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mycompany.myapp.domain.PersonDetails;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.PersonDetailsRepository;
import com.mycompany.myapp.service.dto.PersonDetailsCriteria;

/**
 * Service for executing complex queries for {@link PersonDetails} entities in the database.
 * The main input is a {@link PersonDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PersonDetails} or a {@link Page} of {@link PersonDetails} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonDetailsQueryService extends QueryService<PersonDetails> {

    private final Logger log = LoggerFactory.getLogger(PersonDetailsQueryService.class);

    private final PersonDetailsRepository personDetailsRepository;

    public PersonDetailsQueryService(PersonDetailsRepository personDetailsRepository) {
        this.personDetailsRepository = personDetailsRepository;
    }

    /**
     * Return a {@link List} of {@link PersonDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PersonDetails> findByCriteria(PersonDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PersonDetails> specification = createSpecification(criteria);
        return personDetailsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PersonDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonDetails> findByCriteria(PersonDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PersonDetails> specification = createSpecification(criteria);
        return personDetailsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PersonDetails> specification = createSpecification(criteria);
        return personDetailsRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    protected Specification<PersonDetails> createSpecification(PersonDetailsCriteria criteria) {
        Specification<PersonDetails> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PersonDetails_.id));
            }
            if (criteria.getPersonName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPersonName(), PersonDetails_.personName));
            }
            if (criteria.getPersonId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPersonId(), PersonDetails_.personId));
            }
            if (criteria.getPersonAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPersonAddress(), PersonDetails_.personAddress));
            }
        }
        return specification;
    }
}
