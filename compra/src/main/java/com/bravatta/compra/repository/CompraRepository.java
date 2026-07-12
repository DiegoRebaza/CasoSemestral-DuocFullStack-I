package com.bravatta.compra.repository;

import com.bravatta.compra.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    
    List<Compra> findByFechaCompraBetween(LocalDateTime inicio, LocalDateTime fin);
    
    List<Compra> findByTotalGreaterThanEqual(Double monto);

    List<Compra> findByIdCliente(Long idCliente);

    List<Compra> findByEstado(String estado);

    @Query("SELECT COALESCE(SUM(c.total), 0.0) FROM Compra c")
    Double obtenerTotalVentasAcumuladas();
}