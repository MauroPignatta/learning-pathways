package com.mp.admin.application;

import com.mp.admin.domain.User;
import com.mp.admin.domain.UserService;
import com.mp.security.application.UserDto;
import com.mp.security.domain.Role;
import com.mp.security.domain.UserAttributes;
import com.mp.security.infrastructure.JwtTokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private AuthenticationManager authenticationManager;

  private JwtTokenUtils jwtTokenUtils;

  private PasswordEncoder passwordEncoder;

  private UserService userService;

  public UserController(UserService theUserService, PasswordEncoder thePasswordEncoder,
                        JwtTokenUtils theJwtTokenUtils, AuthenticationManager theAuthenticationManager) {
    passwordEncoder = thePasswordEncoder;
    authenticationManager = theAuthenticationManager;
    jwtTokenUtils = theJwtTokenUtils;
    userService = theUserService;
  }

  @PostMapping("/user/create")
  public ResponseEntity<Void> create(@RequestBody UserDto userDto) {
    UserAttributes attributes = new UserAttributes(userDto.getUsername(), userDto.getPassword(), Role.ADMIN, passwordEncoder);
    User user = new User(attributes);

    userService.save(user);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/user/login")
  public ResponseEntity<Void> login(@RequestBody UserDto userDto) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));

    UserDetails userDetails = userService
      .loadUserByUsername(userDto.getUsername());

    final String token = jwtTokenUtils.generateToken(userDetails);

    return ResponseEntity.ok().header("Authorization", token).build();
  }
}
