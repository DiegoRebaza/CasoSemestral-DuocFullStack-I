package com.bravatta.pagos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transaccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaccion")
    private Long idTransaccion;

    @Column(nullable = false, precision = 12)
    private Integer monto;

    @Column(nullable = false, length = 30)
    private String metodoPago;

    @Column(nullable = false, unique = true, length = 100)
    private String idTransaccionExterna;
   
}