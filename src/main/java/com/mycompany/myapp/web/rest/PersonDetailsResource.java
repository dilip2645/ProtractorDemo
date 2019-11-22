package com.mycompany.myapp.web.rest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.mycompany.myapp.common.ReportGenerator;
import com.mycompany.myapp.common.FileOperation;
import com.mycompany.myapp.common.ReportFileType;
import org.springframework.core.io.InputStreamResource;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mycompany.myapp.domain.PersonDetails;
import com.mycompany.myapp.service.PersonDetailsService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.PersonDetailsCriteria;
import com.mycompany.myapp.service.PersonDetailsQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.PersonDetails}.
 */
@RestController
@RequestMapping("/api")
public class PersonDetailsResource {

    private final Logger log = LoggerFactory.getLogger(PersonDetailsResource.class);

    private static final String ENTITY_NAME = "personDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonDetailsService personDetailsService;

    private final PersonDetailsQueryService personDetailsQueryService;

    private final ReportGenerator reportGenerator;

    public PersonDetailsResource(PersonDetailsService personDetailsService, PersonDetailsQueryService personDetailsQueryService, ReportGenerator reportGenerator) {
        this.personDetailsService = personDetailsService;
        this.personDetailsQueryService = personDetailsQueryService;
        this.reportGenerator = reportGenerator;
    }

    /**
     * {@code POST  /person-details} : Create a new personDetails.
     *
     * @param personDetails the personDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personDetails, or with status {@code 400 (Bad Request)} if the personDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/person-details")
    public ResponseEntity<PersonDetails> createPersonDetails(@Valid @RequestBody PersonDetails personDetails) throws URISyntaxException {
        log.debug("REST request to save PersonDetails : {}", personDetails);
        if (personDetails.getId() != null) {
            throw new BadRequestAlertException("A new personDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonDetails result = personDetailsService.save(personDetails);
        return ResponseEntity.created(new URI("/api/person-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /person-details} : Updates an existing personDetails.
     *
     * @param personDetails the personDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personDetails,
     * or with status {@code 400 (Bad Request)} if the personDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/person-details")
    public ResponseEntity<PersonDetails> updatePersonDetails(@Valid @RequestBody PersonDetails personDetails) throws URISyntaxException {
        log.debug("REST request to update PersonDetails : {}", personDetails);
        if (personDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PersonDetails result = personDetailsService.save(personDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, personDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /person-details} : get all the personDetails.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personDetails in body.
     */
    @GetMapping("/person-details")
    public ResponseEntity<List<PersonDetails>> getAllPersonDetails(PersonDetailsCriteria criteria, Pageable pageable,@RequestParam(value = "exportType", required = false) String exportType) {
        log.debug("REST request to get PersonDetails by criteria: {}", criteria);
        if(exportType==null) {
        PageRequest pageRequest;
        Sort.Order order = new Sort.Order(pageable.getSort().iterator().next().getDirection(),pageable.getSort().iterator().next().getProperty()).ignoreCase();
        pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(order));
        Page<PersonDetails> page = personDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
        else{
           
        List<String> excludedHeader = new ArrayList<>();
			excludedHeader.add("id");
			Date date = Calendar.getInstance().getTime();
			DateFormat dateFormat = new SimpleDateFormat("hhmmssSSS");
			String strDate = dateFormat.format(date);
			String fileName = "Demo_"+strDate;

			List<PersonDetails> personDetailsList = personDetailsQueryService.findByCriteria(criteria);

			ReportFileType  report = reportGenerator.generateListingReport(
                personDetailsList,
					exportType,
					getPersonDetailsReportHeader(),
					"PersonDetails",
					"PersonDetails",
					"PersonDetails Report",
					excludedHeader,
					null);           
			if(report.getReportFile().exists()){
				FileOperation inputStream;
				try {
					inputStream = new FileOperation(report.getReportFile());
					return new ResponseEntity(new InputStreamResource(inputStream), report.getHttpHeader(),
							HttpStatus.OK);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
        }
        return null;
    }

    /**
    * {@code GET  /person-details/count} : count all the personDetails.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/person-details/count")
    public ResponseEntity<Long> countPersonDetails(PersonDetailsCriteria criteria) {
        log.debug("REST request to count PersonDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(personDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /person-details/:id} : get the "id" personDetails.
     *
     * @param id the id of the personDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/person-details/{id}")
    public ResponseEntity<PersonDetails> getPersonDetails(@PathVariable Long id) {
        log.debug("REST request to get PersonDetails : {}", id);
        Optional<PersonDetails> personDetails = personDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personDetails);
    }

    /**
     * {@code DELETE  /person-details/:id} : delete the "id" personDetails.
     *
     * @param id the id of the personDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/person-details/{id}")
    public ResponseEntity<Void> deletePersonDetails(@PathVariable Long id) {
        log.debug("REST request to delete PersonDetails : {}", id);
        personDetailsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    public static Map<String, String> getPersonDetailsReportHeader() {
        Map<String, String> reportHeaders = new LinkedHashMap<>();
            reportHeaders.put("personName","PersonName");
            reportHeaders.put("personId","PersonId");
            reportHeaders.put("personAddress","PersonAddress");
        return reportHeaders;
    }
}
