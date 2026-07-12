package com.bravatta.compra.controller;

import com.bravatta.compra.assembler.CompraModelAssembler;
import com.bravatta.compra.dto.CompraDTO;
import com.bravatta.compra.service.CompraService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private static final Logger log = LoggerFactory.getLogger(CompraController.class);

    private final CompraService compraService;
    private final CompraModelAssembler assembler;

    public CompraController(CompraService compraService, CompraModelAssembler assembler) {
        this.compraService = compraService;
        this.assembler = assembler;
    }

    // POST /api/compras — crear compra
    @PostMapping
    public ResponseEntity<CompraDTO> crearCompra(@Valid @RequestBody CompraDTO compraDTO) {
        log.info("POST /api/compras — creando compra para clienteId: {}", compraDTO.getId_cliente());
        CompraDTO resultado = compraService.guardar(compraDTO);
        log.info("Compra creada exitosamente con ID: {}", resultado.getId_compra());
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    // GET /api/compras — listar todas
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CompraDTO>>> listarCompras() {
        log.info("GET /api/compras — listando todas las compras");
        List<CompraDTO> compras = compraService.listar();

        List<EntityModel<CompraDTO>> comprasModel = compras.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<CompraDTO>> resultado = CollectionModel.of(comprasModel,
                linkTo(methodOn(CompraController.class).listarCompras()).withSelfRel());

        log.info("Se retornaron {} compras.", compras.size());
        return ResponseEntity.ok(resultado);
    }

    // GET /api/compras/{id} — obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CompraDTO>> obtenerCompra(@PathVariable Long id) {
        log.info("GET /api/compras/{} — buscando compra", id);
        CompraDTO compra = compraService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(compra));
    }

    // PUT /api/compras/{id} — actualizar compra
    @PutMapping("/{id}")
    public ResponseEntity<CompraDTO> actualizarCompra(
            @PathVariable Long id,
            @Valid @RequestBody CompraDTO compraDTO) {
        log.info("PUT /api/compras/{} — actualizando compra", id);
        CompraDTO resultado = compraService.actualizar(id, compraDTO);
        log.info("Compra con ID {} actualizada exitosamente.", id);
        return ResponseEntity.ok(resultado);
    }

    // DELETE /api/compras/{id} — eliminar compra
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCompra(@PathVariable Long id) {
        log.info("DELETE /api/compras/{} — eliminando compra", id);
        compraService.eliminar(id);
        log.info("Compra con ID {} eliminada exitosamente.", id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/compras/{id}/exists — verificar existencia
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existeCompra(@PathVariable Long id) {
        log.info("GET /api/compras/{}/exists — verificando existencia", id);
        return ResponseEntity.ok(compraService.existePorId(id));
    }

    // Endpoints extra

    // GET /api/compras/buscar/fechas — filtrar por rango de fechas
    @GetMapping("/buscar/fechas")
    public ResponseEntity<CollectionModel<EntityModel<CompraDTO>>> buscarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        log.info("GET /api/compras/buscar/fechas — entre {} y {}", inicio, fin);
        List<CompraDTO> compras = compraService.buscarPorFechas(inicio, fin);

        List<EntityModel<CompraDTO>> comprasModel = compras.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<CompraDTO>> resultado = CollectionModel.of(comprasModel,
                linkTo(methodOn(CompraController.class).buscarPorFechas(inicio, fin)).withSelfRel());

        return ResponseEntity.ok(resultado);
    }

    // GET /api/compras/total-ventas — total acumulado del sistema
    @GetMapping("/total-ventas")
    public ResponseEntity<Double> obtenerTotalVentas() {
        log.info("GET /api/compras/total-ventas — calculando total acumulado");
        return ResponseEntity.ok(compraService.obtenerTotalVentas());
    }
}