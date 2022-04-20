package com.mp.admin.domain;

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

  /** ORM's Default constructor.*/
  User() {
  }

  public User(UserAttributes theUserAttributes) {
    Validate.notNull(theUserAttributes, "The user attributes cannot be null.");

    userAttributes = theUserAttributes;
  }

  public UserAttributes getUserAttributes() {
    return userAttributes;
  }
}
