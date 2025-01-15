package com.example.springhibernateneonapp.service;

import com.example.springhibernateneonapp.DTOs.EmpresaRequest;
import com.example.springhibernateneonapp.entity.Empresa;
import com.example.springhibernateneonapp.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    @Autowired
    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    public Empresa createEmpresa(EmpresaRequest empresaRequest) {
        Empresa empresa = new Empresa();
        empresa.setNit(empresaRequest.getNit());
        empresa.setNombre(empresaRequest.getNombre());
        empresa.setDireccion(empresaRequest.getDireccion());
        empresa.setTelefono(empresaRequest.getTelefono());
        empresa.setEstado(empresaRequest.getEstado());
        return empresaRepository.save(empresa);
    }

    public Empresa updateEmpresa(UUID id, EmpresaRequest empresaRequest) {
        Optional<Empresa> optionalEmpresa = empresaRepository.findById(id);

        if (optionalEmpresa.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la empresa con el ID: " + id);
        }

        Empresa empresa = optionalEmpresa.get();
        empresa.setNit(empresaRequest.getNit());
        empresa.setNombre(empresaRequest.getNombre());
        empresa.setDireccion(empresaRequest.getDireccion());
        empresa.setTelefono(empresaRequest.getTelefono());
        empresa.setEstado(empresaRequest.getEstado());
        return empresaRepository.save(empresa);
    }

    public Empresa disableEmpresa(UUID id) {
        Optional<Empresa> optionalEmpresa = empresaRepository.findById(id);

        if (optionalEmpresa.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la empresa con el ID: " + id);
        }

        Empresa empresa = optionalEmpresa.get();
        empresa.setEstado(false);
        return empresaRepository.save(empresa);
    }

    public Optional<Empresa> getEmpresaById(UUID id) {
        return empresaRepository.findById(id);
    }

    public Page<Empresa> getAllEmpresas(Pageable pageable) {
        return empresaRepository.findAll(pageable);
    }
}
