package com.mp.security;

import com.mp.security.infrastructure.JwtRequestFilter;
import com.mp.security.infrastructure.JwtTokenUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Security {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean public JwtTokenUtils jwtTokenUtils() {
    return new JwtTokenUtils();
  }

  @Bean public JwtRequestFilter jwtRequestFilter(UserDetailsService detailsService, JwtTokenUtils jwtTokenUtils) {
    return new JwtRequestFilter(detailsService, jwtTokenUtils);
  }
}
