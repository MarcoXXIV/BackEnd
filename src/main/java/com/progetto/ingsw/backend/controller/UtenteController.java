package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.dto.UtenteDTO;
import com.progetto.ingsw.backend.model.Utente;
import com.progetto.ingsw.backend.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/utente")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UtenteController {

    private final UtenteService utenteService;

    @GetMapping("/profilo")
    public ResponseEntity<?> getMioProfilo() {
        try {
            UtenteDTO profilo = utenteService.getProfiloDinamico();
            return ResponseEntity.ok(profilo);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel recupero del profilo: " + e.getMessage());
        }
    }

    @PutMapping("/aggiorna")
    public ResponseEntity<?> aggiorna(@RequestBody Utente nuoviDati) {
        try {
            UtenteDTO aggiornato = utenteService.aggiornaUtente(nuoviDati);
            return ResponseEntity.ok(aggiornato);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'aggiornamento dei dati: " + e.getMessage());
        }
    }

    @PostMapping("/cambia-password")
    public ResponseEntity<String> cambiaPassword(@RequestBody Map<String, String> payload) {
        try {
            utenteService.cambiaPassword(
                    payload.get("vecchiaPassword"),
                    payload.get("nuovaPassword")
            );
            return ResponseEntity.ok("Password aggiornata con successo!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante il cambio password.");
        }
    }

    @GetMapping("/is-admin")
    public ResponseEntity<?> checkAdmin() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return ResponseEntity.ok(false);
            }
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            return ResponseEntity.ok(isAdmin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}