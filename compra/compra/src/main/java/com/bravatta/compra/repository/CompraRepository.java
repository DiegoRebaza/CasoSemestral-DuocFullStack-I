package com.bravatta.compra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bravatta.compra.model.Compra;

@Repository
public interface CompraRepository extends JpaRepository <Compra, Long> {

}
