package com.disaster.managementsystem.component.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotPermittedException extends RuntimeException {
    public NotPermittedException(String message) {
        super(message);
    }
}