package com.bravatta.producto.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bravatta.producto.dto.ProductoDTO;
import com.bravatta.producto.service.ProductoService;

import java.util.List;

@RestController
@RequestMapping("api/producto")
public class ProductoController {

    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Crear = POST
    @PostMapping
    public ResponseEntity<ProductoDTO> crearProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        log.info("POST api/producto - Recibida solicitud para registrar nuevo producto");
        
        ProductoDTO resultado = productoService.guardar(productoDTO);
        
        log.info("Producto registrado exitosamente. Retornando 201 CREATED");
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    // Listar = GET
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarProductos() {
        log.info("GET api/producto - Recibida solicitud para listar todos los productos");
        
        List<ProductoDTO> listaProductos = productoService.listar();
        
        log.info("Retornando lista con {} productos", listaProductos.size());
        return ResponseEntity.ok(listaProductos);
    }

    // Otras listas
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProducto(@PathVariable Long id) {
        log.info("GET api/producto/{} - Buscando producto por ID", id);
        
        ProductoDTO producto = productoService.obtenerPorId(id);
        
        log.info("Producto con ID {} retornado exitosamente", id);
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existeProducto(@PathVariable Long id) {
        log.info("GET api/producto/{}/exists - Comprobando existencia del producto", id);
        return ResponseEntity.ok(productoService.existePorId(id));
    }

    // Actualizar = PUT
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoDTO productoDTO) {
        log.info("PUT api/producto/{} - Recibida solicitud de actualización de producto", id);
        
        ProductoDTO actualizado = productoService.actualizar(id, productoDTO);
        
        log.info("Producto con ID {} actualizado exitosamente. Retornando 200 OK", id);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar = DELETE 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        log.info("DELETE api/producto/{} - Recibida solicitud para eliminar producto", id);
        
        productoService.eliminar(id);
        
        log.info("Producto con ID {} eliminado exitosamente. Retornando 204 NO CONTENT", id);
        return ResponseEntity.noContent().build();
    }
}