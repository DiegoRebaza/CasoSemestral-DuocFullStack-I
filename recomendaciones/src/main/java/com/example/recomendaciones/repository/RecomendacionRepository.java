package com.example.recomendaciones.repository;

import com.example.recomendaciones.model.Recomendacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecomendacionRepository extends JpaRepository<Recomendacion, Long> {
    List<Recomendacion> findByIdCliente(Long idCliente);
}