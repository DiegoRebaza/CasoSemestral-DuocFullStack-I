package com.bravatta.fidelizacion.repository;

import com.bravatta.fidelizacion.model.Fidelizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FidelizacionRepository extends JpaRepository<Fidelizacion, Long> {

    Optional<Fidelizacion> findByIdCliente(Long idCliente);

    boolean existsByIdCliente(Long idCliente);

    List<Fidelizacion> findByNivel(String nivel);

    List<Fidelizacion> findByCuponCumpleanosTrue();

    List<Fidelizacion> findByFechaNacimiento(LocalDate fecha);
}