package com.example.springhibernateneonapp.controller;

import com.example.springhibernateneonapp.DTOs.EmpresaRequest;
import com.example.springhibernateneonapp.entity.Empresa;
import com.example.springhibernateneonapp.service.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/empresas")
@SecurityRequirement(name = "bearerAuth")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @Operation(summary = "Obtener empresa por ID", description = "Obtiene los detalles de una empresa específica por su ID, si está habilitada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"id\": \"UUID\", \"nombre\": \"Empresa X\", \"nit\": \"123456\", \"direccion\": \"Calle 123\", \"telefono\": \"987654321\"}"))),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada o deshabilitada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getEmpresaById(@PathVariable UUID id) {
        try {
            return empresaService.getEmpresaById(id)
                    .filter(Empresa::getEstado)
                    .<ResponseEntity<Object>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                            "error", "Empresa no encontrada o deshabilitada",
                            "id", id
                    )));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }

    @Operation(summary = "Obtener todas las empresas habilitadas", description = "Devuelve una lista de empresas habilitadas con soporte para paginación y ordenamiento por nombre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresas encontradas",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"content\": [{\"id\": \"UUID\", \"nombre\": \"Empresa A\"}, {\"id\": \"UUID\", \"nombre\": \"Empresa B\"}], \"totalElements\": 10, \"totalPages\": 1, \"currentPage\": 0}"))),
            @ApiResponse(responseCode = "404", description = "No se encontraron empresas habilitadas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<Object> getAllEmpresas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Page<Empresa> empresasPage = empresaService.getAllEmpresas(PageRequest.of(
                    page,
                    size,
                    Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy)
            ));


            List<Empresa> empresasHabilitadas = empresasPage.getContent().stream()
                    .filter(Empresa::getEstado)
                    .toList();

            if (empresasHabilitadas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "No se encontraron empresas habilitadas"
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "content", empresasHabilitadas,
                    "totalElements", empresasPage.getTotalElements(),
                    "totalPages", empresasPage.getTotalPages(),
                    "currentPage", empresasPage.getNumber()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }


    @Operation(summary = "Crear una nueva empresa",
            description = "Permite crear una empresa utilizando JSON (body) o form-data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empresa creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Empresa creada exitosamente\", \"id\": \"UUID\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
            @ApiResponse(responseCode = "401", description = "No autorizado (Token inválido o ausente)"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(consumes = {"application/json", "multipart/form-data"})
    public ResponseEntity<?> createEmpresa(
            @RequestBody(required = false) EmpresaRequest empresaRequest,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String nit,
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String telefono) {

        try {

            if (empresaRequest == null && (nombre == null || nit == null)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Faltan datos obligatorios: 'nombre' y 'nit' son requeridos."
                ));
            }

            EmpresaRequest request = empresaRequest != null
                    ? empresaRequest
                    : new EmpresaRequest(nombre, nit, direccion, telefono);


            Empresa createdEmpresa = empresaService.createEmpresa(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Empresa creada exitosamente",
                    "id", createdEmpresa.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error interno del servidor",
                    "details", e.getMessage()
            ));
        }
    }


    @Operation(summary = "Actualizar una empresa", description = "Permite actualizar los datos de una empresa específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Empresa actualizada exitosamente\", \"id\": \"UUID\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateEmpresa(
            @PathVariable UUID id,
            @RequestBody @Valid EmpresaRequest empresaRequest) {
        try {
            Empresa updatedEmpresa = empresaService.updateEmpresa(id, empresaRequest);
            return ResponseEntity.ok(Map.of(
                    "message", "Empresa actualizada exitosamente",
                    "id", updatedEmpresa.getId()
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


    @Operation(summary = "Deshabilitar una empresa", description = "Cambia el estado de una empresa a 'deshabilitada'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa deshabilitada exitosamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Empresa deshabilitada exitosamente\", \"id\": \"UUID\"}"))),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/disable")
    public ResponseEntity<?> disableEmpresa(@PathVariable UUID id) {
        try {
            Empresa disabledEmpresa = empresaService.disableEmpresa(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Empresa deshabilitada exitosamente",
                    "id", disabledEmpresa.getId()
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