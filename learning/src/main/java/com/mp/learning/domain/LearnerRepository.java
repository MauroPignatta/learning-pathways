package com.mp.learning.domain;

import org.apache.commons.lang3.Validate;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class LearnerRepository {

  private final EntityManager entityManager;

  public LearnerRepository(EntityManager theEntityManager) {
    Validate.notNull(theEntityManager, "The entity manager cannot be null.");

    entityManager = theEntityManager;
  }

  public Learner findByUsername(String username) {
    String query = "From User u where u.userAttributes.username = :username";
    TypedQuery<User> typedQuery = entityManager.createQuery(query, User.class);

    typedQuery.setParameter("username", username);
    User user = typedQuery.getSingleResult();

    return user.getLearner();
  }

  public Learner findById(Long learnerId) {
    return entityManager.find(Learner.class, learnerId);
  }
}
