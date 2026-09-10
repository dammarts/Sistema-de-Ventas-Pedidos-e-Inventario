package com.sistemaventas.producto;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de persistencia de Producto.
 * Pendiente (pospuesto a propósito): reemplazar la implementación en memoria por Spring Data JPA + PostgreSQL.
 */
public interface ProductoRepository {

    Producto guardar(Producto producto);

    List<Producto> listarTodos();

    Optional<Producto> buscarPorId(Long id);
}
