package com.bravatta.compra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bravatta.compra.model.Compra;
import com.bravatta.compra.repository.CompraRepository;

@Service
public class CompraService {

    @Autowired

    private CompraRepository compraRepository;

    //Obtener todas las compras

    public List<Compra> obtenerTodas() {
        return compraRepository.findAll();
    }

    //Obtener una compra por ID

    public Compra obtenerPorId(Long id) {
        return compraRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Compra no encontrada con ID: " +id));

    }

    //Crear una compra

    public Compra crearCompra(Compra compra) {
        
        //1
        //ProductoDto producto = productoCliente.obtenerPorId(compra.getProductoId());
        
        //2
        //inventarioCliente.verificarStock(compra.getProductoId(), compra.getCantidad());
        
        //3
        //compra.setTotal(producto.getPrecio() * compra.getCantidad());

        //4
        return compraRepository.save(compra);
    }

    //Actualizar una compra

    public Compra actualizarCompra(Long id, Compra compraActualizada) {
        Compra compraExistente = obtenerPorId(id);

        compraExistente.setProductoId(compraActualizada.getProductoId());
        compraExistente.setCantidad(compraActualizada.getCantidad());
        compraExistente.setTotal(compraActualizada.getTotal());

        return compraRepository.save(compraExistente);
    }

    //Eliminar una compra

    public void eliminarCompra(Long id) {
        Compra compra = obtenerPorId(id);

        compraRepository.delete(compra);
    }
}
