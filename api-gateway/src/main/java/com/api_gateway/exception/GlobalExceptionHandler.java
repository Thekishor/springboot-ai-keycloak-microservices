package com.api_gateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExistsException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "message", exception.getMessage(),
                        "status", HttpStatus.NOT_FOUND
                ));
    }
}
