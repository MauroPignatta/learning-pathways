package com.mp.admin.domain;

import org.apache.commons.lang3.Validate;

import javax.persistence.*;
import java.util.*;

@Entity
public class Question {

  public static final int MIN_OPTIONS = 2;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String question;

  @Column(nullable = false)
  private Integer answer;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "question_id")
  private List<QuestionOption> options;

  Question() {
  }

  public Question(String question, Integer answer, List<QuestionOption> questionOptions) {
    Validate.notBlank(question, "The question cannot be blank.");
    Validate.notNull(answer, "The answer cannot be null.");
    Validate.notEmpty(questionOptions, "The options cannot be empty.");

    Validate.isTrue(questionOptions.size() >= MIN_OPTIONS,
        "The options must contain at least %d elements.", MIN_OPTIONS);
    Validate.isTrue(answer >= 0 && answer < questionOptions.size(), "The answer is out of range.");

    for (QuestionOption option : questionOptions) {
      boolean hasRepeatedOptions =
          questionOptions.stream().filter(op -> op.getOption().equals(option.getOption())).count() != 1;

      Validate.isTrue(!hasRepeatedOptions, "The QuestionOptions contains repeated options.");
    }

    this.question = question;
    this.answer = answer;
    this.options = new ArrayList<>(questionOptions);
  }

  public Long getId() {
    return id;
  }

  public String getQuestion() {
    return question;
  }

  public List<QuestionOption> getOptions() {
    return Collections.unmodifiableList(options);
  }

  public void updateCorrectAnswer(Answer theAnswer) {
    Validate.isTrue(question.equals(theAnswer.getQuestion()));

    QuestionOption correctOption = options.get(answer);

    theAnswer.updateCorrectAnswer(correctOption.getOption());
  }

}
