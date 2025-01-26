package com.luanr.agregadorinvestimentos.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;


@ControllerAdvice
public class GlobalExceptionHandler {
    public record ApiError(int status, String message, Instant timestamp) {}
    public record ValidationError(int status, String message, Instant timestamp) {}
    public record ApiValidationError(int status, String message, Instant timestamp, List<ValidationError> errors) {}
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException ex) {
        ApiError error = new ApiError(
                ex.getStatusCode().value(),
                ex.getReason(),
                Instant.now()
        );
        return new ResponseEntity<>(error, ex.getStatusCode());
    }
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDataAccessException(DataAccessException ex) {
        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno no servidor",
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiValidationError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ValidationError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new ValidationError(
                        HttpStatus.BAD_REQUEST.value(),
                        e.getDefaultMessage(),
                        Instant.now()
                )).toList();
        ApiValidationError error = new ApiValidationError(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                Instant.now(),
                errors
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
