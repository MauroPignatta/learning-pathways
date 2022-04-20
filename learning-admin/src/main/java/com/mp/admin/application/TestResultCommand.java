package com.mp.admin.application;

import java.util.ArrayList;
import java.util.List;

import com.mp.admin.domain.Answer;
import com.mp.admin.domain.TestResult;
import org.apache.commons.lang3.Validate;

import application.lpathway.swagger.AnswerDto;
import application.lpathway.swagger.TestResultDto;

/**
 * Command to instantiate TestResultDto with the given data from TestResults.
 */
public class TestResultCommand extends TestResultDto {

  /** Constructs a new TestResultCommand from the given TestResult entity.
   *
   * @param result The testResult with the data to construct the new TestResultCommand.Cannot be null.
   */
  public TestResultCommand(TestResult result) {
    Validate.notNull(result, "The testResult cannot be null.");

    setTestId(result.getTestId());
    setScore(result.getScore());
    setTotalQuestions(result.getTotalQuestions());

    List<AnswerDto> answers = new ArrayList<>();
    for (Answer answer : result.getAnswers()) {
      AnswerDto answerDto = new AnswerDto();

      answerDto.setQuestion(answer.getQuestion());
      answerDto.setGivenAnswer(answer.getGivenAnswer());
      answerDto.setCorrectAnswer(answer.getCorrectAnswer());

      answers.add(answerDto);
    }
    setAnswers(answers);
  }
}
