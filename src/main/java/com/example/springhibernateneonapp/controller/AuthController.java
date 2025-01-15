package com.example.springhibernateneonapp.controller;

import com.example.springhibernateneonapp.DTOs.LoginRequest;
import com.example.springhibernateneonapp.DTOs.CreateUserRequest;
import com.example.springhibernateneonapp.entity.User;
import com.example.springhibernateneonapp.service.UserService;
import com.example.springhibernateneonapp.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Login de usuario", description = "Endpoint para autenticación de usuarios. Acepta JSON con los campos email y password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Login exitoso.\", \"token\": \"JWT_TOKEN\"}"))
            ),
            @ApiResponse(responseCode = "400", description = "Faltan campos obligatorios"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.findByEmail(loginRequest.getEmail()).orElse(null);
            if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(401).body("Credenciales inválidas.");
            }

            String token = jwtUtil.generateToken(user.getEmail(), user.getUserProfile().getIsAdmin());

            Map<String, String> response = new HashMap<>();
            response.put("message", "Login exitoso.");
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
        }
    }

    @Operation(summary = "Crear usuario y perfil", description = "Endpoint para la creación de un usuario y su perfil asociado. Acepta JSON.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Usuario creado exitosamente.\", \"token\": \"JWT_TOKEN\"}"))
            ),
            @ApiResponse(responseCode = "400", description = "Faltan campos obligatorios"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<?> createUserWithProfile(@Valid @RequestBody CreateUserRequest createRequest) {
        try {
            User createdUser = userService.createUserWithProfile(
                    createRequest.getEmail(),
                    createRequest.getPassword(),
                    createRequest.getName(),
                    createRequest.getPhone(),
                    createRequest.getThemePreference(),
                    createRequest.isAdmin()
            );

            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuario creado exitosamente.");
            response.put("token", jwtUtil.generateToken(createdUser.getEmail(), createRequest.isAdmin()));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
        }
    }
}
