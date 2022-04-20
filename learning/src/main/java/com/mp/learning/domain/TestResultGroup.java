package com.mp.learning.domain;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.apache.commons.lang3.Validate;

@Entity
public class TestResultGroup {

  public static final int MAX_REVISIONS = 3;

  public static final int DAY_IN_HOURS = 24;
  private static final int MIN_REVISION_TIME_HOURS = DAY_IN_HOURS;
  private static final int MAX_REVISION_TIME_HOURS = DAY_IN_HOURS * 7;

  private static final float MIN_MULTIPLIER = 0.2f;
  private static final float MAX_MULTIPLIER = 1f;

  /** The generated id. Might be null if the entity is not persisted yet. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The list of the test result. Never null. Might be empty. */
  @OneToMany(cascade = CascadeType.ALL)
  private List<TestResult> results;

  /** ORM's Default constructor. */
  TestResultGroup() {
  }

  /** Constructor.
   *
   * @param results The test results. Cannot be null, can be empty.
   */
  public TestResultGroup(List<TestResult> results) {
    this.results = new ArrayList<>(results);
  }

  /** Saves a test result.
   *
   * @param testResult The test result to save. Cannot be null.
   */
  public void save(TestResult testResult) {
    Validate.notNull(testResult, "The test result cannot be null.");

    results.add(testResult);
  }

  /** Gets the latest revision for a given test.
   *
   * @param testId The id of the test to get its latest revision. Cannot be null.
   *
   * @return The latest revision of the given test. If no test is found with the given id, it returns 0.
   */
  public int getLatestRevision(Long testId) {
    Validate.notNull(testId, "The testId cannot be null.");
    Optional<TestResult> testResult = results.stream()
      .filter(test -> test.getTestId().equals(testId))
      .max(Comparator.comparingInt(TestResult::getRevision));

    return testResult.map(TestResult::getRevision).orElse(0);
  }

  /** Gets the tests that are recommended to take again.
   *
   * @return the tests that are recommended to take again.
   */
  public List<Long> getReviewRecommendations() {
    List<Long> testIdsToReview = new ArrayList<>();
    for (Map.Entry<Long, TestResult> entry : getLatestTestResultsById().entrySet()) {
      TestResult latestTestResult = entry.getValue();

      int revision = latestTestResult.getRevision();
      int score = latestTestResult.getScore();
      OffsetDateTime creationDate = latestTestResult.getCreationDate();

      if (revision > MAX_REVISIONS) {
        continue;
      }

      float multiplier = (getScoreMultiplier(score) + getRevisionMultiplier(revision)) / 2;
      int neededHoursToReview = (int) (MAX_REVISION_TIME_HOURS *  multiplier);

      boolean shouldReview = creationDate.isBefore(OffsetDateTime.now().minus(neededHoursToReview, ChronoUnit.HOURS));
      if (shouldReview) {
        testIdsToReview.add(latestTestResult.getTestId());
      }
    }
    return testIdsToReview;
  }

  private float getRevisionMultiplier(int revision) {
    if (revision == 1) {
      return 0f;
    }

    if (revision == 2) {
      return 0.33f;
    }

    return 1f;
  }

  private float getScoreMultiplier(int score) {
    float scoreMultiplier = score / 80f;
    return Math.max(MIN_MULTIPLIER, Math.min(scoreMultiplier, MAX_MULTIPLIER));
  }

  private Map<Long, TestResult> getLatestTestResultsById() {
    Map<Long, TestResult> testResultsById = new HashMap<>();

    Set<Long> testIds = results.stream().map(TestResult::getTestId).collect(Collectors.toSet());
    for (Long testId : testIds) {
      TestResult latestTestResult = results.stream()
          .filter(tr -> tr.getTestId().equals(testId))
          .max(Comparator.comparingInt(TestResult::getRevision)).get();

      testResultsById.put(testId, latestTestResult);
    }

    return testResultsById;
  }
}
