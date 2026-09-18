package com.example.laboratorio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record EmpleadoUpdateRequest(
    String nombre,

    @Email(message = "El email no es válido")
    String email,

    @Pattern(regexp = "^(admin|usuario)$", message = "El rol debe ser 'admin' o 'usuario'")
    String rol,

    String cargo,

    String password
) {
}
