package com.mp.admin.application;

import application.lpathway.swagger.ContentDto;
import application.lpathway.swagger.CourseDto;
import application.lpathway.swagger.CourseNodeDto;
import application.lpathway.swagger.ResourceDto;
import com.mp.admin.domain.Content;
import com.mp.admin.domain.ContentType;
import com.mp.admin.domain.Resource;
import com.mp.admin.domain.Course;
import com.mp.admin.domain.CourseNode;
import com.mp.admin.domain.CourseRepository;
import com.mp.admin.infrastructure.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseCommand extends CourseDto {

  private CourseRepository courseRepository;

  public CourseCommand(CourseRepository theCourseRepository) {
    courseRepository = theCourseRepository;
  }

  public CourseCommand(Course course) {
    setId(course.getId());
    setTitle(course.getTitle());
    List<CourseNodeDto> nodesDtos = course.getNodes()
        .stream().map(CourseNodeCommand::new)
        .collect(Collectors.toList());
    setNodes(nodesDtos);
  }

  public Course create() {
    List<CourseNode> nodes = buildCourseNodes();

    return courseRepository.save(new Course(getTitle(), nodes));
  }

  private List<CourseNode> buildCourseNodes() {
    List<CourseNode> nodes = new ArrayList<>();
    for (CourseNodeDto courseNodeDto : getNodes()) {
      nodes.add(buildNode(courseNodeDto));
    }
    return nodes;
  }

  public void update() {
    Input.notNull(getId(), "The id cannot be null.");
    Course course = courseRepository.findById(getId());
    Input.found(course, "No course found with the given id.");

    course.update(getTitle(), buildCourseNodes());
  }

  private CourseNode buildNode(CourseNodeDto nodeDto) {
    String title = nodeDto.getTitle();
    String description = nodeDto.getDescription();

    List<Resource> resources = buildResources(nodeDto);

    return new CourseNode(title, description, resources);
  }

  private List<Resource> buildResources(CourseNodeDto nodeDto) {
    List<Resource> resources = new ArrayList<>();
    for (ResourceDto resourceDto : nodeDto.getResources()) {
      String title = resourceDto.getTitle();
      String description = resourceDto.getDescription();

      List<Content> contents = buildContents(resourceDto);

      Resource resource = new Resource(title, description, contents);
      resources.add(resource);
    }
    return resources;
  }

  private List<Content> buildContents(ResourceDto resourceDto) {
    List<Content> contents = new ArrayList<>();
    for (ContentDto contentDto : resourceDto.getContents()) {
      String title = contentDto.getTitle();
      ContentType contentType = ContentType.valueOf(contentDto.getContentType().toString());
      String url = contentDto.getUrl();

      Content content = new Content(title, contentType, url);
      contents.add(content);
    }
    return contents;
  }
}
