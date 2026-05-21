package com.bravatta.inventario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bravatta.inventario.model.Inventario;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    // Busca un producto específico por su productoId
    // Lo usa descontarStock() en el Service
    Optional<Inventario> findByProductoId(String productoId);

    // Busca todos los productos con stock bajo
    // Útil para saber qué necesita reabastecerse
    List<Inventario> findByStockDisponibleLessThan(Integer cantidad);
}