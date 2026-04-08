package com.progetto.ingsw.backend.repository;

import com.progetto.ingsw.backend.model.Auto;
import com.progetto.ingsw.backend.model.Confronto;
import com.progetto.ingsw.backend.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfrontoRepository extends JpaRepository<Confronto, Integer> {

    // Recupera tutti i confronti fatti da un utente specifico (dal più recente)
    List<Confronto> findByUtenteIdOrderByDataConfrontoDesc(Integer utenteId);

    Optional<Confronto> findByAuto1IdAndAuto2Id(Integer auto1Id, Integer auto2Id);

    @Modifying
    @Transactional
    void deleteByUtenteId(Integer utenteId);

    // Utile per una sezione "Le auto più confrontate dalla community"
    @Query("SELECT c.auto1.id, COUNT(c) FROM Confronto c GROUP BY c.auto1.id ORDER BY COUNT(c) DESC")
    List<Object[]> findMostComparedCars();

    // Restituisce true se l'utente ha già fatto questo specifico confronto
    boolean existsByUtenteIdAndAuto1IdAndAuto2Id(Integer utenteId, Integer auto1Id, Integer auto2Id);

    // Utile per la sezione "Sfide Recenti" nella Home, gli ultimi 10 confronti sulla piattaforma
    List<Confronto> findTop10ByOrderByDataConfrontoDesc();

    long countByUtenteId(Integer utenteId);

    Optional<Confronto> findByUtenteAndAuto1AndAuto2(Utente utente, Auto a1, Auto a2);
}
