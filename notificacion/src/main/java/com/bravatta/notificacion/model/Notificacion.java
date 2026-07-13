package com.bravatta.notificacion.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Long idNotificacion;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    @Column(name = "evento", nullable = false, length = 50)
    private String evento;

    @Column(name = "mensaje", nullable = false, length = 500)
    private String mensaje;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @PrePersist
    protected void onCreate() {
        this.fechaEnvio = LocalDateTime.now();
        if (this.estado == null) this.estado = "ENVIADO";
        if (this.tipo == null) this.tipo = "SMS";
    }
}