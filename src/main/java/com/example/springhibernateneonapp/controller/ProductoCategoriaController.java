package com.example.springhibernateneonapp.controller;

import com.example.springhibernateneonapp.DTOs.ProductoCategoria.ProductoCategoriaRequest;
import com.example.springhibernateneonapp.entity.Producto;
import com.example.springhibernateneonapp.entity.ProductoCategoria;
import com.example.springhibernateneonapp.service.ProductoCategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/producto-categorias")
public class ProductoCategoriaController {

    private final ProductoCategoriaService productoCategoriaService;

    public ProductoCategoriaController(ProductoCategoriaService productoCategoriaService) {
        this.productoCategoriaService = productoCategoriaService;
    }

    @Operation(summary = "Asociar un producto con categorías",
            description = "Permite asociar un producto con una o varias categorías.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto asociado con las categorías exitosamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Producto asociado con las categorías exitosamente\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
            @ApiResponse(responseCode = "404", description = "Producto o categoría(s) no encontrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/associate")
    public ResponseEntity<?> associateProductoWithCategorias(@RequestBody @Valid ProductoCategoriaRequest request) {
        try {
            productoCategoriaService.associateProductoWithCategorias(request);
            return ResponseEntity.ok(Map.of("message", "Producto asociado con las categorías exitosamente"));
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

    @Operation(summary = "Obtener relación Producto-Categoría por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relación encontrada",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Relación no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductoCategoriaById(@PathVariable UUID id) {
        try {
            Map<String, Object> productoCategoria = productoCategoriaService.getProductoCategoriaById(id);
            return ResponseEntity.ok(productoCategoria);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }


    @Operation(summary = "Obtener todas las categorías de un producto",
            description = "Devuelve todas las categorías asociadas a un producto específico por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorías encontradas",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "[{\"categoriaId\": \"UUID\", \"nombre\": \"Categoría A\"}, {\"categoriaId\": \"UUID\", \"nombre\": \"Categoría B\"}]"))),
            @ApiResponse(responseCode = "404", description = "No se encontraron categorías asociadas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<?> getCategoriasByProductoId(@PathVariable UUID productoId) {
        try {
            List<Map<String, Object>> categorias = productoCategoriaService.getCategoriasByProductoId(productoId);
            if (categorias.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "No se encontraron categorías asociadas al producto",
                        "productoId", productoId
                ));
            }

            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }

    @Operation(summary = "Obtener todos los productos asociados a una categoría",
            description = "Devuelve todos los productos que pertenecen a una categoría específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "[{\"id\": \"UUID\", \"nombre\": \"Producto A\"}, {\"id\": \"UUID\", \"nombre\": \"Producto B\"}]"))),
            @ApiResponse(responseCode = "404", description = "No se encontraron productos asociados a la categoría"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/categoria/{categoriaId}/productos")
    public ResponseEntity<?> getProductosByCategoriaId(@PathVariable UUID categoriaId) {
        try {
            List<Producto> productos = productoCategoriaService.getProductosByCategoriaId(categoriaId);
            if (productos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "No se encontraron productos asociados a la categoría",
                        "categoriaId", categoriaId
                ));
            }

            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }




}