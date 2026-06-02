package com.bravatta.inventario.service;

import com.bravatta.inventario.dto.InventarioDTO;
import com.bravatta.inventario.exception.BadRequestException;
import com.bravatta.inventario.exception.ResourceNotFoundException;
import com.bravatta.inventario.model.Inventario;
import com.bravatta.inventario.repository.InventarioRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventarioService {

    private static final Logger log = LoggerFactory.getLogger(InventarioService.class);

    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    // CREAR
    @Transactional
    public InventarioDTO crearInventario(InventarioDTO dto) {
        log.info("Iniciando creación de inventario para productoId: {}", dto.getProductoId());

        if (inventarioRepository.findByProductoId(dto.getProductoId()).isPresent()) {
            log.warn("Ya existe inventario para productoId: {}", dto.getProductoId());
            throw new BadRequestException("Ya existe un inventario para el producto ID: "
                    + dto.getProductoId());
        }

        Inventario inventario = dto.toModel();
        Inventario inventarioGuardado = inventarioRepository.save(inventario);

        log.info("Inventario creado exitosamente con ID: {}", inventarioGuardado.getIdInventario());
        return InventarioDTO.fromModel(inventarioGuardado);
    }

    // LISTAR
    public List<InventarioDTO> listar() {
        log.info("Consultando todos los registros de inventario...");
        List<Inventario> inventarios = inventarioRepository.findAll();
        log.info("Se encontraron {} registros de inventario.", inventarios.size());
        return inventarios.stream()
                .map(InventarioDTO::fromModel)
                .collect(Collectors.toList());
    }

    // OBTENER POR ID
    public InventarioDTO obtenerPorId(Long id) {
        log.info("Buscando inventario con ID: {}", id);
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Inventario no encontrado, ID: {}", id);
                    return new ResourceNotFoundException("Inventario no encontrado con ID: " + id);
                });
        return InventarioDTO.fromModel(inventario);
    }

    // ACTUALIZAR
    @Transactional
    public InventarioDTO actualizarInventario(Long id, InventarioDTO dto) {
        log.info("Iniciando actualización del inventario con ID: {}", id);

        Inventario existente = inventarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Inventario no encontrado para actualización, ID: {}", id);
                    return new ResourceNotFoundException("Inventario no encontrado con ID: " + id);
                });

        existente.setStockDisponible(dto.getStockDisponible());
        existente.setStockMinimo(dto.getStockMinimo());

        Inventario actualizado = inventarioRepository.save(existente);
        log.info("Inventario con ID {} actualizado exitosamente.", actualizado.getIdInventario());
        return InventarioDTO.fromModel(actualizado);
    }

    // ELIMINAR
    @Transactional
    public void eliminarInventario(Long id) {
        log.info("Iniciando eliminación del inventario con ID: {}", id);

        if (!inventarioRepository.existsById(id)) {
            log.warn("Intento de eliminar inventario inexistente, ID: {}", id);
            throw new ResourceNotFoundException("Inventario no encontrado con ID: " + id);
        }

        inventarioRepository.deleteById(id);
        log.info("Inventario con ID {} eliminado exitosamente.", id);
    }

    // DESCONTAR STOCK
    @Transactional
    public InventarioDTO descontarStock(Long productoId, Integer cantidad) {
        log.info("Descontando {} unidades del stock para productoId: {}", cantidad, productoId);

        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> {
                    log.warn("No hay inventario para productoId: {}", productoId);
                    return new ResourceNotFoundException("No hay inventario para el producto ID: "
                            + productoId);
                });

        if (inventario.getStockDisponible() < cantidad) {
            log.warn("Stock insuficiente para productoId: {}. Disponible: {}, Solicitado: {}",
                    productoId, inventario.getStockDisponible(), cantidad);
            throw new BadRequestException("Stock insuficiente. Disponible: "
                    + inventario.getStockDisponible() + ", Solicitado: " + cantidad);
        }

        inventario.setStockDisponible(inventario.getStockDisponible() - cantidad);
        Inventario actualizado = inventarioRepository.save(inventario);

        log.info("Stock actualizado. Nuevo stock para productoId {}: {}",
                productoId, actualizado.getStockDisponible());
        return InventarioDTO.fromModel(actualizado);
    }

    // STOCK BAJO
    public List<InventarioDTO> obtenerStockBajo(Integer cantidad) {
        log.info("Buscando productos con stock menor a: {}", cantidad);
        List<Inventario> bajos = inventarioRepository.findByStockDisponibleLessThan(cantidad);
        log.info("Se encontraron {} productos con stock bajo.", bajos.size());
        return bajos.stream()
                .map(InventarioDTO::fromModel)
                .collect(Collectors.toList());
    }

    // VERIFICAR EXISTENCIA (para otros microservicios)
    public boolean existePorId(Long id) {
        log.info("Verificando existencia del inventario con ID: {}", id);
        return inventarioRepository.existsById(id);
    }
}