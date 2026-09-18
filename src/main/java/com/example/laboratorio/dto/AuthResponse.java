package com.example.laboratorio.dto;

public record AuthResponse(
    String token,
    String email,
    String rol
) {
}
