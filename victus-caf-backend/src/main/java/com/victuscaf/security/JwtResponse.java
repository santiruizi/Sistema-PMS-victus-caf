package com.victuscaf.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private Long numeroDocumento;
    private String nombreCompleto;
    private String rol;

    public JwtResponse(String token, Long id, Long numeroDocumento, String nombreCompleto, String rol) {
        this.token = token;
        this.id = id;
        this.numeroDocumento = numeroDocumento;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }
}