package com.mp.admin.infrastructure;

import org.apache.commons.lang3.Validate;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

  private HttpStatus statusCode;

  public ApiException(String message, HttpStatus theStatusCode) {
    super(message);
    setStatusCode(theStatusCode);
  }

  private void setStatusCode(HttpStatus theStatusCode) {
    Validate.notNull(theStatusCode, "The status code cannot be null.");

    statusCode = theStatusCode;
  }

  public HttpStatus getStatusCode() {
    return statusCode;
  }
}
