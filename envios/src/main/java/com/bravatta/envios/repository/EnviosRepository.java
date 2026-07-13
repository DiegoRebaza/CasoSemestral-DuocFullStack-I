package com.bravatta.envios.repository;

import com.bravatta.envios.model.Envios;
import com.bravatta.envios.model.EstadoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnviosRepository extends JpaRepository<Envios, Long> {

    List<Envios> findByIdCompra(Long idCompra);

    List<Envios> findByEstadoEnvio(EstadoEnvio estadoEnvio);

    List<Envios> findByRepartidorAsignado(String repartidorAsignado);
}