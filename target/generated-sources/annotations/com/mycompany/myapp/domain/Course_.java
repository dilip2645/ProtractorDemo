package com.mycompany.myapp.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Course.class)
public abstract class Course_ {

	public static volatile SingularAttribute<Course, String> courseName;
	public static volatile SingularAttribute<Course, Integer> courseFee;
	public static volatile SingularAttribute<Course, Integer> courseDuration;
	public static volatile SingularAttribute<Course, Long> id;

	public static final String COURSE_NAME = "courseName";
	public static final String COURSE_FEE = "courseFee";
	public static final String COURSE_DURATION = "courseDuration";
	public static final String ID = "id";

}

