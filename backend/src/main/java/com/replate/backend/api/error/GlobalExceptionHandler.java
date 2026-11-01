
package com.replate.backend.api.error;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.replate.backend.adaptation.application.exception.AdaptationNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorResponse> handleValidation(
			MethodArgumentNotValidException exception) {
		Map<String, String> errors = new LinkedHashMap<>();

		exception.getBindingResult()
				.getFieldErrors()
				.forEach(error -> errors.putIfAbsent(
						error.getField(),
						error.getDefaultMessage()));

		ValidationErrorResponse response = new ValidationErrorResponse(
				400,
				"Validation failed",
				errors);

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ValidationErrorResponse> handleUnreadableMessage(
			HttpMessageNotReadableException exception) {
		ValidationErrorResponse response = new ValidationErrorResponse(
				400,
				"Invalid request body",
				Map.of(
						"request",
						"Invalid JSON or unsupported field value"));

		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(AdaptationNotFoundException.class)
	public ResponseEntity<ValidationErrorResponse> handleAdaptationNotFound(
			AdaptationNotFoundException exception) {

		ValidationErrorResponse response = new ValidationErrorResponse(
				404,
				"Adaptation not found",
				Map.of("id", exception.getMessage()));

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

}
