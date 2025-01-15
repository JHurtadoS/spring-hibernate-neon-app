package com.example.springhibernateneonapp.DTOs.Orden;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrdenUpdateRequest {

    private Double total; // Campo opcional para actualizar el total

    private LocalDateTime fecha;

    public OrdenUpdateRequest(Double total) {
        this.total = total;
    }
}