package com.progetto.ingsw.backend.service;

import com.progetto.ingsw.backend.dto.ConfrontoDTO;
import com.progetto.ingsw.backend.mapper.ConfrontoMapper;
import com.progetto.ingsw.backend.model.Auto;
import com.progetto.ingsw.backend.model.Confronto;
import com.progetto.ingsw.backend.model.Utente;
import com.progetto.ingsw.backend.repository.AutoRepository;
import com.progetto.ingsw.backend.repository.ConfrontoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfrontoService {

    private final ConfrontoRepository confrontoRepository;
    private final AutoRepository autoRepository;
    private final UtenteService utenteService;
    private final ConfrontoMapper confrontoMapper;

    private int firstId(int id1, int id2) {
        return Math.min(id1, id2);
    }

    private int secondId(int id1, int id2) {
        return Math.max(id1, id2);
    }


    @Transactional
    public ConfrontoDTO eseguiBattaglia(Integer auto1Id, Integer auto2Id) {
        Utente utente = utenteService.getUtenteLoggatoEntity();

        Auto a1 = autoRepository.findById(auto1Id)
                .orElseThrow(() -> new RuntimeException("Auto 1 non trovata"));
        Auto a2 = autoRepository.findById(auto2Id)
                .orElseThrow(() -> new RuntimeException("Auto 2 non trovata"));

        Optional<Confronto> esistente = confrontoRepository.findByUtenteAndAuto1AndAuto2(utente, a1, a2);

        if (esistente.isPresent()) {
            return confrontoMapper.mapToDTO(esistente.get());
        }

        int p1 = 0;
        int p2 = 0;

        if (compare(a1.getPrezzo(), a2.getPrezzo()) > 0) p1++; else if (compare(a1.getPrezzo(), a2.getPrezzo()) < 0) p2++;
        if (compare(a1.getCv(), a2.getCv()) > 0) p1++; else if (compare(a1.getCv(), a2.getCv()) < 0) p2++;
        if (compare(a1.getVelocitaMax(), a2.getVelocitaMax()) > 0) p1++; else if (compare(a1.getVelocitaMax(), a2.getVelocitaMax()) < 0) p2++;
        if (compare(a1.getCoppia(), a2.getCoppia()) > 0) p1++; else if (compare(a1.getCoppia(), a2.getCoppia()) < 0) p2++;
        if (compare(a1.getCilindrata(), a2.getCilindrata()) > 0) p1++; else if (compare(a1.getCilindrata(), a2.getCilindrata()) < 0) p2++;

        // Più basso è meglio
        if (compare(a1.getAccelerazione0100(), a2.getAccelerazione0100()) < 0) p1++; else if (compare(a1.getAccelerazione0100(), a2.getAccelerazione0100()) > 0) p2++;
        if (compare(a1.getAccelerazione100200(), a2.getAccelerazione100200()) < 0) p1++; else if (compare(a1.getAccelerazione100200(), a2.getAccelerazione100200()) > 0) p2++;
        if (compare(a1.getAccelerazione80140(), a2.getAccelerazione80140()) < 0) p1++; else if (compare(a1.getAccelerazione80140(), a2.getAccelerazione80140()) > 0) p2++;
        if (compare(a1.getPesoKg(), a2.getPesoKg()) < 0) p1++; else if (compare(a1.getPesoKg(), a2.getPesoKg()) > 0) p2++;
        if (compare(a1.getConsumoMedio(), a2.getConsumoMedio()) < 0) p1++; else if (compare(a1.getConsumoMedio(), a2.getConsumoMedio()) > 0) p2++;
        if (compare(a1.getRapportoPesoPotenza(), a2.getRapportoPesoPotenza()) < 0) p1++; else if (compare(a1.getRapportoPesoPotenza(), a2.getRapportoPesoPotenza()) > 0) p2++;
        if (compare(a1.getCo2(), a2.getCo2()) < 0) p1++; else if (compare(a1.getCo2(), a2.getCo2()) > 0) p2++;

        Confronto c = new Confronto();
        c.setUtente(utente);
        c.setAuto1(a1);
        c.setAuto2(a2);
        c.setPunteggio1(p1);
        c.setPunteggio2(p2);
        c.setDataConfronto(java.time.LocalDateTime.now());

        return confrontoMapper.mapToDTO(confrontoRepository.save(c));
    }

    private int compare(Number n1, Number n2) {
        if (n1 == null || n2 == null) return 0;
        return Double.compare(n1.doubleValue(), n2.doubleValue());
    }

    @Transactional(readOnly = true)
    public List<ConfrontoDTO> getCronologiaUtente() {
        Utente u = utenteService.getUtenteLoggatoEntity();
        return confrontoRepository.findByUtenteIdOrderByDataConfrontoDesc(u.getId())
                .stream().map(confrontoMapper::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConfrontoDTO> getSfideRecenti() {
        return confrontoRepository.findTop10ByOrderByDataConfrontoDesc()
                .stream().map(confrontoMapper::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long getNumeroSfideUtente() {
        return confrontoRepository.countByUtenteId(utenteService.getUtenteLoggatoEntity().getId());
    }

    // Metodo implementato per il trend
    @Transactional(readOnly = true)
    public List<Object[]> getAutoPiuConfrontate() {
        return confrontoRepository.findMostComparedCars();
    }

    @Transactional
    public void cancellaMiaCronologia() {
        Utente u = utenteService.getUtenteLoggatoEntity();

        long count = confrontoRepository.countByUtenteId(u.getId());
        if (count == 0) {
            throw new RuntimeException("Non hai confronti da eliminare nella tua cronologia.");
        }

        confrontoRepository.deleteByUtenteId(u.getId());
    }

    @Transactional(readOnly = true)
    public boolean sfidaGiaFatta(Integer auto1Id, Integer auto2Id) {
        Integer utenteId = utenteService.getUtenteLoggatoEntity().getId();
        return confrontoRepository.existsByUtenteIdAndAuto1IdAndAuto2Id(utenteId, auto1Id, auto2Id);
    }
}