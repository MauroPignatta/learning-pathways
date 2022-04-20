package com.mp.admin.domain;

import com.mp.admin.application.CourseCommand;
import org.apache.commons.lang3.Validate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String title;

  @OneToMany(cascade = CascadeType.ALL)
  private List<CourseNode> nodes;

  /** ORM's default constructor.*/
  Course() {
  }

  public Course(String theTitle, List<CourseNode> theNodes) {
    update(theTitle, theNodes);
  }

  public void update(String theTitle, List<CourseNode> theNodes) {
    Validate.notBlank(theTitle, "The title cannot be blank.");
    Validate.notEmpty(theNodes, "The nodes cannot be null nor empty.");

    title = theTitle;
    nodes = new ArrayList<>(theNodes);
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public List<CourseNode> getNodes() {
    return Collections.unmodifiableList(nodes);
  }

  public boolean containsNode(CourseNode courseNode) {
    Validate.notNull(courseNode, "The course node cannot be null.");

    return nodes.contains(courseNode);
  }

}
