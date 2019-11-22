package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Course} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CourseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /courses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CourseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter courseName;

    private IntegerFilter courseFee;

    private IntegerFilter courseDuration;

    public CourseCriteria(){
    }

    public CourseCriteria(CourseCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.courseName = other.courseName == null ? null : other.courseName.copy();
        this.courseFee = other.courseFee == null ? null : other.courseFee.copy();
        this.courseDuration = other.courseDuration == null ? null : other.courseDuration.copy();
    }

    @Override
    public CourseCriteria copy() {
        return new CourseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCourseName() {
        return courseName;
    }

    public void setCourseName(StringFilter courseName) {
        this.courseName = courseName;
    }

    public IntegerFilter getCourseFee() {
        return courseFee;
    }

    public void setCourseFee(IntegerFilter courseFee) {
        this.courseFee = courseFee;
    }

    public IntegerFilter getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(IntegerFilter courseDuration) {
        this.courseDuration = courseDuration;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CourseCriteria that = (CourseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(courseName, that.courseName) &&
            Objects.equals(courseFee, that.courseFee) &&
            Objects.equals(courseDuration, that.courseDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        courseName,
        courseFee,
        courseDuration
        );
    }

    @Override
    public String toString() {
        return "CourseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (courseName != null ? "courseName=" + courseName + ", " : "") +
                (courseFee != null ? "courseFee=" + courseFee + ", " : "") +
                (courseDuration != null ? "courseDuration=" + courseDuration + ", " : "") +
            "}";
    }

}
