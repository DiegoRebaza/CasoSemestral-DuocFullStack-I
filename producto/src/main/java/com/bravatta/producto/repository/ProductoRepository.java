package com.bravatta.producto.repository;

import com.bravatta.producto.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existsByNombreAndSabor(String nombre, String sabor);

    boolean existsByNombreAndSaborAndIdProductoNot(String nombre, String sabor, Long idProducto);
}