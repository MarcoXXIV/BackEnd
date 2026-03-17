package com.progetto.ingsw.backend.service;

import com.progetto.ingsw.backend.model.Confronto;
import com.progetto.ingsw.backend.model.Recensioni;
import com.progetto.ingsw.backend.model.Utente;
import com.progetto.ingsw.backend.repository.ConfrontoRepository;
import com.progetto.ingsw.backend.repository.RecensioniRepository;
import com.progetto.ingsw.backend.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecensioniService {

    @Autowired
    private RecensioniRepository recensioniRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private ConfrontoRepository confrontoRepository;

    // LASCIA UNA RECENSIONE
    public Recensioni scriviRecensione(Integer utenteId, Integer confrontoId, Integer voto, String commento) {
        // Regola: Un utente può recensire una sfida una sola volta
        if (recensioniRepository.existsByUtenteIdAndConfrontoId(utenteId, confrontoId)) {
            throw new RuntimeException("Hai già lasciato una recensione per questo confronto");
        }

        // Validazione voto (es. da 1 a 5)
        if (voto < 1 || voto > 5) {
            throw new RuntimeException("Il voto deve essere compreso tra 1 e 5");
        }

        Utente u = utenteRepository.findById(utenteId).orElseThrow();
        Confronto c = confrontoRepository.findById(confrontoId).orElseThrow();

        Recensioni r = new Recensioni();
        r.setUtente(u);
        r.setConfronto(c);
        r.setVoto(voto);
        r.setCommento(commento);

        return recensioniRepository.save(r);
    }

    // RECUPERA RECENSIONI DI UN CONFRONTO
    @Transactional(readOnly = true)
    public List<Recensioni> getRecensioniPerSfida(Integer confrontoId) {
        return recensioniRepository.findByConfrontoIdOrderByDataRecensioneDesc(confrontoId);
    }

    // MEDIA VOTI
    @Transactional(readOnly = true)
    public Double getMediaVoti(Integer confrontoId) {
        Double media = recensioniRepository.getVotoMedioByConfrontoId(confrontoId);
        return (media != null) ? media : 0.0;
    }

    // ELIMINA RECENSIONE
    public void eliminaRecensione(Integer utenteId, Integer confrontoId) {
        recensioniRepository.deleteByUtenteIdAndConfrontoId(utenteId, confrontoId);
    }

    // RECENSIONI PER PROFILO UTENTE
    @Transactional(readOnly = true)
    public List<Recensioni> getMieRecensioni(Integer utenteId) {
        return recensioniRepository.findByUtenteId(utenteId);
    }
}
