package com.example.laboratorio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.laboratorio.dto.ApiResponse;
import com.example.laboratorio.dto.AuthResponse;
import com.example.laboratorio.dto.LoginRequest;
import com.example.laboratorio.service.EmpleadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final EmpleadoService empleadoService;

    public AuthController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = empleadoService.autenticar(request);
        return ResponseEntity.ok(ApiResponse.success("Autenticación exitosa", authResponse));
    }
}
