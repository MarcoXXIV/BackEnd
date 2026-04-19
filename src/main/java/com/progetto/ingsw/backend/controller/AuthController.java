package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.dto.AuthResponse;
import com.progetto.ingsw.backend.dto.EmailCheckResponse;
import com.progetto.ingsw.backend.dto.LoginRequest;
import com.progetto.ingsw.backend.dto.RegisterRequest;
import com.progetto.ingsw.backend.service.AuthService;
import com.progetto.ingsw.backend.service.UtenteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final UtenteService utenteService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-email")
    public ResponseEntity<EmailCheckResponse> checkEmail(
            @RequestParam
            @NotBlank(message = "Email obbligatoria")
            @Email(message = "Email non valida")
            String email
    ) {
        return ResponseEntity.ok(authService.checkEmailExists(email));
    }
}
