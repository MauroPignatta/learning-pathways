package com.mp.learning.domain;

import org.apache.commons.lang3.Validate;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class UserRepository {

  private EntityManager entityManager;

  public UserRepository(EntityManager theEntityManager) {
    Validate.notNull(theEntityManager, "The entity manager cannot be null.");

    entityManager = theEntityManager;
  }

  public User save(final User user) {
    entityManager.persist(user);
    return user;
  }

  public User findById(Long id) {
    return entityManager.find(User.class, id);
  }

  public User findByUsername(String username) {
    String stringQuery = "FROM User u WHERE u.userAttributes.username = :username";
    TypedQuery<User> query = entityManager.createQuery(stringQuery, User.class);
    query.setParameter("username", username);

    List<User> resultList = query.getResultList();

    if (resultList.isEmpty()) {
      return null;
    }

    return resultList.get(0);
  }

}
