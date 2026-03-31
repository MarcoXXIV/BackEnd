package com.progetto.ingsw.backend.service;

import com.progetto.ingsw.backend.dto.RecensioneDTO;
import com.progetto.ingsw.backend.mapper.RecensioniMapper;
import com.progetto.ingsw.backend.model.Confronto;
import com.progetto.ingsw.backend.model.Recensioni;
import com.progetto.ingsw.backend.model.Utente;
import com.progetto.ingsw.backend.repository.ConfrontoRepository;
import com.progetto.ingsw.backend.repository.RecensioniRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RecensioniService {

    private final RecensioniRepository recensioniRepository;
    private final UtenteService utenteService;
    private final ConfrontoRepository confrontoRepository;
    private final RecensioniMapper recensioniMapper;

    public RecensioneDTO scriviRecensione(Integer confrontoId, Integer voto, String commento) {
        Utente u = utenteService.getUtenteLoggatoEntity();
        if (recensioniRepository.existsByUtenteIdAndConfrontoId(u.getId(), confrontoId)) {
            throw new RuntimeException("Hai già recensito questa sfida");
        }
        if (voto < 1 || voto > 5) throw new RuntimeException("Voto non valido");

        Confronto c = confrontoRepository.findById(confrontoId).orElseThrow(() -> new RuntimeException("Sfida non trovata"));

        Recensioni r = new Recensioni();
        r.setUtente(u);
        r.setConfronto(c);
        r.setVoto(voto);
        r.setCommento(commento);
        r.setDataRecensione(java.time.LocalDateTime.now());

        return recensioniMapper.mapToDTO(recensioniRepository.save(r));
    }

    @Transactional(readOnly = true)
    public List<RecensioneDTO> getRecensioniPerSfida(Integer confrontoId) {
        return recensioniRepository.findByConfrontoIdOrderByDataRecensioneDesc(confrontoId)
                .stream().map(recensioniMapper::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecensioneDTO> getMieRecensioni() {
        Utente u = utenteService.getUtenteLoggatoEntity();
        return recensioniRepository.findByUtenteId(u.getId())
                .stream().map(recensioniMapper::mapToDTO).collect(Collectors.toList());
    }

    public void eliminaRecensione(Integer confrontoId) {
        Utente u = utenteService.getUtenteLoggatoEntity();
        recensioniRepository.deleteByUtenteIdAndConfrontoId(u.getId(), confrontoId);
    }

    @Transactional(readOnly = true)
    public Double getMediaVoti(Integer confrontoId) {
        Double media = recensioniRepository.getVotoMedioByConfrontoId(confrontoId);
        return (media != null) ? media : 0.0;
    }
}