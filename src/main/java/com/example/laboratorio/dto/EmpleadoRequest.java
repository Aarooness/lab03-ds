package com.example.laboratorio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmpleadoRequest(
    @NotBlank(message = "El nombre es obligatorio")
    String nombre,

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
    String email,

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "^(admin|usuario)$", message = "El rol debe ser 'admin' o 'usuario'")
    String rol,

    @NotBlank(message = "El cargo es obligatorio")
    String cargo,

    @NotBlank(message = "La contraseña es obligatoria")
    String password
) {
}
