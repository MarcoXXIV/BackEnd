package com.progetto.ingsw.backend.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AutoDTO {
    private Integer id;
    private String marca;
    private String modello;
    private Integer anno;
    private Long prezzo;
    private String categoria;

    // Parametri Prestazionali
    private Integer cv;
    private Integer velocitaMax;
    private BigDecimal accelerazione0100;
    private BigDecimal accelerazione100200;
    private BigDecimal accelerazione80140;
    private Integer coppia;
    private BigDecimal rapportoPesoPotenza;

    // Parametri Tecnici
    private Integer cilindri;
    private Integer cilindrata;
    private Integer pesoKg;
    private String trazione;
    private String alimentazione;
    private String euro; // Classe ambientale

    // Efficienza
    private BigDecimal consumoMedio;
    private Integer co2;

    // Info di servizio
    private String codice;
}
