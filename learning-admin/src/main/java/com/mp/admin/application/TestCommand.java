package com.mp.admin.application;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

import application.lpathway.swagger.QuestionDto;
import application.lpathway.swagger.TestDto;

import com.mp.admin.domain.*;
import com.mp.admin.infrastructure.Input;
import static com.mp.admin.domain.Question.MIN_OPTIONS;

/** A command to create new tests.
 *
 * This command holds all the attributes from the TestDto and extends it with operation to manipulate tests.
 */
public class TestCommand extends TestDto {

  /** The testRepository to store the tests. Never null. */
  private TestRepository testRepository;

  /**
   * Creates a new empty TestCommand instance.
   *
   * @param theTestRepository The test repository to store tests. Cannot be null.
   */
  public TestCommand(TestRepository theTestRepository) {
    Validate.notNull(theTestRepository, "The testRepository cannot be null.");

    testRepository = theTestRepository;
  }

  /**
   * Constructs a TestCommand from the given Test entity.
   *
   * @param test The entity Test. Cannot be null.
   */
  public TestCommand(Test test) {
    Validate.notNull(test, "The test cannot be null.");
    setId(test.getId());
    setCourseId(test.getCourse().getId());
    setCourseNodeId(test.getCourseNode().getId());

    List<QuestionDto> questionDtos = new ArrayList<>();
    for (Question question : test.getQuestions()) {
      questionDtos.add(new QuestionCommand(question));
    }
    setQuestions(questionDtos);
  }

  /**
   * Creates a Test.
   *
   * @param course The course that the test belong to. Cannot be null.
   *
   * @param courseNode The course node that the test belong to. Cannot be null. It must belong to the given course.
   *
   * @return A new valid Test instance. Never null.
   */
  public Test create(Course course, CourseNode courseNode) {
    Validate.notNull(course, "The course cannot be null.");
    Validate.notNull(courseNode, "The courseNode cannot be null.");
    Validate.isTrue(course.containsNode(courseNode), "The courseNode does not belong to the course.");

    Input.notEmpty(getQuestions(), "The questions cannot be empty.");

    List<Question> questions = new ArrayList<>();
    for (QuestionDto questionDto : getQuestions()) {
      questions.add(buildQuestion(questionDto));
    }
    Test test = new Test(course, courseNode, questions);
    return testRepository.save(test);
  }

  /**
   * Builds a question with the data included in the given QuestionDto.
   *
   * @param questionDto The questionDto with the required data to build a Question entity. Cannot be null.
   *
   * @return A valid Question instance. Never null.
   */
  private Question buildQuestion(QuestionDto questionDto) {
    Input.notBlank(questionDto.getQuestion(), "The question cannot be blank.");
    validateQuestionOptions(questionDto);

    List<QuestionOption> options = new ArrayList<>();
    for (int i = 0; i < questionDto.getOptions().size(); ++i) {
      String option = questionDto.getOptions().get(i);
      options.add(new QuestionOption(option, i));
    }

    return new Question(questionDto.getQuestion(), questionDto.getAnswer(), options);
  }

  /**
   * Verifies that the question options are valid.
   * The options are not valid when:
   *   - Does not contain the minimum amount of options.
   *   - The answer index is out of range.
   *   - Two or more options are the same.
   *
   * @param questionDto The questionDto that contains the options. Cannot be null.
   */
  private void validateQuestionOptions(QuestionDto questionDto) {
    List<String> stringOptions = questionDto.getOptions();
    int optionsSize = stringOptions.size();
    Integer answer = questionDto.getAnswer();
    Input.isTrue(answer >= 0 && answer < optionsSize, "The answer index is out of range.");

    Input.isTrue(stringOptions.size() >= MIN_OPTIONS,
      "The options must contain at least %d elements.", MIN_OPTIONS);
    for (String option : stringOptions) {
      boolean hasRepeatedOptions =
        stringOptions.stream().filter(op -> op.equals(option)).count() != 1;

      Input.isTrue(!hasRepeatedOptions, "The QuestionOptions contains repeated options.");
    }
  }
}
