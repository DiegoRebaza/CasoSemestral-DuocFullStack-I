package com.bravatta.producto.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bravatta.producto.dto.ProductoDTO;
import com.bravatta.producto.exception.BadRequestException;
import com.bravatta.producto.exception.ResourceNotFoundException;
import com.bravatta.producto.model.Producto;
import com.bravatta.producto.repository.ProductoRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    // Creacion de objeto
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Metodos

    // GUARDADO / CREATE
    @Transactional
    public ProductoDTO guardar(ProductoDTO dto) {
        log.info("Iniciando registro de producto: {} - sabor: {}", dto.getNombre(), dto.getSabor());

        if (productoRepository.existsByNombreAndSabor(
                dto.getNombre().trim(),
                dto.getSabor().trim().toLowerCase())) {
            log.warn("Producto duplicado: {} sabor {}", dto.getNombre(), dto.getSabor());
            throw new BadRequestException("Ya existe el producto '"
                    + dto.getNombre() + "' con sabor '" + dto.getSabor() + "'");
        }

        Producto producto = dto.toModel();
        Producto saveProducto = productoRepository.save(producto);

        log.info("Producto registrado exitosamente con ID: {}", saveProducto.getIdProducto());
        return ProductoDTO.fromModel(saveProducto);
    }

    // Buscar por id
    public ProductoDTO obtenerPorId(Long id) {
        log.info("Buscando producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado, ID: {}", id);
                    return new ResourceNotFoundException("Producto no encontrado con ID: " + id);
                });

        return ProductoDTO.fromModel(producto);
    }

    // UPDATE
    @Transactional
    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        log.info("Iniciando actualización del producto con ID: {}", id);

        Producto producto1 = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado para actualización, ID: {}", id);
                    return new ResourceNotFoundException("Producto no encontrado con ID: " + id);
                });

        // Validar si existe el duplicao
        if (productoRepository.existsByNombreAndSaborAndIdProductoNot(
                dto.getNombre().trim(),
                dto.getSabor().trim().toLowerCase(),
                id)) {
            throw new BadRequestException("Ya existe otro producto '"
                    + dto.getNombre() + "' con sabor '" + dto.getSabor() + "'");
        }

        producto1.setNombre(dto.getNombre().trim());
        producto1.setPrecioBase(dto.getPrecioBase());
        producto1.setSabor(dto.getSabor().trim().toLowerCase());

        Producto producto2 = productoRepository.save(producto1);
        log.info("Producto con ID {} actualizado exitosamente.", producto2.getIdProducto());

        return ProductoDTO.fromModel(producto2);
    }

    // DELETE
    @Transactional
    public void eliminar(Long id) {
        log.info("Iniciando eliminación del producto con ID: {}", id);

        if (!productoRepository.existsById(id)) {
            log.warn("Intento de eliminar producto inexistente, ID: {}", id);
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + id);
        }

        productoRepository.deleteById(id);
        log.info("Producto con ID {} eliminado exitosamente.", id);
    }

    // Listar todo
    public List<ProductoDTO> listar() {
        log.info("Consultando todos los productos en la base de datos...");

        List<Producto> productos = productoRepository.findAll();
        log.info("Se encontraron {} productos.", productos.size());

        return productos.stream()
                .map(ProductoDTO::fromModel)
                .collect(Collectors.toList());
    }

    // VERIFICAR EXISTENCIA 
    public boolean existePorId(Long id) {
        log.info("Verificando existencia del producto con ID: {}", id);
        return productoRepository.existsById(id);
    }
}

/* Metodos base (por modificar)
    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    public boolean existePorId(Long id) {
        return clienteRepository.existsById(id);
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }
    */