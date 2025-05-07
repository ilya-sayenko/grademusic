package com.grademusic.main.exception.handler;

import com.grademusic.main.exception.GlobalAppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Order(-1)
    @ExceptionHandler(GlobalAppException.class)
    public ResponseEntity<ErrorResponse> handle(GlobalAppException ex) {
        log.error("An error has occurred: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(
                        ErrorResponse.builder()
                                .error(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception ex) {
        log.error("An error has occurred: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ErrorResponse.builder()
                                .error(ex.getMessage())
                                .build()
                );
    }
}
