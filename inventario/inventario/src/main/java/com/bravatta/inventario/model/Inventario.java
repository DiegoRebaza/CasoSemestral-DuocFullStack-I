package com.bravatta.inventario.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productoId;     

    @Column(nullable = false)
    private Integer stockDisponible; 

    @Column(nullable = false)
    private Integer stockMinimo;     

    private LocalDateTime ultimaActualizacion;

    @PrePersist
    @PreUpdate 
    protected void onUpdate() {
        this.ultimaActualizacion = LocalDateTime.now();
    }
}