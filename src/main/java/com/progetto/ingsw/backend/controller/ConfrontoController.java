package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.dto.ConfrontoDTO;
import com.progetto.ingsw.backend.service.ConfrontoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/confronto")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ConfrontoController {

    private final ConfrontoService confrontoService;

    @PostMapping("/battaglia")
    public ResponseEntity<ConfrontoDTO> eseguiBattaglia(
            @RequestParam Integer idAuto1,
            @RequestParam Integer idAuto2) {
        return ResponseEntity.ok(confrontoService.eseguiBattaglia(idAuto1, idAuto2));
    }

    @GetMapping("/miei")
    public ResponseEntity<List<ConfrontoDTO>> getMieiConfronti() {
        return ResponseEntity.ok(confrontoService.getCronologiaUtente());
    }

    @GetMapping("/recenti")
    public ResponseEntity<List<ConfrontoDTO>> getGlobalRecenti() {
        return ResponseEntity.ok(confrontoService.getSfideRecenti());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getMioTotale() {
        return ResponseEntity.ok(confrontoService.getNumeroSfideUtente());
    }

    // Endpoint per le auto più confrontate (Trend)
    @GetMapping("/trend")
    public ResponseEntity<List<Object[]>> getAutoTrend() {
        return ResponseEntity.ok(confrontoService.getAutoPiuConfrontate());
    }

    @DeleteMapping("/pulisci")
    public ResponseEntity<String> eliminaCronologia() {
        try {
            confrontoService.cancellaMiaCronologia();
            return ResponseEntity.ok("Cronologia eliminata con successo.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint per verificare se l'utente loggato ha già fatto questa sfida
    @GetMapping("/esiste")
    public ResponseEntity<Boolean> verificaSfida(
            @RequestParam Integer idAuto1,
            @RequestParam Integer idAuto2) {
        return ResponseEntity.ok(confrontoService.sfidaGiaFatta(idAuto1, idAuto2));
    }
}