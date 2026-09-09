package com.sistemaventas.producto;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryProductoRepository implements ProductoRepository {

    private final Map<Long, Producto> productos = new ConcurrentHashMap<>();
    private final AtomicLong secuenciaId = new AtomicLong(0);

    @Override
    public Producto guardar(Producto producto) {
        if (producto.getId() == null) {
            producto.setId(secuenciaId.incrementAndGet());
        }
        productos.put(producto.getId(), producto);
        return producto;
    }

    @Override
    public List<Producto> listarTodos() {
        return List.copyOf(productos.values());
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        return Optional.ofNullable(productos.get(id));
    }
}
