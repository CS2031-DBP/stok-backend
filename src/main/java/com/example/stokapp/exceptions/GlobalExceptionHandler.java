package com.example.stokapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFound.class)
    public ResponseEntity<ErrorMessage> NotFound(NotFound mensaje) {
        ErrorMessage error = new ErrorMessage(mensaje.getMessage(), "404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UnauthorizeOperationException.class)
    public ResponseEntity<ErrorMessage> UnauthorizeOperationException(UnauthorizeOperationException mensaje) {
        ErrorMessage error = new ErrorMessage(mensaje.getMessage(), "403");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
}