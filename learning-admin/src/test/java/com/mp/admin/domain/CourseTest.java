package com.mp.admin.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

  private String title;
  private List<CourseNode> nodes;

  @BeforeEach
  void setUp() {
    title = "Backend Developer";
    nodes = Arrays.asList(
        Mockito.mock(CourseNode.class),
        Mockito.mock(CourseNode.class));

  }

  @Test
  public void constructor() {
    Course course = new Course(title, nodes);

    assertEquals(title, course.getTitle());
    assertNotNull(course.getNodes());
    assertEquals(2, course.getNodes().size());
  }

  @Test
  public void constructor_blankTitle() {
    Executable constructor = () -> new Course("", nodes);

    assertThrows(RuntimeException.class, constructor, "The title cannot be blank.");
  }

  @Test
  public void constructor_nullNodes() {
    Executable constructor = () -> new Course(title, null);

    assertThrows(RuntimeException.class, constructor, "The nodes cannot be null nor empty.");
  }

  @Test
  public void constructor_nodesIsEmpty() {
    Executable constructor = () -> new Course(title, Collections.emptyList());

    assertThrows(RuntimeException.class, constructor, "The nodes cannot be null nor empty.");
  }

  @Test
  public void update() {
    Course course = new Course(title, nodes);

    course.update("newTitle", Collections.singletonList(Mockito.mock(CourseNode.class)));

    assertEquals("newTitle", course.getTitle());
    assertNotNull(course.getNodes());
    assertEquals(1, course.getNodes().size());
  }

  @Test
  public void update_blankTitle() {
    Course course = new Course(title, nodes);
    Executable update = () -> course.update("", nodes);

    assertThrows(RuntimeException.class, update, "The title cannot be blank.");
  }

  @Test
  public void update_nullNodes() {
    Course course = new Course(title, nodes);
    Executable update = () -> course.update(title, null);

    assertThrows(RuntimeException.class, update, "The nodes cannot be null nor empty.");
  }

  @Test
  public void update_nodesIsEmpty() {
    Course course = new Course(title, nodes);
    Executable update = () -> course.update(title, Collections.emptyList());

    assertThrows(RuntimeException.class, update, "The nodes cannot be null nor empty.");
  }
}