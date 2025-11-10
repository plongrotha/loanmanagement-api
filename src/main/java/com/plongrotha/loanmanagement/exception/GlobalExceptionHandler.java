package com.plongrotha.loanmanagement.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	 @ExceptionHandler(NotFoundException.class)
	    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex) {
	        Map<String, Object> response = new HashMap<>();
	        response.put("timestamp", LocalDateTime.now());
	        response.put("status", HttpStatus.NOT_FOUND.value());
	        response.put("error", "Not Found");
	        response.put("message", ex.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }

	    // Handle all other exceptions (fallback)
	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
	        Map<String, Object> response = new HashMap<>();
	        response.put("timestamp", LocalDateTime.now());
	        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
	        response.put("error", "Internal Server Error");
	        response.put("message", ex.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }


	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
	        Map<String, Object> response = new HashMap<>();

	        // Collect all validation errors
	        Map<String, String> fieldErrors = ex.getBindingResult()
	                .getFieldErrors()
	                .stream()
	                .collect(Collectors.toMap(
	                        fieldError -> fieldError.getField(),
	                        fieldError -> fieldError.getDefaultMessage(),
	                        (existing, replacement) -> existing // prevent duplicate keys
	                ));

	        response.put("timestamp", LocalDateTime.now());
	        response.put("status", HttpStatus.BAD_REQUEST.value());
	        response.put("error", "Validation Failed");
	        response.put("message", "One or more fields are invalid");
	        response.put("fields", fieldErrors);

	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }

	    @ExceptionHandler(ConflictException.class)
	    public ResponseEntity<Map<String, Object>> handleConflictException(ConflictException ex) {
	        Map<String, Object> response = new HashMap<>();
	        response.put("timestamp", LocalDateTime.now());
	        response.put("status", HttpStatus.CONFLICT.value());
	        response.put("error", "Conflict");
	        response.put("message", ex.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	    }
}
