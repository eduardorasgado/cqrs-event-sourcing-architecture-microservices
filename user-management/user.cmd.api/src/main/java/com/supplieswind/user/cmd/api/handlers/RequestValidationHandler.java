package com.supplieswind.user.cmd.api.handlers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.Objects;

@RestControllerAdvice
public class RequestValidationHandler {


    private static final Logger log = LogManager.getLogger(RequestValidationHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(Exception exception, WebRequest request) {
        var fieldError = ((MethodArgumentNotValidException) exception)
                .getBindingResult()
                .getFieldError();
        var errorMessage = Objects.requireNonNull(fieldError).getDefaultMessage();

        log.error(errorMessage, exception);
        return new ResponseEntity<>(buildResponse(errorMessage, request), HttpStatus.BAD_REQUEST);
    }

    public GeneralExceptionResponse buildResponse(String message, WebRequest request) {
        return GeneralExceptionResponse
                .builder()
                .timestamp(new Date())
                .message(message)
                .details(request.getDescription(false))
                .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GeneralExceptionResponse {
        private Date timestamp;
        private String message;
        private String details;
    }
}
