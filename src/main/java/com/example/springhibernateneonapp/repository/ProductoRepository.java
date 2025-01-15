package com.example.springhibernateneonapp.repository;

import com.example.springhibernateneonapp.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductoRepository extends JpaRepository<Producto, UUID> {

    @Query("SELECT p FROM Producto p WHERE p.empresa.id = :empresaId AND p.estado = true")
    Page<Producto> findActiveProductosByEmpresa(@Param("empresaId") UUID empresaId, Pageable pageable);


    @Query("SELECT p FROM Producto p WHERE p.id = :id AND p.estado = true")
    Optional<Producto> findByIdAndEstadoTrue(@Param("id") UUID id);

    @Query("SELECT p FROM Producto p WHERE p.estado = true")
    List<Producto> findAllActive();
}