package com.mp.learning.domain;

import java.util.ArrayList;

import com.mp.security.domain.UserAttributes;
import org.apache.commons.lang3.Validate;

import javax.persistence.*;

@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private UserAttributes userAttributes;

  @OneToOne(cascade = CascadeType.ALL)
  private Learner learner;

  /** ORM's Default constructor.*/
  User() {
  }

  public User(UserAttributes theUserAttributes) {
    Validate.notNull(theUserAttributes, "The user attributes cannot be null.");

    userAttributes = theUserAttributes;
    learner = new Learner(new ArrayList<>(), new TestResultGroup(new ArrayList<>()));
  }

  public UserAttributes getUserAttributes() {
    return userAttributes;
  }

  public Learner getLearner() {
    return learner;
  }
}
