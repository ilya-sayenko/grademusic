package com.grademusic.auth.exception;

import org.springframework.http.HttpStatus;

public class InvalidUsernameException extends GlobalAppException {

    public InvalidUsernameException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
