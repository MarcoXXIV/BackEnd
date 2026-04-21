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

        int[] punteggi = new int[]{0, 0};

        assegnaPunti(punteggi, a1.getPrezzo(), a2.getPrezzo(), 2, 10000.0, 0.10, 2.0, false);

        assegnaPunti(punteggi, a1.getCv(), a2.getCv(), 6, 50.0, 0.12, 2.5, true);
        assegnaPunti(punteggi, a1.getCoppia(), a2.getCoppia(), 5, 50.0, 0.10, 2.3, true);
        assegnaPunti(punteggi, a1.getCilindrata(), a2.getCilindrata(), 2, 250.0, 0.08, 1.8, true);

        assegnaPunti(punteggi, a1.getVelocitaMax(), a2.getVelocitaMax(), 8, 10.0, 0.15, 2.8, true);

        assegnaPunti(punteggi, a1.getAccelerazione0100(), a2.getAccelerazione0100(), 10, 0.5, 0.25, 3.0, false);
        assegnaPunti(punteggi, a1.getAccelerazione100200(), a2.getAccelerazione100200(), 9, 1.0, 0.20, 3.0, false);
        assegnaPunti(punteggi, a1.getAccelerazione80140(), a2.getAccelerazione80140(), 7, 0.5, 0.18, 2.7, false);

        assegnaPunti(punteggi, a1.getPesoKg(), a2.getPesoKg(), 4, 100.0, 0.10, 2.2, false);
        assegnaPunti(punteggi, a1.getConsumoMedio(), a2.getConsumoMedio(), 3, 1.0, 0.12, 2.0, false);
        assegnaPunti(punteggi, a1.getRapportoPesoPotenza(), a2.getRapportoPesoPotenza(), 8, 0.5, 0.20, 3.0, false);
        assegnaPunti(punteggi, a1.getCo2(), a2.getCo2(), 2, 20.0, 0.08, 1.8, false);

        Confronto c = new Confronto();
        c.setUtente(utente);
        c.setAuto1(a1);
        c.setAuto2(a2);
        c.setPunteggio1(punteggi[0]);
        c.setPunteggio2(punteggi[1]);
        c.setDataConfronto(java.time.LocalDateTime.now());

        return confrontoMapper.mapToDTO(confrontoRepository.save(c));
    }

    private void assegnaPunti(int[] punteggi,
                              Number n1,
                              Number n2,
                              int pesoBase,
                              double stepDifferenza,
                              double bonusPerStep,
                              double moltiplicatoreMax,
                              boolean higherBetter) {

        int cmp = compare(n1, n2);

        if (cmp == 0) {
            return;
        }

        double v1 = n1.doubleValue();
        double v2 = n2.doubleValue();

        int punti = calcolaPuntiDinamici(v1, v2, pesoBase, stepDifferenza, bonusPerStep, moltiplicatoreMax);

        boolean vinceAuto1 = higherBetter ? cmp > 0 : cmp < 0;

        if (vinceAuto1) {
            punteggi[0] += punti;
        } else {
            punteggi[1] += punti;
        }
    }

    private int calcolaPuntiDinamici(double v1,
                                     double v2,
                                     int pesoBase,
                                     double stepDifferenza,
                                     double bonusPerStep,
                                     double moltiplicatoreMax) {

        double differenza = Math.abs(v1 - v2);

        double steps = stepDifferenza > 0 ? (differenza / stepDifferenza) : 0.0;

        double moltiplicatore = 1.0 + (steps * bonusPerStep);

        moltiplicatore = Math.min(moltiplicatore, moltiplicatoreMax);

        return Math.max(1, (int) Math.round(pesoBase * moltiplicatore));
    }

    private int compare(Number n1, Number n2) {
        if (n1 == null || n2 == null) {
            return 0;
        }
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