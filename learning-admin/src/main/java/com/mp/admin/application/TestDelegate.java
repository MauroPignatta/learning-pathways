package com.mp.admin.application;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import application.lpathway.swagger.QuestionDto;
import application.lpathway.swagger.TestApiDelegate;
import application.lpathway.swagger.TestDto;
import application.lpathway.swagger.TestResultDto;

import com.mp.admin.domain.*;
import com.mp.admin.infrastructure.Input;

/**
 * A TestApiDelegate implementation.
 */
public class TestDelegate implements TestApiDelegate {

  /** The test repository to store or retrieve tests. Never null. */
  private final TestRepository testRepository;

  /** The course repository to validate the tests. Never null. */
  private final CourseRepository courseRepository;

  /** The course node repository to validate the tests. Never null. */
  private final CourseNodeRepository courseNodeRepository;

  /**
   * Constructor.
   *
   * @param theTestRepository The test repository to store or retrieve tests. Cannot be null.
   *
   * @param theCourseRepository The course repository to validate the tests. Cannot be null.
   *
   * @param theCourseNodeRepository The course node repository to validate the tests. Never null.
   */
  public TestDelegate(TestRepository theTestRepository,
                      CourseRepository theCourseRepository,
                      CourseNodeRepository theCourseNodeRepository) {
    Validate.notNull(theTestRepository, "The TestRepository cannot be null.");
    Validate.notNull(theCourseRepository, "The CourseRepository cannot be null.");
    Validate.notNull(theCourseNodeRepository, "The CourseNodeRepository cannot be null.");

    testRepository = theTestRepository;
    courseRepository = theCourseRepository;
    courseNodeRepository = theCourseNodeRepository;
  }

  /** Creates a new Test.
   *
   * @param testDto A valid TestDto instance with the necessary data to create a new Test. Cannot be null.
   *
   * @return A ResponseEntity with the 201 with a header Location URI pointing to the added resource.
   * 400 - Incorrect request due to incorrect parameters.
   * It never returns null.
   */
  @Override
  @Transactional
  public ResponseEntity<Void> create(TestDto testDto) {
    Input.notNull(testDto, "The test dto cannot be null.");
    TestCommand command = (TestCommand) testDto;

    Course course = courseRepository.findById(testDto.getCourseId());
    Input.found(course, "No course found with the given id.");

    CourseNode courseNode = courseNodeRepository.findById(testDto.getCourseNodeId());
    Input.found(courseNode, "No course node found with the given id.");

    Input.isTrue(course.containsNode(courseNode), "The course node does not belong to the course.");

    Test test = command.create(course, courseNode);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
      .buildAndExpand(test.getId()).toUri();

    return ResponseEntity.created(location).build();
  }

  /** Gets a test for the given id.
   *
   * @param id The id of the test. Cannot be null.
   *
   * @return A Response entity with a 200 status and the TestDto in its body.
   * - 404: If no test its found with the given id.
   */
  @Override
  public ResponseEntity<TestDto> get(Long id) {
    Input.notNull(id, "The test id cannot be null.");

    Test test = testRepository.findById(id);
    Input.found(test, "No test found with the given id.");

    return ResponseEntity.ok(new TestCommand(test));
  }

  /** Lists all the available tests for a given course.
   *
   * @param courseId The course that the tests belongs to. Cannot be null.
   *
   * @return A ResponseEntity with a 200 status with the list of available tests.
   * The list is never null, but might be empty. The response entity is never null.
   */
  @Override
  public ResponseEntity<List<TestDto>> getAllForCourse(Long courseId) {
    Input.notNull(courseId, "The courseId cannot be null.");

    List<TestDto> testDtos = new ArrayList<>();
    for (Test test : testRepository.findByCourseId(courseId)) {
      testDtos.add(new TestCommand(test));
    }
    return ResponseEntity.ok(testDtos);
  }

  /** Marks a given test and get its results.
   *
   * @param testDto The test dto that with the answers. Cannot be null.
   *
   * @return a ResponseEntity with 201 status and the result of the given test in its body.
   * 400 - If the given test is not valid.
   * 404 - If the test is not found.
   */
  @Override
  public ResponseEntity<TestResultDto> getResults(TestDto testDto) {
    Input.notNull(testDto, "The testDto cannot be null.");
    Test test = testRepository.findById(testDto.getId());

    List<QuestionDto> questions = testDto.getQuestions();

    List<Answer> answers = new ArrayList<>();
    for (QuestionDto questionDto : questions) {
      String question = questionDto.getQuestion();
      String answer = questionDto.getOptions().get(questionDto.getAnswer());

      answers.add(new Answer(question, answer));
    }
    TestResult result = test.mark(answers);

    return ResponseEntity.ok(new TestResultCommand(result));
  }
}
