package com.sistemaventas.producto;

import com.sistemaventas.shared.ReglaNegocioException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductoServiceTest {

    private final ProductoService productoService = new ProductoService(new InMemoryProductoRepository());

    @Test
    void deberiaCrearProductoConDatosValidos() {
        Producto producto = productoService.crear("Mouse", "Mouse inalámbrico", new BigDecimal("50000"), 10);

        assertEquals("Mouse", producto.getNombre());
        assertEquals(EstadoProducto.ACTIVO, producto.getEstado());
    }

    @Test
    void deberiaRechazarPrecioMenorOIgualACero() {
        assertThrows(ReglaNegocioException.class,
                () -> productoService.crear("Mouse", "Mouse inalámbrico", BigDecimal.ZERO, 10));
    }

    @Test
    void deberiaLanzarErrorSiProductoNoExiste() {
        assertThrows(ReglaNegocioException.class, () -> productoService.obtenerPorId(999L));
    }
}
