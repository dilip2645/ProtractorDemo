package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ProtractorTestingApp;
import com.mycompany.myapp.domain.PersonDetails;
import com.mycompany.myapp.repository.PersonDetailsRepository;
import com.mycompany.myapp.service.PersonDetailsService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.PersonDetailsCriteria;
import com.mycompany.myapp.service.PersonDetailsQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PersonDetailsResource} REST controller.
 */
@SpringBootTest(classes = ProtractorTestingApp.class)
public class PersonDetailsResourceIT {

    private static final String DEFAULT_PERSON_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PERSON_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PERSON_ID = 1;
    private static final Integer UPDATED_PERSON_ID = 2;
    private static final Integer SMALLER_PERSON_ID = 1 - 1;

    private static final String DEFAULT_PERSON_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PERSON_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private PersonDetailsRepository personDetailsRepository;

    @Autowired
    private PersonDetailsService personDetailsService;

    @Autowired
    private PersonDetailsQueryService personDetailsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPersonDetailsMockMvc;

    private PersonDetails personDetails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PersonDetailsResource personDetailsResource = new PersonDetailsResource(personDetailsService, personDetailsQueryService,null);
        this.restPersonDetailsMockMvc = MockMvcBuilders.standaloneSetup(personDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonDetails createEntity(EntityManager em) {
        PersonDetails personDetails = new PersonDetails()
            .personName(DEFAULT_PERSON_NAME)
            .personId(DEFAULT_PERSON_ID)
            .personAddress(DEFAULT_PERSON_ADDRESS);
        return personDetails;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonDetails createUpdatedEntity(EntityManager em) {
        PersonDetails personDetails = new PersonDetails()
            .personName(UPDATED_PERSON_NAME)
            .personId(UPDATED_PERSON_ID)
            .personAddress(UPDATED_PERSON_ADDRESS);
        return personDetails;
    }

    @BeforeEach
    public void initTest() {
        personDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createPersonDetails() throws Exception {
        int databaseSizeBeforeCreate = personDetailsRepository.findAll().size();

        // Create the PersonDetails
        restPersonDetailsMockMvc.perform(post("/api/person-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personDetails)))
            .andExpect(status().isCreated());

        // Validate the PersonDetails in the database
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        PersonDetails testPersonDetails = personDetailsList.get(personDetailsList.size() - 1);
        assertThat(testPersonDetails.getPersonName()).isEqualTo(DEFAULT_PERSON_NAME);
        assertThat(testPersonDetails.getPersonId()).isEqualTo(DEFAULT_PERSON_ID);
        assertThat(testPersonDetails.getPersonAddress()).isEqualTo(DEFAULT_PERSON_ADDRESS);
    }

    @Test
    @Transactional
    public void createPersonDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = personDetailsRepository.findAll().size();

        // Create the PersonDetails with an existing ID
        personDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonDetailsMockMvc.perform(post("/api/person-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personDetails)))
            .andExpect(status().isBadRequest());

        // Validate the PersonDetails in the database
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPersonIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = personDetailsRepository.findAll().size();
        // set the field null
        personDetails.setPersonId(null);

        // Create the PersonDetails, which fails.

        restPersonDetailsMockMvc.perform(post("/api/person-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personDetails)))
            .andExpect(status().isBadRequest());

        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPersonDetails() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        // Get all the personDetailsList
        restPersonDetailsMockMvc.perform(get("/api/person-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].personName").value(hasItem(DEFAULT_PERSON_NAME.toString())))
            .andExpect(jsonPath("$.[*].personId").value(hasItem(DEFAULT_PERSON_ID)))
            .andExpect(jsonPath("$.[*].personAddress").value(hasItem(DEFAULT_PERSON_ADDRESS.toString())));
    }
    
    @Test
    @Transactional
    public void getPersonDetails() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        // Get the personDetails
        restPersonDetailsMockMvc.perform(get("/api/person-details/{id}", personDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(personDetails.getId().intValue()))
            .andExpect(jsonPath("$.personName").value(DEFAULT_PERSON_NAME.toString()))
            .andExpect(jsonPath("$.personId").value(DEFAULT_PERSON_ID))
            .andExpect(jsonPath("$.personAddress").value(DEFAULT_PERSON_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getAllPersonDetailsByPersonNameIsEqualToSomething() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        // Get all the personDetailsList where personName equals to DEFAULT_PERSON_NAME
        defaultPersonDetailsShouldBeFound("personName.equals=" + DEFAULT_PERSON_NAME);

        // Get all the personDetailsList where personName equals to UPDATED_PERSON_NAME
        defaultPersonDetailsShouldNotBeFound("personName.equals=" + UPDATED_PERSON_NAME);
    }

    @Test
    @Transactional
    public void getAllPersonDetailsByPersonNameIsInShouldWork() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        // Get all the personDetailsList where personName in DEFAULT_PERSON_NAME or UPDATED_PERSON_NAME
        defaultPersonDetailsShouldBeFound("personName.in=" + DEFAULT_PERSON_NAME + "," + UPDATED_PERSON_NAME);

        // Get all the personDetailsList where personName equals to UPDATED_PERSON_NAME
        defaultPersonDetailsShouldNotBeFound("personName.in=" + UPDATED_PERSON_NAME);
    }

    @Test
    @Transactional
    public void getAllPersonDetailsByPersonNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        // Get all the personDetailsList where personName is not null
        defaultPersonDetailsShouldBeFound("personName.specified=true");

        // Get all the personDetailsList where personName is null
        defaultPersonDetailsShouldNotBeFound("personName.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersonDetailsByPersonIdIsEqualToSomething() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        // Get all the personDetailsList where personId equals to DEFAULT_PERSON_ID
        defaultPersonDetailsShouldBeFound("personId.equals=" + DEFAULT_PERSON_ID);

        // Get all the personDetailsList where personId equals to UPDATED_PERSON_ID
        defaultPersonDetailsShouldNotBeFound("personId.equals=" + UPDATED_PERSON_ID);
    }

    @Test
    @Transactional
    public void getAllPersonDetailsByPersonIdIsInShouldWork() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        // Get all the personDetailsList where personId in DEFAULT_PERSON_ID or UPDATED_PERSON_ID
        defaultPersonDetailsShouldBeFound("personId.in=" + DEFAULT_PERSON_ID + "," + UPDATED_PERSON_ID);

        // Get all the personDetailsList where personId equals to UPDATED_PERSON_ID
        defaultPersonDetailsShouldNotBeFound("personId.in=" + UPDATED_PERSON_ID);
    }

    @Test
    @Transactional
    public void getAllPersonDetailsByPersonIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        // Get all the personDetailsList where personId is not null
        defaultPersonDetailsShouldBeFound("personId.specified=true");

        // Get all the personDetailsList where personId is null
        defaultPersonDetailsShouldNotBeFound("personId.specified=false");
    }

    @Test
    @Transactional
    public void getAllPersonDetailsByPersonIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        // Get all the personDetailsList where personId is greater than or equal to DEFAULT_PERSON_ID
        defaultPersonDetailsShouldBeFound("personId.greaterThanOrEqual=" + DEFAULT_PERSON_ID);

        // Get all the personDetailsList where personId is greater than or equal to UPDATED_PERSON_ID
        defaultPersonDetailsShouldNotBeFound("personId.greaterThanOrEqual=" + UPDATED_PERSON_ID);
    }

    @Test
    @Transactional
    public void getAllPersonDetailsByPersonIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        // Get all the personDetailsList where personId is less than or equal to DEFAULT_PERSON_ID
        defaultPersonDetailsShouldBeFound("personId.lessThanOrEqual=" + DEFAULT_PERSON_ID);

        // Get all the personDetailsList where personId is less than or equal to SMALLER_PERSON_ID
        defaultPersonDetailsShouldNotBeFound("personId.lessThanOrEqual=" + SMALLER_PERSON_ID);
    }

    @Test
    @Transactional
    public void getAllPersonDetailsByPersonIdIsLessThanSomething() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        // Get all the personDetailsList where personId is less than DEFAULT_PERSON_ID
        defaultPersonDetailsShouldNotBeFound("personId.lessThan=" + DEFAULT_PERSON_ID);

        // Get all the personDetailsList where personId is less than UPDATED_PERSON_ID
        defaultPersonDetailsShouldBeFound("personId.lessThan=" + UPDATED_PERSON_ID);
    }

    @Test
    @Transactional
    public void getAllPersonDetailsByPersonIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        // Get all the personDetailsList where personId is greater than DEFAULT_PERSON_ID
        defaultPersonDetailsShouldNotBeFound("personId.greaterThan=" + DEFAULT_PERSON_ID);

        // Get all the personDetailsList where personId is greater than SMALLER_PERSON_ID
        defaultPersonDetailsShouldBeFound("personId.greaterThan=" + SMALLER_PERSON_ID);
    }


    @Test
    @Transactional
    public void getAllPersonDetailsByPersonAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        // Get all the personDetailsList where personAddress equals to DEFAULT_PERSON_ADDRESS
        defaultPersonDetailsShouldBeFound("personAddress.equals=" + DEFAULT_PERSON_ADDRESS);

        // Get all the personDetailsList where personAddress equals to UPDATED_PERSON_ADDRESS
        defaultPersonDetailsShouldNotBeFound("personAddress.equals=" + UPDATED_PERSON_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPersonDetailsByPersonAddressIsInShouldWork() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        // Get all the personDetailsList where personAddress in DEFAULT_PERSON_ADDRESS or UPDATED_PERSON_ADDRESS
        defaultPersonDetailsShouldBeFound("personAddress.in=" + DEFAULT_PERSON_ADDRESS + "," + UPDATED_PERSON_ADDRESS);

        // Get all the personDetailsList where personAddress equals to UPDATED_PERSON_ADDRESS
        defaultPersonDetailsShouldNotBeFound("personAddress.in=" + UPDATED_PERSON_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPersonDetailsByPersonAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        // Get all the personDetailsList where personAddress is not null
        defaultPersonDetailsShouldBeFound("personAddress.specified=true");

        // Get all the personDetailsList where personAddress is null
        defaultPersonDetailsShouldNotBeFound("personAddress.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonDetailsShouldBeFound(String filter) throws Exception {
        restPersonDetailsMockMvc.perform(get("/api/person-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].personName").value(hasItem(DEFAULT_PERSON_NAME)))
            .andExpect(jsonPath("$.[*].personId").value(hasItem(DEFAULT_PERSON_ID)))
            .andExpect(jsonPath("$.[*].personAddress").value(hasItem(DEFAULT_PERSON_ADDRESS)));

        // Check, that the count call also returns 1
        restPersonDetailsMockMvc.perform(get("/api/person-details/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonDetailsShouldNotBeFound(String filter) throws Exception {
        restPersonDetailsMockMvc.perform(get("/api/person-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonDetailsMockMvc.perform(get("/api/person-details/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPersonDetails() throws Exception {
        // Get the personDetails
        restPersonDetailsMockMvc.perform(get("/api/person-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersonDetails() throws Exception {
        // Initialize the database
        personDetailsService.save(personDetails);

        int databaseSizeBeforeUpdate = personDetailsRepository.findAll().size();

        // Update the personDetails
        PersonDetails updatedPersonDetails = personDetailsRepository.findById(personDetails.getId()).get();
        // Disconnect from session so that the updates on updatedPersonDetails are not directly saved in db
        em.detach(updatedPersonDetails);
        updatedPersonDetails
            .personName(UPDATED_PERSON_NAME)
            .personId(UPDATED_PERSON_ID)
            .personAddress(UPDATED_PERSON_ADDRESS);

        restPersonDetailsMockMvc.perform(put("/api/person-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPersonDetails)))
            .andExpect(status().isOk());

        // Validate the PersonDetails in the database
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeUpdate);
        PersonDetails testPersonDetails = personDetailsList.get(personDetailsList.size() - 1);
        assertThat(testPersonDetails.getPersonName()).isEqualTo(UPDATED_PERSON_NAME);
        assertThat(testPersonDetails.getPersonId()).isEqualTo(UPDATED_PERSON_ID);
        assertThat(testPersonDetails.getPersonAddress()).isEqualTo(UPDATED_PERSON_ADDRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingPersonDetails() throws Exception {
        int databaseSizeBeforeUpdate = personDetailsRepository.findAll().size();

        // Create the PersonDetails

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonDetailsMockMvc.perform(put("/api/person-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personDetails)))
            .andExpect(status().isBadRequest());

        // Validate the PersonDetails in the database
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePersonDetails() throws Exception {
        // Initialize the database
        personDetailsService.save(personDetails);

        int databaseSizeBeforeDelete = personDetailsRepository.findAll().size();

        // Delete the personDetails
        restPersonDetailsMockMvc.perform(delete("/api/person-details/{id}", personDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonDetails.class);
        PersonDetails personDetails1 = new PersonDetails();
        personDetails1.setId(1L);
        PersonDetails personDetails2 = new PersonDetails();
        personDetails2.setId(personDetails1.getId());
        assertThat(personDetails1).isEqualTo(personDetails2);
        personDetails2.setId(2L);
        assertThat(personDetails1).isNotEqualTo(personDetails2);
        personDetails1.setId(null);
        assertThat(personDetails1).isNotEqualTo(personDetails2);
    }
}
