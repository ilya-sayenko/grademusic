package com.grademusic.auth.exception;

import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends GlobalAppException {

    public InvalidRefreshTokenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
