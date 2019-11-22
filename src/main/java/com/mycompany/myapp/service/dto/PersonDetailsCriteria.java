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
 * Criteria class for the {@link com.mycompany.myapp.domain.PersonDetails} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PersonDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /person-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PersonDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter personName;

    private IntegerFilter personId;

    private StringFilter personAddress;

    public PersonDetailsCriteria(){
    }

    public PersonDetailsCriteria(PersonDetailsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.personName = other.personName == null ? null : other.personName.copy();
        this.personId = other.personId == null ? null : other.personId.copy();
        this.personAddress = other.personAddress == null ? null : other.personAddress.copy();
    }

    @Override
    public PersonDetailsCriteria copy() {
        return new PersonDetailsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPersonName() {
        return personName;
    }

    public void setPersonName(StringFilter personName) {
        this.personName = personName;
    }

    public IntegerFilter getPersonId() {
        return personId;
    }

    public void setPersonId(IntegerFilter personId) {
        this.personId = personId;
    }

    public StringFilter getPersonAddress() {
        return personAddress;
    }

    public void setPersonAddress(StringFilter personAddress) {
        this.personAddress = personAddress;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PersonDetailsCriteria that = (PersonDetailsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(personName, that.personName) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(personAddress, that.personAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        personName,
        personId,
        personAddress
        );
    }

    @Override
    public String toString() {
        return "PersonDetailsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (personName != null ? "personName=" + personName + ", " : "") +
                (personId != null ? "personId=" + personId + ", " : "") +
                (personAddress != null ? "personAddress=" + personAddress + ", " : "") +
            "}";
    }

}
