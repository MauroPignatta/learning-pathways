package com.mp.learning;

import application.lpathway.swagger.LearnerApiController;
import application.lpathway.swagger.LearnerApiDelegate;
import com.mp.learning.application.LearnerDelegate;
import com.mp.learning.domain.LearnerRepository;
import com.mp.learning.domain.UserRepository;
import com.mp.learning.infrastructure.LearningAdminClient;
import com.mp.security.Security;
import com.mp.security.infrastructure.SecurityConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Configuration
@Import({Security.class, SecurityConfiguration.class})
public class Learning {

  //Delegates
  @Bean public LearnerApiDelegate learnerApiDelegate(LearningAdminClient adminClient,
                                                     LearnerRepository learnerRepository) {
    return new LearnerDelegate(adminClient, learnerRepository);
  }

  //Controllers
  @Bean public LearnerApiController learnerApiController(LearnerApiDelegate learnerApiDelegate) {
    return new LearnerApiController(learnerApiDelegate);
  }

  //Repository
  @Bean public LearnerRepository learnerRepository(EntityManager entityManager) {
    return new LearnerRepository(entityManager);
  }

  @Bean public UserRepository userRepository(EntityManager entityManager) {
    return new UserRepository(entityManager);
  }

  //Clients
  @Bean public LearningAdminClient learningAdminRestClient(
      @Value("${learning.admin.baseUrl}") String baseUrl,
      @Value("${learning.admin.port}") Integer port,
      RestTemplate restTemplate) {
    return new LearningAdminClient(baseUrl, port, restTemplate);
  }

  @Bean public EntityManager entityManager(EntityManagerFactory factory) {
    return SharedEntityManagerCreator.createSharedEntityManager(factory);
  }

  @Bean public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
