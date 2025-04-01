package com.grademusic.auth.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GlobalAppException {

    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
