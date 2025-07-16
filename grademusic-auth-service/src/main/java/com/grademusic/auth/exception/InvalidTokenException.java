package com.grademusic.auth.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends GlobalAppException {

    public InvalidTokenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
