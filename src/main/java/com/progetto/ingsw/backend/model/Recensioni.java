package com.progetto.ingsw.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "recensioni")
@Data
public class Recensioni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "confronto_id", nullable = false)
    private Confronto confronto;

    @Column(nullable = false)
    private Integer voto;

    @Column(columnDefinition = "TEXT")
    private String commento;

    @Column(name = "data_recensione", insertable = false, updatable = false)
    private LocalDateTime dataRecensione;
}