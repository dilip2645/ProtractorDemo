package com.mycompany.myapp.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A PersonDetails.
 */
@Entity
@Table(name = "person_details")
public class PersonDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "person_name")
    private String personName;

    @NotNull
    @Column(name = "person_id", nullable = false, unique = true)
    private Integer personId;

    @Column(name = "person_address")
    private String personAddress;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public PersonDetails personName(String personName) {
        this.personName = personName;
        return this;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getPersonId() {
        return personId;
    }

    public PersonDetails personId(Integer personId) {
        this.personId = personId;
        return this;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getPersonAddress() {
        return personAddress;
    }

    public PersonDetails personAddress(String personAddress) {
        this.personAddress = personAddress;
        return this;
    }

    public void setPersonAddress(String personAddress) {
        this.personAddress = personAddress;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonDetails)) {
            return false;
        }
        return id != null && id.equals(((PersonDetails) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PersonDetails{" +
            "id=" + getId() +
            ", personName='" + getPersonName() + "'" +
            ", personId=" + getPersonId() +
            ", personAddress='" + getPersonAddress() + "'" +
            "}";
    }
}
