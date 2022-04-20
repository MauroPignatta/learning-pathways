package com.mp.admin.domain;

import org.apache.commons.lang3.Validate;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class CourseRepository {

  private EntityManager entityManager;

  public CourseRepository(EntityManager theEntityManager) {
    Validate.notNull(theEntityManager, "The entity manager cannot be null.");

    entityManager = theEntityManager;
  }

  public Course save(final Course course) {
    entityManager.persist(course);
    return course;
  }

  public Course findById(Long id) {
      return entityManager.find(Course.class, id);
  }

  public List<Course> findAll() {
    String stringQuery = "FROM Course c";
    TypedQuery<Course> query = entityManager.createQuery(stringQuery, Course.class);

    return query.getResultList();
  }

  public List<String> findCourseTitles() {
    String stringQuery = "SELECT c.title FROM Course c";
    TypedQuery<String> query = entityManager.createQuery(stringQuery, String.class);

    return query.getResultList();
  }
}
