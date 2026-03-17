package com.progetto.ingsw.backend.dto;

import lombok.Data;

@Data
public class RecensioneDTO {
    private Integer id;
    private String username;
    private Integer voto;
    private String commento;
    private String data;
}
