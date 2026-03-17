package com.progetto.ingsw.backend.service;

import com.progetto.ingsw.backend.model.Confronto;
import com.progetto.ingsw.backend.model.Preferiti;
import com.progetto.ingsw.backend.model.Utente;
import com.progetto.ingsw.backend.repository.ConfrontoRepository;
import com.progetto.ingsw.backend.repository.PreferitiRepository;
import com.progetto.ingsw.backend.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PreferitiService {

    @Autowired
    private PreferitiRepository preferitiRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private ConfrontoRepository confrontoRepository;

    // AGGIUNGI AI PREFERITI
    public void aggiungiAiPreferiti(Integer utenteId, Integer confrontoId) {
        // Verifica se esiste già per evitare duplicati
        if (preferitiRepository.existsByUtenteIdAndConfrontoId(utenteId, confrontoId)) {
            throw new RuntimeException("Questa sfida è già presente nei tuoi preferiti.");
        }

        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        Confronto confronto = confrontoRepository.findById(confrontoId)
                .orElseThrow(() -> new RuntimeException("Confronto non trovato"));

        Preferiti preferito = new Preferiti();
        preferito.setUtente(utente);
        preferito.setConfronto(confronto);

        preferitiRepository.save(preferito);
    }

    // RIMUOVI DAI PREFERITI
    public void rimuoviDaiPreferiti(Integer utenteId, Integer confrontoId) {
        if (!preferitiRepository.existsByUtenteIdAndConfrontoId(utenteId, confrontoId)) {
            throw new RuntimeException("Preferito non trovato");
        }
        preferitiRepository.deleteByUtenteIdAndConfrontoId(utenteId, confrontoId);
    }

    // RECUPERA TUTTI I PREFERITI DI UN UTENTE
    @Transactional(readOnly = true)
    public List<Preferiti> getPreferitiUtente(Integer utenteId) {
        return preferitiRepository.findByUtenteIdOrderByDataAggiuntaDesc(utenteId);
    }

    // Se esiste lo toglie, se non esiste lo aggiunge con un solo click sul bottone
    public boolean togglePreferito(Integer utenteId, Integer confrontoId) {
        Optional<Preferiti> esistente = preferitiRepository.findByUtenteIdAndConfrontoId(utenteId, confrontoId);

        if (esistente.isPresent()) {
            preferitiRepository.delete(esistente.get());
            return false; // Rimosso
        } else {
            aggiungiAiPreferiti(utenteId, confrontoId);
            return true; // Aggiunto
        }
    }

    // CONTEGGIO PREFERITI
    @Transactional(readOnly = true)
    public long contaPreferiti(Integer utenteId) {
        return preferitiRepository.countByUtenteId(utenteId);
    }
}
