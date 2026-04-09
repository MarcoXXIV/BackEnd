package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.dto.RecensioneDTO;
import com.progetto.ingsw.backend.service.RecensioniService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/recensioni")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecensioniController {

    private final RecensioniService recensioniService;

    @PostMapping("/aggiungi")
    public ResponseEntity<?> scrivi(
            @RequestParam Integer auto1,
            @RequestParam Integer auto2,
            @RequestParam Integer voto,
            @RequestParam String commento) {
        try {
            RecensioneDTO nuova = recensioniService.scriviRecensione(auto1, auto2, voto, commento);
            return new ResponseEntity<>(nuova, HttpStatus.CREATED);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/coppia")
    public ResponseEntity<List<RecensioneDTO>> getPerCoppia(
            @RequestParam Integer auto1Id,
            @RequestParam Integer auto2Id) {
        return ResponseEntity.ok(recensioniService.getRecensioniPerCoppia(auto1Id, auto2Id));
    }

    @GetMapping("/mie")
    public ResponseEntity<List<RecensioneDTO>> getMie() {
        return ResponseEntity.ok(recensioniService.getMieRecensioni());
    }

    @GetMapping("/media")
    public ResponseEntity<Double> getMedia(
            @RequestParam Integer auto1Id,
            @RequestParam Integer auto2Id) {
        return ResponseEntity.ok(recensioniService.getMediaVoti(auto1Id, auto2Id));
    }

    @DeleteMapping("/elimina")
    public ResponseEntity<Void> elimina(
            @RequestParam Integer auto1Id,
            @RequestParam Integer auto2Id) {
        recensioniService.eliminaRecensione(auto1Id, auto2Id);
        return ResponseEntity.noContent().build();
    }
}
