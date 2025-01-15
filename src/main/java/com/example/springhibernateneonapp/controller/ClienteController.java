package com.example.springhibernateneonapp.controller;

import com.example.springhibernateneonapp.DTOs.Cliente.ClienteRequest;
import com.example.springhibernateneonapp.DTOs.Cliente.ClienteUpdateRequest;
import com.example.springhibernateneonapp.entity.Cliente;
import com.example.springhibernateneonapp.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Obtener cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado o deshabilitado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getClienteById(@PathVariable UUID id) {
        Optional<Cliente> cliente = clienteService.getClienteById(id);
        if (cliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cliente no encontrado o deshabilitado"));
        }
        return ResponseEntity.ok(cliente.get());
    }

    @Operation(summary = "Obtener todos los clientes habilitados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrados"),
            @ApiResponse(responseCode = "404", description = "No se encontraron clientes habilitados")
    })
    @GetMapping
    public ResponseEntity<Object> getAllClientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        var clientes = clienteService.getAllClientes(PageRequest.of(
                page,
                size,
                Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy)
        ));
        if (clientes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No se encontraron clientes habilitados"));
        }
        return ResponseEntity.ok(Map.of(
                "content", clientes.getContent(),
                "totalElements", clientes.getTotalElements(),
                "totalPages", clientes.getTotalPages(),
                "currentPage", clientes.getNumber()
        ));
    }

    @Operation(summary = "Crear un nuevo cliente", description = "Permite crear un cliente utilizando JSON.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Object> createCliente(@RequestBody @Valid ClienteRequest clienteRequest) {
        try {
            Cliente cliente = clienteService.createCliente(clienteRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Cliente creado exitosamente",
                    "id", cliente.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }

    @Operation(summary = "Actualizar un cliente existente", description = "Permite actualizar parcialmente los datos de un cliente utilizando JSON.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateCliente(
            @PathVariable UUID id,
            @RequestBody @Valid ClienteUpdateRequest clienteUpdateRequest
    ) {
        try {
            Cliente cliente = clienteService.updateCliente(id, clienteUpdateRequest);
            return ResponseEntity.ok(Map.of(
                    "message", "Cliente actualizado exitosamente",
                    "id", cliente.getId()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }

    @Operation(summary = "Deshabilitar un cliente", description = "Cambia el estado de un cliente a deshabilitado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente deshabilitado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/disable")
    public ResponseEntity<Object> disableCliente(@PathVariable UUID id) {
        try {
            Cliente cliente = clienteService.disableCliente(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Cliente deshabilitado exitosamente",
                    "id", cliente.getId()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }




}