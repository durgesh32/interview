package com.example.rqchallenge;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerEntity extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
	protected ResponseEntity<Object> handleContentNotFound(RuntimeException ex, WebRequest request) {
		return new ResponseEntity<>(
			"Access denied message here", new HttpHeaders(), HttpStatus.FORBIDDEN);
	}

}
