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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "auto1_id")
    private Auto auto1;

    @ManyToOne
    @JoinColumn(name = "auto2_id")
    private Auto auto2;

    @Column(nullable = false)
    private Integer voto;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String commento;

    @Column(name = "data_recensione", nullable = false, updatable = false)
    private LocalDateTime dataRecensione;

    @PrePersist
    public void prePersist() {
        if (this.dataRecensione == null) {
            this.dataRecensione = LocalDateTime.now();
        }
    }
}
