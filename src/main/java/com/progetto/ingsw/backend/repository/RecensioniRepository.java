package com.progetto.ingsw.backend.repository;

import com.progetto.ingsw.backend.model.Recensioni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecensioniRepository extends JpaRepository<Recensioni, Integer> {

    // Recupera tutte le recensioni scritte per un determinato confronto (es. BMW vs Audi)
    List<Recensioni> findByConfrontoIdOrderByDataRecensioneDesc(Integer confrontoId);

    // 2. RECENSIONI DELL'UTENTE
    // Tutte le opinioni espresse da un utente sulle varie sfide
    List<Recensioni> findByUtenteId(Integer utenteId);

    // Calcola il voto medio ricevuto da una specifica sfida
    @Query("SELECT AVG(r.voto) FROM Recensioni r WHERE r.confronto.id = :confrontoId")
    Double getVotoMedioByConfrontoId(@Param("confrontoId") Integer confrontoId);

    // Un utente può recensire una sfida specifica una sola volta
    boolean existsByUtenteIdAndConfrontoId(Integer utenteId, Integer confrontoId);

    void deleteByUtenteIdAndConfrontoId(Integer utenteId, Integer confrontoId);
}
