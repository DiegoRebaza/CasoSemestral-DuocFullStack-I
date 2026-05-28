package com.bravatta.clientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bravatta.clientes.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByRut(String rut);

    boolean existsByCorreo(String correo);

    boolean existsByCorreoAndIdClienteNot(String correo, Long idCliente);

    boolean existsByRutAndIdClienteNot(String rut, Long idCliente);
}