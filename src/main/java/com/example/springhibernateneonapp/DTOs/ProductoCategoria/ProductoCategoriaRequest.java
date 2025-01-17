package com.example.springhibernateneonapp.DTOs.ProductoCategoria;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProductoCategoriaRequest {

    @NotNull(message = "El ID del producto es obligatorio.")
    private UUID productoId;

    private UUID categoriaId;

    private List<UUID> categoriasId;

    public ProductoCategoriaRequest() {}

    public List<UUID> getCategoriaIds() {
        return categoriasId;
    }

    public ProductoCategoriaRequest(UUID productoId, UUID categoriaId, List<UUID> categoriasId) {
        this.productoId = productoId;
        this.categoriaId = categoriaId;
        this.categoriasId = categoriasId;
    }
}