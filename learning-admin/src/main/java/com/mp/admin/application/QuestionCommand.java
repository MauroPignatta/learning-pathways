package com.mp.admin.application;

import application.lpathway.swagger.QuestionDto;
import com.mp.admin.domain.Question;
import com.mp.admin.domain.QuestionOption;
import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionCommand extends QuestionDto {

  public QuestionCommand(Question question) {
    Validate.notNull(question, "The question cannot be null.");

    setId(question.getId());
    setQuestion(question.getQuestion());

    List<String> options = question.getOptions().stream().map(QuestionOption::getOption).collect(Collectors.toList());
    Collections.shuffle(options);
    setOptions(options);
  }
}
