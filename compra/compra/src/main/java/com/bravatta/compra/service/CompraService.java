Tienes toda la razón, me desvié agregando la lógica del inventario y los DTOs externos cuando me pediste explícitamente seguir el formato de tu ejemplo. Una disculpa por eso.

Aquí tienes la versión final de tu `CompraService`. Está adaptada **exactamente** al patrón de tu código de ejemplo: utilizando `WebClient` inyectado directamente, leyendo las rutas desde el `application.properties` con `@Value`, validando con `.bodyToMono(Boolean.class)` y limitándose **solo a Cliente y Producto**.

Como tu modelo tiene una lista de detalles (a diferencia del ejemplo que tenía un solo `idProducto`), la validación del producto se hace iterando sobre esa lista, manteniendo el precio simulado original.

### **`CompraService.java`**

```java
package com.bravatta.compra.service;

import com.bravatta.compra.dto.CompraDTO;
import com.bravatta.compra.dto.DetalleCompraDTO;
import com.bravatta.compra.exception.BadRequestException;
import com.bravatta.compra.exception.ResourceNotFoundException;
import com.bravatta.compra.model.Compra;
import com.bravatta.compra.model.DetalleCompra;
import com.bravatta.compra.repository.CompraRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompraService {

    private static final Logger logger = LoggerFactory.getLogger(CompraService.class);

    private final CompraRepository compraRepository;
    private final WebClient webClient;

    @Value("${api.cliente.exists}")
    private String clientePath;

    @Value("${api.producto.exists}")
    private String productoPath;

    public CompraService(CompraRepository compraRepository, WebClient webClient) {
        this.compraRepository = compraRepository;
        this.webClient = webClient;
    }

    // ==========================================
    // MÉTODOS CRUD PRINCIPALES
    // ==========================================

    @Transactional
    public CompraDTO guardar(CompraDTO dto) {

        logger.info("Iniciando proceso de guardado de compra: idCliente={}", dto.getId_cliente());

        Boolean existeCliente;

        // 1. Validar existencia del Cliente
        try {
            logger.debug("Validando existencia de cliente id={}", dto.getId_cliente());

            existeCliente = webClient.get()
                    .uri(String.format(clientePath, dto.getId_cliente()))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            logger.debug("Respuesta existencia cliente: {}", existeCliente);

        } catch (Exception e) {
            logger.error("Error al validar cliente id={}", dto.getId_cliente(), e);
            throw new BadRequestException("Error al validar cliente");
        }

        if (existeCliente == null) {
            logger.warn("Respuesta nula al validar cliente id={}", dto.getId_cliente());
            throw new BadRequestException("No se pudo validar la existencia del cliente");
        }

        if (Boolean.FALSE.equals(existeCliente)) {
            logger.warn("Cliente no existe id={}", dto.getId_cliente());
            throw new ResourceNotFoundException("Cliente no existe");
        }

        Compra compra = dto.toModel();
        double totalCompra = 0.0;

        // 2. Validar existencia de cada Producto en los detalles
        for (DetalleCompra detalle : compra.getDetalles()) {
            Boolean existeProducto;

            try {
                logger.debug("Validando existencia de producto id={}", detalle.getProductoId());

                existeProducto = webClient.get()
                        .uri(String.format(productoPath, detalle.getProductoId()))
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .block();

                logger.debug("Respuesta existencia producto: {}", existeProducto);

            } catch (Exception e) {
                logger.error("Error al validar producto id={}", detalle.getProductoId(), e);
                throw new BadRequestException("Error al validar producto");
            }

            if (existeProducto == null) {
                logger.warn("Respuesta nula al validar producto id={}", detalle.getProductoId());
                throw new BadRequestException("No se pudo validar la existencia del producto");
            }

            if (Boolean.FALSE.equals(existeProducto)) {
                logger.warn("Producto no existe id={}", detalle.getProductoId());
                throw new ResourceNotFoundException("Producto no existe");
            }

            // Cálculos
            detalle.setPrecioUnitario(1000.0); // Simulado
            double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
            detalle.setSubtotal(subtotal);
            totalCompra += subtotal;
        }

        compra.setTotal(totalCompra);
        Compra compraGuardada = compraRepository.save(compra);

        logger.info("Compra guardada exitosamente con id={}", compraGuardada.getIdCompra());

        return CompraDTO.fromModel(compraGuardada);
    }

    public boolean existePorId(Long id) {
        logger.info("Verificando existencia de la compra con ID: {}", id);
        return compraRepository.existsById(id);
    }

    public CompraDTO obtenerPorId(Long id) {
        logger.info("Buscando compra con ID: {}", id);
        
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Compra no encontrada, ID: {}", id);
                    return new ResourceNotFoundException("Compra con ID: [" + id + "] no encontrada");
                });
                
        return CompraDTO.fromModel(compra);
    }

    @Transactional
    public CompraDTO actualizar(Long id, CompraDTO dto) {
        logger.info("Iniciando actualización de la compra de ID: {}", id);

        Compra compraExistente = compraRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Compra no encontrada para actualización, ID: {}", id);
                    return new ResourceNotFoundException("Compra no encontrada con ID: " + id);
                });

        // Validar el nuevo cliente
        Boolean existeCliente;
        try {
            logger.debug("Validando existencia de cliente id={}", dto.getId_cliente());
            existeCliente = webClient.get()
                    .uri(String.format(clientePath, dto.getId_cliente()))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (Exception e) {
            logger.error("Error al validar cliente id={}", dto.getId_cliente(), e);
            throw new BadRequestException("Error al validar cliente");
        }

        if (Boolean.FALSE.equals(existeCliente)) {
            throw new ResourceNotFoundException("Cliente no existe");
        }

        compraExistente.setIdCliente(dto.getId_cliente());
        compraExistente.getDetalles().clear(); 

        double totalCompra = 0.0;
        
        for (DetalleCompraDTO detalleDTO : dto.getDetalles()) {
            DetalleCompra detalle = detalleDTO.toModel(compraExistente);
            
            // Validar el producto
            Boolean existeProducto;
            try {
                existeProducto = webClient.get()
                        .uri(String.format(productoPath, detalle.getProductoId()))
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .block();
            } catch (Exception e) {
                throw new BadRequestException("Error al validar producto");
            }

            if (Boolean.FALSE.equals(existeProducto)) {
                throw new ResourceNotFoundException("Producto no existe id=" + detalle.getProductoId());
            }
            
            detalle.setPrecioUnitario(1000.0); // Simulado
            double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
            detalle.setSubtotal(subtotal);
            
            compraExistente.getDetalles().add(detalle);
            totalCompra += subtotal;
        }

        compraExistente.setTotal(totalCompra);
        Compra nuevaCompra = compraRepository.save(compraExistente);
        
        logger.info("Compra con ID {} actualizada exitosamente.", nuevaCompra.getIdCompra());
        
        return CompraDTO.fromModel(nuevaCompra);
    }

    @Transactional
    public void eliminar(Long id) {
        logger.info("Iniciando eliminación de compra con ID: {}", id);

        if (!compraRepository.existsById(id)) {
            logger.warn("Intento de eliminar compra inexistente, ID: {}", id);
            throw new ResourceNotFoundException("Compra no encontrada con ID: " + id);
        }

        compraRepository.deleteById(id);
        logger.info("Compra ID {} eliminada exitosamente.", id);
    }

    public List<CompraDTO> listar() {
        logger.info("Listando todas las compras");
        List<Compra> compras = compraRepository.findAll();
        logger.debug("Cantidad de compras encontradas: {}", compras.size());

        return compras.stream()
                .map(CompraDTO::fromModel)
                .collect(Collectors.toList());
    }

    // ENDPOINTS EXTRA

    public List<CompraDTO> buscarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        logger.info("Buscando compras realizadas entre {} y {}", inicio, fin);
        return compraRepository.findByFechaCompraBetween(inicio, fin).stream()
                .map(CompraDTO::fromModel)
                .collect(Collectors.toList());
    }

    public Double obtenerTotalVentas() {
        logger.info("Calculando el total acumulado de ventas del sistema...");
        Double total = compraRepository.obtenerTotalVentasAcumuladas();
        return (total != null) ? total : 0.0; 
    }

}

```