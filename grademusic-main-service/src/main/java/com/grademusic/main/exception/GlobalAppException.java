package com.grademusic.main.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalAppException extends RuntimeException {

    private final HttpStatus httpStatus;

    public GlobalAppException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public GlobalAppException(Throwable cause, HttpStatus httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public GlobalAppException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}
