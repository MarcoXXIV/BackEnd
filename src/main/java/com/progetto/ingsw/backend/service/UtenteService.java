package com.progetto.ingsw.backend.service;

import com.progetto.ingsw.backend.dto.UtenteDTO;
import com.progetto.ingsw.backend.mapper.UtenteMapper;
import com.progetto.ingsw.backend.model.Utente;
import com.progetto.ingsw.backend.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final UtenteMapper utenteMapper;
    private final PasswordEncoder passwordEncoder;

    public Utente getUtenteLoggatoEntity() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return utenteRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Utente non loggato"));
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
        if (!passwordEncoder.matches(vecchia, u.getPassword())) throw new RuntimeException("Vecchia password errata");
        u.setPassword(passwordEncoder.encode(nuova));
        utenteRepository.save(u);
    }
}