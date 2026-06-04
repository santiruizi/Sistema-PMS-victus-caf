error id: file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/security/SecurityConfig.java:_empty_/HttpMethod#OPTIONS#
file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/security/SecurityConfig.java
empty definition using pc, found symbol in pc: _empty_/HttpMethod#OPTIONS#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 3079
uri: file://<WORKSPACE>/victus-caf-backend/src/main/java/com/victuscaf/security/SecurityConfig.java
text:
```scala
package com.victuscaf.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity          // habilita @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthTokenFilter jwtAuthTokenFilter;

    // ── PasswordEncoder ────────────────────────────────────────────
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ── AuthenticationProvider con UserDetailsService + BCrypt ──────
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // ── AuthenticationManager (necesario para AuthController) ───────
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // ── SecurityFilterChain ────────────────────────────────────────
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF (API stateless con JWT)
            .csrf(csrf -> csrf.disable())
            // CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Sesiones stateless
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Reglas de autorización
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.@@OPTIONS, "/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            // Registrar el provider
            .authenticationProvider(authenticationProvider())
            // Registrar filtro JWT ANTES del filtro de username/password
            .addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ── CORS (ajustar origins según tu frontend) ───────────────────
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://127.0.0.1:*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);
        return new UrlBasedCorsConfigurationSource() {{
            registerCorsConfiguration("/**", config);
        }};
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/HttpMethod#OPTIONS#