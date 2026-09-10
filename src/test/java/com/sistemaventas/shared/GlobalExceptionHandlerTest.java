package com.sistemaventas.shared;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    @Test
    @DisplayName("Unitaria 1 - manejarReglaNegocio() traduce la excepción en un ResponseEntity con el ErrorResponse esperado")
    void deberiaTraducirReglaNegocioExceptionEnResponseEntity() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ReglaNegocioException excepcion = new ReglaNegocioException(
                "PRECIO_INVALIDO", "El precio del producto debe ser mayor que cero", HttpStatus.BAD_REQUEST);

        ResponseEntity<ErrorResponse> respuesta = handler.manejarReglaNegocio(excepcion);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());

        ErrorResponse body = respuesta.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
        assertEquals("PRECIO_INVALIDO", body.getError());
        assertEquals("El precio del producto debe ser mayor que cero", body.getMessage());
    }

    @Test
    @DisplayName("Unitaria 2 - manejarReglaNegocio() respeta el HttpStatus específico de cada excepción")
    void deberiaRespetarElHttpStatusDeLaExcepcion() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ReglaNegocioException excepcion = new ReglaNegocioException(
                "PRODUCTO_NO_ENCONTRADO", "No existe un producto con id 999", HttpStatus.NOT_FOUND);

        ResponseEntity<ErrorResponse> respuesta = handler.manejarReglaNegocio(excepcion);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), respuesta.getBody().getStatus());
        assertEquals("PRODUCTO_NO_ENCONTRADO", respuesta.getBody().getError());
    }

    @Test
    @DisplayName("Unitaria 3 - ReglaNegocioException expone código, mensaje y status")
    void deberiaExponerCodigoMensajeYStatusDeLaExcepcion() {
        ReglaNegocioException excepcion = new ReglaNegocioException(
                "STOCK_INSUFICIENTE", "No hay stock suficiente", HttpStatus.CONFLICT);

        assertEquals("STOCK_INSUFICIENTE", excepcion.getCodigo());
        assertEquals("No hay stock suficiente", excepcion.getMessage());
        assertEquals(HttpStatus.CONFLICT, excepcion.getStatus());
    }
}
