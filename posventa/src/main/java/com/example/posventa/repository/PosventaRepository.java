package com.example.posventa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.posventa.model.Posventa;
import java.util.List;


@Repository
public interface PosventaRepository extends JpaRepository<Posventa, Long> {
    List<Posventa> findByIdCliente(Long idCliente);
 
    List<Posventa> findByIdCompra(Long idCompra);
 
    List<Posventa> findByEstado(String estado);
}
