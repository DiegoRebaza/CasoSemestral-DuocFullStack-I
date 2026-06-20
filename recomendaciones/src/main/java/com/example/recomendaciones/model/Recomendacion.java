package com.example.recomendaciones.model;

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
@Table(name = "recomendacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recomendacion {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_recomendacion")
    private Long idRecomendacion;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @Column(name = "id_producto", nullable = false)
    private Long idProducto;

    @Column(name = "id_Compra", nullable = false)
    private Long idCompra;

    @Column(name="opinion_usuario", nullable = false)
    private String opinionUsuario;

    @Column(name = "puntuacion_afinidad", nullable = false)
    private Double puntuacionAfinidad;

    @Column(name = "fecha_calculo", nullable = false)
    private LocalDateTime fechaCalculo;

    @PrePersist
    protected void onCreate(){
        this.fechaCalculo=LocalDateTime.now();
    }
}
