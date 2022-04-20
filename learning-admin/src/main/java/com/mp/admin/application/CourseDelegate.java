package com.mp.admin.application;

import application.lpathway.swagger.CourseApiDelegate;
import application.lpathway.swagger.CourseDto;
import application.lpathway.swagger.CourseTitleIdDto;
import com.mp.admin.domain.Course;
import com.mp.admin.domain.CourseRepository;
import com.mp.admin.infrastructure.Input;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class CourseDelegate implements CourseApiDelegate {

  private CourseRepository courseRepository;

  public CourseDelegate(CourseRepository courseRepository) {
    this.courseRepository = courseRepository;
  }

  @Override
  @Transactional
  public ResponseEntity<Void> create(CourseDto courseDto) {
    Input.notNull(courseDto, "The course dto cannot be null.");
    CourseCommand command = (CourseCommand) courseDto;

    Course course = command.create();
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(course.getId()).toUri();

    return ResponseEntity.created(location).build();
  }

  @Override
  @Transactional
  public ResponseEntity<Void> update(CourseDto courseDto) {
    Input.notNull(courseDto, "The course dto cannot be null.");

    CourseCommand command = (CourseCommand) courseDto;
    command.update();

    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<CourseDto> get(Long id) {
    Input.notNull(id, "The id cannot be null.");
    Course course = courseRepository.findById(id);
    Input.found(course, "No course found with the given id.");

    CourseDto dto = new CourseCommand(course);
    return ResponseEntity.ok(dto);
  }

  @Override
  public ResponseEntity<List<CourseTitleIdDto>> getTitles() {
    List<Course> courses = courseRepository.findAll();

    List<CourseTitleIdDto> courseTitles = new ArrayList<>();

    for (Course course : courses) {
      CourseTitleIdDto courseTitleIdDto = new CourseTitleIdDto();

      courseTitleIdDto.setCourseId(course.getId());
      courseTitleIdDto.setCourseTitle(course.getTitle());
      courseTitles.add(courseTitleIdDto);
    }

    return ResponseEntity.ok(courseTitles);
  }

  @Override
  public ResponseEntity<List<CourseDto>> getAll() {
    List<Course> courses = courseRepository.findAll();
    List<CourseDto> dtos = new ArrayList<>(courses.size());

    for (Course course : courses) {
      CourseCommand command = new CourseCommand(course);
      dtos.add(command);
    }

    return ResponseEntity.ok(dtos);
  }
}
