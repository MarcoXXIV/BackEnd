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
        // Recupero oggetti dal DB con gestione errore
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + utenteId));
        Auto a1 = autoRepository.findById(auto1Id)
                .orElseThrow(() -> new RuntimeException("Auto 1 non trovata con ID: " + auto1Id));
        Auto a2 = autoRepository.findById(auto2Id)
                .orElseThrow(() -> new RuntimeException("Auto 2 non trovata con ID: " + auto2Id));

        int p1 = 0;
        int p2 = 0;

        // --- LOGICA DI CONFRONTO (22 Parametri) ---

        // PIÙ ALTO È MEGLIO (+)
        if (compare(a1.getPrezzo(), a2.getPrezzo()) > 0) p1++; else if (compare(a1.getPrezzo(), a2.getPrezzo()) < 0) p2++;
        if (compare(a1.getCv(), a2.getCv()) > 0) p1++; else if (compare(a1.getCv(), a2.getCv()) < 0) p2++;
        if (compare(a1.getVelocitaMax(), a2.getVelocitaMax()) > 0) p1++; else if (compare(a1.getVelocitaMax(), a2.getVelocitaMax()) < 0) p2++;
        if (compare(a1.getCilindri(), a2.getCilindri()) > 0) p1++; else if (compare(a1.getCilindri(), a2.getCilindri()) < 0) p2++;
        if (compare(a1.getCoppia(), a2.getCoppia()) > 0) p1++; else if (compare(a1.getCoppia(), a2.getCoppia()) < 0) p2++;
        if (compare(a1.getCilindrata(), a2.getCilindrata()) > 0) p1++; else if (compare(a1.getCilindrata(), a2.getCilindrata()) < 0) p2++;
        if (compare(a1.getAnno(), a2.getAnno()) > 0) p1++; else if (compare(a1.getAnno(), a2.getAnno()) < 0) p2++;

        // PIÙ BASSO È MEGLIO (-)
        if (compare(a1.getAccelerazione0100(), a2.getAccelerazione0100()) < 0) p1++; else if (compare(a1.getAccelerazione0100(), a2.getAccelerazione0100()) > 0) p2++;
        if (compare(a1.getAccelerazione100200(), a2.getAccelerazione100200()) < 0) p1++; else if (compare(a1.getAccelerazione100200(), a2.getAccelerazione100200()) > 0) p2++;
        if (compare(a1.getAccelerazione80140(), a2.getAccelerazione80140()) < 0) p1++; else if (compare(a1.getAccelerazione80140(), a2.getAccelerazione80140()) > 0) p2++;
        if (compare(a1.getPesoKg(), a2.getPesoKg()) < 0) p1++; else if (compare(a1.getPesoKg(), a2.getPesoKg()) > 0) p2++;
        if (compare(a1.getConsumoMedio(), a2.getConsumoMedio()) < 0) p1++; else if (compare(a1.getConsumoMedio(), a2.getConsumoMedio()) > 0) p2++;
        if (compare(a1.getRapportoPesoPotenza(), a2.getRapportoPesoPotenza()) < 0) p1++; else if (compare(a1.getRapportoPesoPotenza(), a2.getRapportoPesoPotenza()) > 0) p2++;
        if (compare(a1.getCo2(), a2.getCo2()) < 0) p1++; else if (compare(a1.getCo2(), a2.getCo2()) > 0) p2++;

        // Creazione dell'oggetto Confronto
        Confronto c = new Confronto();
        c.setUtente(utente);
        c.setAuto1(a1);
        c.setAuto2(a2);
        c.setPunteggio1(p1);
        c.setPunteggio2(p2);
        c.setDataConfronto(java.time.LocalDateTime.now()); // Fondamentale per la cronologia!

        return confrontoRepository.save(c);
    }

    /**
     * Metodo helper per confrontare valori evitando NullPointerException.
     * Restituisce 0 se i valori sono uguali o uno dei due è null.
     */
    private <T extends Comparable<T>> int compare(T v1, T v2) {
        if (v1 == null || v2 == null) return 0;
        return v1.compareTo(v2);
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