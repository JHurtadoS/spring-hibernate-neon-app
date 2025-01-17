package com.example.springhibernateneonapp.controller;

import com.example.springhibernateneonapp.DTOs.Categoria.CategoriaRequest;
import com.example.springhibernateneonapp.DTOs.Categoria.CategoriaUpdateRequest;
import com.example.springhibernateneonapp.entity.Categoria;
import com.example.springhibernateneonapp.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/categorias")
@SecurityRequirement(name = "bearerAuth")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoriaById(@PathVariable UUID id) {
        try {
            return categoriaService.getCategoriaById(id)
                    .filter(Categoria::getEstado) // Verifica si está habilitada
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                            "error", "Categoría no encontrada o deshabilitada",
                            "id", id
                    )));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCategorias(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Page<Categoria> categoriasPage = categoriaService.getAllCategorias(PageRequest.of(
                    page,
                    size,
                    Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy)
            ));

            List<Categoria> categoriasHabilitadas = categoriasPage.getContent().stream()
                    .filter(Categoria::getEstado)
                    .toList();

            if (categoriasHabilitadas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "No se encontraron categorías habilitadas"
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "content", categoriasHabilitadas,
                    "totalElements", categoriasPage.getTotalElements(),
                    "totalPages", categoriasPage.getTotalPages(),
                    "currentPage", categoriasPage.getNumber()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }

    @Operation(summary = "Crear una nueva categoría",
            description = "Permite crear una categoría utilizando JSON (body) o form-data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Categoría creada exitosamente\", \"id\": \"UUID\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
            @ApiResponse(responseCode = "401", description = "No autorizado (Token inválido o ausente)"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(consumes = {"application/json", "multipart/form-data"})
    public ResponseEntity<?> createCategoria(
            @RequestBody(required = false) @Valid CategoriaRequest categoriaRequest,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String descripcion) {
        try {

            CategoriaRequest request = categoriaRequest != null
                    ? categoriaRequest
                    : new CategoriaRequest(nombre, descripcion);

            if (request.getNombre() == null || request.getNombre().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "El nombre de la categoría es obligatorio."
                ));
            }

            Categoria createdCategoria = categoriaService.createCategoria(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Categoría creada exitosamente",
                    "id", createdCategoria.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }

    @Operation(summary = "Deshabilitar una categoría",
            description = "Permite deshabilitar una categoría específica por su ID. Una categoría deshabilitada no será visible en las consultas de categorías habilitadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría deshabilitada exitosamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Categoría deshabilitada exitosamente\", \"id\": \"UUID\"}"))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/disable")
    public ResponseEntity<?> disableCategoria(@PathVariable UUID id) {
        try {

            Categoria disabledCategoria = categoriaService.disableCategoria(id);

            return ResponseEntity.ok(Map.of(
                    "message", "Categoría deshabilitada exitosamente",
                    "id", disabledCategoria.getId()
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

    @Operation(summary = "Actualizar una categoría",
            description = "Permite actualizar los detalles de una categoría específica. Solo los campos enviados serán actualizados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Categoría actualizada exitosamente\", \"id\": \"UUID\"}"))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCategoria(
            @PathVariable UUID id,
            @RequestBody @Valid CategoriaUpdateRequest categoriaUpdateRequest) {
        try {

            Categoria updatedCategoria = categoriaService.updateCategoria(id, categoriaUpdateRequest);

            return ResponseEntity.ok(Map.of(
                    "message", "Categoría actualizada exitosamente",
                    "id", updatedCategoria.getId()
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

