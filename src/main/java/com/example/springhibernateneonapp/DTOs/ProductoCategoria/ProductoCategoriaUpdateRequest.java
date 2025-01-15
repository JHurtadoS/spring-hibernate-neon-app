package com.example.springhibernateneonapp.DTOs.ProductoCategoria;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductoCategoriaUpdateRequest {

    private UUID productoId;
    private UUID categoriaId;

    public ProductoCategoriaUpdateRequest(UUID productoId, UUID categoriaId) {
        this.productoId = productoId;
        this.categoriaId = categoriaId;
    }
}