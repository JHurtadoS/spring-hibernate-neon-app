package com.example.springhibernateneonapp.DTOs.Cliente;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteUpdateRequest {

    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres.")
    private String nombre;

    @Email(message = "Debe proporcionar un correo válido.")
    @Size(max = 255, message = "El correo no puede exceder los 255 caracteres.")
    private String correo;

    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres.")
    private String telefono;

    public ClienteUpdateRequest(String nombre, String correo, String telefono) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }
}