package com.progetto.ingsw.backend.dto;

import lombok.Data;

@Data
public class RecensioneDTO {
    private Integer id;
    private String nome;
    private String cognome;
    private String email;
    private Integer voto;
    private String commento;
    private String data;
}
