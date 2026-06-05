package com.bravatta.compra.service;

import com.bravatta.compra.client.ClientesClient;
import com.bravatta.compra.client.ProductoClient;
import com.bravatta.compra.dto.CompraDTO;
import com.bravatta.compra.dto.DetalleCompraDTO;
import com.bravatta.compra.exception.ResourceNotFoundException;
import com.bravatta.compra.model.Compra;
import com.bravatta.compra.model.DetalleCompra;
import com.bravatta.compra.repository.CompraRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompraService {

    private static final Logger log = LoggerFactory.getLogger(CompraService.class);

    private final CompraRepository compraRepository;
    private final ClientesClient clientesClient;
    private final ProductoClient productoClient;

    public CompraService(CompraRepository compraRepository,
                         ClientesClient clientesClient,
                         ProductoClient productoClient) {
        this.compraRepository = compraRepository;
        this.clientesClient = clientesClient;
        this.productoClient = productoClient;
    }

    @Transactional
    public CompraDTO guardar(CompraDTO dto) {
        log.info("Iniciando proceso de guardado de compra: idCliente={}", dto.getId_cliente());

        // Validar cliente
        clientesClient.validarExistencia(dto.getId_cliente());

        Compra compra = dto.toModel();
        double totalCompra = 0.0;

        // Validar cada producto y calcular total
        for (DetalleCompra detalle : compra.getDetalles()) {
            // Obtenemos el precio
            Double precioReal = productoClient.obtenerPrecio(detalle.getProductoId());

            detalle.setPrecioUnitario(precioReal); 
            double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
            detalle.setSubtotal(subtotal);
            totalCompra += subtotal;
        }

        compra.setTotal(totalCompra);
        Compra compraGuardada = compraRepository.save(compra);

        log.info("Compra guardada exitosamente con id={}", compraGuardada.getIdCompra());
        return CompraDTO.fromModel(compraGuardada);
    }

    @Transactional
    public CompraDTO actualizar(Long id, CompraDTO dto) {
        log.info("Iniciando actualización de la compra de ID: {}", id);

        Compra compraExistente = compraRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Compra no encontrada para actualización, ID: {}", id);
                    return new ResourceNotFoundException("Compra no encontrada con ID: " + id);
                });

        // Validar cliente
        clientesClient.validarExistencia(dto.getId_cliente());

        compraExistente.setIdCliente(dto.getId_cliente());
        compraExistente.getDetalles().clear();

        double totalCompra = 0.0;

        for (DetalleCompraDTO detalleDTO : dto.getDetalles()) {
            DetalleCompra detalle = detalleDTO.toModel(compraExistente);

            productoClient.validarExistencia(detalle.getProductoId());

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