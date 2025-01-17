package com.example.springhibernateneonapp.repository;
import com.example.springhibernateneonapp.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {


    List<Empresa> findByEstadoTrue();
}