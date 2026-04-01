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

        var savedUser = repository.save(user); // Recuperiamo l'utente salvato (con ID generato)
        var jwtToken = jwtUtils.generateToken(savedUser);

        return AuthResponse.builder()
                .token(jwtToken)
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .ruolo(savedUser.getRuolo())
                .build();
    }

    public AuthResponse authenticate(LoginRequest request) {
        // 1. Esegue l'autenticazione (se fallisce lancia un'eccezione 403)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Recupera l'utente completo dal database
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // 3. Genera il token JWT
        var jwtToken = jwtUtils.generateToken(user);

        // 4. COSTRUISCI LA RISPOSTA COMPLETA (Aggiungi i campi mancanti)
        return AuthResponse.builder()
                .token(jwtToken)
                .id(user.getId())           // <--- Fondamentale
                .email(user.getEmail())     // <--- Fondamentale
                .ruolo(user.getRuolo())     // <--- IL CAMPO CHE SERVE PER ADMIN!
                .build();
    }
}