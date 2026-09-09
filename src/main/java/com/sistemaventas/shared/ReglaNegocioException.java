package com.sistemaventas.shared;

import org.springframework.http.HttpStatus;

/**
 * Excepción para violaciones de reglas de negocio del enunciado
 * (ej. STOCK_INSUFICIENTE, PRECIO_INVALIDO). El código queda en el JSON de error.
 */
public class ReglaNegocioException extends RuntimeException {

    private final String codigo;
    private final HttpStatus status;

    public ReglaNegocioException(String codigo, String mensaje, HttpStatus status) {
        super(mensaje);
        this.codigo = codigo;
        this.status = status;
    }

    public String getCodigo() {
        return codigo;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
