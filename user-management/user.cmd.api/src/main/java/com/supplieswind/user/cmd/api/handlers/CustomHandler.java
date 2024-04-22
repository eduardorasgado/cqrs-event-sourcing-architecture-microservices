package com.supplieswind.user.cmd.api.handlers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

public abstract class CustomHandler {
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

    public abstract ResponseEntity<?> handle(Exception exception, WebRequest request);
}
