package com.progetto.ingsw.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "utente")
@Data
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String cognome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String ruolo = "USER";
}