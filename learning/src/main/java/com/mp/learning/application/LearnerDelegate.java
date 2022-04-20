package com.mp.learning.application;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.Validate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import application.lpathway.swagger.*;

import com.mp.admin.infrastructure.Input;
import com.mp.learning.domain.Learner;
import com.mp.learning.domain.LearnerRepository;
import com.mp.learning.domain.TestResult;
import com.mp.learning.infrastructure.LearningAdminClient;

/** An implementation of the LearnerApiDelegate. */
public class LearnerDelegate implements LearnerApiDelegate {

  /** The learning admin client to retrieve the data. Never null. */
  private LearningAdminClient adminClient;

  /** The learning repository. Never null. */
  private LearnerRepository learnerRepository;

  /** Constructor.
   *
   * @param learningAdminClient The LearningAdminClient. Cannot be null.
   *
   * @param theLearnerRepository The LearnerRepository. Cannot be null.
   */
  public LearnerDelegate(LearningAdminClient learningAdminClient, LearnerRepository theLearnerRepository) {
    Validate.notNull(learningAdminClient, "The learning admin client cannot be null.");
    Validate.notNull(theLearnerRepository, "The learning repository cannot be null.");

    adminClient = learningAdminClient;
    learnerRepository = theLearnerRepository;
  }

  /** Subscribes the learner from the current session to the given course.
   *
   * @param subscriptionDto The subscription which contains the course to subscribe. Cannot be null.
   *
   * @return A ResponseEntity with status 200 as the learner is successfully subscribed to the course.
   * - 400 if the subscription is not valid.
   * - 404 if either the learner or the course is not found.
   */
  @Override
  @Transactional
  public ResponseEntity<Void> subscribe(LearnerSubscribeBodyDto subscriptionDto) {
    Validate.notNull(subscriptionDto, "The subscriptionDto cannot be null.");

    Learner learner = learnerRepository.findByUsername(getUsername());
    CourseDto courseDto = adminClient.getCourse(subscriptionDto.getCourseId());

    Input.found(learner, "No learner found with the given username.");
    Input.found(courseDto, "No course found with the given id.");
    Input.isTrue(!learner.isSubscribed(courseDto.getId()), "Already subscribed to the given course.");

    learner.subscribe(courseDto.getId());

    return ResponseEntity.ok().build();
  }

  /** Gets a course from the learner from the current session. The learner must be already subscribed to the course
   * in order to retrieve it successfully.
   *
   * @param courseId The id of the course to get. The learner must be already subscribed to the course. Cannot be null.
   *
   * @return A ResponseEntity with status 200 with a body that contains the course.
   * - 400 if the learner is not subscribed to the course.
   * - 404 if either the learner or the course is not found.
   */
  @Override
  public ResponseEntity<CourseDto> getCourse(Long courseId) {
    Learner learner = learnerRepository.findByUsername(getUsername());
    Input.found(learner, "No learner found with the given username.");

    Input.isTrue(learner.isSubscribed(courseId), "The learner is not subscribed to the given course.");

    return ResponseEntity.ok(adminClient.getCourse(courseId));
  }

  /** Gets all the course available.
   *
   * @return A Response entity with status 200 with a body that contain all the courses available for the learner.
   * - 404 if the learner is not found.
   */
  @Override
  public ResponseEntity<List<CourseTitleIdDto>> getAllCoursesNames() {
    Learner learner = learnerRepository.findByUsername(getUsername());
    Input.found(learner, "No learner found with the given username.");

    return ResponseEntity.ok(adminClient.getCourseTitles());
  }

  /** Gets all the course available for the learner to subscribe.
   *
   * @return A Response entity with status 200 with a body that contain all the courses available for the learner.
   * - 404 if the learner is not found.
   */
  @Override
  public ResponseEntity<List<CourseTitleIdDto>> getCoursesNames() {
    Learner learner = learnerRepository.findByUsername(getUsername());
    Input.found(learner, "No learner found with the given username.");

    List<CourseTitleIdDto> courseNames = new ArrayList<>();
    for (Long courseId : learner.getCourses()) {
      CourseTitleIdDto courseName = new CourseTitleIdDto();

      CourseDto courseDto = adminClient.getCourse(courseId);
      courseName.setCourseId(courseDto.getId());
      courseName.setCourseTitle(courseDto.getTitle());
      courseNames.add(courseName);
    }

    return ResponseEntity.ok(courseNames);
  }

