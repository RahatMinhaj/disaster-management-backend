package com.disaster.managementsystem.component.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataShouldBeNotNullException extends RuntimeException {
    public DataShouldBeNotNullException(String message) {
        super(message);
    }
}