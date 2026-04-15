package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.dto.AutoDTO;
import com.progetto.ingsw.backend.dto.UtenteDTO;
import com.progetto.ingsw.backend.model.Auto;
import com.progetto.ingsw.backend.service.AutoService;
import com.progetto.ingsw.backend.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*")
public class AdminController {

    private final UtenteService utenteService;
    private final AutoService autoService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUtenti() {
        try {
            List<UtenteDTO> utenti = utenteService.getAllUtenti();
            return ResponseEntity.ok(utenti);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel recupero della lista utenti.");
        }
    }

    @PutMapping("/cambia-ruolo/{id}")
    public ResponseEntity<?> cambiaRuolo(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
        try {
            String nuovoRuolo = payload.get("ruolo");
            UtenteDTO aggiornato = utenteService.cambiaRuolo(id, nuovoRuolo);
            return ResponseEntity.ok(aggiornato);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ruolo non valido.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il cambio ruolo.");
        }
    }

    @DeleteMapping("/elimina-utente/{id}")
    public ResponseEntity<String> eliminaUtente(@PathVariable Integer id) {
        try {
            utenteService.eliminaUtente(id);
            return ResponseEntity.ok("Utente eliminato correttamente");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Impossibile eliminare: utente non trovato.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'eliminazione dell'utente: " + e.getMessage());
        }
    }

    @PostMapping("/aggiungi")
    public ResponseEntity<?> aggiungiAuto(@RequestBody Auto auto) {
        try {
            AutoDTO nuova = autoService.salvaAuto(auto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuova);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel salvataggio dell'auto.");
        }
    }

    @DeleteMapping("/elimina-auto/{id}")
    public ResponseEntity<String> eliminaAuto(@PathVariable Integer id) {
        try {
            autoService.eliminaAuto(id);
            return ResponseEntity.ok("Auto rimossa con successo dal catalogo");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Auto non trovata.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'eliminazione dell'auto: " + e.getMessage());
        }
    }

    @PutMapping("/aggiorna/{id}")
    public ResponseEntity<?> aggiornaAuto(@PathVariable Integer id, @RequestBody Auto auto) {
        try {
            auto.setId(id);
            AutoDTO aggiornata = autoService.salvaAuto(auto);
            return ResponseEntity.ok(aggiornata);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Auto non trovata per l'aggiornamento.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'aggiornamento dell'auto.");
        }
    }
}