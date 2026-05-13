package com.bravatta.compra.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "compras")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productoId;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double total;

    private LocalDateTime fechaCompra;

    @PrePersist
    protected void onCreate(){
        this.fechaCompra = LocalDateTime.now();
    }
    

}
