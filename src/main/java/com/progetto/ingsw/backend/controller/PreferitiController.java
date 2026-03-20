package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.model.Preferiti;
import com.progetto.ingsw.backend.service.PreferitiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preferiti")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PreferitiController {

    private final PreferitiService preferitiService;

    // 1. RECUPERA TUTTI I PREFERITI (Per la pagina "I miei preferiti"), funziona
    @GetMapping("/miei")
    public ResponseEntity<List<Preferiti>> getMieiPreferiti() {
        // Simuliamo l'ID 1 finché non integriamo il recupero dal Token
        Integer idUtenteCorrente = 1;
        return ResponseEntity.ok(preferitiService.getPreferitiUtente(idUtenteCorrente));
    }

    // 2. TOGGLE PREFERITO (Aggiunge se manca, rimuove se esiste), funziona
    @PostMapping("/toggle/{confrontoId}")
    public ResponseEntity<String> toggle(@PathVariable Integer confrontoId) {
        Integer idUtenteCorrente = 1;
        boolean aggiunto = preferitiService.togglePreferito(idUtenteCorrente, confrontoId);

        String messaggio = aggiunto ? "Aggiunto ai preferiti" : "Rimosso dai preferiti";
        return ResponseEntity.ok(messaggio);
    }

    // 3. CONTEGGIO (Per mostrarlo nel profilo dell'utente), funziona
    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        Integer idUtenteCorrente = 1;
        return ResponseEntity.ok(preferitiService.contaPreferiti(idUtenteCorrente));
    }
}
