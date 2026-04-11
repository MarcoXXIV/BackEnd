package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.dto.AutoDTO;
import com.progetto.ingsw.backend.model.Auto;
import com.progetto.ingsw.backend.service.AutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auto")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AutoController {

    private final AutoService autoService;

    @GetMapping("/all")
    public ResponseEntity<List<AutoDTO>> getAll() {
        return ResponseEntity.ok(autoService.getAllAuto());
    }

    @GetMapping("/random")
    public ResponseEntity<List<AutoDTO>> getRandom10() {
        return ResponseEntity.ok(autoService.getAny10Auto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutoDTO> getAutoById(@PathVariable Integer id) {
        AutoDTO auto = autoService.getAutoById(id);

        if (auto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(auto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AutoDTO>> search(
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modello) {
        return ResponseEntity.ok(autoService.cercaAuto(marca, modello));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<AutoDTO>> getByCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(autoService.getAutoPerCategoria(categoria));
    }

    @PostMapping("/AggiungiAuto")
    public ResponseEntity<?> salvaAuto(@RequestBody Auto auto) {
        try {
            return ResponseEntity.ok(autoService.salvaAuto(auto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/top/accelerazione")
    public ResponseEntity<List<AutoDTO>> getTopAccelerazione() {
        return ResponseEntity.ok(autoService.getTopAccelerazione());
    }

    @DeleteMapping("/elimina/{id}")
    public ResponseEntity<String> eliminaAuto(@PathVariable Integer id) {
        autoService.eliminaAuto(id);
        return ResponseEntity.ok("L'auto con ID " + id + " è stata eliminata correttamente.");
    }

    @GetMapping("/top/green")
    public ResponseEntity<List<AutoDTO>> getTopGreen() {
        return ResponseEntity.ok(autoService.getPiuEcologiche());
    }
}