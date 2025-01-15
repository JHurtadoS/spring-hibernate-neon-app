package com.example.springhibernateneonapp.repository;
import com.example.springhibernateneonapp.entity.Ordene;
import com.example.springhibernateneonapp.entity.Ordene;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrdenRepository extends JpaRepository<Ordene, UUID> {

    // Obtener todas las órdenes activas (estado = true)
    Page<Ordene> findByEstadoTrue(Pageable pageable);

    // Obtener una orden por ID si está activa
    List<Ordene> findByIdAndEstadoTrue(UUID id);

    // Obtener todas las órdenes de un cliente específico que estén activas
    Page<Ordene> findByClienteIdAndEstadoTrue(UUID clienteId, Pageable pageable);
}