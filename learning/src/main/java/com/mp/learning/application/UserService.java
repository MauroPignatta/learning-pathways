package com.mp.learning.application;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mp.admin.infrastructure.Input;
import com.mp.learning.domain.User;
import com.mp.learning.domain.UserRepository;

/** An implementation of a UserDetailsService in charge of persisting and retrieving users. */
@Service
public class UserService implements UserDetailsService {

  /** The user repository to persist and retrieve Users. Never null. */
  private UserRepository userRepository;

  /** Constructor.
   *
   * @param theUserRepository The user repository. Cannot be null.
   */
  public UserService(UserRepository theUserRepository) {
    Validate.notNull(theUserRepository, "The user repository cannot be null.");

    userRepository = theUserRepository;
  }

  /** Retrieves a user given its username.
   *
   * @param username The username of the user to find. Cannot be blank.
   *
   * @return the user. Can be null if no user found with the given username.
   */
  public User findByUsername(String username) {
    Validate.notBlank(username, "The username cannot be blank.");

    return userRepository.findByUsername(username);
  }

  /** Persists a user instance.
   *
   * @param user The user to persist. Cannot be null.
   */
  public void save(User user) {
    Validate.notNull(user, "The user cannot be null.");

    userRepository.save(user);
  }

  /** {@inheritDoc} */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    Input.found(user, "No user found with the given username.");

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(user.getUserAttributes().getRole().toString()));
    return new org.springframework.security.core.userdetails.User(user.getUserAttributes().getUsername(),
        user.getUserAttributes().getPassword(), authorities);
  }
}
