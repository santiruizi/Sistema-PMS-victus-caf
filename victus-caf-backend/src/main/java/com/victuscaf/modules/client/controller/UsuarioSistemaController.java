package com.victuscaf.modules.client.controller;

import com.victuscaf.modules.client.dto.UsuarioSistemaRequestDTO;
import com.victuscaf.modules.client.models.UsuarioSistema;
import com.victuscaf.modules.client.service.UsuarioSistemaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios-sistema")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class UsuarioSistemaController {

    private final UsuarioSistemaService usuarioSistemaService;

    @GetMapping
    public List<UsuarioSistema> listar() {
        return usuarioSistemaService.listarTodos();
    }

    @GetMapping("/buscar")
    public UsuarioSistema buscar(@RequestParam Long documento) {
        return usuarioSistemaService.buscarPorDocumento(documento);
    }

    @PostMapping
    public UsuarioSistema crear(@Valid @RequestBody UsuarioSistemaRequestDTO dto) {
        return usuarioSistemaService.crear(dto);
    }

    @PutMapping("/{id}")
    public UsuarioSistema actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioSistemaRequestDTO dto) {
        return usuarioSistemaService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void desactivar(@PathVariable Long id) {
        usuarioSistemaService.desactivar(id);
    }

    @PatchMapping("/{id}/reactivar")
    public UsuarioSistema reactivar(@PathVariable Long id) {
        return usuarioSistemaService.reactivar(id);
    }
}
