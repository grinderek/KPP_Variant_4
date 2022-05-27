package com.web.kpp.entity;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ExceptionInfo {
    private String message;
    private HttpStatus errorCode;
    private List<String> errors;

    public HttpStatus getErrorCode() {
        return this.errorCode;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public ExceptionInfo(HttpStatus httpStatus, String message, List<String> errors) {
        this.message = message;
        this.errorCode = httpStatus;
        this.errors = errors;
    }
}
