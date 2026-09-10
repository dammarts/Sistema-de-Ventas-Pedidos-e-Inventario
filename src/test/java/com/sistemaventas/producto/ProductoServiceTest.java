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

        BigDecimal precioNegativo = new BigDecimal("-100");
        ReglaNegocioException errorConNegativo = assertThrows(ReglaNegocioException.class,
                () -> productoService.crear("Mouse", "Mouse inalámbrico", precioNegativo, 10));
        assertEquals("PRECIO_INVALIDO", errorConNegativo.getCodigo());
    }

    @Test
    void deberiaLanzarErrorSiProductoNoExiste() {
        assertThrows(ReglaNegocioException.class, () -> productoService.obtenerPorId(999L));
    }

    @Test
    @DisplayName("Unitaria 4 - Actualizar producto con datos válidos modifica y persiste los cambios")
    void deberiaActualizarProductoConDatosValidos() {
        Producto original = productoService.crear("Mouse", "Mouse inalámbrico", new BigDecimal("50000"), 10);

        Producto actualizado = productoService.actualizar(original.getId(), "Mouse Pro", "Mouse gamer",
                new BigDecimal("75000"), 25, EstadoProducto.INACTIVO);

        assertEquals(original.getId(), actualizado.getId());
        assertEquals("Mouse Pro", actualizado.getNombre());
        assertEquals("Mouse gamer", actualizado.getDescripcion());
        assertEquals(0, new BigDecimal("75000").compareTo(actualizado.getPrecio()));
        assertEquals(25, actualizado.getStock());
        assertEquals(EstadoProducto.INACTIVO, actualizado.getEstado());

        Producto recuperado = productoService.obtenerPorId(original.getId());
        assertEquals("Mouse Pro", recuperado.getNombre());
        assertEquals(EstadoProducto.INACTIVO, recuperado.getEstado());
    }

    @Test
    @DisplayName("Unitaria 5 - Rechaza actualizar con precio menor o igual a cero (regla #10)")
    void deberiaRechazarActualizacionConPrecioInvalido() {
        Producto original = productoService.crear("Mouse", "Mouse inalámbrico", new BigDecimal("50000"), 10);
        Long idOriginal = original.getId();

        ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
                () -> productoService.actualizar(idOriginal, "Mouse Pro", "Mouse gamer",
                        BigDecimal.ZERO, 25, EstadoProducto.INACTIVO));
        assertEquals("PRECIO_INVALIDO", error.getCodigo());

        Producto sinCambios = productoService.obtenerPorId(original.getId());
        assertEquals("Mouse", sinCambios.getNombre(),
                "El producto no debe modificarse cuando la actualización es rechazada");
    }

    @Test
    @DisplayName("Unitaria 6 - Rechaza actualizar un producto inexistente")
    void deberiaRechazarActualizacionDeProductoInexistente() {
        BigDecimal precioValido = new BigDecimal("75000");
        assertThrows(ReglaNegocioException.class,
                () -> productoService.actualizar(999L, "Mouse Pro", "Mouse gamer",
                        precioValido, 25, EstadoProducto.INACTIVO));
    }
}
