package com.disaster.managementsystem.component.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DataAlreadyExistsException extends RuntimeException{

    public DataAlreadyExistsException(String message) {
        //Organization Designation Data Already Exists
        super(message);
    }
}
