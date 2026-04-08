package com.progetto.ingsw.backend.repository;

import com.progetto.ingsw.backend.model.Recensioni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecensioniRepository extends JpaRepository<Recensioni, Integer> {

    List<Recensioni> findByAuto1_IdAndAuto2_IdOrderByDataRecensioneDesc(Integer auto1, Integer auto2);

    List<Recensioni> findByUtente_IdOrderByDataRecensioneDesc(Integer utenteId);

    @Query("SELECT AVG(r.voto) FROM Recensioni r WHERE r.auto1.id = :auto1 AND r.auto2.id = :auto2")
    Double getVotoMedioByAutoIds(@Param("auto1") Integer auto1Id,
                                 @Param("auto2") Integer auto2Id);

    boolean existsByUtente_IdAndAuto1_IdAndAuto2_Id(Integer utenteId, Integer auto1, Integer auto2);

    void deleteByUtente_IdAndAuto1_IdAndAuto2_Id(Integer utenteId, Integer auto1, Integer auto2);
}
