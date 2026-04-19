package com.progetto.ingsw.backend.repository;

import com.progetto.ingsw.backend.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Integer> {

    Optional<Utente> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Utente> findByNomeAndCognome(String nome, String cognome);

    Optional<Utente> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}