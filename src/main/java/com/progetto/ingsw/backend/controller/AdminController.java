package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.dto.AutoDTO;
import com.progetto.ingsw.backend.dto.UtenteDTO;
import com.progetto.ingsw.backend.model.Auto;
import com.progetto.ingsw.backend.service.AutoService;
import com.progetto.ingsw.backend.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    private final UtenteService utenteService;
    private final AutoService autoService;

    // --- GESTIONE UTENTI ---

    @GetMapping("/all")
    public ResponseEntity<List<UtenteDTO>> getAllUtenti() {
        return ResponseEntity.ok(utenteService.getAllUtenti());
    }

    @PutMapping("/cambia-ruolo/{id}")
    public ResponseEntity<UtenteDTO> cambiaRuolo(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
        String nuovoRuolo = payload.get("ruolo");
        return ResponseEntity.ok(utenteService.cambiaRuolo(id, nuovoRuolo));
    }

    @DeleteMapping("/elimina-utente/{id}")
    public ResponseEntity<String> eliminaUtente(@PathVariable Integer id) {
        utenteService.eliminaUtente(id);
        return ResponseEntity.ok("Utente eliminato correttamente");
    }

    // --- GESTIONE AUTO ---

    @PostMapping("/aggiungi")
    public ResponseEntity<AutoDTO> aggiungiAuto(@RequestBody Auto auto) {
        return ResponseEntity.ok(autoService.salvaAuto(auto));
    }

    @DeleteMapping("/elimina-auto/{id}")
    public ResponseEntity<String> eliminaAuto(@PathVariable Integer id) {
        autoService.eliminaAuto(id);
        return ResponseEntity.ok("Auto rimossa con successo dal catalogo");
    }

    @PutMapping("/aggiorna/{id}")
    public ResponseEntity<AutoDTO> aggiornaAuto(@PathVariable Integer id, @RequestBody Auto auto) {
        auto.setId(id); // Forza l'ID del path nell'oggetto da salvare
        return ResponseEntity.ok(autoService.salvaAuto(auto));
    }
}