package com.progetto.ingsw.backend.service;

import com.progetto.ingsw.backend.model.Auto;
import com.progetto.ingsw.backend.repository.AutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // Migliora le prestazioni per le letture
public class AutoService {

    @Autowired
    private AutoRepository autoRepository;

    // RECUPERO TUTTO IL CATALOGO
    public List<Auto> getAllAuto() {
        return autoRepository.findAll();
    }

    // DETTAGLIO AUTO (Per la scheda tecnica singola)
    public Auto getAutoById(Integer id) {
        return autoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auto non trovata con ID: " + id));
    }

    // RICERCA FILTRATA (Fondamentale per la UI di Angular)
    public List<Auto> cercaAuto(String marca, String modello) {
        if (marca != null && modello != null) {
            return autoRepository.findByMarcaContainingIgnoreCaseOrModelloContainingIgnoreCase(marca, modello);
        } else if (marca != null) {
            return autoRepository.findByMarcaIgnoreCase(marca);
        } else {
            return autoRepository.findAll();
        }
    }

    /*
    da completare
    // AGGIUNTA/MODIFICA AUTO
    @Transactional
    public Auto salvaAuto(Auto auto) {
        // Possiamo aggiungere logiche di validazione (es. CV non negativi)
        if (auto.getCv() < 0) {
            throw new RuntimeException("I cavalli vapore non possono essere negativi!");
        }
        return autoRepository.save(auto);
    }
    */

    // Filtro per categoria (es: SUV, Berlina, Supercar)
    public List<Auto> getAutoPerCategoria(String categoria) {
        return autoRepository.findByCategoriaIgnoreCase(categoria);
    }

    // Le 10 auto più veloci nello scatto 0-100
    public List<Auto> getTopAccelerazione() {
        return autoRepository.findTop10ByOrderByAccelerazione0100Asc();
    }

    // Le 10 auto con più CV (Cavalli Vapore)
    public List<Auto> getTopCavalli() {
        return autoRepository.findTop10ByOrderByCvDesc();
    }

    // Le 10 auto più "Green" (meno CO2)
    public List<Auto> getPiuEcologiche() {
        return autoRepository.findTop10ByOrderByCo2Asc();
    }


    @Transactional
    public void eliminaAuto(Integer id) {
        if (!autoRepository.existsById(id)) {
            throw new RuntimeException("Impossibile eliminare: Auto con ID " + id + " non trovata.");
        }
        autoRepository.deleteById(id);
    }

    // usiamo direttamente il repository
    public List<Auto> getTopPotenza() {
        // Usiamo il metodo del repository che è molto più veloce
        return autoRepository.findTop10ByOrderByCvDesc().stream()
                .limit(5) // Se ne vuoi solo 5
                .collect(Collectors.toList());
    }
}
