package com.example.springhibernateneonapp.DTOs.Cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteRequest {

    @NotNull(message = "El nombre del cliente es obligatorio.")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres.")
    private String nombre;

    @Email(message = "Debe proporcionar un correo válido.")
    @NotNull(message = "El correo electrónico es obligatorio.")
    @Size(max = 255, message = "El correo no puede exceder los 255 caracteres.")
    private String correo;

    @NotNull(message = "El teléfono del cliente es obligatorio.")
    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres.")
    private String telefono;

    public ClienteRequest(String nombre, String correo, String telefono) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }
}