package com.grademusic.auth.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends GlobalAppException {

    public InvalidPasswordException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
