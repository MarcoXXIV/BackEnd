package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.model.Utente;
import com.progetto.ingsw.backend.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/utente")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UtenteController {

    private final UtenteService utenteService;

    // RECUPERA PROFILO (Dati personali), funziona
    @GetMapping("/profilo")
    public ResponseEntity<Utente> getMioProfilo() {
        // Temporaneo: ID 1. Poi lo prenderemo dal Token JWT
        Integer idUtenteCorrente = 1;
        return ResponseEntity.ok(utenteService.getProfilo(idUtenteCorrente));
    }

    // AGGIORNA DATI BASE (Nome, Cognome)
    @PutMapping("/aggiorna")
    public ResponseEntity<Utente> aggiorna(@RequestBody Utente nuoviDati) {
        Integer idUtenteCorrente = 1;
        return ResponseEntity.ok(utenteService.aggiornaUtente(idUtenteCorrente, nuoviDati));
    }

    // CAMBIO PASSWORD
    @PostMapping("/cambia-password")
    public ResponseEntity<String> cambiaPassword(@RequestBody Map<String, String> payload) {
        Integer idUtenteCorrente = 1;

        String vecchiaPsw = payload.get("vecchiaPassword");
        String nuovaPsw = payload.get("nuovaPassword");

        utenteService.cambiaPassword(idUtenteCorrente, vecchiaPsw, nuovaPsw);
        return ResponseEntity.ok("Password aggiornata con successo!");
    }

    // VERIFICA SE ADMIN (Utile per Angular per mostrare/nascondere il tasto "Admin Panel"), funziona
    @GetMapping("/is-admin")
    public ResponseEntity<Boolean> checkAdmin() {
        Integer idUtenteCorrente = 1;
        return ResponseEntity.ok(utenteService.isUserAdmin(idUtenteCorrente));
    }
}