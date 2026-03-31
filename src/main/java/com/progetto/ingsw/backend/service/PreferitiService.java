package com.progetto.ingsw.backend.service;

import com.progetto.ingsw.backend.dto.PreferitoDTO;
import com.progetto.ingsw.backend.mapper.PreferitiMapper;
import com.progetto.ingsw.backend.model.Confronto;
import com.progetto.ingsw.backend.model.Preferiti;
import com.progetto.ingsw.backend.model.Utente;
import com.progetto.ingsw.backend.repository.ConfrontoRepository;
import com.progetto.ingsw.backend.repository.PreferitiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PreferitiService {

    private final PreferitiRepository preferitiRepository;
    private final UtenteService utenteService;
    private final ConfrontoRepository confrontoRepository;
    private final PreferitiMapper preferitiMapper;

    public void aggiungiAiPreferiti(Integer confrontoId) {
        Utente u = utenteService.getUtenteLoggatoEntity();
        if (preferitiRepository.existsByUtenteIdAndConfrontoId(u.getId(), confrontoId)) {
            throw new RuntimeException("Già presente nei preferiti");
        }
        Confronto c = confrontoRepository.findById(confrontoId).orElseThrow(() -> new RuntimeException("Confronto non trovato"));

        Preferiti p = new Preferiti();
        p.setUtente(u);
        p.setConfronto(c);
        p.setDataAggiunta(java.time.LocalDateTime.now());
        preferitiRepository.save(p);
    }

    public void rimuoviDaiPreferiti(Integer confrontoId) {
        Utente u = utenteService.getUtenteLoggatoEntity();
        preferitiRepository.deleteByUtenteIdAndConfrontoId(u.getId(), confrontoId);
    }

    @Transactional(readOnly = true)
    public List<PreferitoDTO> getPreferitiUtente() {
        Utente u = utenteService.getUtenteLoggatoEntity();
        return preferitiRepository.findByUtenteIdOrderByDataAggiuntaDesc(u.getId())
                .stream().map(preferitiMapper::mapToDTO).collect(Collectors.toList());
    }

    public boolean togglePreferito(Integer confrontoId) {
        Utente u = utenteService.getUtenteLoggatoEntity();
        Optional<Preferiti> esistente = preferitiRepository.findByUtenteIdAndConfrontoId(u.getId(), confrontoId);
        if (esistente.isPresent()) {
            preferitiRepository.delete(esistente.get());
            return false;
        } else {
            aggiungiAiPreferiti(confrontoId);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public long contaPreferiti() {
        return preferitiRepository.countByUtenteId(utenteService.getUtenteLoggatoEntity().getId());
    }
}