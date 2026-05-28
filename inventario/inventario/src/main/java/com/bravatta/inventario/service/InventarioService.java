package com.bravatta.inventario.service;

import com.bravatta.inventario.dto.InventarioRequestDTO;
import com.bravatta.inventario.model.Inventario;
import com.bravatta.inventario.repository.InventarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class InventarioService {

    private static final Logger logger = LoggerFactory.getLogger(InventarioService.class);

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private RestTemplate restTemplate;

  
    private final String PRODUCTO_SERVICE_URL = "http://localhost:8084/api/productos/";

    public List<Inventario> obtenerTodos() {
        logger.info("Obteniendo todos los registros de inventario");
        return inventarioRepository.findAll();
    }

    public Inventario obtenerPorId(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + id));
    }

    @Transactional
    public Inventario crearInventario(InventarioRequestDTO dto) {
        logger.info("Creando inventario para el producto ID: {}", dto.getProductoId());

        
        if (inventarioRepository.findByProductoId(dto.getProductoId()).isPresent()) {
            throw new RuntimeException("Ya existe un inventario para el producto ID: " + dto.getProductoId());
        }

        
        try {
            Object productoJson = restTemplate.getForObject(PRODUCTO_SERVICE_URL + dto.getProductoId(), Object.class);
            if (productoJson == null) {
                throw new RuntimeException("El producto no existe en el sistema.");
            }
        } catch (Exception e) {
            logger.error("Error al validar existencia del producto: {}", e.getMessage());
            throw new RuntimeException("El producto ID " + dto.getProductoId() + " no es válido o el servicio Producto no responde.");
        }

        Inventario inventario = new Inventario();
        inventario.setProductoId(dto.getProductoId());
        inventario.setStockDisponible(dto.getStockDisponible());
        inventario.setStockMinimo(dto.getStockMinimo());

        Inventario nuevo = inventarioRepository.save(inventario);
        logger.info("Inventario creado exitosamente con ID: {}", nuevo.getId());
        return nuevo;
    }

    @Transactional
    public Inventario actualizarInventario(Long id, InventarioRequestDTO dto) {
        Inventario existente = obtenerPorId(id);
        
        existente.setStockDisponible(dto.getStockDisponible());
        existente.setStockMinimo(dto.getStockMinimo());
        
        logger.info("Inventario actualizado para el ID: {}", id);
        return inventarioRepository.save(existente);
    }

    public void eliminarInventario(Long id) {
        Inventario inventario = obtenerPorId(id);
        inventarioRepository.delete(inventario);
        logger.info("Inventario eliminado correctamente, ID: {}", id);
    }

    
    @Transactional
    public Inventario descontarStock(Long productoId, Integer cantidad) {
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new RuntimeException("No hay inventario para el producto ID: " + productoId));

        if (inventario.getStockDisponible() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + inventario.getStockDisponible() + ", Pedido: " + cantidad);
        }

        inventario.setStockDisponible(inventario.getStockDisponible() - cantidad);
        logger.info("Stock descontado. Nuevo stock para producto ID {}: {}", productoId, inventario.getStockDisponible());
        return inventarioRepository.save(inventario);
    }

    
    public List<Inventario> obtenerStockBajo(Integer cantidad) {
        logger.info("Buscando productos con stock menor a: {}", cantidad);
        return inventarioRepository.findByStockDisponibleLessThan(cantidad);
    }
}