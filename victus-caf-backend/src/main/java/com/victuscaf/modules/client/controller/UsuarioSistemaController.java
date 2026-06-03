package com.victuscaf.modules.client.controller;

import com.victuscaf.modules.client.models.UsuarioSistema;
import com.victuscaf.modules.client.repository.UsuarioSistemaRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios-sistema")
@RequiredArgsConstructor
public class UsuarioSistemaController {

    private final UsuarioSistemaRepository usuarioSistemaRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public List<UsuarioSistema> listar() {
        return usuarioSistemaRepository.findAll();
    }

    @PostMapping
    public UsuarioSistema crear(@RequestBody UsuarioSistema usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuario.setEstado(true);
        return usuarioSistemaRepository.save(usuario);
    }

    @PutMapping("/{id}")
    public UsuarioSistema actualizar(@PathVariable Long id, @RequestBody UsuarioSistema usuarioActualizado) {
        UsuarioSistema usuario = usuarioSistemaRepository.findById(id).orElseThrow();
        usuario.setNombreCompleto(usuarioActualizado.getNombreCompleto());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setCorreoElectronico(usuarioActualizado.getCorreoElectronico());
        usuario.setRol(usuarioActualizado.getRol());
        if (usuarioActualizado.getContrasena() != null && !usuarioActualizado.getContrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(usuarioActualizado.getContrasena()));
        }
        return usuarioSistemaRepository.save(usuario);
    }

    @DeleteMapping("/{id}")
    public void desactivar(@PathVariable Long id) {
        UsuarioSistema usuario = usuarioSistemaRepository.findById(id).orElseThrow();
        usuario.setEstado(false);
        usuarioSistemaRepository.save(usuario);
    }
}