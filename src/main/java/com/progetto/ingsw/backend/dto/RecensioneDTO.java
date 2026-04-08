package com.progetto.ingsw.backend.dto;

import lombok.Data;

@Data
public class RecensioneDTO {
    private Integer id;
    private UtenteDTO utente;
    private AutoDTO auto1;
    private AutoDTO auto2;
    private Integer voto;
    private String commento;
    private String data;
}
