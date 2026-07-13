package com.example.recomendaciones.dto;

import java.time.LocalDateTime;

import com.example.recomendaciones.model.Recomendacion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RecomendacionDTO {

    private Long id_recomendacion;

    @NotNull(message = "El id no puede ser nulo")
    private Long id_cliente;

    @NotNull(message = "El id del producto no puede ser nulo")
    private Long id_producto;

    @NotNull(message = "El id de la compra no puede ser nulo")
    private Long id_compra;

    @NotBlank
    @Size(max = 300, message = "Maximo 300 caracteres")
    private String opnion_usuario;

    @Min(value = 1, message = "La nota minima es 1")
    private double puntacion;


    private LocalDateTime fecha_calculo;

    public Recomendacion toModel() {
        return Recomendacion.builder()
        .idRecomendacion(this.id_recomendacion)
        .idCliente(this.id_cliente)
        .idProducto(this.id_producto)
        .idCompra(this.id_compra)
        .opinionUsuario(this.opnion_usuario)
        .puntuacionAfinidad(this.puntacion)
        .build();
    }

    public static RecomendacionDTO fromModel(Recomendacion c){
        if(c==null){
            return null;
        }

        return RecomendacionDTO.builder()
        .id_recomendacion(c.getIdRecomendacion())
        .id_cliente(c.getIdCliente())
        .id_compra(c.getIdCompra())
        .id_producto(c.getIdProducto())
        .opnion_usuario(c.getOpinionUsuario())
        .puntacion(c.getPuntuacionAfinidad())
        .fecha_calculo(c.getFechaCalculo())
        .build();
    }
}