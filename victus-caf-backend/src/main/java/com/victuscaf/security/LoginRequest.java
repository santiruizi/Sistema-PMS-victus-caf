package com.victuscaf.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    @NotNull(message = "El número de documento es obligatorio")
    private Long numeroDocumento;  // username

    @NotBlank
    private String contrasena;
}