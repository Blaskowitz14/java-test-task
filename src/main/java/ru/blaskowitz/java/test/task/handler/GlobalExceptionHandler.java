package ru.blaskowitz.java.test.task.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.blaskowitz.java.test.task.dto.ErrorResponse;
import ru.blaskowitz.java.test.task.exception.AuthenticationException;
import ru.blaskowitz.java.test.task.exception.DuplicateContactException;
import ru.blaskowitz.java.test.task.exception.InsufficientFunds;
import ru.blaskowitz.java.test.task.exception.InvalidTransferException;
import ru.blaskowitz.java.test.task.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        log.error("NotFoundException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse("Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex) {
        log.error("AuthenticationException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse("Unauthorized", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(DuplicateContactException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateContact(DuplicateContactException ex) {
        log.error("DuplicateContactException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse("Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InsufficientFunds.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFunds(InsufficientFunds ex) {
        log.error("InsufficientFunds: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse("Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidTransferException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTransfer(InvalidTransferException ex) {
        log.error("InvalidTransferException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse("Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return errors;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", "Something went wrong");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
