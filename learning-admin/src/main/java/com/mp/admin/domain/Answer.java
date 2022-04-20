package com.mp.admin.domain;

import org.apache.commons.lang3.Validate;

public class Answer {

  private final String question;

  private final String givenAnswer;

  private String correctAnswer;

  public Answer(String theQuestion, String theGivenAnswer) {
    Validate.notBlank(theQuestion, "The Question cannot be blank.");
    Validate.notBlank(theGivenAnswer, "The Given Answer cannot be blank.");

    question = theQuestion;
    givenAnswer = theGivenAnswer;
  }

  public String getQuestion() {
    return question;
  }

  public String getGivenAnswer() {
    return givenAnswer;
  }

  public String getCorrectAnswer() {
    return correctAnswer;
  }

  public void updateCorrectAnswer(String correctAnswer) {
    this.correctAnswer = correctAnswer;
  }

  public boolean isCorrect() {
    return givenAnswer.equals(correctAnswer);
  }
}
