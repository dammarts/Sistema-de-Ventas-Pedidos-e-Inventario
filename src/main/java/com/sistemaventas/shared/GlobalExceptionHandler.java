package com.sistemaventas.shared;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> manejarReglaNegocio(ReglaNegocioException ex) {
        ErrorResponse body = new ErrorResponse(ex.getStatus().value(), ex.getCodigo(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(body);
    }
}
