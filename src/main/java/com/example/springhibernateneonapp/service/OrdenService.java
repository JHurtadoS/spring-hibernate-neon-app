package com.example.springhibernateneonapp.service;

import com.example.springhibernateneonapp.DTOs.Orden.OrdenRequest;
import com.example.springhibernateneonapp.DTOs.Orden.OrdenUpdateRequest;
import com.example.springhibernateneonapp.entity.Cliente;
import com.example.springhibernateneonapp.entity.Ordene;
import com.example.springhibernateneonapp.repository.ClienteRepository;
import com.example.springhibernateneonapp.repository.OrdenRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final ClienteRepository clienteRepository;

    public OrdenService(OrdenRepository ordenRepository, ClienteRepository clienteRepository) {
        this.ordenRepository = ordenRepository;
        this.clienteRepository = clienteRepository;
    }

    /**
     * Crear una nueva orden
     */
    public Ordene createOrden(OrdenRequest ordenRequest) {
        Optional<Cliente> optionalCliente = clienteRepository.findById(ordenRequest.getClienteId());

        if (optionalCliente.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el cliente con el ID: " + ordenRequest.getClienteId());
        }

        Ordene orden = new Ordene();
        orden.setCliente(optionalCliente.get());
        orden.setFecha(ordenRequest.getFecha().toInstant(ZoneOffset.UTC));
        orden.setTotal(BigDecimal.valueOf(ordenRequest.getTotal()));
        orden.setEstado(true); // Activa por defecto
        return ordenRepository.save(orden);
    }

    /**
     * Obtener todas las órdenes activas con paginación
     */
    public Page<Ordene> getAllOrdenes(Pageable pageable) {
        return ordenRepository.findByEstadoTrue(pageable);
    }

    /**
     * Obtener una orden activa por ID
     */
    public Optional<Ordene> getOrdenById(UUID id) {
        return ordenRepository.findByIdAndEstadoTrue(id).stream().findFirst();
    }

    /**
     * Obtener órdenes activas por cliente con paginación
     */
    public Page<Ordene> getOrdenesByCliente(UUID clienteId, PageRequest pageRequest) {
        return ordenRepository.findByClienteIdAndEstadoTrue(clienteId, pageRequest);
    }

    /**
     * Actualizar una orden
     */
    @Transactional
    public Ordene updateOrden(UUID id, OrdenUpdateRequest ordenUpdateRequest) {
        Optional<Ordene> optionalOrden = ordenRepository.findById(id);

        if (optionalOrden.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la orden con el ID: " + id);
        }

        Ordene orden = optionalOrden.get();
        if (ordenUpdateRequest.getFecha() != null) {
            orden.setFecha(ordenUpdateRequest.getFecha().toInstant(ZoneOffset.UTC));
        }
        if (ordenUpdateRequest.getTotal() != null) {
            orden.setTotal(BigDecimal.valueOf(ordenUpdateRequest.getTotal()));
        }
        return ordenRepository.save(orden);
    }

    /**
     * Deshabilitar una orden
     */
    @Transactional
    public Ordene disableOrden(UUID id) {
        Optional<Ordene> optionalOrden = ordenRepository.findById(id);

        if (optionalOrden.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la orden con el ID: " + id);
        }

        Ordene orden = optionalOrden.get();
        orden.setEstado(false);
        return ordenRepository.save(orden);
    }
}
