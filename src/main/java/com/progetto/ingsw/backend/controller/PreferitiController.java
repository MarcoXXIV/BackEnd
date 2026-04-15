package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.dto.PreferitoDTO;
import com.progetto.ingsw.backend.service.PreferitiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/preferiti")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PreferitiController {

    private final PreferitiService preferitiService;

    @GetMapping("/miei")
    public ResponseEntity<?> getMieiPreferiti() {
        try {
            List<PreferitoDTO> preferiti = preferitiService.getPreferitiUtente();
            return ResponseEntity.ok(preferiti);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel recupero dei preferiti: " + e.getMessage());
        }
    }

    @PostMapping("/toggle/{confrontoId}")
    public ResponseEntity<String> toggle(@PathVariable Integer confrontoId) {
        try {
            boolean aggiunto = preferitiService.togglePreferito(confrontoId);
            String messaggio = aggiunto ? "Aggiunto ai preferiti" : "Rimosso dai preferiti";
            return ResponseEntity.ok(messaggio);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Confronto non trovato.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'operazione: " + e.getMessage());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCount() {
        try {
            Long count = preferitiService.contaPreferiti();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.ok(0L);
        }
    }
}