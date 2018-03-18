package com.everis.training.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import src.com.everis.training.api.ErrorResource;

@ControllerAdvice
public class ContactsErrorHandler extends ResponseEntityExceptionHandler {
 
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = { IllegalStateException.class })
	protected ResponseEntity<ErrorResource> handleNotFound(RuntimeException ex, WebRequest request) {
		final ErrorResource resource = new ErrorResource();
		resource.setStatus(HttpStatus.NOT_FOUND.value());
		resource.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
		resource.setDescription(ex.getMessage());
		return
			ResponseEntity.
				status(HttpStatus.NOT_FOUND).
					body(resource);
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(value = { IllegalArgumentException.class })
	protected ResponseEntity<ErrorResource> handleConflict(RuntimeException ex, WebRequest request) {
		final ErrorResource resource = new ErrorResource();
		resource.setStatus(HttpStatus.CONFLICT.value());
		resource.setError(HttpStatus.CONFLICT.getReasonPhrase());
		resource.setDescription(ex.getMessage());
		return
			ResponseEntity.
				status(HttpStatus.CONFLICT).
					body(resource);
	}

}