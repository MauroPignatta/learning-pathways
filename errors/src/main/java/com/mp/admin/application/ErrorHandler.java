package com.mp.admin.application;

import com.mp.admin.infrastructure.ApiException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

  @ExceptionHandler
  public ResponseEntity<ErrorDto> unexpectedException(Exception exception) {
    ErrorDto errorDto = new ErrorDto(exception);

    return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({
        HttpMessageConversionException.class,
        TypeMismatchException.class
  })
  public ResponseEntity<ErrorDto> badRequests(Exception exception) {
    ErrorDto errorDto = new ErrorDto(exception);

    return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorDto> methodArgumentNotValid(MethodArgumentNotValidException exception) {
    ErrorDto errorDto = null;

    FieldError fieldError = exception.getFieldError();
    if (fieldError != null) {
      String name = exception.getClass().getName();
      String message = String.format("%s: %s.", fieldError.getField(), fieldError.getDefaultMessage());
      errorDto = new ErrorDto(name, message);
    } else {
      errorDto = new ErrorDto(exception);
    }

    return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
      ExpiredJwtException.class,
      UnsupportedJwtException.class,
      MalformedJwtException.class
  })
  public ResponseEntity<ErrorDto> jwtException(Exception exception) {
    ErrorDto errorDto = new ErrorDto(exception);

    return new ResponseEntity<>(errorDto, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorDto> illegalArgument(IllegalArgumentException exception) {
    ErrorDto errorDto = new ErrorDto(exception);

    return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ErrorDto> apiErrors(ApiException exception) {
    ErrorDto errorDto = new ErrorDto(exception);

    return new ResponseEntity<>(errorDto, exception.getStatusCode());
  }
}

