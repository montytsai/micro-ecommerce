package io.github.montytsai.ecommerce.handler;

import io.github.montytsai.ecommerce.exception.CustomerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(CustomerNotFoundException ex, HttpServletRequest req) {
        log.warn("Customer Not Found Exception occurred: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(req.getRequestURI(), ex.getMsg());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException ex, HttpServletRequest req) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        log.warn("Validation failed: {}", errors);
        ErrorResponse response = new ErrorResponse(req.getRequestURI(), "Validation failed", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handle(HandlerMethodValidationException ex, HttpServletRequest req) {
        var errors = new HashMap<String, String>();
        ex.getAllErrors().forEach(error -> {
            if (error instanceof FieldError fError) {
                errors.put(fError.getField(), fError.getDefaultMessage());
            } else if (error instanceof ObjectError oError) {
                errors.put(oError.getObjectName(), oError.getDefaultMessage());
            }
        });
        log.warn("Handler method validation failed: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(
                req.getRequestURI(),
                "Method argument validation failed",
                errors
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handle(ConstraintViolationException ex, HttpServletRequest req) {
        var errors = new HashMap<String, String>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );
        log.warn("Constraint violation for request parameters: {}", errors);
        ErrorResponse response = new ErrorResponse(req.getRequestURI(), ex.getMessage(), errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handle(IllegalArgumentException ex, HttpServletRequest req) {
        log.warn("Invalid argument provided: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(req.getRequestURI(), ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception ex, HttpServletRequest req) {
        String uri = req.getRequestURI();
        log.error("An unexpected internal server error occurred for uri: {}", uri, ex);
        String message = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred.";
        ErrorResponse response = new ErrorResponse(uri, message);
        return ResponseEntity.internalServerError().body(response);
    }

}
