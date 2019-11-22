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

import com.mycompany.myapp.domain.Course;
import com.mycompany.myapp.service.CourseService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.CourseCriteria;
import com.mycompany.myapp.service.CourseQueryService;

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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Course}.
 */
@RestController
@RequestMapping("/api")
public class CourseResource {

    private final Logger log = LoggerFactory.getLogger(CourseResource.class);

    private static final String ENTITY_NAME = "course";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseService courseService;

    private final CourseQueryService courseQueryService;

    private final ReportGenerator reportGenerator;

    public CourseResource(CourseService courseService, CourseQueryService courseQueryService, ReportGenerator reportGenerator) {
        this.courseService = courseService;
        this.courseQueryService = courseQueryService;
        this.reportGenerator = reportGenerator;
    }

    /**
     * {@code POST  /courses} : Create a new course.
     *
     * @param course the course to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new course, or with status {@code 400 (Bad Request)} if the course has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) throws URISyntaxException {
        log.debug("REST request to save Course : {}", course);
        if (course.getId() != null) {
            throw new BadRequestAlertException("A new course cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Course result = courseService.save(course);
        return ResponseEntity.created(new URI("/api/courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /courses} : Updates an existing course.
     *
     * @param course the course to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated course,
     * or with status {@code 400 (Bad Request)} if the course is not valid,
     * or with status {@code 500 (Internal Server Error)} if the course couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/courses")
    public ResponseEntity<Course> updateCourse(@RequestBody Course course) throws URISyntaxException {
        log.debug("REST request to update Course : {}", course);
        if (course.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Course result = courseService.save(course);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, course.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /courses} : get all the courses.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courses in body.
     */
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses(CourseCriteria criteria, Pageable pageable,@RequestParam(value = "exportType", required = false) String exportType) {
        log.debug("REST request to get Courses by criteria: {}", criteria);
        if(exportType==null) {
        PageRequest pageRequest;
        Sort.Order order = new Sort.Order(pageable.getSort().iterator().next().getDirection(),pageable.getSort().iterator().next().getProperty()).ignoreCase();
        pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(order));
        Page<Course> page = courseQueryService.findByCriteria(criteria, pageable);
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

			List<Course> courseList = courseQueryService.findByCriteria(criteria);

			ReportFileType  report = reportGenerator.generateListingReport(
                courseList,
					exportType,
					getCourseReportHeader(),
					"Course",
					"Course",
					"Course Report",
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
    * {@code GET  /courses/count} : count all the courses.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/courses/count")
    public ResponseEntity<Long> countCourses(CourseCriteria criteria) {
        log.debug("REST request to count Courses by criteria: {}", criteria);
        return ResponseEntity.ok().body(courseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /courses/:id} : get the "id" course.
     *
     * @param id the id of the course to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the course, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        log.debug("REST request to get Course : {}", id);
        Optional<Course> course = courseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(course);
    }

    /**
     * {@code DELETE  /courses/:id} : delete the "id" course.
     *
     * @param id the id of the course to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        log.debug("REST request to delete Course : {}", id);
        courseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    public static Map<String, String> getCourseReportHeader() {
        Map<String, String> reportHeaders = new LinkedHashMap<>();
            reportHeaders.put("courseName","CourseName");
            reportHeaders.put("courseFee","CourseFee");
            reportHeaders.put("courseDuration","CourseDuration");
        return reportHeaders;
    }
}
