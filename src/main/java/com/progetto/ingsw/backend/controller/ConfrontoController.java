package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.model.Confronto;
import com.progetto.ingsw.backend.service.ConfrontoService;
import com.progetto.ingsw.backend.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/confronto")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ConfrontoController {

    private final ConfrontoService confrontoService;

    // funziona
    @PostMapping("/battaglia")
    public ResponseEntity<Confronto> eseguiBattaglia(
            @RequestParam Integer idAuto1,
            @RequestParam Integer idAuto2) {

        // RECUPERO ID UTENTE DAL TOKEN (Logica di sicurezza)
        // lo passo come RequestParam per ora
        Integer idUtenteCorrente = 1;

        Confronto risultato = confrontoService.eseguiBattaglia(idUtenteCorrente, idAuto1, idAuto2);
        return ResponseEntity.ok(risultato);
    }

    // CRONOLOGIA PERSONALE (Pagina "I miei confronti" su Angular), funziona
    @GetMapping("/miei")
    public ResponseEntity<List<Confronto>> getMieiConfronti() {
        Integer idUtenteCorrente = 1; // Da recuperare dal Token
        return ResponseEntity.ok(confrontoService.getCronologiaUtente(idUtenteCorrente));
    }

    // STATISTICHE PROFILO (Quante sfide hai fatto?), funziona
    @GetMapping("/count")
    public ResponseEntity<Long> getMioTotale() {
        Integer idUtenteCorrente = 1; // Da recuperare dal Token
        return ResponseEntity.ok(confrontoService.getNumeroSfideUtente(idUtenteCorrente));
    }

    // SFIDE RECENTI (Pubblico - per la Home Page della Community), funziona
    @GetMapping("/recenti")
    public ResponseEntity<List<Confronto>> getGlobalRecenti() {
        return ResponseEntity.ok(confrontoService.getSfideRecenti());
    }

    // TREND (Le auto più "sfidate" del momento), funziona
    @GetMapping("/trend")
    public ResponseEntity<List<Object[]>> getAutoTrend() {
        return ResponseEntity.ok(confrontoService.getAutoPiuConfrontate());
    }
}
