package com.progetto.ingsw.backend.service;

import com.progetto.ingsw.backend.model.Utente;
import com.progetto.ingsw.backend.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Recupera i dati del profilo
    public Utente getProfilo(Integer id) {
        return utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    // Metodo per verificare se l'utente ha i permessi da Admin
    public boolean isUserAdmin(Integer utenteId) {
        return utenteRepository.findById(utenteId)
                .map(u -> "ADMIN".equals(u.getRuolo()))
                .orElse(false);
    }

    // Aggiorna le informazioni base
    public Utente aggiornaUtente(Integer id, Utente nuoviDati) {
        Utente esistente = getProfilo(id);
        esistente.setNome(nuoviDati.getNome());
        esistente.setCognome(nuoviDati.getCognome());
        return utenteRepository.save(esistente);
    }

    @Transactional
    public void cambiaPassword(Integer id, String vecchiaPassword, String nuovaPassword) {
        // 1. Recupero l'utente dal DB
        Utente utente = getProfilo(id);

        // 2. Controllo se la vecchia password fornita coincide con quella (criptata) sul DB
        // .matches(password_chiaro, password_criptata)
        if (!passwordEncoder.matches(vecchiaPassword, utente.getPassword())) {
            throw new RuntimeException("La vecchia password non è corretta");
        }

        // 3. Controllo se la nuova password è uguale alla vecchia (opzionale ma consigliato)
        if (vecchiaPassword.equals(nuovaPassword)) {
            throw new RuntimeException("La nuova password non può essere uguale alla precedente");
        }

        // 4. Cripto la nuova password prima di salvarla
        String passwordCriptata = passwordEncoder.encode(nuovaPassword);
        utente.setPassword(passwordCriptata);

        // 5. Salvo l'utente aggiornato
        utenteRepository.save(utente);
    }
}
