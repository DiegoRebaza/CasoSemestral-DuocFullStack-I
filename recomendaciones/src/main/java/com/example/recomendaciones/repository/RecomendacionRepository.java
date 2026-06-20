package com.example.recomendaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.recomendaciones.model.Recomendacion;

@Repository
public interface RecomendacionRepository extends JpaRepository<Recomendacion, Long> {

}
