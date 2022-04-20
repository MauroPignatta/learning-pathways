package com.mp.admin.domain;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class TestRepository {

  private EntityManager entityManager;

  public TestRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public Test save(Test test) {
    entityManager.persist(test);
    return test;
  }

  public Test findById(Long id) {
    return entityManager.find(Test.class, id);
  }

  public List<Test> findByCourseId(Long courseId) {
    String query = "FROM Test t WHERE t.course.id = :courseId";
    TypedQuery<Test> typedQuery = entityManager.createQuery(query, Test.class);

    typedQuery.setParameter("courseId", courseId);

    return typedQuery.getResultList();
  }
}