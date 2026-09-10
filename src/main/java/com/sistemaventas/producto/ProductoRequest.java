package com.sistemaventas.producto;

import java.math.BigDecimal;

/** Cuerpo de entrada para crear/actualizar un Producto. */
public record ProductoRequest(String nombre, String descripcion, BigDecimal precio, int stock, EstadoProducto estado) {
}
