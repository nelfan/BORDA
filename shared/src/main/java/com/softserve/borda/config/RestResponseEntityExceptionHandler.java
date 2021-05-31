package com.softserve.borda.config;

import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.exceptions.CustomFailedToDeleteEntityException;
import lombok.extern.java.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Log
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {CustomEntityNotFoundException.class, CustomFailedToDeleteEntityException.class})
    protected ResponseEntity<Object> handleEntityNotFound(
            RuntimeException ex, WebRequest request) {
        log.severe(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}