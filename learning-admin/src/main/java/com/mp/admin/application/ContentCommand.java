package com.mp.admin.application;

import application.lpathway.swagger.ContentDto;
import com.mp.admin.domain.Content;

public class ContentCommand extends ContentDto {

  public ContentCommand(Content content) {
    setId(content.getId());
    setTitle(content.getTitle());
    String contentTypeName = content.getContentType().name();
    setContentType(ContentTypeEnum.fromValue(contentTypeName));
    setUrl(content.getUrl());
  }
}
