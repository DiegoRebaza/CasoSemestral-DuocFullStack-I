package com.bravatta.compra.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "compras")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Long idCompra;

    @Column(name = "fecha_compra")
    private LocalDateTime fechaCompra;

    @Column(nullable = false)
    private Double total;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @Column(nullable = false, length = 50)
    private String estado;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleCompra> detalles;

    @PrePersist
    protected void onCreate() {
        this.fechaCompra = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = "PENDIENTE";
        }
    }
}