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

    // Constructor vacío requerido para la deserialización
    public ProductoCategoriaRequest() {}

    public List<UUID> getCategoriaIds() {
        return categoriasId;
    }


    // Constructor con argumentos para inicializar los campos
    public ProductoCategoriaRequest(UUID productoId, UUID categoriaId, List<UUID> categoriasId) {
        this.productoId = productoId;
        this.categoriaId = categoriaId;
        this.categoriasId = categoriasId;
    }
}