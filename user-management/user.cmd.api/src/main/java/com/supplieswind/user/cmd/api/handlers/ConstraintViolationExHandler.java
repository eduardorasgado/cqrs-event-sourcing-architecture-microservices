package com.supplieswind.user.cmd.api.handlers;

import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ConstraintViolationExHandler extends CustomHandler {
    private static final Logger log = LogManager.getLogger(MethodArgumentNotValidExHandler.class);

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> handle(Exception exception, WebRequest request) {
        var constraintErrorMsg = ((ConstraintViolationException) exception).getConstraintViolations().stream()
                .findFirst()
                .orElseThrow()
                .getMessage();

        return new ResponseEntity<>(buildResponse(constraintErrorMsg, request), HttpStatus.BAD_REQUEST);
    }
}
