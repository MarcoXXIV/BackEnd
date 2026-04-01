package com.progetto.ingsw.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "preferiti")
@Data
public class Preferiti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confronto_id", nullable = false)
    private Confronto confronto;

    @CreationTimestamp
    @Column(name = "data_aggiunta", nullable = false, updatable = false)
    private LocalDateTime dataAggiunta;
}
