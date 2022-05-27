package com.web.kpp.handlers;

import com.web.kpp.entity.ExceptionInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
  private static final Logger logger = LogManager.getLogger(RestExceptionHandler.class);

  private void logError(ExceptionInfo error) {
    logger.error(
        "\n\nStatus: "
            + error.getErrorCode()
            + ".\nMessage: "
            + error.getMessage()
            + ".\nAll Errors: "
            + error.getErrors()
            + "\n\n");
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    List<String> errors = new ArrayList<>();

    for (FieldError error : exception.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }

    for (ObjectError error : exception.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }

    ExceptionInfo validationError =
        new ExceptionInfo(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), errors);

    logError(validationError);

    return handleExceptionInternal(
        exception, validationError, headers, validationError.getErrorCode(), request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    List<String> errors = new ArrayList<>();

    errors.add(exception.getParameterName() + " parameter is missing!");

    ExceptionInfo paramNotProvidedError =
        new ExceptionInfo(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), errors);

    logError(paramNotProvidedError);

    return new ResponseEntity<Object>(
        paramNotProvidedError, new HttpHeaders(), paramNotProvidedError.getErrorCode());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException exception) {
    List<String> errors = new ArrayList<>();

    for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
      errors.add(
          violation.getRootBeanClass().getName()
              + " "
              + violation.getPropertyPath()
              + ": "
              + violation.getMessage());
    }

    ExceptionInfo constrViolationError =
        new ExceptionInfo(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), errors);

    logError(constrViolationError);

    return new ResponseEntity<Object>(
        constrViolationError, new HttpHeaders(), constrViolationError.getErrorCode());
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException exception, WebRequest request) {
    List<String> errors = new ArrayList<>();

    errors.add(exception.getName() + " should be of type " + exception.getRequiredType().getName());

    ExceptionInfo typeError =
        new ExceptionInfo(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), errors);

    logError(typeError);

    return new ResponseEntity<Object>(typeError, new HttpHeaders(), typeError.getErrorCode());
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<Object> handleAll(Exception exception, WebRequest request) {
    List<String> errors = new ArrayList<>();

    errors.add("Something went wrong!");

    ExceptionInfo error =
        new ExceptionInfo(
            HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage(), errors);

    logError(error);

    return new ResponseEntity<Object>(error, new HttpHeaders(), error.getErrorCode());
  }
}
