package com.example.posventa.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posventa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Posventa {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_posventa")
    private Long idPosventa;
 
    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;
 
    @Column(name = "id_compra", nullable = false)
    private Long idCompra;
 
    @Column(nullable = false, length = 500)
    private String motivo;
 
    @Column(nullable = false, length = 20)
    private String estado;
 
    @Column(nullable = false)
    private LocalDateTime fecha;
 
    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = "PENDIENTE";
        }
    }
}
