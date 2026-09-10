package com.sistemaventas.producto;

import com.sistemaventas.shared.ReglaNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductoControllerTest {

    private ProductoController productoController;

    @BeforeEach
    void setUp() {
        ProductoService productoService = new ProductoService(new InMemoryProductoRepository());
        productoController = new ProductoController(productoService);
    }

    @Test
    @DisplayName("Unitaria 1 - crear() delega en el service y devuelve el producto persistido")
    void deberiaCrearProductoDesdeElRequest() {
        ProductoRequest request = new ProductoRequest("Teclado", "Teclado mecánico",
                new BigDecimal("120000"), 5, EstadoProducto.ACTIVO);

        Producto creado = productoController.crear(request);

        assertNotNull(creado.getId());
        assertEquals("Teclado", creado.getNombre());
        assertEquals("Teclado mecánico", creado.getDescripcion());
        assertEquals(0, new BigDecimal("120000").compareTo(creado.getPrecio()));
        assertEquals(5, creado.getStock());
        assertEquals(EstadoProducto.ACTIVO, creado.getEstado());
    }

    @Test
    @DisplayName("Unitaria 2 - listar() devuelve todos los productos creados")
    void deberiaListarTodosLosProductosCreados() {
        productoController.crear(new ProductoRequest("Teclado", "Teclado mecánico",
                new BigDecimal("120000"), 5, EstadoProducto.ACTIVO));
        productoController.crear(new ProductoRequest("Mouse", "Mouse inalámbrico",
                new BigDecimal("50000"), 10, EstadoProducto.ACTIVO));

        List<Producto> productos = productoController.listar();

        assertEquals(2, productos.size());
        assertTrue(productos.stream().anyMatch(p -> p.getNombre().equals("Teclado")));
        assertTrue(productos.stream().anyMatch(p -> p.getNombre().equals("Mouse")));
    }

    @Test
    @DisplayName("Unitaria 3 - obtener() devuelve el producto por id")
    void deberiaObtenerProductoPorId() {
        Producto creado = productoController.crear(new ProductoRequest("Teclado", "Teclado mecánico",
                new BigDecimal("120000"), 5, EstadoProducto.ACTIVO));

        Producto obtenido = productoController.obtener(creado.getId());

        assertEquals(creado.getId(), obtenido.getId());
        assertEquals("Teclado", obtenido.getNombre());
    }

    @Test
    @DisplayName("Unitaria 4 - obtener() propaga el error cuando el producto no existe")
    void deberiaLanzarErrorAlObtenerProductoInexistente() {
        assertThrows(ReglaNegocioException.class, () -> productoController.obtener(999L));
    }

    @Test
    @DisplayName("Unitaria 5 - actualizar() delega en el service y devuelve el producto modificado")
    void deberiaActualizarProductoDesdeElRequest() {
        Producto creado = productoController.crear(new ProductoRequest("Teclado", "Teclado mecánico",
                new BigDecimal("120000"), 5, EstadoProducto.ACTIVO));

        ProductoRequest requestActualizacion = new ProductoRequest("Teclado Pro", "Teclado mecánico RGB",
                new BigDecimal("150000"), 8, EstadoProducto.INACTIVO);
        Producto actualizado = productoController.actualizar(creado.getId(), requestActualizacion);

        assertEquals(creado.getId(), actualizado.getId());
        assertEquals("Teclado Pro", actualizado.getNombre());
        assertEquals("Teclado mecánico RGB", actualizado.getDescripcion());
        assertEquals(0, new BigDecimal("150000").compareTo(actualizado.getPrecio()));
        assertEquals(8, actualizado.getStock());
        assertEquals(EstadoProducto.INACTIVO, actualizado.getEstado());
    }

    @Test
    @DisplayName("Unitaria 6 - el ProductoRequest expone los valores con los que se construye")
    void deberiaExponerLosAccessorsDelProductoRequest() {
        BigDecimal precio = new BigDecimal("99000");
        ProductoRequest request = new ProductoRequest("Monitor", "Monitor 24 pulgadas", precio, 3, EstadoProducto.ACTIVO);

        assertEquals("Monitor", request.nombre());
        assertEquals("Monitor 24 pulgadas", request.descripcion());
        assertEquals(precio, request.precio());
        assertEquals(3, request.stock());
        assertEquals(EstadoProducto.ACTIVO, request.estado());
    }
}
