package com.progetto.ingsw.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "confronto")
@Data
public class Confronto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "auto1_id")
    private Auto auto1;

    @ManyToOne
    @JoinColumn(name = "auto2_id")
    private Auto auto2;

    @Column(name = "punteggio_auto1")
    private Integer punteggio1;

    @Column(name = "punteggio_auto2")
    private Integer punteggio2;

    @Column(name = "data_confronto", insertable = false, updatable = false)
    private LocalDateTime dataConfronto;
}
