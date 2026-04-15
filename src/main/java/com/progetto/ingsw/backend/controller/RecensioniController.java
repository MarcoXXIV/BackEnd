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
        try {
            List<RecensioneDTO> recensioni = recensioniService.getRecensioniPerCoppia(auto1Id, auto2Id);
            return ResponseEntity.ok(recensioni);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/mie")
    public ResponseEntity<List<RecensioneDTO>> getMie() {
        try {
            List<RecensioneDTO> mie = recensioniService.getMieRecensioni();
            return ResponseEntity.ok(mie);
        } catch (Exception e) {
            // Se c'è un errore nel recupero dell'utente dal token o DB
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/media")
    public ResponseEntity<Double> getMedia(
            @RequestParam Integer auto1Id,
            @RequestParam Integer auto2Id) {
        try {
            Double media = recensioniService.getMediaVoti(auto1Id, auto2Id);
            // Se il service restituisce null (nessun voto), restituiamo 0.0 o 404 a seconda della tua logica
            return ResponseEntity.ok(media != null ? media : 0.0);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/elimina")
    public ResponseEntity<?> elimina(
            @RequestParam Integer auto1Id,
            @RequestParam Integer auto2Id) {
        try {
            recensioniService.eliminaRecensione(auto1Id, auto2Id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recensione non trovata.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'eliminazione: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> eliminaTutte() {
        try {
            recensioniService.eliminaTutteLeRecensioniUtente();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante la cancellazione massiva: " + e.getMessage());
        }
    }
}
