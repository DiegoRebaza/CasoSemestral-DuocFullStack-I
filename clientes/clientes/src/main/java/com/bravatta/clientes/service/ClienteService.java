package com.bravatta.clientes.service;

import com.bravatta.clientes.dto.ClienteDTO;
import com.bravatta.clientes.exception.BadRequestException;
import com.bravatta.clientes.exception.ResourceNotFoundException;
import com.bravatta.clientes.model.Cliente;
import com.bravatta.clientes.model.Direccion;
import com.bravatta.clientes.repository.ClienteRepository;

import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ClienteService {
    

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);
    // Creacion de objeto
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Metodos

    // GUARDADO / CREATE
    @Transactional
    public ClienteDTO guardar(ClienteDTO dto){
        log.info("Iniciando persistencia/conexion en BD para obtener RUT del cliente: {}", dto.getRut());

        // Verificar si YA existe:
        if (clienteRepository.existsByRut(dto.getRut())) {
            log.warn("Intento de registro con RUT duplicado: {}", dto.getRut());
            throw new BadRequestException("Ya existe un cliente con el RUT: " + dto.getRut());
        }
        if (clienteRepository.existsByCorreo(dto.getCorreo())) {
            log.warn("Intento de registro con correo duplicado: {}", dto.getCorreo());
            throw new BadRequestException("Ya existe un cliente con el correo: " + dto.getCorreo());
        }
        
        // toa la tracala de weas q siguen

        Cliente cliente = dto.toModel();

        Cliente saveCliente = clienteRepository.save(cliente);

        log.info("Cliente de ID {} se guardo exitosamente.", saveCliente.getIdCliente());
        
        Direccion saveDireccion = obtenerDireccion(saveCliente);;
        return ClienteDTO.fromModel(saveCliente, saveDireccion);
    }

    // Validar existencia de cliente por ID
    public boolean existePorId(Long id) {
        log.info("Verificando existencia del cliente con ID: {}", id);
        return clienteRepository.existsById(id);
    }

    // Buscar por id
    public ClienteDTO obtenerPorId(Long id){
        log.info("Buscando cliente con ID: {}",id);
         
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cliente no encontrado, ID: {}", id);
                    return new ResourceNotFoundException("Cliente con ID: [" + id + "] no encontrado");
                });
        return ClienteDTO.fromModel(cliente, obtenerDireccion(cliente));
    }

    // UPDATE
    @Transactional
    public ClienteDTO actualizar(Long id, ClienteDTO dto) {
        log.info("Iniciando actualizacion del cliente de ID: {}", id);

        Cliente cliente1 = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cliente no encontrado para actualización, ID: {}", id);
                    return new ResourceNotFoundException("Cliente no encontrado con ID: " + id);
                });

        // Valida duplicados excluyendo el propio cliente que se edita
        if (clienteRepository.existsByRutAndIdClienteNot(dto.getRut(), id)) {
            throw new BadRequestException("Ya existe otro cliente con el RUT: " + dto.getRut());
        }
        if (clienteRepository.existsByCorreoAndIdClienteNot(dto.getCorreo(), id)) {
            throw new BadRequestException("Ya existe otro cliente con el correo: " + dto.getCorreo());
        }

        cliente1.setRut(dto.getRut().trim().toUpperCase());
        cliente1.setNombre(dto.getNombre());
        cliente1.setCorreo(dto.getCorreo().trim().toLowerCase());

        // Actualiza dirección
        if (dto.getDireccion() != null && !cliente1.getDirecciones().isEmpty()) {
            Direccion dir = cliente1.getDirecciones().get(0);
            dir.setCalle(dto.getDireccion().getCalle());
            dir.setComuna(dto.getDireccion().getComuna());
        }
        
        Cliente nuevoCliente = clienteRepository.save(cliente1);
        log.info("Cliente con ID {} actualizado exitosamente.", nuevoCliente.getIdCliente());

        return ClienteDTO.fromModel(nuevoCliente, obtenerDireccion(nuevoCliente));
    }

    // DELETE
    @Transactional
    public void eliminar(Long id) {
        log.info("Iniciando eliminación de cliente con ID: {}", id);

        if (!clienteRepository.existsById(id)) {
            log.warn("Intento de eliminar cliente inexistente, ID: {}", id);
            throw new ResourceNotFoundException("Cliente no encontrado con ID: " + id);
        }

        clienteRepository.deleteById(id);
        log.info("Cliente ID {} eliminado exitosamente.", id);
    }

    // Listar todo
    public List<ClienteDTO> listar(){
        log.info("Consultando todos los clientes en la base de datos...");
        List<Cliente> clientes = clienteRepository.findAll();

       return clientes.stream().map(cliente -> {
            // Obtener direccion
            Direccion dir = (cliente.getDirecciones() != null 
                    && !cliente.getDirecciones().isEmpty()) 
                    ?cliente.getDirecciones().get(0) 
                    :null;
            // Y aca retorna los datos junto con su direccion del cliente
            return ClienteDTO.fromModel(cliente, dir);
        }).collect(Collectors.toList());
    }

    // Auxiliar de los cojones

    private Direccion obtenerDireccion(Cliente cliente) {
        if (cliente.getDirecciones() != null && !cliente.getDirecciones().isEmpty()) {
            return cliente.getDirecciones().get(0);
        }
        return null;
    }
}

/* Metodos base (por modificar)
    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    public boolean existePorId(Long id) {
        return clienteRepository.existsById(id);
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }
    */