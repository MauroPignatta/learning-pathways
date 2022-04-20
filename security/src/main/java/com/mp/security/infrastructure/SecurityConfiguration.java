package com.mp.security.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private PasswordEncoder passwordEncoder;

  private UserDetailsService userDetailsService;

  private JwtRequestFilter requestFilter;

  private JwtTokenUtils jwtTokenUtils;

  public SecurityConfiguration(PasswordEncoder thePasswordEncoder, JwtRequestFilter theJwtRequestFilter,
                               UserDetailsService theUserDetailsService, JwtTokenUtils theJwtTokenUtil) {
    passwordEncoder = thePasswordEncoder;
    requestFilter = theJwtRequestFilter;
    userDetailsService = theUserDetailsService;
    jwtTokenUtils = theJwtTokenUtil;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/user/**").permitAll()
        .antMatchers("/swagger").permitAll()
        .antMatchers("/swagger/**").permitAll()
        .antMatchers("/swagger-ui/**").permitAll()
        .antMatchers("/v3/api-docs/**").permitAll()
        .antMatchers("/api.yaml").permitAll()
        .antMatchers("/h2-console/**").permitAll()

        .antMatchers(HttpMethod.POST, "/course/**").authenticated()
        .antMatchers(HttpMethod.DELETE, "/course/**").authenticated()
        .antMatchers(HttpMethod.PUT, "/course/**").authenticated()
        .antMatchers(HttpMethod.GET, "/course/**").permitAll()

        .antMatchers(HttpMethod.POST, "/test/results").permitAll()
        .antMatchers(HttpMethod.GET, "/test/**").permitAll()

        .antMatchers(HttpMethod.POST, "/test/**").authenticated()
        .antMatchers(HttpMethod.DELETE, "/test/**").authenticated()
        .antMatchers(HttpMethod.PUT, "/test/**").authenticated()

        .antMatchers("/learner/**").authenticated();

    http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
    http.headers().frameOptions().disable();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
