package com.bravatta.notificacion.repository;

import com.bravatta.notificacion.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByIdCliente(Long idCliente);

    List<Notificacion> findByEvento(String evento);

    List<Notificacion> findByIdClienteAndEvento(Long idCliente, String evento);
}