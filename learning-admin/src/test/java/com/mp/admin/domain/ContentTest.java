package com.mp.admin.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class ContentTest {

  private String title;
  private ContentType contentType;
  private String url;

  @BeforeEach
  void setUp() {
    title = "Title";
    contentType = ContentType.READ;
    url = "http://www.validurl.com/some-readable-content";
  }

  @Test
  public void constructor() {
    Content content = new Content(title, contentType, url);

    assertEquals(title, content.getTitle());
    assertEquals(contentType, content.getContentType());
    assertEquals(url, content.getUrl());
  }

  @Test
  public void constructor_blankTitle() {
    Executable constructor = () -> new Content("", ContentType.READ, url);

    assertThrows(RuntimeException.class, constructor, "The title cannot be blank.");
  }

  @Test
  public void constructor_nullContentType() {
    Executable constructor = () -> new Content(title, null, url);

    assertThrows(RuntimeException.class, constructor, "The content type cannot be null.");
  }

  @Test
  public void constructor_blankUrl() {
    Executable constructor = () -> new Content(title, ContentType.READ, null);

    assertThrows(RuntimeException.class, constructor, "The url cannot be blank.");
  }
}