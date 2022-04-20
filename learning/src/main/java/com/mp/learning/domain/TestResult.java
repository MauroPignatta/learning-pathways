package com.mp.learning.domain;

import org.apache.commons.lang3.Validate;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class TestResult {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long testId;

  @Column(nullable = false)
  private Integer score;

  @Column(nullable = false)
  private OffsetDateTime creationDate;

  @Column(nullable = false)
  private int revision;

  /** ORM's Default constructor. */
  TestResult() {}

  public TestResult(Long theTestId, Integer theScore, Integer theRevision) {
    Validate.notNull(theTestId, "The test id cannot be null.");
    Validate.notNull(theScore, "The score cannot be null.");
    Validate.notNull(theRevision, "The revision cannot be null.");

    testId = theTestId;
    score = theScore;
    revision = theRevision;
    creationDate = OffsetDateTime.now();
  }

  public Long getTestId() {
    return testId;
  }

  public int getScore() {
    return score;
  }

  public OffsetDateTime getCreationDate() {
    return creationDate;
  }

  public int getRevision() {
    return revision;
  }
}
