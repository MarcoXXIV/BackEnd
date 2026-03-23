package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.model.Auto;
import com.progetto.ingsw.backend.service.AutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auto")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AutoController {

    private final AutoService autoService;

    // Recupero di tutto il catalogo , funziona
    @GetMapping("/all")
    public ResponseEntity<List<Auto>> getAll() {
        return ResponseEntity.ok(autoService.getAllAuto());
    }

    // Dettaglio singola auto (per la scheda tecnica), funziona
    @GetMapping("/{id}")
    public ResponseEntity<Auto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(autoService.getAutoById(id));
    }

    // Ricerca filtrata (es: /search?marca=Ferrari&modello=F8)
    @GetMapping("/search")
    public ResponseEntity<List<Auto>> search(
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modello) {
        return ResponseEntity.ok(autoService.cercaAuto(marca, modello));
    }

    // Filtro per categoria (es: /categoria/SUV), funziona
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Auto>> getByCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(autoService.getAutoPerCategoria(categoria));
    }

    // per aggiungere auto dal admin
    @PostMapping("/AggiungiAuto")
    public ResponseEntity<?> salvaAuto(@RequestBody Auto auto) {
        try {
            Auto nuovaAuto = autoService.salvaAuto(auto);
            return ResponseEntity.ok(nuovaAuto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // auto con top accellerazione, funziona
    @GetMapping("/top/accelerazione")
    public ResponseEntity<List<Auto>> getTopAccelerazione() {
        return ResponseEntity.ok(autoService.getTopAccelerazione());
    }

    @DeleteMapping("/elimina/{id}")
    public ResponseEntity<?> eliminaAuto(@PathVariable Integer id) {
        autoService.eliminaAuto(id);
        return ResponseEntity.ok("L'auto con ID " + id + " è stata eliminata correttamente dal catalogo.");
    }

    // auto con minor impatto ambientale, funziona
    @GetMapping("/top/green")
    public ResponseEntity<List<Auto>> getTopGreen() {
        return ResponseEntity.ok(autoService.getPiuEcologiche());
    }

}