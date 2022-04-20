package com.mp.learning.domain;

import org.apache.commons.lang3.Validate;

import java.util.*;

import javax.persistence.*;

@Entity
public class Learner {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @ElementCollection(fetch = FetchType.EAGER)
  private List<Long> coursesIds;

  @OneToOne(cascade = CascadeType.ALL)
  private TestResultGroup testResultGroup;

  /**
   * ORM's Default constructor.
   */
  Learner() {
  }

  public Learner(List<Long> theCoursesIds, TestResultGroup theTestResultGroup) {
    Validate.notNull(theCoursesIds, "The coursesIds cannot be null.");
    Validate.notNull(theTestResultGroup, "The testResult cannot be null.");

    coursesIds = theCoursesIds;
    testResultGroup = theTestResultGroup;
  }

  public Long getId() {
    return id;
  }

  public List<Long> getCourses() {
    return Collections.unmodifiableList(coursesIds);
  }

  /** Subscribes to a course given its id.
   *
   * @param courseId The course id. Cannot be null.
   */
  public void subscribe(Long courseId) {
    Validate.notNull(courseId, "The course cannot be null.");
    Validate.isTrue(!isSubscribed(courseId), "Already subscribed to the given course.");

    coursesIds.add(courseId);
  }

  /** Checks if is subscribed to a given course.
   *
   * @param courseId The course id to check the subscription. Cannot be null.
   *
   * @return True if the learner is subscribed to the course, false otherwise.
   */
  public boolean isSubscribed(Long courseId) {
    Validate.notNull(courseId, "The courseId cannot be null.");

    return coursesIds.contains(courseId);
  }

  /** Saves a TestResult.
   *
   * @param testResult The test result to save. Cannot be null.
   */
  public void saveTestResult(TestResult testResult) {
    Validate.notNull(testResult, "The TestResult cannot be null.");

    testResultGroup.save(testResult);
  }

  /** Gets the latest revision for a given test.
   *
   * @param testId The id of the test to get its latest revision. Cannot be null.
   *
   * @return The latest revision of the given test. If no test is found with the given id, it returns 0.
   */
  public Integer getLatestRevision(Long testId) {
    return testResultGroup.getLatestRevision(testId);
  }

  public List<Long> getReviewRecommendations() {
    return testResultGroup.getReviewRecommendations();
  }
}
