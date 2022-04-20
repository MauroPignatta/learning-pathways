package com.mp.admin.domain;

import javax.persistence.EntityManager;

public class CourseNodeRepository {

  private EntityManager entityManager;

  public CourseNodeRepository(EntityManager theEntityManager) {
    entityManager = theEntityManager;
  }

  public CourseNode findById(Long id) {
    return entityManager.find(CourseNode.class, id);
  }
}
