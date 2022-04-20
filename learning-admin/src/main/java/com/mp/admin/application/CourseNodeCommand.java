package com.mp.admin.application;

import application.lpathway.swagger.CourseNodeDto;
import application.lpathway.swagger.ResourceDto;
import com.mp.admin.domain.Resource;
import com.mp.admin.domain.CourseNode;

import java.util.ArrayList;
import java.util.List;

public class CourseNodeCommand extends CourseNodeDto {

  public CourseNodeCommand(CourseNode courseNode) {
    setId(courseNode.getId());
    setTitle(courseNode.getTitle());
    setDescription(courseNode.getDescription());

    List<ResourceDto> resources = new ArrayList<>();
    for (Resource resource : courseNode.getResources()) {
      resources.add(new ResourceCommand(resource));
    }
    setResources(resources);
  }
}
