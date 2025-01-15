package com.example.springhibernateneonapp.controller;

import com.example.springhibernateneonapp.DTOs.Product.ProductoRequest;
import com.example.springhibernateneonapp.DTOs.Product.ProductoUpdateRequest;
import com.example.springhibernateneonapp.entity.Producto;
import com.example.springhibernateneonapp.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
@SecurityRequirement(name = "bearerAuth") // Token requerido en Swagger
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Obtener producto por ID", description = "Obtiene los detalles de un producto específico por su ID, si está habilitado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"id\": \"UUID\", \"nombre\": \"Producto X\", \"descripcion\": \"Descripción del producto\", \"precio\": 100.0, \"stock\": 50}"))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado o deshabilitado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductoById(@PathVariable UUID id) {
        try {
            return productoService.getProductoById(id)
                    .filter(producto -> producto.getStock() > 0) // Verifica que el stock sea mayor a 0
                    .<ResponseEntity<Object>>map(ResponseEntity::ok) // Devuelve el producto si está habilitado
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                            "error", "Producto no encontrado o deshabilitado",
                            "id", id
                    )));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }

    @Operation(summary = "Obtener todos los productos habilitados", description = "Devuelve una lista de productos habilitados con soporte para paginación y ordenamiento por nombre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"content\": [{\"id\": \"UUID\", \"nombre\": \"Producto A\", \"precio\": 50.0}, {\"id\": \"UUID\", \"nombre\": \"Producto B\", \"precio\": 100.0}], \"totalElements\": 20, \"totalPages\": 2, \"currentPage\": 0}"))),
            @ApiResponse(responseCode = "404", description = "No se encontraron productos habilitados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<Object> getAllProductos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Page<Producto> productosPage = productoService.getAllActiveProductos(PageRequest.of(
                    page,
                    size,
                    Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy)
            ));

            // Filtra solo productos habilitados (stock mayor a 0)
            List<Producto> productosHabilitados = productosPage.getContent().stream()
                    .filter(producto -> producto.getStock() > 0)
                    .toList();

            if (productosHabilitados.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "No se encontraron productos habilitados"
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "content", productosHabilitados,
                    "totalElements", productosPage.getTotalElements(),
                    "totalPages", productosPage.getTotalPages(),
                    "currentPage", productosPage.getNumber()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }

    @Operation(
            summary = "Obtener productos activos por empresa",
            description = "Obtiene una lista de productos activos (estado = true) de una empresa específica con soporte para paginación y ordenamiento."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"content\": [{\"id\": \"UUID\", \"nombre\": \"Producto A\"}, {\"id\": \"UUID\", \"nombre\": \"Producto B\"}], \"totalElements\": 20, \"totalPages\": 2, \"currentPage\": 0}"))),
            @ApiResponse(responseCode = "404", description = "No se encontraron productos activos para la empresa especificada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<Object> getProductosByEmpresa(
            @PathVariable UUID empresaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Page<Producto> productosPage = productoService.getProductosByEmpresa(
                    empresaId,
                    PageRequest.of(
                            page,
                            size,
                            Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy)
                    )
            );

            if (productosPage.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "No se encontraron productos activos para la empresa especificada",
                        "empresaId", empresaId
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "content", productosPage.getContent(),
                    "totalElements", productosPage.getTotalElements(),
                    "totalPages", productosPage.getTotalPages(),
                    "currentPage", productosPage.getNumber()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }

    @Operation(summary = "Crear un nuevo producto",
            description = "Permite crear un producto para una empresa específica utilizando JSON (body) o form-data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Producto creado exitosamente\", \"id\": \"UUID\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
            @ApiResponse(responseCode = "401", description = "No autorizado (Token inválido o ausente)"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(consumes = {"application/json", "multipart/form-data"})
    public ResponseEntity<?> createProducto(
            @RequestBody(required = false) @Valid ProductoRequest productoRequest,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) BigDecimal precio,
            @RequestParam(required = false) Integer stock,
            @RequestParam UUID empresaId) { // `empresaId` ahora es obligatorio
        try {
            // Manejar entrada desde JSON o form-data
            ProductoRequest request = productoRequest != null
                    ? productoRequest
                    : new ProductoRequest(nombre, descripcion, precio, stock, empresaId);

            // Validar campos obligatorios
            if (request.getNombre() == null || request.getPrecio() == null || request.getStock() == null ) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Faltan datos obligatorios. Los campos 'nombre', 'precio', 'stock'"
                ));
            }

            // Crear el producto
            Producto createdProducto = productoService.createProducto(request, empresaId);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Producto creado exitosamente",
                    "id", createdProducto.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }

    @Operation(summary = "Actualizar un producto",
            description = "Permite actualizar los datos de un producto para una empresa específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Producto actualizado exitosamente\", \"id\": \"UUID\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado o deshabilitado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProducto(
            @PathVariable UUID id,
            @RequestBody(required = false) @Valid ProductoUpdateRequest productoRequest,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) BigDecimal precio,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) UUID empresaId) {
        try {
            // Manejar entrada desde JSON o form-data
            ProductoUpdateRequest request = productoRequest != null
                    ? productoRequest
                    : new ProductoUpdateRequest(nombre, descripcion, precio, stock, empresaId);

            // Validar campos obligatorios
            if (request.getNombre() == null && request.getPrecio() == null &&  request.getStock() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Faltan datos. 'nombre' y/o 'precio', y/o 'stock'"
                ));
            }

            // Actualizar el producto
            Producto updatedProducto = productoService.updateProducto(id, request);

            return ResponseEntity.ok(Map.of(
                    "message", "Producto actualizado exitosamente",
                    "id", updatedProducto.getId()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "Producto no encontrado o deshabilitado",
                    "details", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }

    @Operation(summary = "Deshabilitar un producto",
            description = "Permite deshabilitar un producto para que no esté disponible.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto deshabilitado exitosamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Producto deshabilitado exitosamente\", \"id\": \"UUID\"}"))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado o ya deshabilitado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/disable")
    public ResponseEntity<?> disableProducto(@PathVariable UUID id) {
        try {
            // Deshabilitar el producto
            Producto disabledProducto = productoService.disableProducto(id);

            return ResponseEntity.ok(Map.of(
                    "message", "Producto deshabilitado exitosamente",
                    "id", disabledProducto.getId()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "Producto no encontrado o ya deshabilitado",
                    "details", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }


}
