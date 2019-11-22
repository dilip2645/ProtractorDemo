package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ProtractorTestingApp;
import com.mycompany.myapp.domain.Course;
import com.mycompany.myapp.repository.CourseRepository;
import com.mycompany.myapp.service.CourseService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.CourseCriteria;
import com.mycompany.myapp.service.CourseQueryService;

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
 * Integration tests for the {@link CourseResource} REST controller.
 */
@SpringBootTest(classes = ProtractorTestingApp.class)
public class CourseResourceIT {

    private static final String DEFAULT_COURSE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_COURSE_FEE = 1;
    private static final Integer UPDATED_COURSE_FEE = 2;
    private static final Integer SMALLER_COURSE_FEE = 1 - 1;

    private static final Integer DEFAULT_COURSE_DURATION = 1;
    private static final Integer UPDATED_COURSE_DURATION = 2;
    private static final Integer SMALLER_COURSE_DURATION = 1 - 1;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseQueryService courseQueryService;

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

    private MockMvc restCourseMockMvc;

    private Course course;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourseResource courseResource = new CourseResource(courseService, courseQueryService,null);
        this.restCourseMockMvc = MockMvcBuilders.standaloneSetup(courseResource)
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
    public static Course createEntity(EntityManager em) {
        Course course = new Course()
            .courseName(DEFAULT_COURSE_NAME)
            .courseFee(DEFAULT_COURSE_FEE)
            .courseDuration(DEFAULT_COURSE_DURATION);
        return course;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createUpdatedEntity(EntityManager em) {
        Course course = new Course()
            .courseName(UPDATED_COURSE_NAME)
            .courseFee(UPDATED_COURSE_FEE)
            .courseDuration(UPDATED_COURSE_DURATION);
        return course;
    }

    @BeforeEach
    public void initTest() {
        course = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getCourseName()).isEqualTo(DEFAULT_COURSE_NAME);
        assertThat(testCourse.getCourseFee()).isEqualTo(DEFAULT_COURSE_FEE);
        assertThat(testCourse.getCourseDuration()).isEqualTo(DEFAULT_COURSE_DURATION);
    }

