package com.victuscaf.security;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // 1. Autenticar: Spring Security usa UserDetailsServiceImpl + BCryptPasswordEncoder
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getNumeroDocumento().toString(),
                        loginRequest.getContrasena()));

        // 2. Establecer contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generar JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        // 4. Extraer datos del usuario autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getNumeroDocumento(),
                userDetails.getNombreCompleto(),
                userDetails.getRol()));
    }
}
