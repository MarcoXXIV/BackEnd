package com.progetto.ingsw.backend.dto;

import lombok.Data;

@Data
public class ConfrontoDTO {
    private Integer id;

    private UtenteDTO utente;
    private AutoDTO auto1;
    private AutoDTO auto2;

    private Integer punteggio1;
    private Integer punteggio2;

    private String dataConfronto;
}