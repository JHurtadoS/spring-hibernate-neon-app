package com.example.springhibernateneonapp.repository;

import com.example.springhibernateneonapp.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    Page<Cliente> findAllByEstadoTrue(Pageable pageable);
}