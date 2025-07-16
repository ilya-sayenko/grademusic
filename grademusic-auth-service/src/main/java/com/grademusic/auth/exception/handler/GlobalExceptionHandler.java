package com.grademusic.auth.exception.handler;

import com.grademusic.auth.exception.GlobalAppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Map<String, String> constraintErrorMessages = Map.of(
            "users_email_unique_index", "Email exists, please choose different",
            "users_username_unique_index", "Username exists, please choose different"
    );

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handle(DataIntegrityViolationException ex) {
        log.error("An error has occurred: {}", ex.getMessage(), ex);
        String message = ex.getMostSpecificCause().getMessage();
        String responseMessage = message;
        for (var entry : constraintErrorMessages.entrySet()) {
            if (message.contains(entry.getKey())) {
                responseMessage = entry.getValue();
            }
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .error(responseMessage)
                                .build()
                );
    }
}
