package com.bravatta.pagos.repository;
import com.bravatta.pagos.model.Pagos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

;

@Repository
public interface PagosRepository extends JpaRepository<Pagos, Long> {
    
    boolean existsByIdTransaccionExterna(String idTransaccionExterna);

    Optional<Pagos> findByIdTransaccionExterna(String idTransaccionExterna);
    
}