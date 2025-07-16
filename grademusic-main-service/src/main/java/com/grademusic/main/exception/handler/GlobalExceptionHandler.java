package com.grademusic.main.exception.handler;

import com.grademusic.main.exception.GlobalAppException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        log.error("An error has occurred: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .error(createValidationErrorMessage(ex.getBindingResult()))
                                .build()
                );
    }

    private String createValidationErrorMessage(Errors errors) {
        if (errors == null || !errors.hasErrors()) {
            return "";
        }
        StringBuilder messageBuilder = new StringBuilder();
        errors.getGlobalErrors()
                .forEach(error -> {
                    messageBuilder.append(error.getDefaultMessage());
                    messageBuilder.append(";");
                });
        errors.getFieldErrors()
                .forEach(error -> {
                    messageBuilder.append(error.getField());
                    messageBuilder.append(" : ");
                    messageBuilder.append(error.getDefaultMessage());
                    messageBuilder.append(";");
                });
        if (!messageBuilder.isEmpty()) {
            messageBuilder.setCharAt(messageBuilder.lastIndexOf(";"), '.');
        }
        return messageBuilder.toString();
    }
}
