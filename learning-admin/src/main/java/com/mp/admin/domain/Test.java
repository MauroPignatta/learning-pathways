package com.mp.admin.domain;

import org.apache.commons.lang3.Validate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "course_id", "course_node_id" }) })
public class Test {

  public static final int MAX_SCORE = 100;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(optional = false)
  private Course course;

  @OneToOne(optional = false)
  private CourseNode courseNode;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Question> questions;

  /** ORM's Default constructor. */
  Test() {

  }

  public Test(Course theCourse, CourseNode theCourseNode, List<Question> theQuestions) {
    Validate.notNull(theCourse, "The course cannot be null.");
    Validate.notNull(theCourseNode, "The course node cannot be null.");
    Validate.notEmpty(theQuestions, "The questions cannot be null not empty.");

    course = theCourse;
    courseNode = theCourseNode;
    questions = new ArrayList<>(theQuestions);
  }

  public Long getId() {
    return id;
  }

  public Course getCourse() {
    return course;
  }

  public CourseNode getCourseNode() {
    return courseNode;
  }

  public List<Question> getQuestions() {
    return Collections.unmodifiableList(questions);
  }

  public TestResult mark(List<Answer> answers) {
    validateAnswers(answers);

    for (Answer answer : answers) {
      Question question = questions.stream()
        .filter(q -> q.getQuestion().equals(answer.getQuestion())).findFirst().get();

      question.updateCorrectAnswer(answer);
    }

    int totalQuestions = questions.size();
    int correctAnswer = (int) answers.stream().filter(Answer::isCorrect).count();

    int score = correctAnswer * MAX_SCORE / totalQuestions;

    return new TestResult(id, totalQuestions, score, answers);
  }

  private void validateAnswers(List<Answer> answers) {
    Validate.notNull(answers, "The answers cannot be null.");
    Validate.isTrue(answers.size() <= questions.size(), "There are more answers than questions.");

    for (Answer answer : answers) {
      boolean onlyOneAnswer =
        answers.stream().filter(ans -> ans.getQuestion().equals(answer.getQuestion())).count() == 1;
      Validate.isTrue(onlyOneAnswer, "There should be one answer for each question.");

      boolean isQuestionInTest =
        questions.stream().anyMatch(q -> q.getQuestion().equals(answer.getQuestion()));
      Validate.isTrue(isQuestionInTest, "There are answer for question that are not included in the test.");
    }
  }
}
