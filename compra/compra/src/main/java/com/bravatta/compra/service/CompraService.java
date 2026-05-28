package com.bravatta.compra.service;

import com.bravatta.compra.dto.CompraRequestDTO;
import com.bravatta.compra.dto.DetalleCompraDTO;
import com.bravatta.compra.model.Compra;
import com.bravatta.compra.model.DetalleCompra;
import com.bravatta.compra.repository.CompraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompraService {

    private static final Logger logger = LoggerFactory.getLogger(CompraService.class);

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String PRODUCTO_SERVICE_URL = "http://localhost:8082/api/productos/";
    private final String INVENTARIO_SERVICE_URL = "http://localhost:8083/api/inventario/";

    public List<Compra> obtenerTodas() {
        return compraRepository.findAll();
    }

    public Compra obtenerPorId(Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con ID: " + id));
    }

    @Transactional
    public Compra crearCompra(CompraRequestDTO compraDTO) {
        logger.info("Iniciando creación de nueva compra para el cliente ID: {}", compraDTO.getClienteId());
        
        Compra compra = new Compra();
        compra.setClienteId(compraDTO.getClienteId());
        
        List<DetalleCompra> detalles = new ArrayList<>();
        double totalCompra = 0.0;

        for (DetalleCompraDTO detalleDTO : compraDTO.getDetalles()) {
            Long productoId = detalleDTO.getProductoId();
            Integer cantidadSolicitada = detalleDTO.getCantidad();

            try {
                Object productoJson = restTemplate.getForObject(PRODUCTO_SERVICE_URL + productoId, Object.class);
                if (productoJson == null) throw new RuntimeException("El producto ID " + productoId + " no existe.");
            } catch (Exception e) {
                throw new RuntimeException("El producto ID " + productoId + " no es válido o el servicio falló.");
            }

            try {
                Integer stockDisponible = restTemplate.getForObject(INVENTARIO_SERVICE_URL + "stock/" + productoId, Integer.class);
                if (stockDisponible == null || stockDisponible < cantidadSolicitada) {
                    throw new RuntimeException("Stock insuficiente para el producto ID " + productoId);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error de comunicación con el inventario o Stock insuficiente.");
            }

            DetalleCompra detalle = new DetalleCompra();
            detalle.setProductoId(productoId);
            detalle.setCantidad(cantidadSolicitada);
            detalle.setPrecioUnitario(1000.0); // Simulado
            detalle.setCompra(compra);
            
            double subtotal = cantidadSolicitada * detalle.getPrecioUnitario();
            detalle.setSubtotal(subtotal);
            
            detalles.add(detalle);
            totalCompra += subtotal;
        }

        compra.setDetalles(detalles);
        compra.setTotal(totalCompra);

        return compraRepository.save(compra);
    }

    @Transactional
    public Compra actualizarCompra(Long id, CompraRequestDTO compraDTO) {
        logger.info("Actualizando compra con ID: {}", id);
        Compra compraExistente = obtenerPorId(id);

        compraExistente.setClienteId(compraDTO.getClienteId());
        compraExistente.getDetalles().clear(); // Limpiamos los anteriores para reconstruir

        double totalCompra = 0.0;
        for (DetalleCompraDTO detalleDTO : compraDTO.getDetalles()) {
            DetalleCompra detalle = new DetalleCompra();
            detalle.setProductoId(detalleDTO.getProductoId());
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(1000.0); // Simulado
            detalle.setCompra(compraExistente);
            
            double subtotal = detalleDTO.getCantidad() * detalle.getPrecioUnitario();
            detalle.setSubtotal(subtotal);
            
            compraExistente.getDetalles().add(detalle);
            totalCompra += subtotal;
        }

        compraExistente.setTotal(totalCompra);
        logger.info("Compra actualizada exitosamente");
        return compraRepository.save(compraExistente);
    }

    public void eliminarCompra(Long id) {
        logger.info("Eliminando compra con ID: {}", id);
        Compra compra = obtenerPorId(id);
        compraRepository.delete(compra);
        logger.info("Compra eliminada exitosamente");
    }

    public List<Compra> buscarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return compraRepository.findByFechaCompraBetween(inicio, fin);
    }

    public List<Compra> buscarPorMontoMinimo(Double monto) {
        return compraRepository.findByTotalGreaterThanEqual(monto);
    }
}