package com.disaster.managementsystem.component.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class DataNotCreatedException extends RuntimeException {
    public DataNotCreatedException(String message) {
        super(message);
    }
}