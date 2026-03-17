package com.progetto.ingsw.backend.repository;


import com.progetto.ingsw.backend.model.Preferiti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PreferitiRepository extends JpaRepository<Preferiti, Integer> {

    // Ottiene tutte le sfide salvate da un utente specifico
    List<Preferiti> findByUtenteIdOrderByDataAggiuntaDesc(Integer utenteId);

    // Verifica se il confronto è già nei preferiti (Usa ConfrontoId o Confronto_Id a seconda del campo)
    boolean existsByUtenteIdAndConfrontoId(Integer utenteId, Integer confrontoId);

    // Permette di togliere il confronto dai preferiti
    void deleteByUtenteIdAndConfrontoId(Integer utenteId, Integer confrontoId);

    // Conta i confronti preferiti
    long countByUtenteId(Integer utenteId);

    // Recupera l'oggetto singolo
    Optional<Preferiti> findByUtenteIdAndConfrontoId(Integer utenteId, Integer confrontoId);
}
