package com.mitocode.auth.service;

import com.mitocode.auth.controller.dto.*;
import com.mitocode.auth.entity.UserEntity;
import com.mitocode.auth.repository.UserRepository;
import com.mitocode.auth.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public void register(RegisterRequest request) {
        // Validación de existencia
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Usuario ya existe");
        }

        // Construcción usando la nueva entidad que definiste
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword())) // Encriptamos pass
                .email(request.getEmail())
                .roles(request.getRoles()) // List<String> directo
                .provider("LOCAL") // Al ser registro manual, el provider es local
                .build();

        userRepository.save(user);
        log.info("Usuario registrado: {}", user.getUsername());
    }

    public TokenResponse login(LoginRequest request) {
        // Spring Security valida las credenciales automáticamente
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        List<String> roles = user.getRoles();

        String token = jwtUtil.generateToken(user.getUsername(), roles);
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        // jwtUtil.getJwtExpiration() devuelve un objeto Duration, usamos toMillis()
        return new TokenResponse(token, refreshToken, jwtUtil.getJwtExpiration().toMillis());
    }

    public TokenResponse refresh(RefreshRequest request) {
        if (!jwtUtil.validateToken(request.getRefreshToken())) {
            throw new RuntimeException("Refresh token invalido");
        }

        String username = jwtUtil.getUsernameFromToken(request.getRefreshToken());
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        List<String> roles = user.getRoles();
        String token = jwtUtil.generateToken(username, roles);

        return new TokenResponse(token, request.getRefreshToken(), jwtUtil.getJwtExpiration().toMillis());
    }

    public ValidateResponse validate(ValidateRequest request) {
        boolean isValid = jwtUtil.validateToken(request.getToken());

        if (!isValid) {
            return new ValidateResponse(false, null, null, null);
        }

        String username = jwtUtil.getUsernameFromToken(request.getToken());
        List<String> roles = jwtUtil.getRolesFromToken(request.getToken());
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        return new ValidateResponse(
                true,
                user != null ? user.getId() : null,
                username,
                roles
        );
    }

}
