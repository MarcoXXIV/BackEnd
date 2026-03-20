package com.progetto.ingsw.backend.controller;

import com.progetto.ingsw.backend.dto.RecensioneDTO;
import com.progetto.ingsw.backend.model.Recensioni;
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

    // LASCIA UNA RECENSIONE (POST), funziona
    @PostMapping("/aggiungi/{confrontoId}")
    public ResponseEntity<Recensioni> scrivi(
            @PathVariable Integer confrontoId,
            @RequestParam Integer voto,
            @RequestParam String commento) {

        // Temporaneo: ID utente 1 finché non usiamo il Token
        Integer idUtenteCorrente = 1;

        Recensioni nuova = recensioniService.scriviRecensione(idUtenteCorrente, confrontoId, voto, commento);
        return new ResponseEntity<>(nuova, HttpStatus.CREATED);
    }

    // RECUPERA RECENSIONI DI UNA SFIDA SPECIFICA, funziona
    @GetMapping("/sfida/{confrontoId}")
    public ResponseEntity<List<RecensioneDTO>> getPerSfida(@PathVariable Integer confrontoId) {
        return ResponseEntity.ok(recensioniService.getRecensioniPerSfida(confrontoId));
    }

    // MEDIA VOTI DI UN CONFRONTO, funziona
    @GetMapping("/media/{confrontoId}")
    public ResponseEntity<Double> getMedia(@PathVariable Integer confrontoId) {
        return ResponseEntity.ok(recensioniService.getMediaVoti(confrontoId));
    }

    // LE MIE RECENSIONI (Profilo Utente), funziona
    @GetMapping("/mie")
    public ResponseEntity<List<Recensioni>> getMie() {
        Integer idUtenteCorrente = 1;
        return ResponseEntity.ok(recensioniService.getMieRecensioni(idUtenteCorrente));
    }

    // ELIMINA RECENSIONE, funziona
    @DeleteMapping("/elimina/{confrontoId}")
    public ResponseEntity<Void> elimina(@PathVariable Integer confrontoId) {
        Integer idUtenteCorrente = 1;
        recensioniService.eliminaRecensione(idUtenteCorrente, confrontoId);
        return ResponseEntity.noContent().build();
    }
}