package com.coderhouse.ventas.servicios;

import com.coderhouse.ventas.dtos.ClienteDTO;
import com.coderhouse.ventas.modelos.Cliente;
import com.coderhouse.ventas.repositorios.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    public List<ClienteDTO> obtenerTodosLosClientes() {
        return clienteRepositorio.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO obtenerClientePorId(Long id) {
        return clienteRepositorio.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con id: " + id));
    }

    public ClienteDTO guardarCliente(ClienteDTO clienteDTO) {
        Cliente cliente = convertirAEntidad(clienteDTO);
        Cliente clienteGuardado = clienteRepositorio.save(cliente);
        return convertirADTO(clienteGuardado);
    }

    public ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTOActualizado) {
        return clienteRepositorio.findById(id)
                .map(cliente -> {
                    cliente.setNombre(clienteDTOActualizado.getNombre());
                    cliente.setApellido(clienteDTOActualizado.getApellido());
                    cliente.setDni(clienteDTOActualizado.getDni());
                    Cliente clienteActualizado = clienteRepositorio.save(cliente);
                    return convertirADTO(clienteActualizado);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con id: " + id));
    }

    public void eliminarCliente(Long id) {
        if (clienteRepositorio.existsById(id)) {
            clienteRepositorio.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con id: " + id);
        }
    }

    private ClienteDTO convertirADTO(Cliente cliente) {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(cliente.getId());
        clienteDTO.setNombre(cliente.getNombre());
        clienteDTO.setApellido(cliente.getApellido());
        clienteDTO.setDni(cliente.getDni());
        return clienteDTO;
    }

    private Cliente convertirAEntidad(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setId(clienteDTO.getId());
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setApellido(clienteDTO.getApellido());
        cliente.setDni(clienteDTO.getDni());
        return cliente;
    }
}