package com.victuscaf.security;

import com.victuscaf.modules.client.models.UsuarioSistema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private Long id;
    private Long numeroDocumento;
    private String nombreCompleto;
    private String contrasena;
    private String rol;

    public static UserDetailsImpl build(UsuarioSistema usuario) {
        return new UserDetailsImpl(
                usuario.getIdUsuarioSistema(),
                usuario.getNumeroDeDocumento(),
                usuario.getNombreCompleto(),
                usuario.getContrasena(),
                usuario.getRol().name()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol));
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return numeroDocumento.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}