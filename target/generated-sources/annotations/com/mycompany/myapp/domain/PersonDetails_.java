package com.mycompany.myapp.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PersonDetails.class)
public abstract class PersonDetails_ {

	public static volatile SingularAttribute<PersonDetails, String> personName;
	public static volatile SingularAttribute<PersonDetails, String> personAddress;
	public static volatile SingularAttribute<PersonDetails, Integer> personId;
	public static volatile SingularAttribute<PersonDetails, Long> id;

	public static final String PERSON_NAME = "personName";
	public static final String PERSON_ADDRESS = "personAddress";
	public static final String PERSON_ID = "personId";
	public static final String ID = "id";

}

