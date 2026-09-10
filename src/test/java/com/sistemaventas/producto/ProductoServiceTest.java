package com.sistemaventas.producto;

import com.sistemaventas.shared.ReglaNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductoServiceTest {

    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        productoService = new ProductoService(new InMemoryProductoRepository());
    }

    @Test
    @DisplayName("Unitaria 1 - Crear producto con datos válidos queda ACTIVO y persistido")
    void deberiaCrearProductoConDatosValidos() {
        Producto producto = productoService.crear("Mouse", "Mouse inalámbrico", new BigDecimal("50000"), 10);

        assertEquals("Mouse", producto.getNombre());
        assertEquals(EstadoProducto.ACTIVO, producto.getEstado());
        assertNotNull(producto.getId(), "El producto creado debe tener un id asignado por el repositorio");

        Producto recuperado = productoService.obtenerPorId(producto.getId());
        assertEquals(producto.getId(), recuperado.getId());
        assertEquals("Mouse", recuperado.getNombre());
        assertTrue(productoService.listarTodos().contains(recuperado),
                "El producto persistido debe aparecer en el listado completo");
    }

    @Test
    @DisplayName("Unitaria 2 - Rechaza precio menor o igual a cero (regla #10)")
    void deberiaRechazarPrecioMenorOIgualACero() {
        ReglaNegocioException errorConCero = assertThrows(ReglaNegocioException.class,
                () -> productoService.crear("Mouse", "Mouse inalámbrico", BigDecimal.ZERO, 10));
        assertEquals("PRECIO_INVALIDO", errorConCero.getCodigo());

        ReglaNegocioException errorConNegativo = assertThrows(ReglaNegocioException.class,
                () -> productoService.crear("Mouse", "Mouse inalámbrico", new BigDecimal("-100"), 10));
        assertEquals("PRECIO_INVALIDO", errorConNegativo.getCodigo());
    }

    @Test
    void deberiaLanzarErrorSiProductoNoExiste() {
        assertThrows(ReglaNegocioException.class, () -> productoService.obtenerPorId(999L));
    }
}
