package com.progetto.ingsw.backend.repository;

import com.progetto.ingsw.backend.model.Auto;
import com.progetto.ingsw.backend.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoRepository extends JpaRepository<Auto, Integer> {

    List<Auto> findByMarcaContainingIgnoreCaseOrModelloContainingIgnoreCase(String marca, String modello);

    List<Auto> findByMarcaIgnoreCase(String marca);

    List<Auto> findByCategoriaIgnoreCase(String categoria);

    List<Auto> findTop10ByOrderByAccelerazione0100Asc();

    List<Auto> findTop10ByOrderByCvDesc();

    List<Auto> findTop10ByOrderByCo2Asc();

    List<Auto> findAutoById(Integer id);
}
