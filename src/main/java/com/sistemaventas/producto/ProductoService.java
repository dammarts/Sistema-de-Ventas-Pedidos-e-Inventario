package com.sistemaventas.producto;

import com.sistemaventas.shared.ReglaNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Producto crear(String nombre, String descripcion, BigDecimal precio, int stock) {
        validarPrecio(precio);
        Producto producto = new Producto(null, nombre, descripcion, precio, stock, EstadoProducto.ACTIVO);
        return productoRepository.guardar(producto);
    }

    public Producto actualizar(Long id, String nombre, String descripcion, BigDecimal precio, int stock, EstadoProducto estado) {
        validarPrecio(precio);
        Producto producto = obtenerPorId(id);
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setEstado(estado);
        return productoRepository.guardar(producto);
    }

    public List<Producto> listarTodos() {
        return productoRepository.listarTodos();
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.buscarPorId(id)
                .orElseThrow(() -> new ReglaNegocioException(
                        "PRODUCTO_NO_ENCONTRADO",
                        "No existe un producto con id " + id,
                        HttpStatus.NOT_FOUND));
    }

    private void validarPrecio(BigDecimal precio) {
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException(
                    "PRECIO_INVALIDO",
                    "El precio del producto debe ser mayor que cero",
                    HttpStatus.BAD_REQUEST);
        }
    }
}
