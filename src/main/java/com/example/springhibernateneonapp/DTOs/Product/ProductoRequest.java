package com.example.springhibernateneonapp.DTOs.Product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ProductoRequest {

    @NotNull(message = "El nombre del producto es obligatorio.")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres.")
    private String nombre;

    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres.")
    private String descripcion;

    @NotNull(message = "El precio del producto es obligatorio.")
    private BigDecimal precio;

    @NotNull(message = "El stock del producto es obligatorio.")
    private Integer stock;

//    @NotNull(message = "El ID de la empresa es obligatorio.")
//    private UUID empresaId;

    // Constructor vacío (necesario para frameworks como Jackson)
    public ProductoRequest() {
    }

    // Constructor completo
    public ProductoRequest(String nombre, String descripcion, BigDecimal precio, Integer stock, UUID empresaId) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
//        this.empresaId = empresaId;
    }
}
