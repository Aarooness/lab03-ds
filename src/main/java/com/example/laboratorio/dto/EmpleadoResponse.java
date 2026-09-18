package com.example.laboratorio.dto;

public record EmpleadoResponse(
    Long id,
    String nombre,
    String email,
    String rol,
    String cargo
) {
}
