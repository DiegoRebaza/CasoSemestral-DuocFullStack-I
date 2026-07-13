package com.bravatta.producto.controller;

import com.bravatta.producto.assembler.ProductoModelAssembler;
import com.bravatta.producto.dto.ProductoDTO;
import com.bravatta.producto.service.ProductoService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("api/producto/v2")
public class ProductoControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ProductoControllerV2.class);

    private final ProductoService productoService;
    private final ProductoModelAssembler assembler;

    public ProductoControllerV2(ProductoService productoService, ProductoModelAssembler assembler) {
        this.productoService = productoService;
        this.assembler = assembler;
    }

    // Crear = POST
    @PostMapping
    public ResponseEntity<EntityModel<ProductoDTO>> crearProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        log.info("POST api/producto/v2 - Recibida solicitud para registrar nuevo producto");
        ProductoDTO resultado = productoService.guardar(productoDTO);
        log.info("Producto registrado exitosamente. Retornando 201 CREATED");
        return new ResponseEntity<>(assembler.toModel(resultado), HttpStatus.CREATED);
    }

    // Listar = GET
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ProductoDTO>>> listarProductos() {
        log.info("GET api/producto/v2 - Recibida solicitud para listar todos los productos");
        List<ProductoDTO> listaProductos = productoService.listar();

        List<EntityModel<ProductoDTO>> productosModel = listaProductos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ProductoDTO>> resultado = CollectionModel.of(productosModel,
                linkTo(methodOn(ProductoControllerV2.class).listarProductos()).withSelfRel());

        log.info("Retornando lista con {} productos", listaProductos.size());
        return ResponseEntity.ok(resultado);
    }

    // Obtener por id = GET
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProductoDTO>> obtenerProducto(@PathVariable Long id) {
        log.info("GET api/producto/v2/{} - Buscando producto por ID", id);
        ProductoDTO producto = productoService.obtenerPorId(id);
        log.info("Producto con ID {} retornado exitosamente", id);
        return ResponseEntity.ok(assembler.toModel(producto));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existeProducto(@PathVariable Long id) {
        log.info("GET api/producto/v2/{}/exists - Comprobando existencia del producto", id);
        return ResponseEntity.ok(productoService.existePorId(id));
    }

    // Actualizar = PUT
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProductoDTO>> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoDTO productoDTO) {
        log.info("PUT api/producto/v2/{} - Recibida solicitud de actualización de producto", id);
        ProductoDTO actualizado = productoService.actualizar(id, productoDTO);
        log.info("Producto con ID {} actualizado exitosamente. Retornando 200 OK", id);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    // Eliminar = DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        log.info("DELETE api/producto/v2/{} - Recibida solicitud para eliminar producto", id);
        productoService.eliminar(id);
        log.info("Producto con ID {} eliminado exitosamente. Retornando 204 NO CONTENT", id);
        return ResponseEntity.noContent().build();
    }
}