package com.bravatta.delivery.repository;

import com.bravatta.delivery.model.Delivery;
import com.bravatta.delivery.model.EstadoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findByIdCompra(Long idCompra);

    List<Delivery> findByEstadoEnvio(EstadoEnvio estadoEnvio);

    List<Delivery> findByRepartidorAsignado(String repartidorAsignado);
}