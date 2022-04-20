package com.mp.security.domain;

import org.apache.commons.lang3.Validate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Embeddable
public class UserAttributes {

  @Column(nullable = false, unique = true)
  private String username;

  @Embedded
  @Column(nullable = false)
  private Password password;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  /** ORM's Default constructor.*/
  UserAttributes() {
  }

  public UserAttributes(String theUsername, String thePassword, Role theRole, PasswordEncoder passwordEncoder) {
    Validate.notNull(theRole, "The role cannot be null.");
    Validate.notEmpty(theUsername, "The username cannot be empty.");

    username = theUsername;
    password = new Password(thePassword, passwordEncoder);
    role = theRole;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password.getValue();
  }

  public Role getRole() {
    return role;
  }
}
