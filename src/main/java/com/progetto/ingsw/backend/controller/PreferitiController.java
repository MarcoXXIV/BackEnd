package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.dto.PreferitoDTO;
import com.progetto.ingsw.backend.service.PreferitiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preferiti")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PreferitiController {

    private final PreferitiService preferitiService;

    @GetMapping("/miei")
    public ResponseEntity<List<PreferitoDTO>> getMieiPreferiti() {
        return ResponseEntity.ok(preferitiService.getPreferitiUtente());
    }

    @PostMapping("/toggle/{confrontoId}")
    public ResponseEntity<String> toggle(@PathVariable Integer confrontoId) {
        boolean aggiunto = preferitiService.togglePreferito(confrontoId);
        String messaggio = aggiunto ? "Aggiunto ai preferiti" : "Rimosso dai preferiti";
        return ResponseEntity.ok(messaggio);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        return ResponseEntity.ok(preferitiService.contaPreferiti());
    }
}
