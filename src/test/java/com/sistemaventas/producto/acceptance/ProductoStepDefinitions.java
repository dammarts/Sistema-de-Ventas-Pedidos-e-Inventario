package com.sistemaventas.producto.acceptance;

import com.sistemaventas.producto.EstadoProducto;
import com.sistemaventas.producto.InMemoryProductoRepository;
import com.sistemaventas.producto.Producto;
import com.sistemaventas.producto.ProductoService;
import com.sistemaventas.shared.ReglaNegocioException;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Steps de aceptación para ProductoService, a nivel de servicio (sin Spring ni HTTP).
 * Cada escenario arranca con un ProductoService fresco sobre un repositorio en memoria.
 */
public class ProductoStepDefinitions {

    private ProductoService productoService;
    private Producto productoCreado;
    private ReglaNegocioException errorCapturado;

    @Dado("que no existe ningún producto registrado")
    public void que_no_existe_ningun_producto_registrado() {
        productoService = new ProductoService(new InMemoryProductoRepository());
        productoCreado = null;
        errorCapturado = null;
        assertTrue(productoService.listarTodos().isEmpty(), "El catálogo debe iniciar vacío");
    }

    @Cuando("registro un producto llamado {string} con precio {int} y stock {int}")
    public void registro_un_producto_llamado_con_precio_y_stock(String nombre, int precio, int stock) {
        productoCreado = productoService.crear(nombre, nombre, BigDecimal.valueOf(precio), stock);
    }

    @Cuando("intento registrar un producto llamado {string} con precio {int} y stock {int}")
    public void intento_registrar_un_producto_llamado_con_precio_y_stock(String nombre, int precio, int stock) {
        BigDecimal precioDecimal = BigDecimal.valueOf(precio);
        try {
            productoService.crear(nombre, nombre, precioDecimal, stock);
            fail("Se esperaba que la creación del producto fallara por precio inválido");
        } catch (ReglaNegocioException ex) {
            errorCapturado = ex;
        }
    }

    @Entonces("el producto queda registrado con estado ACTIVO")
    public void el_producto_queda_registrado_con_estado_activo() {
        assertNotNull(productoCreado, "El producto debía haberse creado");
        assertEquals(EstadoProducto.ACTIVO, productoCreado.getEstado());
    }

    @Entonces("el producto puede recuperarse por su id")
    public void el_producto_puede_recuperarse_por_su_id() {
        assertNotNull(productoCreado.getId(), "El producto creado debe tener un id asignado");
        Producto recuperado = productoService.obtenerPorId(productoCreado.getId());
        assertEquals(productoCreado.getNombre(), recuperado.getNombre());
    }

    @Entonces("la operación falla con el código de error {string}")
    public void la_operacion_falla_con_el_codigo_de_error(String codigoEsperado) {
        assertNotNull(errorCapturado, "Se esperaba una ReglaNegocioException");
        assertEquals(codigoEsperado, errorCapturado.getCodigo());
    }
}
