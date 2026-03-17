package com.progetto.ingsw.backend.dto;

import lombok.Data;

@Data
public class ConfrontoDTO {
    private Integer id;
    private Integer auto1Id;
    private String auto1Nome;
    private Integer auto2Id;
    private String auto2Nome;
    private Integer punteggio1;
    private Integer punteggio2;
    private String dataConfronto;
}