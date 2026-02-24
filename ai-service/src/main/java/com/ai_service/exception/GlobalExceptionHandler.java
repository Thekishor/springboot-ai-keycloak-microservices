package com.ai_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ActivityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleActivityNotFoundException(ActivityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "message", exception.getMessage(),
                        "status", HttpStatus.NOT_FOUND
                ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExistsException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "message", exception.getMessage(),
                        "status", HttpStatus.NOT_FOUND
                ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException exception) {
        log.error("Unexpected error occurred: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "message", exception.getMessage()
                ));
    }
}
