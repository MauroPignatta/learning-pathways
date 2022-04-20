package com.mp.admin.domain;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestResult {

  private Long testId;

  private int totalQuestions;

  private int score;

  private List<Answer> answers;

  public TestResult(Long testId, int totalQuestions, int score, List<Answer> answers) {
    Validate.notNull(testId, "The test id cannot be null.");
    Validate.notNull(answers, "The answers cannot be null.");

    this.testId = testId;
    this.totalQuestions = totalQuestions;
    this.score = score;
    this.answers = new ArrayList<>(answers);
  }

  public Long getTestId() {
    return testId;
  }

  public int getTotalQuestions() {
    return totalQuestions;
  }

  public int getScore() {
    return score;
  }

  public List<Answer> getAnswers() {
    return Collections.unmodifiableList(answers);
  }
}
