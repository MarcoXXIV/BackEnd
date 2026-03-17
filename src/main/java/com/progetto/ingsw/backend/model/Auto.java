package com.progetto.ingsw.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "auto")
@Data
public class Auto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String marca;
    private String modello;
    private Integer anno;
    private Long prezzo;
    private String categoria;
    private Integer cv;

    @Column(name = "velocita_max")
    private Integer velocitaMax;

    @Column(name = "accelerazione_0_100")
    private Double accelerazione0100;

    @Column(name = "accelerazione_100_200")
    private Double accelerazione100200;

    @Column(name = "accelerazione_80_140")
    private Double accelerazione80140;

    private Integer cilindri;

    @Column(name = "peso_kg")
    private Integer pesoKg;

    @Column(name = "consumo_medio")
    private Double consumoMedio;

    private Integer coppia;
    private String trazione;

    @Column(name = "rapporto_peso_potenza")
    private Double rapportoPesoPotenza;

    private Integer cilindrata;
    private String alimentazione;

    @Column(unique = true)
    private String codice;

    private String euro;

    private Integer co2;

}
