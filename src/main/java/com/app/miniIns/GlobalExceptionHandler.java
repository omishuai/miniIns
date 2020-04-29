package com.app.miniIns;

import com.app.miniIns.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {DuplicateDataException.class, VerificationFailureException.class})
    protected ResponseEntity<Object> handleConflict(
            Exception ex, WebRequest request) {

        return handleExceptionInternal(ex, new ErrorResponse(ex.getMessage()),
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {EmptyInputException.class})
    protected ResponseEntity<Object> handleBadRequest(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, new ErrorResponse(ex.getMessage()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolationRequest(
            ConstraintViolationException ex, WebRequest request) {
        return handleExceptionInternal(ex, new ErrorResponse(ex.getConstraintViolations().iterator().next().getMessage()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

    }
}
