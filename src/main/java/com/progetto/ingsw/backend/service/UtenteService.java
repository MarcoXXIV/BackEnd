package com.progetto.ingsw.backend.service;

import com.progetto.ingsw.backend.dto.UtenteDTO;
import com.progetto.ingsw.backend.mapper.UtenteMapper;
import com.progetto.ingsw.backend.model.Utente;
import com.progetto.ingsw.backend.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final UtenteMapper utenteMapper;
    private final PasswordEncoder passwordEncoder;

    public Utente getUtenteLoggatoEntity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            throw new RuntimeException("Utente non autenticato");
        }

        String email = auth.getName();

        return utenteRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con email: " + email));
    }

    @Transactional(readOnly = true)
    public UtenteDTO getProfiloDinamico() {
        return utenteMapper.mapToDTO(getUtenteLoggatoEntity());
    }

    public UtenteDTO aggiornaUtente(Utente nuoviDati) {
        Utente esistente = getUtenteLoggatoEntity();
        esistente.setNome(nuoviDati.getNome());
        esistente.setCognome(nuoviDati.getCognome());
        return utenteMapper.mapToDTO(utenteRepository.save(esistente));
    }

    public boolean isUserAdmin() {
        Utente u = getUtenteLoggatoEntity();
        return u.getRuolo() != null && u.getRuolo().equalsIgnoreCase("ADMIN");
    }

    public void cambiaPassword(String vecchia, String nuova) {
        Utente u = getUtenteLoggatoEntity();

        if (!passwordEncoder.matches(vecchia, u.getPassword())) {
            throw new RuntimeException("Vecchia password errata");
        }

        u.setPassword(passwordEncoder.encode(nuova));
        utenteRepository.save(u);
    }

    public List<UtenteDTO> getAllUtenti() {
        return utenteRepository.findAll().stream()
                .map(utenteMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    public UtenteDTO cambiaRuolo(Integer id, String nuovoRuolo) {
        Utente u = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        u.setRuolo(nuovoRuolo.toUpperCase());
        return utenteMapper.mapToDTO(utenteRepository.save(u));
    }

    public void eliminaUtente(Integer id) {
        if (!utenteRepository.existsById(id)) {
            throw new RuntimeException("Utente non trovato");
        }
        utenteRepository.deleteById(id);
    }
}
