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

    @Transactional
    public Auto salvaAuto(Auto auto) {

        if (auto == null) {
            throw new RuntimeException("L'auto non può essere null");
        }

        // --- STRINGHE OBBLIGATORIE ---
        if (auto.getMarca() == null || auto.getMarca().isBlank()) {
            throw new RuntimeException("La marca è obbligatoria");
        }

        if (auto.getModello() == null || auto.getModello().isBlank()) {
            throw new RuntimeException("Il modello è obbligatorio");
        }

        if (auto.getCategoria() == null || auto.getCategoria().isBlank()) {
            throw new RuntimeException("La categoria è obbligatoria");
        }

        if (auto.getTrazione() == null || auto.getTrazione().isBlank()) {
            throw new RuntimeException("La trazione è obbligatoria");
        }

        if (auto.getAlimentazione() == null || auto.getAlimentazione().isBlank()) {
            throw new RuntimeException("L'alimentazione è obbligatoria");
        }

        if (auto.getCodice() == null || auto.getCodice().isBlank()) {
            throw new RuntimeException("Il codice è obbligatorio");
        }

        if (auto.getEuro() == null || auto.getEuro().isBlank()) {
            throw new RuntimeException("La classe euro è obbligatoria");
        }

        // --- NUMERICI ---
        if (auto.getAnno() < 1886) {
            throw new RuntimeException("Anno non valido");
        }

        if (auto.getPrezzo() < 0) {
            throw new RuntimeException("Prezzo non valido");
        }

        if (auto.getCv() < 0) {
            throw new RuntimeException("CV non validi");
        }

        if (auto.getVelocitaMax() < 0) {
            throw new RuntimeException("Velocità massima non valida");
        }

        if (auto.getAccelerazione0100() < 0) {
            throw new RuntimeException("Accelerazione 0-100 non valida");
        }

        if (auto.getAccelerazione100200() < 0) {
            throw new RuntimeException("Accelerazione 100-200 non valida");
        }

        if (auto.getAccelerazione80140() < 0) {
            throw new RuntimeException("Accelerazione 80-140 non valida");
        }

        if (auto.getCilindri() < 0) {
            throw new RuntimeException("Numero cilindri non valido");
        }

        if (auto.getPesoKg() < 0) {
            throw new RuntimeException("Peso non valido");
        }

        if (auto.getConsumoMedio() < 0) {
            throw new RuntimeException("Consumo medio non valido");
        }

        if (auto.getCoppia() < 0) {
            throw new RuntimeException("Coppia non valida");
        }

        if (auto.getRapportoPesoPotenza() < 0) {
            throw new RuntimeException("Rapporto peso/potenza non valido");
        }

        if (auto.getCilindrata() < 0) {
            throw new RuntimeException("Cilindrata non valida");
        }

        if (auto.getCo2() < 0) {
            throw new RuntimeException("CO2 non valida");
        }

        // --- NORMALIZZAZIONE STRINGHE ---
        auto.setMarca(auto.getMarca().trim());
        auto.setModello(auto.getModello().trim());
        auto.setCategoria(auto.getCategoria().trim());
        auto.setTrazione(auto.getTrazione().trim());
        auto.setAlimentazione(auto.getAlimentazione().trim());
        auto.setCodice(auto.getCodice().trim());
        auto.setEuro(auto.getEuro().trim());

        // --- SALVATAGGIO ---
        return autoRepository.save(auto);
    }


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
}
