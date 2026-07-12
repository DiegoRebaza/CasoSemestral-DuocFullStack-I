package com.bravatta.producto.dto;

import com.bravatta.producto.model.Producto;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDTO {

    private Long idProducto;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotNull(message = "El precio base es obligatorio")
    @Positive(message = "El precio base debe ser mayor a cero")
    private Integer precioBase;

    @NotBlank(message = "El sabor es obligatorio")
    @Size(max = 50, message = "El sabor no puede superar los 50 caracteres")
    private String sabor;

    public Producto toModel() {
        return Producto.builder()
                .idProducto(this.idProducto)
                .nombre(this.nombre != null ? this.nombre.trim() : null)
                .precioBase(this.precioBase)
                .sabor(this.sabor != null ? this.sabor.trim().toLowerCase() : null)
                .build();
    }

    public static ProductoDTO fromModel(Producto p) {
        if (p == null) return null;
        return ProductoDTO.builder()
                .idProducto(p.getIdProducto())
                .nombre(p.getNombre())
                .precioBase(p.getPrecioBase())
                .sabor(p.getSabor())
                .build();
    }
}