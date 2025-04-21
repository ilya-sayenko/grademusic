package com.grademusic.main.exception;

import org.springframework.http.HttpStatus;

public class AlbumGradeNotFoundException extends GlobalAppException {

    public AlbumGradeNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
