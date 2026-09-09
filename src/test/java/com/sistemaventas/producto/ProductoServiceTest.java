package com.sistemaventas.producto;

import com.sistemaventas.shared.ReglaNegocioException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ProductoServiceTest {

    private final ProductoService productoService = new ProductoService(new InMemoryProductoRepository());

    @Test
    public void deberiaCrearProductoConDatosValidos() {
        Producto producto = productoService.crear("Mouse", "Mouse inalámbrico", new BigDecimal("50000"), 10);

        assertEquals("Mouse", producto.getNombre());
        assertEquals(EstadoProducto.ACTIVO, producto.getEstado());
    }

    @Test
    public void deberiaRechazarPrecioMenorOIgualACero() {
        assertThrows(ReglaNegocioException.class,
                () -> productoService.crear("Mouse", "Mouse inalámbrico", BigDecimal.ZERO, 10));
    }

    @Test
    public void deberiaLanzarErrorSiProductoNoExiste() {
        assertThrows(ReglaNegocioException.class, () -> productoService.obtenerPorId(999L));
    }
}
