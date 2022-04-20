package com.mp.admin.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.apache.commons.lang3.Validate;

@Entity
public class CourseNode {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "course_node_id")
  private List<Resource> resources;

  /** ORM's default constructor. */
  CourseNode() {
  }

  public CourseNode(String theTitle, String theDescription, List<Resource> theResources) {
    Validate.notBlank(theTitle, "The title cannot be blank.");
    Validate.notBlank(theDescription, "The description cannot be blank.");
    Validate.notNull(theResources, "The resources cannot be null.");

    title = theTitle;
    description = theDescription;
    resources = new ArrayList<>(theResources);
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public List<Resource> getResources() {
    return resources;
  }
}
