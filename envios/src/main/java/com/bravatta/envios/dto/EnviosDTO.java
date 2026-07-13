package com.bravatta.envios.dto;

import com.bravatta.envios.model.Envios;
import com.bravatta.envios.model.EstadoEnvio;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnviosDTO {

    private Long idEnvio;

    @NotNull(message = "El id de la compra es obligatorio")
    private Long idCompra;

    @NotNull(message = "La dirección de entrega es obligatoria")
    @Pattern(
        regexp = "^(?=.*[A-Za-zÁÉÍÓÚáéíóúÑñ])(?=.*\\d).+$",
        message = "La dirección debe incluir nombre de calle y número"
    )
    private String direccionEntrega;

    private EstadoEnvio estadoEnvio;

    private String repartidorAsignado;

    private LocalDate fechaEstimadaEntrega;

    private LocalDateTime fechaRegistro;

    public Envios toModel() {
        return Envios.builder()
                .idEnvio(this.idEnvio)
                .idCompra(this.idCompra)
                .direccionEntrega(this.direccionEntrega)
                .estadoEnvio(this.estadoEnvio)
                .repartidorAsignado(this.repartidorAsignado)
                .fechaEstimadaEntrega(this.fechaEstimadaEntrega)
                .fechaRegistro(this.fechaRegistro)
                .build();
    }

    public static EnviosDTO fromModel(Envios e) {
        if (e == null) {
            return null;
        }
        return EnviosDTO.builder()
                .idEnvio(e.getIdEnvio())
                .idCompra(e.getIdCompra())
                .direccionEntrega(e.getDireccionEntrega())
                .estadoEnvio(e.getEstadoEnvio())
                .repartidorAsignado(e.getRepartidorAsignado())
                .fechaEstimadaEntrega(e.getFechaEstimadaEntrega())
                .fechaRegistro(e.getFechaRegistro())
                .build();
    }
}