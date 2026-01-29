package com.mich.ecommerce.customer.handler;

import com.mich.ecommerce.customer.exception.CustomerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException exception) {
        log.warn("Customer resource not found: {}", exception.getMsg());

        var status = HttpStatus.NOT_FOUND;
        var response = new ErrorResponse(
                status.value(),
                exception.getMsg(),
                null
        );
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.warn("Validation failed for request: {} errors found", exception.getBindingResult().getErrorCount());
        var status = HttpStatus.BAD_REQUEST;
        var errors = new HashMap<String, String>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        var response = new ErrorResponse(
                status.value(),
                "Validation failed for the request",
                errors
        );
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("Unhandled internal server error: ", ex);

        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var response = new ErrorResponse(
                status.value(),
                "An unexpected internal server error occurred",
                Map.of("info", "Please contact support if the issue persists")
        );
        return ResponseEntity.status(status).body(response);
    }
}