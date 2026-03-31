package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.dto.RecensioneDTO;
import com.progetto.ingsw.backend.service.RecensioniService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recensioni")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class RecensioniController {

    private final RecensioniService recensioniService;

    @PostMapping("/aggiungi/{confrontoId}")
    public ResponseEntity<RecensioneDTO> scrivi(
            @PathVariable Integer confrontoId,
            @RequestParam Integer voto,
            @RequestParam String commento) {

        RecensioneDTO nuova = recensioniService.scriviRecensione(confrontoId, voto, commento);
        return new ResponseEntity<>(nuova, HttpStatus.CREATED);
    }

    @GetMapping("/sfida/{confrontoId}")
    public ResponseEntity<List<RecensioneDTO>> getPerSfida(@PathVariable Integer confrontoId) {
        return ResponseEntity.ok(recensioniService.getRecensioniPerSfida(confrontoId));
    }

    @GetMapping("/mie")
    public ResponseEntity<List<RecensioneDTO>> getMie() {
        return ResponseEntity.ok(recensioniService.getMieRecensioni());
    }

    @GetMapping("/media/{confrontoId}")
    public ResponseEntity<Double> getMedia(@PathVariable Integer confrontoId) {
        return ResponseEntity.ok(recensioniService.getMediaVoti(confrontoId));
    }

    @DeleteMapping("/elimina/{confrontoId}")
    public ResponseEntity<Void> elimina(@PathVariable Integer confrontoId) {
        recensioniService.eliminaRecensione(confrontoId);
        return ResponseEntity.noContent().build();
    }
}