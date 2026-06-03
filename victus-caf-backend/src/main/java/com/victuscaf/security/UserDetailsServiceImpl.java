package com.victuscaf.security;

import com.victuscaf.modules.client.models.UsuarioSistema;
import com.victuscaf.modules.client.repository.AdministradorRepository;
import com.victuscaf.modules.client.repository.EntrenadorRepository;
import com.victuscaf.modules.client.repository.SecretariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdministradorRepository administradorRepository;
    private final EntrenadorRepository entrenadorRepository;
    private final SecretariaRepository secretariaRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String numeroDocumentoStr) throws UsernameNotFoundException {
        Long numeroDocumento = Long.parseLong(numeroDocumentoStr);

        UsuarioSistema usuario = administradorRepository.findByNumeroDeDocumento(numeroDocumento)
                .map(u -> (UsuarioSistema) u)
                .or(() -> entrenadorRepository.findByNumeroDeDocumento(numeroDocumento))
                .or(() -> secretariaRepository.findByNumeroDeDocumento(numeroDocumento))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con documento: " + numeroDocumento));

        return UserDetailsImpl.build(usuario);
    }
}