package com.mp.admin.domain;

import org.apache.commons.lang3.Validate;

import javax.persistence.*;

@Entity
public class QuestionOption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String option;

  @Column(nullable = false)
  private int index;

  QuestionOption() {
  }

  public QuestionOption(String theOption, int theIndex) {
    Validate.notNull(theOption, "The option cannot be null.");

    option = theOption;
    index = theIndex;
  }

  public String getOption() {
    return option;
  }

  public int getIndex() {
    return index;
  }
}
