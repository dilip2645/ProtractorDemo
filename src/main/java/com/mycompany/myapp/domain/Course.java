package com.mycompany.myapp.domain;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "course_fee")
    private Integer courseFee;

    @Column(name = "course_duration")
    private Integer courseDuration;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public Course courseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCourseFee() {
        return courseFee;
    }

    public Course courseFee(Integer courseFee) {
        this.courseFee = courseFee;
        return this;
    }

    public void setCourseFee(Integer courseFee) {
        this.courseFee = courseFee;
    }

    public Integer getCourseDuration() {
        return courseDuration;
    }

    public Course courseDuration(Integer courseDuration) {
        this.courseDuration = courseDuration;
        return this;
    }

    public void setCourseDuration(Integer courseDuration) {
        this.courseDuration = courseDuration;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", courseName='" + getCourseName() + "'" +
            ", courseFee=" + getCourseFee() +
            ", courseDuration=" + getCourseDuration() +
            "}";
    }
}
