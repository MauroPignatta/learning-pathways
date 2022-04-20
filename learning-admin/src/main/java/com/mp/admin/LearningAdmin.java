package com.mp.admin;

import application.lpathway.swagger.*;
import com.mp.admin.application.CourseCommandFactory;
import com.mp.admin.application.CourseDelegate;
import com.mp.admin.application.TestCommandFactory;
import com.mp.admin.application.TestDelegate;
import com.mp.admin.domain.*;
import com.mp.admin.infrastructure.CustomFactoryModule;
import com.mp.admin.infrastructure.FactoryForType;
import com.mp.security.Security;
import com.mp.security.infrastructure.SecurityConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.SharedEntityManagerCreator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Configuration
@Import({Security.class, SecurityConfiguration.class})
public class LearningAdmin {

  // Api Controllers

  @Bean public CourseApi courseApi(CourseApiDelegate courseDelegate) {
    return new CourseApiController(courseDelegate);
  }

  @Bean public TestApi testApi(TestApiDelegate testDelegate) {
    return new TestApiController(testDelegate);
  }

  // Delegates

  @Bean public CourseApiDelegate courseDelegate(CourseRepository courseRepository) {
    return new CourseDelegate(courseRepository);
  }

  @Bean public TestApiDelegate testDelegate(TestRepository testRepository,
                                            CourseRepository courseRepository,
                                            CourseNodeRepository courseNodeRepository) {
    return new TestDelegate(testRepository, courseRepository, courseNodeRepository);
  }

  // Repositories

  @Bean public CourseRepository courseRepository(EntityManager entityManager) {
    return new CourseRepository(entityManager);
  }

  @Bean public CourseNodeRepository courseNodeRepository(EntityManager entityManager) {
    return new CourseNodeRepository(entityManager);
  }

  @Bean public TestRepository testRepository(EntityManager entityManager) {
    return new TestRepository(entityManager);
  }

  @Bean public UserRepository userRepository(EntityManager entityManager) {
    return new UserRepository(entityManager);
  }

  // Factories

  @Bean public CourseCommandFactory courseCommandFactory(CourseRepository courseRepository) {
    return new CourseCommandFactory(courseRepository);
  }

  @Bean public TestCommandFactory testCommandFactory(TestRepository testRepository) {
    return new TestCommandFactory(testRepository);
  }

  @Bean public EntityManager entityManager(EntityManagerFactory factory) {
    return SharedEntityManagerCreator.createSharedEntityManager(factory);
  }

  @Bean CustomFactoryModule jacksonFactoryModule(List<FactoryForType<?>> factories) {
    CustomFactoryModule factoryModule = new CustomFactoryModule();
    for (FactoryForType<?> factory : factories) {
      factoryModule.registerFactory(factory);
    }
    return factoryModule;
  }
}
