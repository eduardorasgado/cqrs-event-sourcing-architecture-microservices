package com.supplieswind.bankacc.cmd.api.middleware;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Objects;

@RestControllerAdvice
public class MethodArgumentNotValidExHandler extends CustomHandler {

    private static final Logger log = LogManager.getLogger(MethodArgumentNotValidExHandler.class);

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> handle(Exception exception, WebRequest request) {
        var fieldError = ((MethodArgumentNotValidException) exception)
                .getBindingResult()
                .getFieldError();
        var errorMessage = Objects.requireNonNull(fieldError).getDefaultMessage();

        log.error(errorMessage, exception);
        return new ResponseEntity<>(buildResponse(errorMessage, request), HttpStatus.BAD_REQUEST);
    }
}
