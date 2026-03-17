package com.progetto.ingsw.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "preferiti")
@Data
public class Preferiti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "confronto_id", nullable = false)
    private Confronto confronto;

    @Column(name = "data_aggiunta", insertable = false, updatable = false)
    private LocalDateTime dataAggiunta;
}
