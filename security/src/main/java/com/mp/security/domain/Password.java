package com.mp.security.domain;

import org.apache.commons.lang3.Validate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Embeddable;

@Embeddable
public class Password {

  /** The regular expression that the password must match in order to be a valid password. */
  private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]+$";

  /** The password minimum length. */
  private static final int MIN_LENGTH = 8;

  /** The password maximus length. */
  private static final int MAX_LENGTH = 64;

  private String password;

  /** ORM's Default constructor.*/
  Password() {
  }

  public Password(String thePassword, PasswordEncoder encoder) {
    isValid(thePassword);

    password = encoder.encode(thePassword);
  }

  public static void isValid(String thePassword) {
    Validate.notNull(thePassword, "The password cannot be null.");
    Validate.inclusiveBetween(MIN_LENGTH, MAX_LENGTH, thePassword.length(),
        "The password must contain between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters.");
    Validate.matchesPattern(thePassword, PASSWORD_REGEX, "The password must contain 1 uppercase, 1 lowercase and 1 number.");
  }

  public String getValue() {
    return password;
  }
}
