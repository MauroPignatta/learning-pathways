package com.mp.learning.application;

import javax.transaction.Transactional;

import org.apache.commons.lang3.Validate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mp.admin.infrastructure.Input;
import com.mp.learning.domain.User;
import com.mp.security.application.UserDto;
import com.mp.security.domain.Role;
import com.mp.security.domain.UserAttributes;
import com.mp.security.infrastructure.JwtTokenUtils;

@RestController
public class UserController {

  /** The authentication manager to authenticate the users. Never null. */
  private final AuthenticationManager authenticationManager;

  /** The jwt token utils to create and validate jwt tokens. Never null. */
  private final JwtTokenUtils jwtTokenUtils;

  /** The password encoder used to encode the passwords. Never null. */
  private final PasswordEncoder passwordEncoder;

  /** The user service. Never null. */
  private final UserService userService;

  /** Constructor.
   *
   * @param theUserService The user service. Cannot be null.
   *
   * @param thePasswordEncoder The password encoder. Cannot be null.
   *
   * @param theJwtTokenUtils The jwt token utils. Cannot be null.
   *
   * @param theAuthenticationManager The authentication manager. Cannot be null.
   */
  public UserController(UserService theUserService, PasswordEncoder thePasswordEncoder,
                        JwtTokenUtils theJwtTokenUtils, AuthenticationManager theAuthenticationManager) {
    Validate.notNull(theUserService, "The user service cannot be null.");
    Validate.notNull(thePasswordEncoder, "The password encoder cannot be null.");
    Validate.notNull(theJwtTokenUtils);
    Validate.notNull(theAuthenticationManager);

    passwordEncoder = thePasswordEncoder;
    authenticationManager = theAuthenticationManager;
    jwtTokenUtils = theJwtTokenUtils;
    userService = theUserService;
  }

  /** Creates a new User.
   *
   * @param userDto The userDto with the required data to create a new User. Cannot be null.
   *
   * @return A Response entity with status 201 when the user is created successfully.
   */
  @Transactional
  @PostMapping("/user/create")
  public ResponseEntity<Void> create(@RequestBody UserDto userDto) {
    UserAttributes attributes =
      new UserAttributes(userDto.getUsername(), userDto.getPassword(), Role.USER, passwordEncoder);
    User user = new User(attributes);

    userService.save(user);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/user/login")
  public ResponseEntity<Void> login(@RequestBody UserDto userDto) {
    User user = userService.findByUsername(userDto.getUsername());
    Input.found(user, "No user found with the given username.");

    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));

    UserDetails userDetails = userService
      .loadUserByUsername(userDto.getUsername());

    final String token = jwtTokenUtils.generateToken(userDetails);

    return ResponseEntity.ok().header("Authorization", token).build();
  }
}
