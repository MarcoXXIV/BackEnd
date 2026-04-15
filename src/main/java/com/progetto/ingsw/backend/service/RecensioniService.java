package com.progetto.ingsw.backend.service;

import com.progetto.ingsw.backend.dto.RecensioneDTO;
import com.progetto.ingsw.backend.mapper.RecensioniMapper;
import com.progetto.ingsw.backend.model.Auto;
import com.progetto.ingsw.backend.model.Recensioni;
import com.progetto.ingsw.backend.model.Utente;
import com.progetto.ingsw.backend.repository.AutoRepository;
import com.progetto.ingsw.backend.repository.RecensioniRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RecensioniService {

    private final RecensioniRepository recensioniRepository;
    private final UtenteService utenteService;
    private final AutoRepository autoRepository;
    private final RecensioniMapper recensioniMapper;

    public RecensioneDTO scriviRecensione(Integer auto1, Integer auto2, Integer voto, String commento) {
        Utente utente = utenteService.getUtenteLoggatoEntity();

        int[] ids = normalizzaCoppia(auto1, auto2);
        auto1 = ids[0];
        auto2 = ids[1];

        if (voto == null || voto < 1 || voto > 5) {
            throw new IllegalArgumentException("Voto non valido");
        }

        if (commento == null || commento.trim().isEmpty()) {
            throw new IllegalArgumentException("Il commento non può essere vuoto");
        }

        if (recensioniRepository.existsByUtente_IdAndAuto1_IdAndAuto2_Id(
                utente.getId(), auto1, auto2)) {
            throw new IllegalStateException("Hai già recensito questa coppia di auto");
        }

        Auto Auto1 = autoRepository.findById(auto1)
                .orElseThrow(() -> new NoSuchElementException("Auto 1 non trovata"));

        Auto Auto2 = autoRepository.findById(auto2)
                .orElseThrow(() -> new NoSuchElementException("Auto 2 non trovata"));

        Recensioni recensione = new Recensioni();
        recensione.setUtente(utente);
        recensione.setAuto1(Auto1);
        recensione.setAuto2(Auto2);
        recensione.setVoto(voto);
        recensione.setCommento(commento.trim());

        Recensioni salvata = recensioniRepository.save(recensione);
        return recensioniMapper.mapToDTO(salvata);
    }

    @Transactional(readOnly = true)
    public List<RecensioneDTO> getRecensioniPerCoppia(Integer auto1Id, Integer auto2Id) {
        int[] ids = normalizzaCoppia(auto1Id, auto2Id);

        return recensioniRepository
                .findByAuto1_IdAndAuto2_IdOrderByDataRecensioneDesc(ids[0], ids[1])
                .stream()
                .map(recensioniMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecensioneDTO> getMieRecensioni() {
        Utente u = utenteService.getUtenteLoggatoEntity();

        return recensioniRepository.findByUtente_IdOrderByDataRecensioneDesc(u.getId())
                .stream()
                .map(recensioniMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    public void eliminaRecensione(Integer auto1Id, Integer auto2Id) {
        Utente u = utenteService.getUtenteLoggatoEntity();

        int[] ids = normalizzaCoppia(auto1Id, auto2Id);
        recensioniRepository.deleteByUtente_IdAndAuto1_IdAndAuto2_Id(u.getId(), ids[0], ids[1]);
    }

    @Transactional(readOnly = true)
    public Double getMediaVoti(Integer auto1Id, Integer auto2Id) {
        int[] ids = normalizzaCoppia(auto1Id, auto2Id);

        Double media = recensioniRepository.getVotoMedioByAutoIds(ids[0], ids[1]);
        return media != null ? media : 0.0;
    }

    private int[] normalizzaCoppia(Integer auto1Id, Integer auto2Id) {
        if (auto1Id == null || auto2Id == null) {
            throw new IllegalArgumentException("Entrambe le auto sono obbligatorie");
        }

        if (auto1Id.equals(auto2Id)) {
            throw new IllegalArgumentException("Non puoi recensire la stessa auto contro sé stessa");
        }

        return auto1Id < auto2Id
                ? new int[]{auto1Id, auto2Id}
                : new int[]{auto2Id, auto1Id};
    }

    @Transactional
    public void eliminaTutteLeRecensioniUtente() {
        Utente u = utenteService.getUtenteLoggatoEntity();
        recensioniRepository.deleteByUtente_Id(u.getId());
    }
}
