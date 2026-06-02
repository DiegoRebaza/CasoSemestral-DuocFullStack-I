package com.bravatta.compra.dto;

import com.bravatta.compra.model.Compra;
import com.bravatta.compra.model.DetalleCompra;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraDTO {

    private Long id_compra;

    private LocalDateTime fechaCompra;

    @NotNull(message = "El total es obligatorio")
    @Min(value = 0, message = "El total no puede ser negativo")
    private Double total;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long id_cliente; 

    private String estado;

    @Valid
    @NotEmpty(message = "La compra debe tener al menos un detalle")
    private List<DetalleCompraDTO> detalles;

    public Compra toModel() {
        Compra c = Compra.builder()
                .idCompra(this.id_compra)
                .fechaCompra(this.fechaCompra)
                .total(this.total)
                .idCliente(this.id_cliente) 
                .estado(this.estado != null ? this.estado.trim().toUpperCase() : null)
                .build();

        if (this.detalles != null && !this.detalles.isEmpty()) {
            List<DetalleCompra> listaDetalles = this.detalles.stream()
                    .map(detalleDto -> detalleDto.toModel(c))
                    .collect(Collectors.toList());
            c.setDetalles(listaDetalles);
        }

        return c;
    }

    public static CompraDTO fromModel(Compra c) {
        if (c == null) return null;

        List<DetalleCompraDTO> detallesDTO = null;
        if (c.getDetalles() != null) {
            detallesDTO = c.getDetalles().stream()
                    .map(DetalleCompraDTO::fromModel)
                    .collect(Collectors.toList());
        }

        return CompraDTO.builder()
                .id_compra(c.getIdCompra())
                .fechaCompra(c.getFechaCompra())
                .total(c.getTotal())
                .id_cliente(c.getIdCliente())
                .estado(c.getEstado())
                .detalles(detallesDTO)
                .build();
    }
}