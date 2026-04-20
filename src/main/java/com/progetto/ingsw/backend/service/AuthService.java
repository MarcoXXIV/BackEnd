package com.progetto.ingsw.backend.service;

import com.progetto.ingsw.backend.dto.AuthResponse;
import com.progetto.ingsw.backend.dto.EmailCheckResponse;
import com.progetto.ingsw.backend.dto.ForgotPasswordRequest;
import com.progetto.ingsw.backend.dto.ForgotPasswordResponse;
import com.progetto.ingsw.backend.dto.LoginRequest;
import com.progetto.ingsw.backend.dto.RegisterRequest;
import com.progetto.ingsw.backend.model.Utente;
import com.progetto.ingsw.backend.repository.UtenteRepository;
import com.progetto.ingsw.backend.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UtenteRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = normalizeEmail(request.getEmail());

        if (repository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new RuntimeException("Email già registrata");
        }

        var user = Utente.builder()
                .nome(request.getNome())
                .cognome(request.getCognome())
                .email(normalizedEmail)
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
        String normalizedEmail = normalizeEmail(request.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        normalizedEmail,
                        request.getPassword()
                )
        );

        var user = repository.findByEmailIgnoreCase(normalizedEmail)
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

    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
        String normalizedEmail = normalizeEmail(request.getEmail());

        if (normalizedEmail.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email obbligatoria");
        }

        Utente user = repository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email non trovata"));

        String oldEncodedPassword = user.getPassword();
        String newPlainPassword = generateTemporaryPassword();

        try {
            user.setPassword(passwordEncoder.encode(newPlainPassword));
            repository.save(user);

            emailService.sendNewPasswordEmail(
                    user.getEmail(),
                    user.getNome(),
                    newPlainPassword
            );

            return new ForgotPasswordResponse(
                    user.getEmail(),
                    true,
                    "Una nuova password è stata inviata via email."
            );

        } catch (Exception e) {
            log.error("Errore in forgotPassword per email={}", normalizedEmail, e);

            try {
                user.setPassword(oldEncodedPassword);
                repository.save(user);
            } catch (Exception rollbackEx) {
                log.error("Errore durante il rollback manuale della password per email={}", normalizedEmail, rollbackEx);
            }

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Errore durante il reset della password o invio email"
            );
        }
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private String generateTemporaryPassword() {
        final String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789@#$!";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }
}
