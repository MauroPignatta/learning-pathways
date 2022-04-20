package com.mp.learning.infrastructure;

import java.util.Arrays;
import java.util.List;

import application.lpathway.swagger.CourseTitleIdDto;
import application.lpathway.swagger.TestDto;
import application.lpathway.swagger.TestResultDto;
import org.apache.commons.lang3.Validate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import application.lpathway.swagger.CourseDto;

public class LearningAdminClient extends RestClient {

  private RestTemplate rest;

  public LearningAdminClient(String theBaseUrl, Integer thePort, RestTemplate theRestTemplate) {
    super(theBaseUrl, thePort);
    rest = theRestTemplate;
  }

  public List<CourseTitleIdDto> getCourseTitles() {
    ResponseEntity<CourseTitleIdDto[]> response =
        rest.getForEntity(getUrl() + "/course/titles", CourseTitleIdDto[].class);

    return Arrays.asList(response.getBody());
  }

  public CourseDto getCourse(Long id) {
    Validate.notNull(id, "The id cannot be null.");

    ResponseEntity<CourseDto> response = rest.getForEntity(getUrl() + "/course/" + id, CourseDto.class);

    return response.getBody();
  }

  public List<TestDto> getTestsByCourse(Long courseId) {
    Validate.notNull(courseId, "The courseId cannot be null.");

    ResponseEntity<TestDto[]> response = rest.getForEntity(getUrl() + "/test/course/" + courseId, TestDto[].class);

    return Arrays.asList(response.getBody());
  }

  public TestDto getTest(Long id) {
    Validate.notNull(id, "The id cannot be null.");

    ResponseEntity<TestDto> response = rest.getForEntity(getUrl() + "/test/" + id, TestDto.class);

    return response.getBody();
  }

  public TestResultDto markTest(TestDto testDto) {
    Validate.notNull(testDto, "The test dto cannot be null.");

    ResponseEntity<TestResultDto> response =
        rest.postForEntity(getUrl() + "/test/results", testDto, TestResultDto.class);

    return response.getBody();
  }
}
