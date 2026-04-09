package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.dto.UtenteDTO;
import com.progetto.ingsw.backend.model.Utente;
import com.progetto.ingsw.backend.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/utente")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UtenteController {

    private final UtenteService utenteService;

    @GetMapping("/profilo")
    public ResponseEntity<UtenteDTO> getMioProfilo() {
        return ResponseEntity.ok(utenteService.getProfiloDinamico());
    }

    @PutMapping("/aggiorna")
    public ResponseEntity<UtenteDTO> aggiorna(@RequestBody Utente nuoviDati) {
        return ResponseEntity.ok(utenteService.aggiornaUtente(nuoviDati));
    }

    @PostMapping("/cambia-password")
    public ResponseEntity<String> cambiaPassword(@RequestBody Map<String, String> payload) {
        utenteService.cambiaPassword(
                payload.get("vecchiaPassword"),
                payload.get("nuovaPassword")
        );
        return ResponseEntity.ok("Password aggiornata con successo!");
    }

    @GetMapping("/is-admin")
    public ResponseEntity<Boolean> checkAdmin() {
        // Recupera l'utente autenticato dal contesto di sicurezza
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(isAdmin);
    }
}