    @Test
    @Transactional
    public void createCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course with an existing ID
        course.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].courseName").value(hasItem(DEFAULT_COURSE_NAME.toString())))
            .andExpect(jsonPath("$.[*].courseFee").value(hasItem(DEFAULT_COURSE_FEE)))
            .andExpect(jsonPath("$.[*].courseDuration").value(hasItem(DEFAULT_COURSE_DURATION)));
    }
    
    @Test
    @Transactional
    public void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", course.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(course.getId().intValue()))
            .andExpect(jsonPath("$.courseName").value(DEFAULT_COURSE_NAME.toString()))
            .andExpect(jsonPath("$.courseFee").value(DEFAULT_COURSE_FEE))
            .andExpect(jsonPath("$.courseDuration").value(DEFAULT_COURSE_DURATION));
    }

    @Test
    @Transactional
    public void getAllCoursesByCourseNameIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where courseName equals to DEFAULT_COURSE_NAME
        defaultCourseShouldBeFound("courseName.equals=" + DEFAULT_COURSE_NAME);

        // Get all the courseList where courseName equals to UPDATED_COURSE_NAME
        defaultCourseShouldNotBeFound("courseName.equals=" + UPDATED_COURSE_NAME);
    }

    @Test
    @Transactional
    public void getAllCoursesByCourseNameIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where courseName in DEFAULT_COURSE_NAME or UPDATED_COURSE_NAME
        defaultCourseShouldBeFound("courseName.in=" + DEFAULT_COURSE_NAME + "," + UPDATED_COURSE_NAME);

        // Get all the courseList where courseName equals to UPDATED_COURSE_NAME
        defaultCourseShouldNotBeFound("courseName.in=" + UPDATED_COURSE_NAME);
    }

    @Test
    @Transactional
    public void getAllCoursesByCourseNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where courseName is not null
        defaultCourseShouldBeFound("courseName.specified=true");

        // Get all the courseList where courseName is null
        defaultCourseShouldNotBeFound("courseName.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByCourseFeeIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where courseFee equals to DEFAULT_COURSE_FEE
        defaultCourseShouldBeFound("courseFee.equals=" + DEFAULT_COURSE_FEE);

        // Get all the courseList where courseFee equals to UPDATED_COURSE_FEE
        defaultCourseShouldNotBeFound("courseFee.equals=" + UPDATED_COURSE_FEE);
    }

    @Test
    @Transactional
    public void getAllCoursesByCourseFeeIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where courseFee in DEFAULT_COURSE_FEE or UPDATED_COURSE_FEE
        defaultCourseShouldBeFound("courseFee.in=" + DEFAULT_COURSE_FEE + "," + UPDATED_COURSE_FEE);

        // Get all the courseList where courseFee equals to UPDATED_COURSE_FEE
        defaultCourseShouldNotBeFound("courseFee.in=" + UPDATED_COURSE_FEE);
    }

    @Test
    @Transactional
    public void getAllCoursesByCourseFeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where courseFee is not null
        defaultCourseShouldBeFound("courseFee.specified=true");

        // Get all the courseList where courseFee is null
        defaultCourseShouldNotBeFound("courseFee.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByCourseFeeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where courseFee is greater than or equal to DEFAULT_COURSE_FEE
        defaultCourseShouldBeFound("courseFee.greaterThanOrEqual=" + DEFAULT_COURSE_FEE);

        // Get all the courseList where courseFee is greater than or equal to UPDATED_COURSE_FEE
        defaultCourseShouldNotBeFound("courseFee.greaterThanOrEqual=" + UPDATED_COURSE_FEE);
    }

    @Test
    @Transactional
    public void getAllCoursesByCourseFeeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where courseFee is less than or equal to DEFAULT_COURSE_FEE
        defaultCourseShouldBeFound("courseFee.lessThanOrEqual=" + DEFAULT_COURSE_FEE);

        // Get all the courseList where courseFee is less than or equal to SMALLER_COURSE_FEE
        defaultCourseShouldNotBeFound("courseFee.lessThanOrEqual=" + SMALLER_COURSE_FEE);
    }

    @Test
    @Transactional
    public void getAllCoursesByCourseFeeIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where courseFee is less than DEFAULT_COURSE_FEE
        defaultCourseShouldNotBeFound("courseFee.lessThan=" + DEFAULT_COURSE_FEE);

        // Get all the courseList where courseFee is less than UPDATED_COURSE_FEE
        defaultCourseShouldBeFound("courseFee.lessThan=" + UPDATED_COURSE_FEE);
    }

    @Test
    @Transactional
    public void getAllCoursesByCourseFeeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where courseFee is greater than DEFAULT_COURSE_FEE
        defaultCourseShouldNotBeFound("courseFee.greaterThan=" + DEFAULT_COURSE_FEE);

        // Get all the courseList where courseFee is greater than SMALLER_COURSE_FEE
        defaultCourseShouldBeFound("courseFee.greaterThan=" + SMALLER_COURSE_FEE);
    }


    @Test
    @Transactional
    public void getAllCoursesByCourseDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where courseDuration equals to DEFAULT_COURSE_DURATION
        defaultCourseShouldBeFound("courseDuration.equals=" + DEFAULT_COURSE_DURATION);

        // Get all the courseList where courseDuration equals to UPDATED_COURSE_DURATION
        defaultCourseShouldNotBeFound("courseDuration.equals=" + UPDATED_COURSE_DURATION);
    }

    @Test
    @Transactional
    public void getAllCoursesByCourseDurationIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where courseDuration in DEFAULT_COURSE_DURATION or UPDATED_COURSE_DURATION
        defaultCourseShouldBeFound("courseDuration.in=" + DEFAULT_COURSE_DURATION + "," + UPDATED_COURSE_DURATION);

        // Get all the courseList where courseDuration equals to UPDATED_COURSE_DURATION
        defaultCourseShouldNotBeFound("courseDuration.in=" + UPDATED_COURSE_DURATION);
    }

    @Test
    @Transactional
    public void getAllCoursesByCourseDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where courseDuration is not null
        defaultCourseShouldBeFound("courseDuration.specified=true");

        // Get all the courseList where courseDuration is null
        defaultCourseShouldNotBeFound("courseDuration.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByCourseDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where courseDuration is greater than or equal to DEFAULT_COURSE_DURATION
        defaultCourseShouldBeFound("courseDuration.greaterThanOrEqual=" + DEFAULT_COURSE_DURATION);

        // Get all the courseList where courseDuration is greater than or equal to UPDATED_COURSE_DURATION
        defaultCourseShouldNotBeFound("courseDuration.greaterThanOrEqual=" + UPDATED_COURSE_DURATION);
    }

    @Test
    @Transactional
    public void getAllCoursesByCourseDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where courseDuration is less than or equal to DEFAULT_COURSE_DURATION
        defaultCourseShouldBeFound("courseDuration.lessThanOrEqual=" + DEFAULT_COURSE_DURATION);

        // Get all the courseList where courseDuration is less than or equal to SMALLER_COURSE_DURATION
        defaultCourseShouldNotBeFound("courseDuration.lessThanOrEqual=" + SMALLER_COURSE_DURATION);
    }

    @Test
    @Transactional
    public void getAllCoursesByCourseDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where courseDuration is less than DEFAULT_COURSE_DURATION
        defaultCourseShouldNotBeFound("courseDuration.lessThan=" + DEFAULT_COURSE_DURATION);

        // Get all the courseList where courseDuration is less than UPDATED_COURSE_DURATION
        defaultCourseShouldBeFound("courseDuration.lessThan=" + UPDATED_COURSE_DURATION);
    }

    @Test
    @Transactional
    public void getAllCoursesByCourseDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where courseDuration is greater than DEFAULT_COURSE_DURATION
        defaultCourseShouldNotBeFound("courseDuration.greaterThan=" + DEFAULT_COURSE_DURATION);

        // Get all the courseList where courseDuration is greater than SMALLER_COURSE_DURATION
        defaultCourseShouldBeFound("courseDuration.greaterThan=" + SMALLER_COURSE_DURATION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCourseShouldBeFound(String filter) throws Exception {
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].courseName").value(hasItem(DEFAULT_COURSE_NAME)))
            .andExpect(jsonPath("$.[*].courseFee").value(hasItem(DEFAULT_COURSE_FEE)))
            .andExpect(jsonPath("$.[*].courseDuration").value(hasItem(DEFAULT_COURSE_DURATION)));

        // Check, that the count call also returns 1
        restCourseMockMvc.perform(get("/api/courses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCourseShouldNotBeFound(String filter) throws Exception {
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCourseMockMvc.perform(get("/api/courses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCourse() throws Exception {
        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourse() throws Exception {
        // Initialize the database
        courseService.save(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Course updatedCourse = courseRepository.findById(course.getId()).get();
        // Disconnect from session so that the updates on updatedCourse are not directly saved in db
        em.detach(updatedCourse);
        updatedCourse
            .courseName(UPDATED_COURSE_NAME)
            .courseFee(UPDATED_COURSE_FEE)
            .courseDuration(UPDATED_COURSE_DURATION);

        restCourseMockMvc.perform(put("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourse)))
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getCourseName()).isEqualTo(UPDATED_COURSE_NAME);
        assertThat(testCourse.getCourseFee()).isEqualTo(UPDATED_COURSE_FEE);
        assertThat(testCourse.getCourseDuration()).isEqualTo(UPDATED_COURSE_DURATION);
    }

    @Test
    @Transactional
    public void updateNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Create the Course

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc.perform(put("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourse() throws Exception {
        // Initialize the database
        courseService.save(course);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        // Delete the course
        restCourseMockMvc.perform(delete("/api/courses/{id}", course.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Course.class);
        Course course1 = new Course();
        course1.setId(1L);
        Course course2 = new Course();
        course2.setId(course1.getId());
        assertThat(course1).isEqualTo(course2);
        course2.setId(2L);
        assertThat(course1).isNotEqualTo(course2);
        course1.setId(null);
        assertThat(course1).isNotEqualTo(course2);
    }
}
