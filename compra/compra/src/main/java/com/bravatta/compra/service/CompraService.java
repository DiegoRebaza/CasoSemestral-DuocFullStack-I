package com.bravatta.compra.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bravatta.compra.model.Compra;
import com.bravatta.compra.repository.CompraRepository;

@Service
public class CompraService {

    private static final Logger logger = LoggerFactory.getLogger(CompraService.class);

    @Autowired
    private CompraRepository compraRepository;

    public List<Compra> obtenerTodas() {
        logger.info("Obteniendo todas las compras");
        return compraRepository.findAll();
    }

    public Compra obtenerPorId(Long id) {
        logger.info("Buscando compra con ID: {}", id);
        return compraRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Compra no encontrada con ID: {}", id);
                return new RuntimeException("Compra no encontrada con ID: " + id);
            });
    }

    public Compra crearCompra(Compra compra) {
        logger.info("Creando nueva compra para producto: {}", compra.getProductoId());
        Compra nueva = compraRepository.save(compra);
        logger.info("Compra creada exitosamente con ID: {}", nueva.getId());
        return nueva;
    }

    public Compra actualizarCompra(Long id, Compra compraActualizada) {
        logger.info("Actualizando compra con ID: {}", id);
        Compra compraExistente = obtenerPorId(id);
        compraExistente.setProductoId(compraActualizada.getProductoId());
        compraExistente.setCantidad(compraActualizada.getCantidad());
        compraExistente.setTotal(compraActualizada.getTotal());
        logger.info("Compra actualizada exitosamente");
        return compraRepository.save(compraExistente);
    }

    public void eliminarCompra(Long id) {
        logger.info("Eliminando compra con ID: {}", id);
        Compra compra = obtenerPorId(id);
        compraRepository.delete(compra);
        logger.info("Compra eliminada exitosamente");
    }
}