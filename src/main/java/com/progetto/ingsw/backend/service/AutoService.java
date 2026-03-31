package com.progetto.ingsw.backend.service;

import com.progetto.ingsw.backend.dto.AutoDTO;
import com.progetto.ingsw.backend.mapper.AutoMapper;
import com.progetto.ingsw.backend.model.Auto;
import com.progetto.ingsw.backend.repository.AutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AutoService {

    private final AutoRepository autoRepository;
    private final AutoMapper autoMapper;

    public List<AutoDTO> getAllAuto() {
        return autoRepository.findAll().stream()
                .map(autoMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    public AutoDTO getAutoById(Integer id) {
        Auto auto = autoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auto non trovata con ID: " + id));
        return autoMapper.mapToDTO(auto);
    }

    public List<AutoDTO> cercaAuto(String marca, String modello) {
        List<Auto> lista;

        // 1. Caso: Entrambi i filtri sono compilati (Ricerca specifica con AND)
        if (isNotBlank(marca) && isNotBlank(modello)) {
            lista = autoRepository.findByMarcaContainingIgnoreCaseAndModelloContainingIgnoreCase(marca, modello);
        }
        // 2. Caso: Solo la marca è compilata
        else if (isNotBlank(marca)) {
            lista = autoRepository.findByMarcaContainingIgnoreCase(marca);
        }
        // 3. Caso: Solo il modello è compilato (Questo risolverà il problema "Golf")
        else if (isNotBlank(modello)) {
            lista = autoRepository.findByModelloContainingIgnoreCase(modello);
        }
        // 4. Caso: Nessun filtro (Catalogo completo)
        else {
            lista = autoRepository.findAll();
        }

        return lista.stream()
                .map(autoMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    // Metodo helper per pulizia
    private boolean isNotBlank(String s) {
        return s != null && !s.isBlank();
    }

    public List<AutoDTO> getAny10Auto() {
        return autoRepository.findTop10By().stream()
                .map(autoMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<AutoDTO> getAutoPerCategoria(String categoria) {
        return autoRepository.findByCategoriaIgnoreCase(categoria).stream()
                .map(autoMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<AutoDTO> getTopAccelerazione() {
        return autoRepository.findTop10ByOrderByAccelerazione0100Asc().stream()
                .map(autoMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<AutoDTO> getTopCavalli() {
        return autoRepository.findTop10ByOrderByCvDesc().stream()
                .map(autoMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<AutoDTO> getPiuEcologiche() {
        return autoRepository.findTop10ByOrderByCo2Asc().stream()
                .map(autoMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AutoDTO salvaAuto(Auto auto) {
        if (auto == null) throw new RuntimeException("L'auto non può essere null");

        // VALIDAZIONI STRINGHE
        if (isInvalid(auto.getMarca())) throw new RuntimeException("La marca è obbligatoria");
        if (isInvalid(auto.getModello())) throw new RuntimeException("Il modello è obbligatorio");
        if (isInvalid(auto.getCategoria())) throw new RuntimeException("La categoria è obbligatoria");
        if (isInvalid(auto.getTrazione())) throw new RuntimeException("La trazione è obbligatoria");
        if (isInvalid(auto.getAlimentazione())) throw new RuntimeException("L'alimentazione è obbligatoria");
        if (isInvalid(auto.getCodice())) throw new RuntimeException("Il codice è obbligatorio");
        if (isInvalid(auto.getEuro())) throw new RuntimeException("La classe euro è obbligatoria");

        // VALIDAZIONI NUMERICHE
        if (auto.getAnno() < 1886) throw new RuntimeException("Anno non valido");
        if (auto.getPrezzo() < 0) throw new RuntimeException("Prezzo non valido");
        if (auto.getCv() < 0) throw new RuntimeException("CV non validi");
        if (auto.getVelocitaMax() < 0) throw new RuntimeException("Velocità massima non valida");
        if (auto.getAccelerazione0100() < 0) throw new RuntimeException("Accelerazione 0-100 non valida");
        if (auto.getAccelerazione100200() < 0) throw new RuntimeException("Accelerazione 100-200 non valida");
        if (auto.getAccelerazione80140() < 0) throw new RuntimeException("Accelerazione 80-140 non valida");
        if (auto.getCilindri() < 0) throw new RuntimeException("Numero cilindri non valido");
        if (auto.getPesoKg() < 0) throw new RuntimeException("Peso non valido");
        if (auto.getConsumoMedio() < 0) throw new RuntimeException("Consumo medio non valido");
        if (auto.getCoppia() < 0) throw new RuntimeException("Coppia non valida");
        if (auto.getRapportoPesoPotenza() < 0) throw new RuntimeException("Rapporto peso/potenza non valido");
        if (auto.getCilindrata() < 0) throw new RuntimeException("Cilindrata non valida");
        if (auto.getCo2() < 0) throw new RuntimeException("CO2 non valida");

        // NORMALIZZAZIONE
        auto.setMarca(auto.getMarca().trim());
        auto.setModello(auto.getModello().trim());
        auto.setCategoria(auto.getCategoria().trim());
        auto.setTrazione(auto.getTrazione().trim());
        auto.setAlimentazione(auto.getAlimentazione().trim());
        auto.setCodice(auto.getCodice().trim());
        auto.setEuro(auto.getEuro().trim());

        return autoMapper.mapToDTO(autoRepository.save(auto));
    }

    @Transactional
    public void eliminaAuto(Integer id) {
        if (!autoRepository.existsById(id)) throw new RuntimeException("Auto non trovata");
        autoRepository.deleteById(id);
    }

    private boolean isInvalid(String s) { return s == null || s.isBlank(); }
}