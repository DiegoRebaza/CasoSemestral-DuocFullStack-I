package com.bravatta.delivery.dto;

import com.bravatta.delivery.model.Delivery;
import com.bravatta.delivery.model.EstadoEnvio;
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
public class DeliveryDTO {

    private Long idDelivery;

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

    // DTO -> Entity
    public Delivery toModel() {
        return Delivery.builder()
                .idDelivery(this.idDelivery)
                .idCompra(this.idCompra)
                .direccionEntrega(this.direccionEntrega)
                .estadoEnvio(this.estadoEnvio)
                .repartidorAsignado(this.repartidorAsignado)
                .fechaEstimadaEntrega(this.fechaEstimadaEntrega)
                .fechaRegistro(this.fechaRegistro)
                .build();
    }

    // Entity -> DTO
    public static DeliveryDTO fromModel(Delivery d) {
        if (d == null) {
            return null;
        }
        return DeliveryDTO.builder()
                .idDelivery(d.getIdDelivery())
                .idCompra(d.getIdCompra())
                .direccionEntrega(d.getDireccionEntrega())
                .estadoEnvio(d.getEstadoEnvio())
                .repartidorAsignado(d.getRepartidorAsignado())
                .fechaEstimadaEntrega(d.getFechaEstimadaEntrega())
                .fechaRegistro(d.getFechaRegistro())
                .build();
    }
}