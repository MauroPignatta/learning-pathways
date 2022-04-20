package com.mp.admin.application;

import org.apache.commons.lang3.Validate;

public class ErrorDto {

  private final String exceptionClass;

  private final String exceptionMessage;

  public ErrorDto(Exception exception) {
    Validate.notNull(exception, "The exception cannot be null");

    exceptionClass = exception.getClass().getName();
    exceptionMessage = exception.getMessage();
  }

  public ErrorDto(String theExceptionClass, String message) {
    Validate.notBlank(theExceptionClass, "The exception class cannot be null.");
    Validate.notBlank(message, "The message cannot be null.");

    exceptionClass = theExceptionClass;
    exceptionMessage = message;
  }

  public String getExceptionClass() {
    return exceptionClass;
  }

  public String getExceptionMessage() {
    return exceptionMessage;
  }
}
