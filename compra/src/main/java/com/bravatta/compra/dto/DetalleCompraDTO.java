package com.bravatta.compra.dto;

import com.bravatta.compra.model.Compra;
import com.bravatta.compra.model.DetalleCompra;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleCompraDTO {

    private Long id_detalle_compra;

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @Min(value = 0, message = "El precio unitario no puede ser negativo")
    private Double precioUnitario;

    @NotNull(message = "El subtotal es obligatorio")
    @Min(value = 0, message = "El subtotal no puede ser negativo")
    private Double subtotal;

    public DetalleCompra toModel(Compra compra) {
        return DetalleCompra.builder()
                .idDetalleCompra(this.id_detalle_compra)
                .productoId(this.productoId)
                .cantidad(this.cantidad)
                .precioUnitario(this.precioUnitario)
                .subtotal(this.subtotal)
                .compra(compra) 
                .build();
    }

    public static DetalleCompraDTO fromModel(DetalleCompra d) {
        if (d == null) return null;
        return DetalleCompraDTO.builder()
                .id_detalle_compra(d.getIdDetalleCompra())
                .productoId(d.getProductoId())
                .cantidad(d.getCantidad())
                .precioUnitario(d.getPrecioUnitario())
                .subtotal(d.getSubtotal())
                .build();
    }
}