package com.victuscaf.modules.client.dto;

public record MensajeResponseDTO(String mensaje, boolean success, Object data) {
    public static MensajeResponseDTO ok(String mensaje, Object data) {
        return new MensajeResponseDTO(mensaje, true, data);
    }
    public static MensajeResponseDTO error(String mensaje) {
        return new MensajeResponseDTO(mensaje, false, null);
    }
}