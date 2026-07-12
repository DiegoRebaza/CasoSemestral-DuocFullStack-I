package com.bravatta.inventario.repository;

import com.bravatta.inventario.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    Optional<Inventario> findByProductoId(Long productoId); // Ahora recibe Long
    List<Inventario> findByStockDisponibleLessThan(Integer cantidad);
}