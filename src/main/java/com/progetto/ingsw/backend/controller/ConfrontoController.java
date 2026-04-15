package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.dto.ConfrontoDTO;
import com.progetto.ingsw.backend.service.ConfrontoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/confronto")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ConfrontoController {

    private final ConfrontoService confrontoService;

    @PostMapping("/battaglia")
    public ResponseEntity<?> eseguiBattaglia(
            @RequestParam Integer idAuto1,
            @RequestParam Integer idAuto2) {
        try {
            ConfrontoDTO risultato = confrontoService.eseguiBattaglia(idAuto1, idAuto2);
            return ResponseEntity.ok(risultato);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Una o entrambe le auto non sono state trovate.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'esecuzione della battaglia: " + e.getMessage());
        }
    }

    @GetMapping("/miei")
    public ResponseEntity<?> getMieiConfronti() {
        try {
            List<ConfrontoDTO> cronologia = confrontoService.getCronologiaUtente();
            return ResponseEntity.ok(cronologia);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel recupero della cronologia: " + e.getMessage());
        }
    }

    @GetMapping("/recenti")
    public ResponseEntity<?> getGlobalRecenti() {
        try {
            List<ConfrontoDTO> recenti = confrontoService.getSfideRecenti();
            return ResponseEntity.ok(recenti);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> getMioTotale() {
        try {
            Long totale = confrontoService.getNumeroSfideUtente();
            return ResponseEntity.ok(totale);
        } catch (Exception e) {
            return ResponseEntity.ok(0L);
        }
    }

    @GetMapping("/trend")
    public ResponseEntity<?> getAutoTrend() {
        try {
            List<Object[]> trend = confrontoService.getAutoPiuConfrontate();
            return ResponseEntity.ok(trend);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/pulisci")
    public ResponseEntity<String> eliminaCronologia() {
        try {
            confrontoService.cancellaMiaCronologia();
            return ResponseEntity.ok("Cronologia eliminata con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante la pulizia della cronologia: " + e.getMessage());
        }
    }

    @GetMapping("/esiste")
    public ResponseEntity<?> verificaSfida(
            @RequestParam Integer idAuto1,
            @RequestParam Integer idAuto2) {
        try {
            Boolean esiste = confrontoService.sfidaGiaFatta(idAuto1, idAuto2);
            return ResponseEntity.ok(esiste);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
}