package com.progetto.ingsw.backend.service;

import com.progetto.ingsw.backend.dto.*;
import com.progetto.ingsw.backend.model.Utente;
import com.progetto.ingsw.backend.repository.UtenteRepository;
import com.progetto.ingsw.backend.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UtenteRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        var user = Utente.builder()
                .nome(request.getNome())
                .cognome(request.getCognome())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .ruolo("USER")
                .build();

        var savedUser = repository.save(user);
        var jwtToken = jwtUtils.generateToken(savedUser);

        return AuthResponse.builder()
                .token(jwtToken)
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .ruolo(savedUser.getRuolo())
                .build();
    }

    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        var jwtToken = jwtUtils.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .id(user.getId())
                .email(user.getEmail())
                .ruolo(user.getRuolo())
                .build();
    }

    public EmailCheckResponse checkEmailExists(String email) {
        String normalizedEmail = normalizeEmail(email);
        boolean exists = repository.existsByEmailIgnoreCase(normalizedEmail);
        return new EmailCheckResponse(exists);
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

}