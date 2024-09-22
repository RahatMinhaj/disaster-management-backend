package com.disaster.managementsystem.component.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class FailedException extends RuntimeException {
    public FailedException(String message) {
        super(message);
    }
}