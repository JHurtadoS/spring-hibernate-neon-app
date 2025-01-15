package com.example.springhibernateneonapp.DTOs.Orden;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class OrdenRequest {

    @NotNull(message = "El ID del cliente es obligatorio.")
    private UUID clienteId;

    @NotNull(message = "El total de la orden es obligatorio.")
    private Double total;

    private LocalDateTime fecha;

    public OrdenRequest(UUID clienteId, Double total) {
        this.clienteId = clienteId;
        this.total = total;
    }
}