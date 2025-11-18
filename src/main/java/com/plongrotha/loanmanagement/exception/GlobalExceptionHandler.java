package com.plongrotha.loanmanagement.exception;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;



@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(LoanAmountZeroException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleLoanAmountZeroException(LoanAmountZeroException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Loan Amount Error");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
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

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<Map<String, Object>> handleConflictException(ConflictException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("timestamp", LocalDateTime.now());
		response.put("status", HttpStatus.CONFLICT.value());
		response.put("error", "Conflict");
		response.put("message", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problemDetail.setTitle("Validation Failed");
		problemDetail.setProperty("timestamp", LocalDateTime.now());

		// Collect field validation errors
		Map<String, String> errors = new HashMap<>();
		int blankFieldErrors = 0;

		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());

			// Check if the error is specifically "must not be blank"
			if ("must not be blank".equals(fieldError.getDefaultMessage())) {
				blankFieldErrors++;
			}
		}

		// Add errors to ProblemDetail as extra properties
		problemDetail.setProperty("errors", errors);

		return problemDetail;
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	public ProblemDetail handleMethodValidationException(HandlerMethodValidationException e) {
		Map<String, String> errors = new HashMap<>();

		// Loop through each invalid parameter validation result
		e.getParameterValidationResults().forEach(parameterError -> {
			String paramName = parameterError.getMethodParameter().getParameterName(); // Get parameter name

			// Loop through each validation error message for this parameter
			for (var messageError : parameterError.getResolvableErrors()) {
				errors.put(paramName, messageError.getDefaultMessage()); // Store error message
			}
		});

		// Create structured ProblemDetail response
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problemDetail.setTitle("Method Parameter Validation Failed");
		problemDetail.setProperties(Map.of("timestamp", LocalDateTime.now(), "errors", errors // Attach validation
																								// errors
		));

		return problemDetail;
	}

}
