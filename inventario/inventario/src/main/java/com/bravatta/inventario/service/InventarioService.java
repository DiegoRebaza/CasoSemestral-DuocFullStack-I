package com.bravatta.inventario.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.bravatta.inventario.model.Inventario;
import com.bravatta.inventario.repository.InventarioRepository;

@Service


public class InventarioService {

    private static final Logger logger = LoggerFactory.getLogger(InventarioService.class);

    @Autowired

    private InventarioRepository inventarioRepository;

    
    public List<Inventario> obtenerTodos() {

        logger.info("Obteniendo todos los registros de inventario");
        return inventarioRepository.findAll();
    }

    
    public Inventario obtenerPorId(Long id) {
        logger.info("Buscando inventario con ID: {}", id);

        return inventarioRepository.findById(id)
        .orElseThrow(() -> {
                logger.error("Inventario no encontrado con ID: {}", id);
              return new RuntimeException("Inventario no encontrado con ID: " + id);
                
            });
    }

    public Inventario crearInventario(Inventario inventario) {
        logger.info("Creando registro de inventario para producto: {}", inventario.getProductoId());

        Inventario nuevo = inventarioRepository.save(inventario);
        logger.info("Inventario creado exitosamente con ID: {}", nuevo.getId());
        return nuevo;
        
    }

    
    public Inventario actualizarInventario(Long id, Inventario inventarioActualizado) {
        logger.info("Actualizando inventario con ID: {}", id);
        Inventario inventarioExistente = obtenerPorId(id);
        inventarioExistente.setProductoId(inventarioActualizado.getProductoId());
        inventarioExistente.setStockDisponible(inventarioActualizado.getStockDisponible());
        inventarioExistente.setStockMinimo(inventarioActualizado.getStockMinimo());
        logger.info("Inventario actualizado exitosamente");
        return inventarioRepository.save(inventarioExistente);
    }

    
    public void eliminarInventario(Long id) {
        

        logger.info("Eliminando inventario con ID: {}", id);
        Inventario inventario = obtenerPorId(id);
        inventarioRepository.delete(inventario);
        logger.info("Inventario eliminado exitosamente");
    }

    
   
    public Inventario descontarStock(String productoId, Integer cantidad) {
        logger.info("Descontando {} unidades del producto: {}", cantidad, productoId);
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
            .orElseThrow(() -> {
                logger.error("Producto no encontrado en inventario: {}", productoId);
                return new RuntimeException("Producto no encontrado en inventario: " + productoId);
            });

        if (inventario.getStockDisponible() < cantidad) {
            logger.error("Stock insuficiente para producto: {}. Stock actual: {}, cantidad pedida: {}",
                productoId, inventario.getStockDisponible(), cantidad);
            throw new RuntimeException("Stock insuficiente para: " + productoId);
        }

        inventario.setStockDisponible(inventario.getStockDisponible() - cantidad);
        logger.info("Stock actualizado. Nuevo stock para {}: {}",
            productoId, inventario.getStockDisponible());

        return inventarioRepository.save(inventario);
       
    }

    
    public List<Inventario> obtenerStockBajo(Integer cantidad) {
        logger.info("Buscando productos con stock menor a: {}", cantidad);
        return inventarioRepository.findByStockDisponibleLessThan(cantidad);
        
    }
}