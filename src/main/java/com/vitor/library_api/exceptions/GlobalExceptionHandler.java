package com.vitor.library_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LivroNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLivroNotFound(LivroNotFoundException ex) {

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        ErrorResponse errorResponse = new ErrorResponse(ex.getBindingResult().getFieldError().getDefaultMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);

    }

}
