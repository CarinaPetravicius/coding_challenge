package com.challenge.coding_challenge.client.handler;

import com.challenge.coding_challenge.client.model.CreditLineResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class RestErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(RestErrorHandler.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CreditLineResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error("Bad Request: {}", exception.getMessage(), exception);

        return new CreditLineResponse(null, null, null,
                new ArrayList<>(Collections.singleton(exception.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CreditLineResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("Bad Request: {}", exception.getMessage(), exception);

        String errorMessage = exception.getMessage();
        if (exception.getFieldError() != null && exception.getFieldError().getDefaultMessage() != null) {
            errorMessage = exception.getFieldError().getDefaultMessage();
        }

        return new CreditLineResponse(null, null, null,
                new ArrayList<>(Collections.singleton(errorMessage)));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CreditLineResponse handleConstraintViolationException(ConstraintViolationException exception) {
        final List<String> errors = new ArrayList<>();
        exception.getConstraintViolations().forEach(constraintViolation ->
                errors.add(constraintViolation.getMessage()));

        log.error("Bad Request: {}", errors, exception);

        return new CreditLineResponse(null, null, null, errors);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CreditLineResponse handleGeneralInternalException(Throwable exception) {
        log.error("Internal Error: {}", exception.getMessage(), exception);

        return new CreditLineResponse(null, null, null,
                new ArrayList<>(Collections.singleton(exception.getMessage())));
    }

}