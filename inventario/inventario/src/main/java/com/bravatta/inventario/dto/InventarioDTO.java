package com.bravatta.inventario.dto;

import com.bravatta.inventario.model.Inventario;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioDTO {

    private Long inventarioId;

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @NotNull(message = "El stock disponible es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stockDisponible;

    @NotNull(message = "El stock mínimo es obligatorio")
    @Min(value = 1, message = "El stock mínimo debe ser al menos 1")
    private Integer stockMinimo;

    public Inventario toModel() {
        return Inventario.builder()
                .inventarioId(this.inventarioId)
                .productoId(this.productoId)
                .stockDisponible(this.stockDisponible)
                .stockMinimo(this.stockMinimo)
                .build();
    }

    public static InventarioDTO fromModel(Inventario inventario) {
        if (inventario == null) return null;

        return InventarioDTO.builder()
                .inventarioId(inventario.getInventarioId())
                .productoId(inventario.getProductoId())
                .stockDisponible(inventario.getStockDisponible())
                .stockMinimo(inventario.getStockMinimo())
                .build();
    }
}