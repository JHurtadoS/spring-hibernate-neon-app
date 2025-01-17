package com.example.springhibernateneonapp.DTOs.Product;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoUpdateRequest {
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres.")
    private String nombre;

    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres.")
    private String descripcion;

    private BigDecimal precio;

    private Integer stock;

    private UUID empresaId;
}
