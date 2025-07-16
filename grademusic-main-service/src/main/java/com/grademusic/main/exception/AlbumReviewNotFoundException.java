package com.grademusic.main.exception;

import org.springframework.http.HttpStatus;

public class AlbumReviewNotFoundException extends GlobalAppException {

    public AlbumReviewNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
