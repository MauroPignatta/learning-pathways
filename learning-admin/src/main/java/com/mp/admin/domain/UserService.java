package com.mp.admin.domain;

import com.mp.admin.infrastructure.Input;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

  private UserRepository userRepository;

  public UserService(UserRepository theUserRepository) {
    userRepository = theUserRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    Input.found(user, "No user found with the given username.");

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(user.getUserAttributes().getRole().toString()));
    return new org.springframework.security.core.userdetails.User(user.getUserAttributes().getUsername(),
        user.getUserAttributes().getPassword(), authorities);
  }

  @Transactional
  public void save(User user) {
    userRepository.save(user);
  }
}
