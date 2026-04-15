package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.dto.AutoDTO;
import com.progetto.ingsw.backend.model.Auto;
import com.progetto.ingsw.backend.service.AutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/auto")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AutoController {

    private final AutoService autoService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(autoService.getAllAuto());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/random")
    public ResponseEntity<?> getRandom10() {
        try {
            return ResponseEntity.ok(autoService.getAny10Auto());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAutoById(@PathVariable Integer id) {
        try {
            AutoDTO auto = autoService.getAutoById(id);
            if (auto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Auto non trovata.");
            }
            return ResponseEntity.ok(auto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modello) {
        try {
            return ResponseEntity.ok(autoService.cercaAuto(marca, modello));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<?> getByCategoria(@PathVariable String categoria) {
        try {
            return ResponseEntity.ok(autoService.getAutoPerCategoria(categoria));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/AggiungiAuto")
    public ResponseEntity<?> salvaAuto(@RequestBody Auto auto) {
        try {
            AutoDTO nuova = autoService.salvaAuto(auto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuova);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante il salvataggio dell'auto.");
        }
    }

    @GetMapping("/top/accelerazione")
    public ResponseEntity<?> getTopAccelerazione() {
        try {
            return ResponseEntity.ok(autoService.getTopAccelerazione());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/elimina/{id}")
    public ResponseEntity<String> eliminaAuto(@PathVariable Integer id) {
        try {
            autoService.eliminaAuto(id);
            return ResponseEntity.ok("L'auto con ID " + id + " è stata eliminata correttamente.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Impossibile eliminare: auto non trovata.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'eliminazione dell'auto: " + e.getMessage());
        }
    }

    @GetMapping("/top/green")
    public ResponseEntity<?> getTopGreen() {
        try {
            return ResponseEntity.ok(autoService.getPiuEcologiche());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}