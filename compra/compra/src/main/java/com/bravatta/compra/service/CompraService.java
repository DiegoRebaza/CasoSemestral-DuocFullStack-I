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

    private static final Logger log = LoggerFactory.getLogger(CompraService.class);

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

    
    @Transactional
    public CompraDTO guardar(CompraDTO dto) {
        log.info("Iniciando proceso de guardado de compra: idCliente={}", dto.getId_cliente());

        Boolean existeCliente;

        try {
            log.debug("Validando existencia de cliente id={}", dto.getId_cliente());
            existeCliente = webClient.get()
                    .uri(String.format(clientePath, dto.getId_cliente()))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            log.debug("Respuesta existencia cliente: {}", existeCliente);
        } catch (Exception e) {
            log.error("Error al validar cliente id={}", dto.getId_cliente(), e);
            throw new BadRequestException("Error al validar cliente");
        }

        if (existeCliente == null) {
            log.warn("Respuesta nula al validar cliente id={}", dto.getId_cliente());
            throw new BadRequestException("No se pudo validar la existencia del cliente");
        }

        if (Boolean.FALSE.equals(existeCliente)) {
            log.warn("Cliente no existe id={}", dto.getId_cliente());
            throw new ResourceNotFoundException("Cliente no existe");
        }

        Compra compra = dto.toModel();
        double totalCompra = 0.0;

        for (DetalleCompra detalle : compra.getDetalles()) {
            Boolean existeProducto;
            
            try {
                log.debug("Validando existencia de producto id={}", detalle.getProductoId());
                existeProducto = webClient.get()
                        .uri(String.format(productoPath, detalle.getProductoId()))
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .block();
                log.debug("Respuesta existencia producto: {}", existeProducto);
            } catch (Exception e) {
                log.error("Error al validar producto id={}", detalle.getProductoId(), e);
                throw new BadRequestException("Error al validar producto");
            }

            if (existeProducto == null) {
                log.warn("Respuesta nula al validar producto id={}", detalle.getProductoId());
                throw new BadRequestException("No se pudo validar la existencia del producto");
            }

            if (Boolean.FALSE.equals(existeProducto)) {
                log.warn("Producto no existe id={}", detalle.getProductoId());
                throw new ResourceNotFoundException("Producto no existe");
            }

            detalle.setPrecioUnitario(1000.0); // Precio simulado según requerimiento
            double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
            detalle.setSubtotal(subtotal);
            totalCompra += subtotal;
        }

        compra.setTotal(totalCompra);
        Compra compraGuardada = compraRepository.save(compra);

        log.info("Compra guardada exitosamente con id={}", compraGuardada.getIdCompra());
        return CompraDTO.fromModel(compraGuardada);
    }

    public boolean existePorId(Long id) {
        log.info("Verificando existencia de la compra con ID: {}", id);
        return compraRepository.existsById(id);
    }

    public CompraDTO obtenerPorId(Long id) {
        log.info("Buscando compra con ID: {}", id);
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Compra no encontrada, ID: {}", id);
                    return new ResourceNotFoundException("Compra con ID: [" + id + "] no encontrada");
                });
        return CompraDTO.fromModel(compra);
    }

    @Transactional
    public CompraDTO actualizar(Long id, CompraDTO dto) {
        log.info("Iniciando actualización de la compra de ID: {}", id);

        Compra compraExistente = compraRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Compra no encontrada para actualización, ID: {}", id);
                    return new ResourceNotFoundException("Compra no encontrada con ID: " + id);
                });

        Boolean existeCliente;
        try {
            log.debug("Validando existencia de cliente id={}", dto.getId_cliente());
            existeCliente = webClient.get()
                    .uri(String.format(clientePath, dto.getId_cliente()))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al validar cliente id={}", dto.getId_cliente(), e);
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
            
            detalle.setPrecioUnitario(1000.0);
            double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
            detalle.setSubtotal(subtotal);
            
            compraExistente.getDetalles().add(detalle);
            totalCompra += subtotal;
        }

        compraExistente.setTotal(totalCompra);
        Compra nuevaCompra = compraRepository.save(compraExistente);
        
        log.info("Compra con ID {} actualizada exitosamente.", nuevaCompra.getIdCompra());
        return CompraDTO.fromModel(nuevaCompra);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Iniciando eliminación de compra con ID: {}", id);
        if (!compraRepository.existsById(id)) {
            log.warn("Intento de eliminar compra inexistente, ID: {}", id);
            throw new ResourceNotFoundException("Compra no encontrada con ID: " + id);
        }
        compraRepository.deleteById(id);
        log.info("Compra ID {} eliminada exitosamente.", id);
    }

    public List<CompraDTO> listar() {
        log.info("Listando todas las compras");
        List<Compra> compras = compraRepository.findAll();
        log.debug("Cantidad de compras encontradas: {}", compras.size());
        return compras.stream()
                .map(CompraDTO::fromModel)
                .collect(Collectors.toList());
    }

    // ENDPOINTS EXTRA

    public List<CompraDTO> buscarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        log.info("Buscando compras realizadas entre {} y {}", inicio, fin);
        return compraRepository.findByFechaCompraBetween(inicio, fin).stream()
                .map(CompraDTO::fromModel)
                .collect(Collectors.toList());
    }

    public Double obtenerTotalVentas() {
        log.info("Calculando el total acumulado de ventas del sistema...");
        Double total = compraRepository.obtenerTotalVentasAcumuladas();
        return (total != null) ? total : 0.0; 
    }
}