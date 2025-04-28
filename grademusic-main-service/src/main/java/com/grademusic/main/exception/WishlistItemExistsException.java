package com.grademusic.main.exception;

import org.springframework.http.HttpStatus;

public class WishlistItemExistsException extends GlobalAppException {

    public WishlistItemExistsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
