package com.example.springhibernateneonapp.controller;

import com.example.springhibernateneonapp.DTOs.Orden.OrdenRequest;
import com.example.springhibernateneonapp.DTOs.Orden.OrdenUpdateRequest;
import com.example.springhibernateneonapp.entity.Ordene;
import com.example.springhibernateneonapp.service.OrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @Operation(summary = "Crear una nueva orden", description = "Permite crear una orden asociada a un cliente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Orden creada exitosamente\", \"id\": \"UUID\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> createOrden(@RequestBody @Valid OrdenRequest ordenRequest) {
        try {
            // Llama al servicio para crear la orden
            Ordene createdOrden = ordenService.createOrden(ordenRequest);

            // Retorna respuesta exitosa
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Orden creada exitosamente",
                    "id", createdOrden.getId()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }

    @Operation(summary = "Obtener orden por ID", description = "Obtiene los detalles de una orden específica por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"id\": \"UUID\", \"fecha\": \"2025-01-15T10:00:00\", \"total\": 1500.00, \"clienteId\": \"UUID\"}"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrdenById(@PathVariable UUID id) {
        try {
            return ordenService.getOrdenById(id)
                    .<ResponseEntity<Object>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                            "error", "Orden no encontrada",
                            "id", id
                    )));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }

    @Operation(summary = "Obtener todas las órdenes", description = "Devuelve una lista de todas las órdenes existentes con soporte para paginación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Órdenes encontradas",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"content\": [{\"id\": \"UUID\", \"fecha\": \"2025-01-15T10:00:00\", \"total\": 1500.00}], \"totalElements\": 10, \"totalPages\": 1, \"currentPage\": 0}"))),
            @ApiResponse(responseCode = "404", description = "No se encontraron órdenes"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<?> getAllOrdenes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fecha") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Page<Ordene> ordenesPage = ordenService.getAllOrdenes(PageRequest.of(
                    page,
                    size,
                    Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy)
            ));

            if (ordenesPage.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "No se encontraron órdenes"
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "content", ordenesPage.getContent(),
                    "totalElements", ordenesPage.getTotalElements(),
                    "totalPages", ordenesPage.getTotalPages(),
                    "currentPage", ordenesPage.getNumber()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }

    @Operation(summary = "Obtener todas las órdenes de un cliente", description = "Devuelve una lista de todas las órdenes realizadas por un cliente específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Órdenes encontradas para el cliente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"content\": [{\"id\": \"UUID\", \"fecha\": \"2025-01-15T10:00:00\", \"total\": 1500.00}], \"totalElements\": 5, \"totalPages\": 1, \"currentPage\": 0}"))),
            @ApiResponse(responseCode = "404", description = "No se encontraron órdenes para el cliente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> getOrdenesByCliente(
            @PathVariable UUID clienteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fecha") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Page<Ordene> ordenesPage = ordenService.getOrdenesByCliente(clienteId, PageRequest.of(
                    page,
                    size,
                    Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy)
            ));

            if (ordenesPage.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "No se encontraron órdenes para el cliente",
                        "clienteId", clienteId
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "content", ordenesPage.getContent(),
                    "totalElements", ordenesPage.getTotalElements(),
                    "totalPages", ordenesPage.getTotalPages(),
                    "currentPage", ordenesPage.getNumber()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }

    @Operation(summary = "Deshabilitar una orden", description = "Deshabilita una orden por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden deshabilitada exitosamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Orden deshabilitada exitosamente\", \"id\": \"UUID\"}"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/disable")
    public ResponseEntity<?> disableOrden(@PathVariable UUID id) {
        try {
            ordenService.disableOrden(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Orden deshabilitada exitosamente",
                    "id", id
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", e.getMessage(),
                    "id", id
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }


    @Operation(summary = "Actualizar una orden", description = "Permite actualizar los datos de una orden utilizando JSON (body).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Orden actualizada exitosamente\", \"id\": \"UUID\"}"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateOrden(
            @PathVariable UUID id,
            @RequestBody @Valid OrdenUpdateRequest ordenUpdateRequest) {
        try {
            Ordene updatedOrden = ordenService.updateOrden(id, ordenUpdateRequest);
            return ResponseEntity.ok(Map.of(
                    "message", "Orden actualizada exitosamente",
                    "id", updatedOrden.getId()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", e.getMessage(),
                    "id", id
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }





}