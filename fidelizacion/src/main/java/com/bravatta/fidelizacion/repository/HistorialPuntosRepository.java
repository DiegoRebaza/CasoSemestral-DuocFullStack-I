package com.bravatta.fidelizacion.repository;

import com.bravatta.fidelizacion.model.HistorialPuntos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialPuntosRepository extends JpaRepository<HistorialPuntos, Long> {

    List<HistorialPuntos> findByIdFidelizacion(Long idFidelizacion);

    boolean existsByIdFidelizacionAndIdPago(Long idFidelizacion, Long idPago);
}