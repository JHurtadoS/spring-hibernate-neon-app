package com.example.springhibernateneonapp.repository;

import com.example.springhibernateneonapp.entity.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {

    Page<Categoria> findByEstadoTrue(Pageable pageable);
}