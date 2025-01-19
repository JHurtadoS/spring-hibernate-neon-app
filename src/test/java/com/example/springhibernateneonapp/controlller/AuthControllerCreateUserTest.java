package com.example.springhibernateneonapp.controlller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.springhibernateneonapp.DTOs.CreateUserRequest;
import com.example.springhibernateneonapp.controller.AuthController;
import com.example.springhibernateneonapp.entity.User;
import com.example.springhibernateneonapp.service.UserService;
import com.example.springhibernateneonapp.config.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

class AuthControllerCreateUserTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        AuthController authController = new AuthController(userService, passwordEncoder, jwtUtil);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateUser_Success() throws Exception {
        // Arrange
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("test@example.com");
        createUserRequest.setPassword("password123");
        createUserRequest.setName("Test User");
        createUserRequest.setPhone("123456789");
        createUserRequest.setThemePreference("dark");
        createUserRequest.setAdmin(false);

        User mockUser = new User();
        mockUser.setId(UUID.randomUUID());
        mockUser.setEmail(createUserRequest.getEmail());
        
        when(userService.createUserWithProfile(
                createUserRequest.getEmail(),
                createUserRequest.getPassword(),
                createUserRequest.getName(),
                createUserRequest.getPhone(),
                createUserRequest.getThemePreference(),
                createUserRequest.isAdmin()
        )).thenReturn(mockUser);

        when(jwtUtil.generateToken(mockUser.getEmail(), createUserRequest.isAdmin())).thenReturn("mockToken");

        // Act & Assert
        mockMvc.perform(post("/api/auth/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Usuario creado exitosamente."))
                .andExpect(jsonPath("$.token").value("mockToken"));
        
        // Verificar que las dependencias fueron invocadas correctamente
        verify(userService, times(1)).createUserWithProfile(
                createUserRequest.getEmail(),
                createUserRequest.getPassword(),
                createUserRequest.getName(),
                createUserRequest.getPhone(),
                createUserRequest.getThemePreference(),
                createUserRequest.isAdmin()
        );
        verify(jwtUtil, times(1)).generateToken(mockUser.getEmail(), createUserRequest.isAdmin());
    }
}