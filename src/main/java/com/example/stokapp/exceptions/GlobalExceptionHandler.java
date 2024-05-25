package com.example.stokapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFound.class)
    public ResponseEntity<ErrorMessage> NotFound(NotFound mensaje) {
        ErrorMessage error = new ErrorMessage(mensaje.getMessage(), "404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
