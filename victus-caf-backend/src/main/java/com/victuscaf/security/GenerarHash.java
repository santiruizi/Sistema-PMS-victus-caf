package com.victuscaf.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad para generar hashes BCrypt.
 * Ejecutar como: java GenerarHash.java
 *
 * Luego usar el hash generado en SQL:
 * UPDATE usuario_sistema SET contrasena = '<hash>' WHERE numero_de_documento = 123456789;
 */
public class GenerarHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin123";
        String hash = encoder.encode(rawPassword);
        System.out.println("Contraseña: " + rawPassword);
        System.out.println("Hash BCrypt: " + hash);
        System.out.println();
        System.out.println("SQL para actualizar:");
        System.out.println("UPDATE usuario_sistema SET contrasena = '" + hash + "' WHERE numero_de_documento = 123456789;");
    }
}