  /** Gets all the tests available for the learner from the current session. This includes only the tests from
   *  the courses that the learner is subscribed to.
   *
   * @return A Response entity with status 200 with a body that contain the keys to the tests
   * that are available for the current learner.
   * 404 - if the learner is not found.
   */
  @Override
  public ResponseEntity<List<TestKeyDto>> getTestsForCourse() {
    Learner learner = learnerRepository.findByUsername(getUsername());
    Input.found(learner, "No learner found with the given username.");

    List<TestKeyDto> testKeys = new ArrayList<>();
    for (Long courseId : learner.getCourses()) {
      List<TestDto> testDtos = adminClient.getTestsByCourse(courseId);

      for (TestDto testDto : testDtos) {
        TestKeyDto testKeyDto = new TestKeyDto();
        testKeyDto.setCourseId(testDto.getCourseId());
        testKeyDto.setTestId(testDto.getId());
        testKeyDto.setCourseNodeId(testDto.getCourseNodeId());

        testKeys.add(testKeyDto);
      }
    }

    return ResponseEntity.ok(testKeys);
  }

  /** Submits a Test to the LearnerAdminClient then awaits that the client validates and marks the test and
   * save its results.
   *
   * @param testDto The test with the responses that the learner gave. Cannot be null.
   *
   * @return A Response entity with status 200 with a body that contains the results of the given test.
   * - 400 if the test is not valid.
   * - 404 if the learner or the test are not found.
   */
  @Override
  @Transactional
  public ResponseEntity<TestResultDto> submitTest(TestDto testDto) {
    Learner learner = learnerRepository.findByUsername(getUsername());
    Input.found(learner, "No learner found with the given username.");

    Input.isTrue(learner.getCourses().contains(testDto.getCourseId()), "The learner is not subscribed to the course.");

    TestResultDto testResultDto = adminClient.markTest(testDto);

    Integer revision = learner.getLatestRevision(testDto.getId()) + 1;

    learner.saveTestResult(new TestResult(testResultDto.getTestId(), testResultDto.getScore(), revision));

    return ResponseEntity.ok(testResultDto);
  }

  /** Gets a test given its id. The learner from the current session must be already subscribed to the course that the
   * test belongs in order to successfully retrieve the tests.
   *
   * @param testId The id of the test to get. Cannot be null.
   *
   * @return A Response entity with status 200 with a body that contains the test.
   * - 400 if the learner is not subscribed to the course that the test belong.
   * - 404 if the learner or the test are not found.
   */
  @Override
  public ResponseEntity<TestDto> takeTest(Long testId) {
    Validate.notNull(testId, "The testId cannot be null.");

    Learner learner = learnerRepository.findByUsername(getUsername());
    Input.found(learner, "No learner found with the given username.");

    TestDto test = adminClient.getTest(testId);
    Input.isTrue(learner.getCourses().contains(test.getCourseId()), "The learner is not subscribed to the course.");

    return ResponseEntity.ok(test);
  }

  /** Gets a tests that are recommended to take again.
   *
   * This recommendation is based on the spaced repetition technique, which suggests that information is loss over time
   * and to help to retain the information in the long term, it is recommended to review the information after certain
   * time.
   *
   * @return A Response entity with status 200 with a body that contains the ids of the tests that are recommended to
   * take again in order increase the rate of learning.
   * - 404 if the learner is not found.
   */
  @Override
  public ResponseEntity<List<Long>> getReviewRecommendations() {
    Learner learner = learnerRepository.findByUsername(getUsername());
    Input.found(learner, "No learner found with the given username.");

    List<Long> testKeys = learner.getReviewRecommendations();

    return ResponseEntity.ok(testKeys);
  }

  /** Gets the username that is currently authenticated from the SecurityContext.
   *
   * @return The username from the current session. Never null.
   */
  private String getUsername() {
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return userDetails.getUsername();
  }
}
