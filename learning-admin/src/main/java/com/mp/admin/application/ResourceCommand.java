package com.mp.admin.application;

import application.lpathway.swagger.ContentDto;
import application.lpathway.swagger.ResourceDto;
import com.mp.admin.domain.Content;
import com.mp.admin.domain.Resource;

import java.util.ArrayList;
import java.util.List;

public class ResourceCommand extends ResourceDto {

  public ResourceCommand(Resource resource) {
    setId(resource.getId());
    setTitle(resource.getTitle());
    setDescription(resource.getDescription());

    List<ContentDto> contentDtos = new ArrayList<>();
    for (Content content : resource.getContents()) {
      contentDtos.add(new ContentCommand(content));
    }
    setContents(contentDtos);
  }
}
