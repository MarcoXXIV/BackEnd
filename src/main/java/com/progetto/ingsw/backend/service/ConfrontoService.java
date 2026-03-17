package com.progetto.ingsw.backend.service;

import com.progetto.ingsw.backend.model.Auto;
import com.progetto.ingsw.backend.model.Confronto;
import com.progetto.ingsw.backend.model.Utente;
import com.progetto.ingsw.backend.repository.AutoRepository;
import com.progetto.ingsw.backend.repository.ConfrontoRepository;
import com.progetto.ingsw.backend.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConfrontoService {

    @Autowired
    private ConfrontoRepository confrontoRepository;
    @Autowired
    private AutoRepository autoRepository;
    @Autowired
    private UtenteRepository utenteRepository;

    @Transactional
    public Confronto eseguiBattaglia(Integer utenteId, Integer auto1Id, Integer auto2Id) {
        // 1. Recupero oggetti dal DB
        Utente utente = utenteRepository.findById(utenteId).orElseThrow(() -> new RuntimeException("Utente non trovato"));
        Auto a1 = autoRepository.findById(auto1Id).orElseThrow(() -> new RuntimeException("Auto 1 non trovata"));
        Auto a2 = autoRepository.findById(auto2Id).orElseThrow(() -> new RuntimeException("Auto 2 non trovata"));

        int p1 = 0;
        int p2 = 0;

        // PIÙ ALTO È MEGLIO (+)
        if (a1.getPrezzo() > a2.getPrezzo()) p1++; else if (a2.getPrezzo() > a1.getPrezzo()) p2++;
        if (a1.getCv() > a2.getCv()) p1++; else if (a2.getCv() > a1.getCv()) p2++;
        if (a1.getVelocitaMax() > a2.getVelocitaMax()) p1++; else if (a2.getVelocitaMax() > a1.getVelocitaMax()) p2++;
        if (a1.getCilindri() > a2.getCilindri()) p1++; else if (a2.getCilindri() > a1.getCilindri()) p2++;
        if (a1.getCoppia() > a2.getCoppia()) p1++; else if (a2.getCoppia() > a1.getCoppia()) p2++;
        if (a1.getCilindrata() > a2.getCilindrata()) p1++; else if (a2.getCilindrata() > a1.getCilindrata()) p2++;
        if (a1.getAnno() > a2.getAnno()) p1++; else if (a2.getAnno() > a1.getAnno()) p2++;

        // PIÙ BASSO È MEGLIO (-)
        if (a1.getAccelerazione0100() < a2.getAccelerazione0100()) p1++; else if (a2.getAccelerazione0100() < a1.getAccelerazione0100()) p2++;
        if (a1.getAccelerazione100200() < a2.getAccelerazione100200()) p1++; else if (a2.getAccelerazione100200() < a1.getAccelerazione100200()) p2++;
        if (a1.getAccelerazione80140() < a2.getAccelerazione80140()) p1++; else if (a2.getAccelerazione80140() < a1.getAccelerazione80140()) p2++;

        if (a1.getPesoKg() < a2.getPesoKg()) p1++; else if (a2.getPesoKg() < a1.getPesoKg()) p2++;
        if (a1.getConsumoMedio() < a2.getConsumoMedio()) p1++; else if (a2.getConsumoMedio() < a1.getConsumoMedio()) p2++;
        if (a1.getRapportoPesoPotenza() < a2.getRapportoPesoPotenza()) p1++; else if (a2.getRapportoPesoPotenza() < a1.getRapportoPesoPotenza()) p2++;
        if (a1.getCo2() < a2.getCo2()) p1++; else if (a2.getCo2() < a1.getCo2()) p2++;

        Confronto c = new Confronto();
        c.setUtente(utente);
        c.setAuto1(a1);
        c.setAuto2(a2);
        c.setPunteggio1(p1);
        c.setPunteggio2(p2);

        return confrontoRepository.save(c);
    }

    // Angular userà questo per la pagina "I miei confronti"
    public List<Confronto> getCronologiaUtente(Integer utenteId) {
        return confrontoRepository.findByUtenteIdOrderByDataConfrontoDesc(utenteId);
    }

    // Mostra alla community gli ultimi duelli avvenuti
    public List<Confronto> getSfideRecenti() {
        return confrontoRepository.findTop10ByOrderByDataConfrontoDesc();
    }

    // Utile per mostrare nel profilo: "Hai effettuato 45 sfide!"
    public long getNumeroSfideUtente(Integer utenteId) {
        return confrontoRepository.countByUtenteId(utenteId);
    }

    // Utile se vuoi evitare che un utente prema "Confronta" 10 volte sulle stesse auto
    public boolean sfidaGiaFatta(Integer utenteId, Integer a1, Integer a2) {
        return confrontoRepository.existsByUtenteIdAndAuto1IdAndAuto2Id(utenteId, a1, a2);
    }

    // Qui prendiamo i dati grezzi dal Repository e potremmo trasformarli
    // in qualcosa di più leggibile per il frontend
    public List<Object[]> getAutoPiuConfrontate() {
        return confrontoRepository.findMostComparedCars();
    }

}