package com.bravatta.inventario.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bravatta.inventario.model.Inventario;
import com.bravatta.inventario.repository.InventarioRepository;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    // OBTENER TODOS
    public List<Inventario> obtenerTodos() {
        return inventarioRepository.findAll();
    }

    // OBTENER POR ID
    public Inventario obtenerPorId(Long id) {
        return inventarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + id));
    }

    // CREAR INVENTARIO — registra el stock inicial de un producto
    public Inventario crearInventario(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    // ACTUALIZAR INVENTARIO — modificación manual del stock
    public Inventario actualizarInventario(Long id, Inventario inventarioActualizado) {
        Inventario inventarioExistente = obtenerPorId(id);

        inventarioExistente.setProductoId(inventarioActualizado.getProductoId());
        inventarioExistente.setStockDisponible(inventarioActualizado.getStockDisponible());
        inventarioExistente.setStockMinimo(inventarioActualizado.getStockMinimo());
        // ↑ solo actualizamos estos campos
        // ultimaActualizacion se actualiza sola por @PreUpdate

        return inventarioRepository.save(inventarioExistente);
    }

    // ELIMINAR INVENTARIO
    public void eliminarInventario(Long id) {
        Inventario inventario = obtenerPorId(id);
        inventarioRepository.delete(inventario);
    }

    // DESCONTAR STOCK — lo llama el microservicio de Compra automáticamente
    public Inventario descontarStock(String productoId, Integer cantidad) {
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado en inventario: " + productoId));
            // ↑ busca por productoId, no por id interno

        if (inventario.getStockDisponible() < cantidad) {
            throw new RuntimeException("Stock insuficiente para el producto: " + productoId);
            // ↑ aquí aplica tu lógica de Opción B — falla rápido si no hay stock
        }

        inventario.setStockDisponible(inventario.getStockDisponible() - cantidad);
        // ↑ resta la cantidad comprada al stock actual

        return inventarioRepository.save(inventario);
        // ↑ @PreUpdate actualiza ultimaActualizacion automáticamente
    }

    public List<Inventario> obtenerStockBajo(Integer cantidad) {
    return inventarioRepository.findByStockDisponibleLessThan(cantidad);
}
}