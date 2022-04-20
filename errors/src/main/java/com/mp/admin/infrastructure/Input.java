package com.mp.admin.infrastructure;

import org.springframework.http.HttpStatus;

import java.util.Collection;

public class Input {

  public static void notNull(Object object, String message) {
    if (object == null) {
      throw new ApiException(message, HttpStatus.BAD_REQUEST);
    }
  }

  public static void notBlank(String string, String message) {
    if (string == null || string.trim().length() == 0) {
      throw new ApiException(message, HttpStatus.BAD_REQUEST);
    }
  }

  public static void found(Object object, String message) {
    if (object == null) {
      throw new ApiException(message, HttpStatus.NOT_FOUND);
    }
  }

  public static void isTrue(boolean expression, String message) {
    if (!expression) {
      throw new ApiException(message, HttpStatus.BAD_REQUEST);
    }
  }

  public static void isTrue(boolean expression, String format, Object ... params) {
    if (!expression) {
      String message = String.format(format, params);
      throw new ApiException(message, HttpStatus.BAD_REQUEST);
    }
  }

  public static void notEmpty(Collection<?> collection, String message) {
    if (collection == null || collection.size() == 0) {
      throw new ApiException(message, HttpStatus.BAD_REQUEST);
    }
  }
}
