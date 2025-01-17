package com.example.springhibernateneonapp.service;

import com.example.springhibernateneonapp.DTOs.Categoria.CategoriaRequest;
import com.example.springhibernateneonapp.DTOs.Categoria.CategoriaUpdateRequest;
import com.example.springhibernateneonapp.entity.Categoria;
import com.example.springhibernateneonapp.repository.CategoriaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }


    public Categoria createCategoria(CategoriaRequest categoriaRequest) {
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaRequest.getNombre());
        categoria.setDescripcion(categoriaRequest.getDescripcion());
        categoria.setEstado(true); // Por defecto, la categoría está habilitada
        return categoriaRepository.save(categoria);
    }


    public Optional<Categoria> getCategoriaById(UUID id) {
        return categoriaRepository.findById(id);
    }


    public Page<Categoria> getAllCategorias(Pageable pageable) {
        return categoriaRepository.findByEstadoTrue(pageable);
    }


    public Categoria updateCategoria(UUID id, CategoriaUpdateRequest categoriaUpdateRequest) {
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(id);

        if (optionalCategoria.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la categoría con el ID: " + id);
        }

        Categoria categoria = optionalCategoria.get();

        if (categoriaUpdateRequest.getNombre() != null) {
            categoria.setNombre(categoriaUpdateRequest.getNombre());
        }
        if (categoriaUpdateRequest.getDescripcion() != null) {
            categoria.setDescripcion(categoriaUpdateRequest.getDescripcion());
        }
        if (categoriaUpdateRequest.getEstado() != null) {
            categoria.setEstado(categoriaUpdateRequest.getEstado());
        }

        return categoriaRepository.save(categoria);
    }


    public Categoria disableCategoria(UUID id) {
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(id);

        if (optionalCategoria.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la categoría con el ID: " + id);
        }

        Categoria categoria = optionalCategoria.get();
        categoria.setEstado(false);
        return categoriaRepository.save(categoria);
    }
}

