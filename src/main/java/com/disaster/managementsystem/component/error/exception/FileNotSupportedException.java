package com.disaster.managementsystem.component.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class FileNotSupportedException extends RuntimeException {

    public FileNotSupportedException(String message) {
        super(message);
    }
}