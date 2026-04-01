package com.progetto.ingsw.backend.repository;

import com.progetto.ingsw.backend.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Integer> {

    // Cerca l'utente tramite email per verificare poi la password nel Service
    Optional<Utente> findByEmail(String email);

    // Evita errori di "Duplicate Key" lanciati dal database
    boolean existsByEmail(String email);

    // Utile se vuoi recuperare l'utente tramite nome e cognome (es. per una ricerca admin)
    Optional<Utente> findByNomeAndCognome(String nome, String cognome);

}