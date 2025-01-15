package com.example.springhibernateneonapp.service;

import com.example.springhibernateneonapp.DTOs.Cliente.ClienteRequest;
import com.example.springhibernateneonapp.DTOs.Cliente.ClienteUpdateRequest;
import com.example.springhibernateneonapp.entity.Cliente;
import com.example.springhibernateneonapp.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente createCliente(ClienteRequest clienteRequest) {
        Cliente cliente = new Cliente();
        cliente.setNombre(clienteRequest.getNombre());
        cliente.setEmail((clienteRequest.getCorreo()));
        cliente.setTelefono(clienteRequest.getTelefono());
        cliente.setEstado(true);
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> getClienteById(UUID id) {
        return clienteRepository.findById(id).filter(Cliente::getEstado);
    }

    public Page<Cliente> getAllClientes(Pageable pageable) {
        return clienteRepository.findAllByEstadoTrue(pageable);
    }

    public Cliente disableCliente(UUID id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        cliente.setEstado(false);
        return clienteRepository.save(cliente);
    }

    public Cliente updateCliente(UUID id, ClienteUpdateRequest clienteUpdateRequest) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        if (clienteUpdateRequest.getNombre() != null) {
            cliente.setNombre(clienteUpdateRequest.getNombre());
        }
        if (clienteUpdateRequest.getCorreo() != null) {
            cliente.setEmail(clienteUpdateRequest.getCorreo());
        }
        if (clienteUpdateRequest.getTelefono() != null) {
            cliente.setTelefono(clienteUpdateRequest.getTelefono());
        }
        return clienteRepository.save(cliente);
    }
}