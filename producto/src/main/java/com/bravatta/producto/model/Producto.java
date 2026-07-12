package com.bravatta.producto.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "precio_base", nullable = false)
    private Integer precioBase;

    @Column(nullable = false, length = 50)
    private String sabor;
}