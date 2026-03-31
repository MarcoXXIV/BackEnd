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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfrontoService {

    private final ConfrontoRepository confrontoRepository;
    private final AutoRepository autoRepository;
    private final UtenteService utenteService;
    private final ConfrontoMapper confrontoMapper;

    @Transactional
    public ConfrontoDTO eseguiBattaglia(Integer auto1Id, Integer auto2Id) {
        Utente utente = utenteService.getUtenteLoggatoEntity();

        // 2. CONTROLLO ANTI-DUPLICATI: Se la sfida esiste già, blocchiamo tutto
        if (sfidaGiaFatta(auto1Id, auto2Id)) {
            throw new RuntimeException("Hai già effettuato questo confronto. Controlla la tua cronologia!");
        }

        Auto a1 = autoRepository.findById(auto1Id).orElseThrow(() -> new RuntimeException("Auto 1 non trovata"));
        Auto a2 = autoRepository.findById(auto2Id).orElseThrow(() -> new RuntimeException("Auto 2 non trovata"));

        int p1 = 0; int p2 = 0;

        // --- LOGICA DI CONFRONTO ---
        if (compare(a1.getPrezzo(), a2.getPrezzo()) > 0) p1++; else if (compare(a1.getPrezzo(), a2.getPrezzo()) < 0) p2++;
        if (compare(a1.getCv(), a2.getCv()) > 0) p1++; else if (compare(a1.getCv(), a2.getCv()) < 0) p2++;
        if (compare(a1.getVelocitaMax(), a2.getVelocitaMax()) > 0) p1++; else if (compare(a1.getVelocitaMax(), a2.getVelocitaMax()) < 0) p2++;
        if (compare(a1.getCilindri(), a2.getCilindri()) > 0) p1++; else if (compare(a1.getCilindri(), a2.getCilindri()) < 0) p2++;
        if (compare(a1.getCoppia(), a2.getCoppia()) > 0) p1++; else if (compare(a1.getCoppia(), a2.getCoppia()) < 0) p2++;
        if (compare(a1.getCilindrata(), a2.getCilindrata()) > 0) p1++; else if (compare(a1.getCilindrata(), a2.getCilindrata()) < 0) p2++;
        if (compare(a1.getAnno(), a2.getAnno()) > 0) p1++; else if (compare(a1.getAnno(), a2.getAnno()) < 0) p2++;

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

    private <T extends Comparable<T>> int compare(T v1, T v2) {
        if (v1 == null || v2 == null) return 0;
        return v1.compareTo(v2);
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
        // Recuperiamo l'utente dal Token (Sicurezza!)
        Utente u = utenteService.getUtenteLoggatoEntity();

        // Verifichiamo se ha effettivamente dei confronti prima di provare a cancellare
        long count = confrontoRepository.countByUtenteId(u.getId());
        if (count == 0) {
            throw new RuntimeException("Non hai confronti da eliminare nella tua cronologia.");
        }

        confrontoRepository.deleteByUtenteId(u.getId());
    }

    // Metodo implementato per il controllo esistenza sfida
    @Transactional(readOnly = true)
    public boolean sfidaGiaFatta(Integer auto1Id, Integer auto2Id) {
        Integer utenteId = utenteService.getUtenteLoggatoEntity().getId();
        return confrontoRepository.existsByUtenteIdAndAuto1IdAndAuto2Id(utenteId, auto1Id, auto2Id);
    }
}