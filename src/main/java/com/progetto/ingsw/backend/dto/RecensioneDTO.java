package com.progetto.ingsw.backend.dto;

import lombok.Data;

@Data
public class RecensioneDTO {
    private Integer id;
    private UtenteDTO utente;
    private ConfrontoDTO confronto;
    private Integer voto;
    private String commento;
    private String data;
}
