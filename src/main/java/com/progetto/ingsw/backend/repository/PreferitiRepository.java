package com.progetto.ingsw.backend.repository;

import com.progetto.ingsw.backend.model.Preferiti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PreferitiRepository extends JpaRepository<Preferiti, Integer> {

    boolean existsByUtenteIdAndConfrontoId(Integer utenteId, Integer confrontoId);

    Optional<Preferiti> findByUtenteIdAndConfrontoId(Integer utenteId, Integer confrontoId);

    @Modifying
    @Transactional
    void deleteByUtenteIdAndConfrontoId(Integer utenteId, Integer confrontoId);

    long countByUtenteId(Integer utenteId);

    @Query("""
        select distinct p
        from Preferiti p
        join fetch p.confronto c
        left join fetch c.auto1
        left join fetch c.auto2
        where p.utente.id = :utenteId
        order by p.dataAggiunta desc
    """)

    List<Preferiti> findAllCompletiByUtenteId(@Param("utenteId") Integer utenteId);
}
