package com.disaster.managementsystem.component.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataNotConfiguredException extends RuntimeException{
    public DataNotConfiguredException(String message) {
        super(message);
    }
}